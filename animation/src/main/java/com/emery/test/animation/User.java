package com.emery.test.animation;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MyPC on 2017/2/23.
 */

public class User implements Parcelable{
    private String name;

    private String paassword;

    protected User(Parcel in) {
        name = in.readString();
        paassword = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(paassword);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
