package com.emery.test.inject;

/**
 * Created by MyPC on 2017/3/15.
 */

public interface ViewBinder<T> {
    void binder(T target);
}
