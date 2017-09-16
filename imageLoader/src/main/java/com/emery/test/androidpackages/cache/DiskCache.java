package com.emery.test.androidpackages.cache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.emery.test.androidpackages.cache.Interface.IBitmapCache;
import com.emery.test.androidpackages.disk.DiskLruCache;
import com.emery.test.androidpackages.disk.IOUtil;
import com.emery.test.androidpackages.request.BitmapRequest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static android.R.attr.versionCode;

/**
 * Created by MyPC on 2017/2/11.
 *
 * 硬盘缓存
 */

public class DiskCache implements IBitmapCache {
    private static DiskCache diskCache;
    /**
     * 缓存路径
     */
    private String mCacheDir = "image";
    /**
     * 缓存单位
     */
    private static final int MB = 1024 * 1024;
    /**
     * 硬盘缓存解决方案
     */
    private DiskLruCache mDiskLruCache;

    private DiskCache(Context context) {
        initDiskCache(context);
    }
    public static DiskCache getInstance(Context context){
        if(diskCache==null){
            synchronized (DiskCache.class){
                if(diskCache==null){
                    diskCache=new DiskCache(context);
                }
            }
        }
        return  diskCache;
    }
    private void initDiskCache(Context context) {
        //得到缓存目录
        File dictionary = getDiskCachePath(context);
        if (!dictionary.exists()) {
            dictionary.mkdir();
        }
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        try {
            mDiskLruCache = DiskLruCache.open(dictionary, versionCode, 1, 50 * MB);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getDiskCachePath(Context context) {
        //本该写入该目录，但是不利于我们观察效果 File cacheDir = context.getCacheDir();
        return new File(Environment.getExternalStorageDirectory(), mCacheDir);
    }

    @Override
    public void put(BitmapRequest request, Bitmap bitmap) {
        DiskLruCache.Editor editor = null;
        OutputStream outputStream = null;
        try {
            editor = mDiskLruCache.edit(request.getImageUrlMD5());
            outputStream = editor.newOutputStream(0);
            if (bitmap2disk(bitmap, outputStream)) {
                editor.commit();
            } else {
                editor.abort();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean bitmap2disk(Bitmap bitmap, OutputStream outputStream) {
        BufferedOutputStream bos = new BufferedOutputStream(outputStream);
        boolean compress = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        try {
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtil.closeQuietly(bos);
        }
        return compress;
    }

    @Override
    public void remove(BitmapRequest request) {
        try {
            mDiskLruCache.remove(request.getImageUrlMD5());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Bitmap get(BitmapRequest request) {
        Bitmap bitmap = null;
        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(request.getImageUrlMD5());
            if(snapshot!=null){
                InputStream inputStream = snapshot.getInputStream(0);
                bitmap = BitmapFactory.decodeStream(inputStream);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
