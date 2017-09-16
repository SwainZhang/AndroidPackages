package com.emery.test.rxjava;

/**
 * Created by MyPC on 2017/3/4.
 */

public interface Operator<T,R> extends Func1<Subscriber<? super T>,Subscriber<? super R>> {
}
