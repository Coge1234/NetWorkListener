package com.jochen.networklibrary.bean;

import java.lang.reflect.Method;

/**
 * Created by JoChen on 2020/4/21.
 * 用于保存使用了网络监听注解方法的封装类
 */
public class MethodBean {
    private Class<?> typeClass;
    private NetworkType networkType;
    private Method method;

    public MethodBean(Class<?> typeClass, NetworkType networkType, Method method) {
        this.typeClass = typeClass;
        this.networkType = networkType;
        this.method = method;
    }

    public Class<?> getTypeClass() {
        return typeClass;
    }

    public void setTypeClass(Class<?> typeClass) {
        this.typeClass = typeClass;
    }

    public NetworkType getNetworkType() {
        return networkType;
    }

    public void setNetworkType(NetworkType networkType) {
        this.networkType = networkType;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
