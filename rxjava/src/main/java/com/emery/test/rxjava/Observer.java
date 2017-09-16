package com.emery.test.rxjava;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by MyPC on 2017/3/4.
 * <p>
 * 集市
 */

public class Observer<T> {
    //卖羊的人（回调）
    onSubscriber<T> mTonSubscriber;

    public Observer(onSubscriber<T> tonSubscriber) {
        System.out.println("Observer Observer（）构造调用了");
        mTonSubscriber = tonSubscriber;
    }

    /**
     * 实例化买卖羊的集市
     */
    public static <T> Observer<T> create(onSubscriber<T> tonSubscriber) {
        System.out.println("Observer create（）调用了");
        return new Observer<>(tonSubscriber);
    }

    public void subcriber(Subscriber<? super T> subscriber) {
        System.out.println("Observer subcriber（）调用了");
        mTonSubscriber.call(subscriber);
    }

    /**
     * 实例化打铁的集市
     *
     * @param <R>是返回类型 所以extends
     * @return
     */
    public <R> Observer<R> map(Func1<? super T, ? extends R> func1) {
        System.out.println("Observer map（）调用了");
        return lift(new OperationMap<T, R>(func1));
    }

    private <R> Observer<R> lift(OperationMap<T, R> trOperationMap) {
        System.out.println("Observer lift（）调用了");
        return new Observer<>(new onSubscribeLift<>(mTonSubscriber, trOperationMap));
    }

    /**
     * 相当于又造了一个新的集市，将代码转入到另一线程中执行。
     *
     * @return
     */
    public Observer<T> subscribeIO() {
        return create(new onSubscribIO<T>(this));
    }

    public Observer<T> subscribeMain() {
        return create(new onSubscribMain<T>(new Handler(Looper.getMainLooper()), this));
    }
}
