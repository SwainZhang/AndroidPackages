package com.emery.test.androidpackages.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.emery.test.androidpackages.request.BitmapRequest;
import com.emery.test.androidpackages.utils.BitmapDecoder;
import com.emery.test.androidpackages.utils.ImageViewHelper;

import java.io.File;

/**
 * Created by MyPC on 2017/2/11.
 *
 * 硬盘图片加载器
 *
 */

public class LocalLoader extends  AbstractLoader{
    @Override
    protected Bitmap onLoad(BitmapRequest request) {
        //得到本地图片的路径
        String path= Uri.parse(request.getImageUri()).getPath();
        //判断
        final File file=new File(path);
        if(!file.exists()){
            return null;
        }

        final BitmapDecoder bitmapDecoder=new BitmapDecoder() {
            @Override
            public Bitmap decodeBitmapWithOptions(BitmapFactory.Options options) {
               return BitmapFactory.decodeFile(file.getAbsolutePath(),options);
            }
        };
        return bitmapDecoder.decodeBitmap(ImageViewHelper.getImageViewWidth(request.getImageView()),ImageViewHelper.getImageViewHeight(request.getImageView()));
    }
}
