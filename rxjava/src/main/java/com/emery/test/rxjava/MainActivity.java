package com.emery.test.rxjava;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView) findViewById(R.id.iv);
    }

    public void start(View view) {
        Observer.create(new onSubscriber<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                //这里的subscriber就是下面subcriber()方法里的new 的Subscriber。
                //而一切的发生都将会在subscribe()方法调用后，流水线才开始运转起来，前面只是进行了一些初始化操作，
                subscriber.onNext("开始打铁。。。");
            }
        }).subcriber(new Subscriber<String>() {
            @Override
            public void onNext(String s) {
                System.out.println(s);
            }
        });
    }

    public void change(View view) {

        //String 代表羊
        //Bitmap 代表铁
        Observer.create(new onSubscriber<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                System.out.println("1--放羊的人：我要铁");
                //这里的subscriber并不是最后拿到结果的那个subscriber，而是通过subscriber.onNext()触发了真实的订阅者，类型转化也是该方法里发生的
                // 这里也相当于是一个回调，传递了一个字符串给Subscriber的实现类MapSubscrib(operationMap的内部类)
                subscriber.onNext("1--羊");
                System.out.println("1--放羊的人");
            }
        }).map(new Func1<String, Bitmap>() {

            //将羊转成铁
            @Override
            public Bitmap call(String s) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.trojan,
                        null);
                System.out.println("2--开始转化 羊--铁");
                return bitmap;
            }
        }).subscribeMain().subcriber(new Subscriber<Bitmap>() {
            @Override
            public void onNext(Bitmap bitmap) {
                System.out.println("3--得到了铁");
                mImageView.setImageBitmap(bitmap);
            }
        });
    }

    public void RxJava(View view) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                System.out.println("RxJava start:" + "开始了 传了url");
                System.out.println("1@RxJava Thread:" + Thread.currentThread().getName());
                e.onNext("url");

            }

            //说明我们要在这observeOn()之前订阅在子线程，subscribeOn只能调用一次，二次调用无效，observeOn()可以多次调用，observeOn()之后调用subscribeOn()无效
        }).subscribeOn(Schedulers.io()).doOnNext(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("doOnNext:" + s);
            }
        }).observeOn(AndroidSchedulers.mainThread()).map(new Function<String, Bitmap>() {
                    @Override
                    public Bitmap apply(String s) throws Exception {
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap
                                .ic_launcher, null);
                        System.out.println("2@RxJava Thread:" + Thread.currentThread().getName());
                        return bitmap;
                    }
                }).subscribeOn(Schedulers.io()).map(new Function<Bitmap, Drawable>() {
            @Override
            public Drawable apply(Bitmap bitmap) throws Exception {
                Drawable drawable = new BitmapDrawable(bitmap);
                System.out.println("3@RxJava Thread:" + Thread.currentThread().getName());
                return drawable;
            }

            //观察在主线程，说明我要在这之后运行在主线程
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Drawable>() {
            @Override
            public void accept(Drawable bitmap) throws Exception {
                System.out.println("4@RxJava Thread:" + Thread.currentThread().getName());
                mImageView.setImageDrawable(bitmap);
            }
        });
    }

}
