package com.emery.test.inject;

import android.app.Activity;

/**
 * Created by MyPC on 2017/3/15.
 */

public class InjectView {
    public static void bind(Activity activity) {
        String className = activity.getClass().getName();
        try {
            Class<?> clazz = Class.forName(className + "$$ViewBinder");
            ViewBinder binder = (ViewBinder) clazz.newInstance();
            binder.binder(activity);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
