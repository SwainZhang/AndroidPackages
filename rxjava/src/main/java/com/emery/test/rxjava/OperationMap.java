package com.emery.test.rxjava;

/**
 * Created by MyPC on 2017/3/4.
 *
 * 中间人
 */

public class OperationMap<T, R> implements Operator<R, T> {
    /**
     * T是羊，R是铁
     */
    Func1<? super T, ? extends R> mTransform;//转换函数

    public OperationMap(Func1<? super T, ? extends R> transform) {
        System.out.println("OperationMap OperationMap（）构造调用了");

        mTransform = transform;
    }

    /**
     *
     * @param subscriber  就是最后得到结果的订阅者
     * @return
     */
    @Override
    public Subscriber<? super T> call(Subscriber<? super R> subscriber) {
        System.out.println("OperationMap call（）调用了");
        return new MapSubscrib<>(mTransform, subscriber);
    }


    private class MapSubscrib<T, R> extends Subscriber<T> {

        Func1<? super T, ? extends R> mTransform;
        /**
         * 最后得到结果的定阅者
         */
        private Subscriber<? super R> actual;

        public MapSubscrib(Func1<? super T, ? extends R> transform, Subscriber<? super R>
                subscriber) {
            System.out.println("MapSubscrib MapSubscrib（）构造调用了");
            mTransform = transform;
            actual = subscriber;//这是外面传进来的实现类，
        }

        /**
         * 真正完成转换的方法。发布者通过subscriber.onNext()发布类型，
         * 实际上在发布的时候就发生了转化
         *
         * @param t 订阅者发布的类型
         */

        @Override
        public void onNext(T t) {
            System.out.println("MapSubscrib onNext（）构造调用了");

            System.out.println("---opreation onNext:"+t.toString());
           /**
            接口回调，实际上是在回调func1（内型转换接口），
            传递给了func1()里call()一个t类型(见activity调用)，也就是订阅想要订阅的类型，
             返回一个订阅者真正想要的内型，或订阅者二次转换的内型。
             */
            R call = mTransform.call(t); //羊和铁完成交换

            /**
             * 最后得到结果的订阅者(Subsrcibe()里的的subcriber)回调订阅者想要的类型。
             */
            actual.onNext(call);//最终传入的子类里可以拿到结果,也就是最后的订阅者
        }
    }
}
