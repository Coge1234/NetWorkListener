package com.jochen.networklibrary;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Looper;
import android.util.Log;

import com.jochen.networklibrary.annotation.NetworkChange;
import com.jochen.networklibrary.bean.MethodBean;
import com.jochen.networklibrary.core.NetworkCallbackImpl;
import com.jochen.networklibrary.utils.Constants;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by JoChen on 2020/4/21.
 */
public class NetworkManager {
    // volatile修饰的变量不允许线程内部缓存和重排序，即直接修改内存
    private static volatile NetworkManager instance;
    private Application application;
    // 用来保存这些带注解的方法（订阅者的回调方法）
    private Map<Object, List<MethodBean>> cacheMap;

    public NetworkManager() {
        cacheMap = new HashMap<>();
    }

    public static NetworkManager getDefault() {
        if (instance == null) {
            synchronized (NetworkManager.class) {
                if (instance == null) {
                    instance = new NetworkManager();
                }
            }
        }
        return instance;
    }

    public Application getApplication() {
        if (application == null) {
            throw new RuntimeException("NetworkManager.getDefault().init()未初始化");
        }
        return application;
    }

    @SuppressLint("MissingPermission")
    public void init(Application application) {
        this.application = application;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 采用新的监听，不通过广播
            ConnectivityManager.NetworkCallback networkCallback = new NetworkCallbackImpl();
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            NetworkRequest request = builder.build();
            ConnectivityManager cmgr = (ConnectivityManager) NetworkManager.getDefault().getApplication()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cmgr != null) {
                cmgr.registerNetworkCallback(request, networkCallback);
            }
        } else {
            // 做广播注册
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(Constants.ANDROID_NET_CHANGE_ACTION);
//        application.registerReceiver(receiver, filter);
        }
    }

    // 找到所有带符合注解的方法
    public void register(Object getter) {
        // 获取MainActivity所有的方法
        List<MethodBean> methodList = cacheMap.get(getter);
        if (methodList == null) {
            // 不为空表示以前注册完成
            methodList = findAnnotation(getter);
            Log.i(Constants.LOG_TAG, "methodList的大小：" + methodList.size());
            cacheMap.put(getter, methodList);
        }
    }

    // 获取MainActivity中所有注解的方法
    private List<MethodBean> findAnnotation(Object getter) {
        List<MethodBean> methodList = new ArrayList<>();
        Class<?> clazz = getter.getClass();
        while (clazz != null) {
            String clazzName = clazz.getName();
            if (clazzName.startsWith("java.") || clazzName.startsWith("javax.") || clazzName.startsWith("android.") || clazzName.startsWith("androidx.")) {
                break;
            }
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                NetworkChange networkChange = method.getAnnotation(NetworkChange.class);
                if (networkChange == null) {
                    continue;
                }
                Type returnType = method.getGenericReturnType();
                if (!"void".equals(returnType.toString())) {
                    throw new RuntimeException(method.getName() + "方法返回必须是void");
                }
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length != 1) {
                    throw new RuntimeException(method.getName() + "方法有且只有一个参数");
                }
                MethodBean manager = new MethodBean(parameterTypes[0], networkChange.networkType(), method);
                methodList.add(manager);
            }
            clazz = clazz.getSuperclass();
        }
        return methodList;
    }

    public void post(final Object setter) {
        Set<Object> set = cacheMap.keySet();
        for (final Object getter : set) {
            List<MethodBean> methodList = cacheMap.get(getter);
            if (methodList != null) {
                for (final MethodBean method : methodList) {
                    if (method.getTypeClass().isAssignableFrom(setter.getClass())) {
                        invoke(method, getter, setter);
                    }
                }
            }
        }
    }

    // 找到匹配方法后，通过反射调用MainActivity中所有符合要求的方法
    private void invoke(MethodBean method, Object getter, Object setter) {
        Method execute = method.getMethod();
        try {
            execute.invoke(getter, setter);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void unRegister(Object register) {
        cacheMap.remove(register);
    }

    // 移除所有
    public void unRegisterAll() {
        cacheMap.clear();
    }
}
