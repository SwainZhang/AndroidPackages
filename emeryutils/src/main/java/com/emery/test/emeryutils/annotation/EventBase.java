package com.emery.test.emeryutils.annotation;

/**
 * Created by MyPC on 2017/3/12.
 */

public @interface EventBase {
    /**
     * 事件源
     * @return
     */
    String listenerSetter();

    /**
     * 事件类型
     * @return
     */
    Class<?> listenerType();

    /**
     * 事件回调
     * @return
     */
    String listenerCallBack();
}
