package com.emery.test.androidpackages.policy.Interface;

import com.emery.test.androidpackages.request.BitmapRequest;

/**
 * Created by MyPC on 2017/2/11.
 *
 * 加载策略
 */

public interface ILoaderPolicy {
    int compareTo(BitmapRequest request1,BitmapRequest bitmapRequest2);
}
