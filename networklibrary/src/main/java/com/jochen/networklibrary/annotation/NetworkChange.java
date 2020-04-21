package com.jochen.networklibrary.annotation;

import com.jochen.networklibrary.bean.NetworkType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by JoChen on 2020/4/21.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NetworkChange {
    NetworkType networkType() default NetworkType.AUTO;
}
