package com.emery.test.rxjava;

/**
 * Created by MyPC on 2017/3/4.
 */

public class onSubscribeLift<T, R> implements onSubscriber<R> {
    onSubscriber<T> parent;
    Operator<? extends R, ? super T> mOpreator;

    /**
     *
     * @param tonSubscriber  onCreate()发布消息的
     * @param opreator       转换函数
     */
    public onSubscribeLift(onSubscriber<T> tonSubscriber, Operator<? extends R, ? super T>
            opreator) {
        System.out.println("onSubscribeLift onSubscribeLift（）构造调用了");
        parent = tonSubscriber;
        mOpreator = opreator;
    }

    /**
     *
     * @param subscriber 就是最后得到结果的订阅者
     */
    @Override
    public void call(Subscriber<? super R> subscriber) {
        System.out.println("onSubscribeLift call（）构造调用了");
        Subscriber<? super T> call = mOpreator.call(subscriber);//羊铁互换
        parent.call(call);//接口回调实际调用的是Create()里onSubscriber接口的call()，传过去这里的订阅者
    }
}
