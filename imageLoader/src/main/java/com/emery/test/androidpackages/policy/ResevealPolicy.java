package com.emery.test.androidpackages.policy;

import com.emery.test.androidpackages.policy.Interface.ILoaderPolicy;
import com.emery.test.androidpackages.request.BitmapRequest;

/**
 * Created by MyPC on 2017/2/11.
 *
 * 反向，后进先出
 */

public class ResevealPolicy implements ILoaderPolicy {
    @Override
    public int compareTo(BitmapRequest request1, BitmapRequest bitmapRequest2) {
        return bitmapRequest2.getScacleNo()-request1.getScacleNo();
    }
}
