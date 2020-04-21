package com.jochen.networklistener;

import android.app.Application;

import com.jochen.networklibrary.NetworkManager;

/**
 * Created by JoChen on 2020/4/21.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NetworkManager.getDefault().init(this);
    }
}
