package com.example;

import javax.lang.model.type.TypeMirror;

/**
 * Created by MyPC on 2017/3/15.
 *
 * @BindView(R.id.x) TextView title
 * <p>
 * a FieldViewBind make up of 3 part including above
 */

public class FieldViewBind {
    private String name;//title
    private TypeMirror mTypeMirror;//TextView
    private int resId;//R.id.title

    public FieldViewBind(String name, TypeMirror typeMirror, int resId) {
        this.name = name;
        mTypeMirror = typeMirror;
        this.resId = resId;
    }

    public String getName() {
        return name;
    }

    public TypeMirror getTypeMirror() {
        return mTypeMirror;
    }

    public int getResId() {
        return resId;
    }
}
