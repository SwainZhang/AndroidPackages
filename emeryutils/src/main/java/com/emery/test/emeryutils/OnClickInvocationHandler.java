package com.emery.test.emeryutils;

import android.content.Context;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by MyPC on 2017/3/12.
 * <p>
 * 点击事件的动态代理handler
 */

public class OnClickInvocationHandler implements InvocationHandler {
    //真实对象，activity
    private Context mContext;
    //被代理的方法
    private Map<String, Method> mMethodMap;


    public OnClickInvocationHandler(Context context, Map<String, Method> methodMap) {
        mContext = context;
        mMethodMap = methodMap;
    }

    /**
     * 动态代理方法每次都会触发这个方法
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //需要被代理的方法
        Method proxyMethod = mMethodMap.get(method.getName());
        if (proxyMethod != null) {
            //这里相当于调用 MainActivity 里自己定的一个点击回调方法click()
            //因为mMethodMap<String,Method>保存了,名字为onClick,方法为click 的键值对
            //当点击事件触发 ，也即是View.OnClickListener接口 调用自己的onClick的时候，进来
            //此时的method就是onClick方法，我们mMethodMap匹配后发现这个方法是我们想要代理的方法，
            //此时就让我们自己的方法来触发，也就是MainActivity里的click()方法来响应事件，所以达到了
            //以前onClick()同样的效果
            Object invoke = proxyMethod.invoke(mContext, args);
            return invoke;
        } else {
            //这里相当于调用 代理对象 proxy（实现View.OnClickListener）的onClick
            //也就是mMethodMap匹配后发现触发的method并不是我们需要代理的方法(代理方法会在注解里声明)，就让 proxy 来响应事件实际上会死循环
            Object invoke = method.invoke(proxy, args);
            return invoke;
        }

    }
}
