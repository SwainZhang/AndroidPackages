package com.emery.test.emeryutils.annotation;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by MyPC on 2017/3/12.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@EventBase(listenerSetter = "setOnLongClickListener", listenerType = View.OnLongClickListener.class,
        listenerCallBack = "onLongClick")
public @interface OnLongClick {
    int[] value();
}
