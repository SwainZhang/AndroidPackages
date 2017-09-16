package com.emery.test.rxjava;

/**
 * Created by MyPC on 2017/3/4.
 * @param <T>  调用T这种能力的角色去干活
 */
public interface Action1<T> {
    void call(T t);
}
