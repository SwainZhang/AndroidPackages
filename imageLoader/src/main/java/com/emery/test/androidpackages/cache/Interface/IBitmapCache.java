package com.emery.test.androidpackages.cache.Interface;

import android.graphics.Bitmap;

import com.emery.test.androidpackages.request.BitmapRequest;

/**
 * Created by MyPC on 2017/2/11.
 */

public interface IBitmapCache {
    /**
     * 缓存bitmap
     * @param request
     */
    void put(BitmapRequest request,Bitmap bitmap);

    /**
     * 移除bimap
     * @param request
     */
    void remove(BitmapRequest request);

    /**
     * 取出bitmap
     * @param request
     * @return
     */
    Bitmap get(BitmapRequest request);
}
