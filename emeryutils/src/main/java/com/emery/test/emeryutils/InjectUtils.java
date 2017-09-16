package com.emery.test.emeryutils;

import android.content.Context;
import android.view.View;

import com.emery.test.emeryutils.annotation.ContentView;
import com.emery.test.emeryutils.annotation.EventBase;
import com.emery.test.emeryutils.annotation.ViewInject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by MyPC on 2017/3/12.
 */

public class InjectUtils {
    public static void inject(Context context) {
        injectLayout(context);
        injectView(context);
        injectEvent(context);
    }

    private static void injectEvent(Context context) {
        Class<?> clazz = context.getClass();
        Method[] methods = clazz.getMethods();
        //拿到类的所有方法
        for (Method method : methods) {
            //拿到每个方法上面的所有注解
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                //拿到每个注解的类型 如：onclick
                Class<? extends Annotation> annotationType = annotation.annotationType();
                //拿到注解的注解 如:onclick的 eventbase
                EventBase eventBase = annotationType.getAnnotation(EventBase.class);
                if (eventBase == null) {
                    continue;
                }
                //获取事件的三要素
                String eventSetter = eventBase.listenerSetter();
                //事件类型：如View.onClickListener
                Class<?> eventType = eventBase.listenerType();
                //回调方法：如OnClick注解里的listenerCallback=onClick OnLongClick注解里的 onLongClick
                String eventCallBack = eventBase.listenerCallBack();

                Map<String, Method> methodMap = new HashMap<>();
                methodMap.put(eventCallBack, method);// method 就是acitivty里面的click()

                try {
                    Method annotationMethod = annotationType.getDeclaredMethod("value");
                    //invoke（object，params）的时候，我们反射拿到的方法是谁的object就填哪个对象，params就是该方法的参数
                    //在这里value（）属于annotation的方法，无参数，方法的返回值是int []
                    //此外，以前通常是通过某个具体的注解annotation.value()拿到注解的值 如下面的injectLayout()方法中，
                    //但是，为了扩展不同的注解类型，适应不同的如 OnClick OnLongClick这种类型所以使用了反射取值
                    //这里就相当于拿到了OnClick里的值，也就是R.id.XX,至于为什么是数组，这样可以让多个view绑定同一个事件
                    int[] viewId = (int[]) annotationMethod.invoke(annotation);

                    //此刻我们只是需要代理MainActivity了，前面我们知道动态代理的方法必须抽象成接口，
                    //我们这里需要代理的是点击事件，相当于MainActivity实现了View.onClickListener接口，所以
                    //MainActivity里有了onCick()方法，而这个方法正是我们想要代理的方法。

                    for (int id : viewId) {
                        Method findViewById = clazz.getMethod("findViewById", int.class);
                        View view = (View) findViewById.invoke(context, id);
                        if (view == null) {
                            continue;
                        }
                        //通过view 反射拿到如setOnClickListener()，参数类型是 View.onClickListener
                        Method setOnXXClickListener = view.getClass().getMethod(eventSetter,
                                eventType);
                        //context相当于我们的真实对象
                        OnClickInvocationHandler handler = new OnClickInvocationHandler(context,
                                methodMap);

                        //这里得到了相当于就是实现了View.onClickListener的代理对象
                        Object proxy = Proxy.newProxyInstance(eventType.getClassLoader(), new
                                Class[]{eventType}, handler);
                        //setOnXXClickListener是view的方法所以填view
                        //这里相当于平时view.setOnClickListener(this)
                        // this就是相当于我们平时的MainActivity实现了View.onClickListener，这里是proxy
                        setOnXXClickListener.invoke(view, proxy);

                    }


                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }


            }
        }

    }

    public static void injectView(Context context) {
        Class<?> clazz = context.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            ViewInject annotation = field.getAnnotation(ViewInject.class);
            if (annotation != null) {
                int viewId = annotation.value();
                try {
                    //找到findViewById方法
                    Method method = clazz.getMethod("findViewById", int.class);
                    //触发方法，相当于 findViewById(R.id.xx),invoke()第一个参数，我们触发的方法是属于谁的就填写谁，这里是context的
                    View view = (View) method.invoke(context, viewId);//触发方法的返回值就是我们反射调用方法的返回值
                    //给成员变量赋值 如，TextView tv=findViewById(R.id.xx)
                    field.setAccessible(true);
                    field.set(context, view);

                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void injectLayout(Context context) {
        int layoutId = 0;
        Class<?> clazz = context.getClass();
        ContentView annotation = clazz.getAnnotation(ContentView.class);
        if (annotation != null) {
            layoutId = annotation.value();
        }
        try {
            Method method = clazz.getMethod("setContentView", int.class);
            method.invoke(context, layoutId);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
