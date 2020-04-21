package com.jochen.networklibrary.bean;

/**
 * Created by JoChen on 2020/4/21.
 * 网络类型的枚举
 */
public enum NetworkType {
    AUTO, // 有网络，包括wifi/gprs
    WIFI, // wifi网络
    CMNET, // PC，笔记本电脑其他设备
    CMWAP, // 手机网络
    NONE // 没有网络
}
