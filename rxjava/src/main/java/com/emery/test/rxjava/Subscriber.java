package com.emery.test.rxjava;

/**
 * 角色B 打铁的人
 *
 * @param <T> 打铁的人拥有的铁
 */

public abstract class Subscriber<T> {
    public abstract void onNext(T t);
}
