package com.emery.test.androidpackages.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by MyPC on 2017/2/12.
 */

public abstract class BitmapDecoder {

    public Bitmap decodeBitmap(int reqWidth,int reqHeight){
        //初始化option
        BitmapFactory.Options options=new BitmapFactory.Options();

        //只需要读取图片的宽高信息，而不需要读取整张图片
        options.inJustDecodeBounds=true;

        //这里是第一次读，读的边界，相当于个option里的宽高赋值宽
        decodeBitmapWithOptions(options);
        //使用图片里options的宽高，与控件的宽高计算比例(最终得出的是inSampleSize,存在在出参，入参变量options里)
        caculateScacleSizeWithOptions(options,reqWidth,reqHeight);
        //第二次开始真正的读取整张图片
        Bitmap bitmap = decodeBitmapWithOptions(options);
        return bitmap;
    }

    /**
     * 根据imageView控件的宽高比来计算，图片的缩放比例
     * @param options
     */
    private void caculateScacleSizeWithOptions(BitmapFactory.Options options,int reqWidth,int reqHeight){
        //图片的原始宽高
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;
        int inSampleSize=1;//采样率=2的时候图片的宽高就会变成原来的1/2
        if(outWidth>reqWidth||outHeight>reqHeight){
            //宽高缩放比例
            int heightRatio=Math.round((float) outHeight/(float) reqHeight);
            int widthRatio=Math.round((float) outWidth/(float) reqWidth);
            //有的是长了，有的是宽了
            inSampleSize=Math.max(heightRatio,widthRatio);
        }
        //原图
        options.inSampleSize=inSampleSize;
        //每个像素两个字节
        options.inPreferredConfig= Bitmap.Config.RGB_565;
        //第一次读边界，第二次读整张图片，所以false
        options.inJustDecodeBounds=false;
        //内存不足回收bitmap
        options.inPurgeable=true;
        options.inInputShareable=true;
    }

    /**
     * 专门用来读取流的方法。这里会两次调用，第一次获取图片的边界，第二次读取整张图
     * @param options
     * @return
     */
    public abstract Bitmap decodeBitmapWithOptions(BitmapFactory.Options options) ;
}
