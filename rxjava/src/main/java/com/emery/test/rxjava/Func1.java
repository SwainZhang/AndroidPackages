package com.emery.test.rxjava;

/**
 * Created by MyPC on 2017/3/4.
 *
 * 交换 t,r
 */

/**
 *
 * @param <T> 能力T
 * @param <R> 能力R
 */
public interface Func1<T,R> {
    R call(T t);
}
