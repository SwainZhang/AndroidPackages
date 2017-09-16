package com.emery.test.rxjava;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by MyPC on 2017/3/11.
 */

public class onSubscribIO<T> implements onSubscriber<T> {
    private static ExecutorService executorService = Executors.newCachedThreadPool();
    private Observer<T> source;

    public onSubscribIO(Observer<T> source) {
        this.source = source;
    }

    @Override
    public void call(final Subscriber<? super T> subscriber) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                source.subcriber(subscriber);
            }
        };
        executorService.execute(runnable);
    }
}
