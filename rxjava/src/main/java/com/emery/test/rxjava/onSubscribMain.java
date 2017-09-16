package com.emery.test.rxjava;

import android.os.Handler;

/**
 * Created by MyPC on 2017/3/11.
 */

public class onSubscribMain<T> implements onSubscriber<T> {
    Handler mHandler ;
    Observer<T> source;

    public onSubscribMain(Handler mHandler,Observer<T> source) {
        this.source = source;
        this.mHandler=mHandler;
    }

    @Override
    public void call(final Subscriber<? super T> subscriber) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                source.subcriber(subscriber);
            }
        });
    }
}
