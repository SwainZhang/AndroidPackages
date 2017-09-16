package com.emery.test.androidpackages.policy;

import com.emery.test.androidpackages.policy.Interface.ILoaderPolicy;
import com.emery.test.androidpackages.request.BitmapRequest;

/**
 * Created by MyPC on 2017/2/11.
 *
 * 正向，先进先出
 */

public class SerialPolicy implements ILoaderPolicy {
    @Override
    public int compareTo(BitmapRequest request1, BitmapRequest bitmapRequest2) {
        return request1.getScacleNo()-bitmapRequest2.getScacleNo();
    }
}
