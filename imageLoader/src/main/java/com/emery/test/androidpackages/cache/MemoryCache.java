package com.emery.test.androidpackages.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.emery.test.androidpackages.cache.Interface.IBitmapCache;
import com.emery.test.androidpackages.request.BitmapRequest;

/**
 * Created by MyPC on 2017/2/11.
 *
 * 内存缓存
 */

public class MemoryCache implements IBitmapCache {
    private LruCache<String,Bitmap> cache;
   public MemoryCache(){
       int max= (int) (Runtime.getRuntime().freeMemory()/1024/8);
       cache=new LruCache<String,Bitmap>(max){
           @Override
           protected int sizeOf(String key, Bitmap value) {
              // value.getRowBytes()*value.getHeight();
               return value.getByteCount();
           }
       };
   }

    @Override
    public void put(BitmapRequest request, Bitmap bitmap) {
          cache.put(request.getImageUrlMD5(),bitmap);
    }

    @Override
    public void remove(BitmapRequest request) {
         cache.remove(request.getImageUrlMD5());
    }

    @Override
    public Bitmap get(BitmapRequest request) {
        return cache.get(request.getImageUrlMD5());
    }
}
