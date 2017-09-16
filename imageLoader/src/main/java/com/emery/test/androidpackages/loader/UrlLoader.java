package com.emery.test.androidpackages.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.emery.test.androidpackages.request.BitmapRequest;
import com.emery.test.androidpackages.utils.BitmapDecoder;
import com.emery.test.androidpackages.utils.ImageViewHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by MyPC on 2017/2/11.
 * <p>
 * 网络图片加载器
 */

public class UrlLoader extends AbstractLoader {
    @Override
    protected Bitmap onLoad(final BitmapRequest request) {
         //先下载后读取
        downloadImgByUrl(request.getImageUri(),getCache(request.getImageUrlMD5()));
        BitmapDecoder bitmapDecoder=new BitmapDecoder() {
            @Override
            public Bitmap decodeBitmapWithOptions(BitmapFactory.Options options) {
                return BitmapFactory.decodeFile(getCache(request.getImageUrlMD5()).getAbsolutePath(),options);
            }
        };
        return bitmapDecoder.decodeBitmap(ImageViewHelper.getImageViewWidth(request
                .getImageView()), ImageViewHelper.getImageViewHeight(request.getImageView
                ()));


    }
    public static boolean downloadImgByUrl(String urlStr, File file)
    {
        FileOutputStream fos = null;
        InputStream is = null;
        try
        {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            is = conn.getInputStream();
            fos = new FileOutputStream(file);
            byte[] buf = new byte[512];
            int len = 0;
            while ((len = is.read(buf)) != -1)
            {
                fos.write(buf, 0, len);
            }
            fos.flush();
            return true;

        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (is != null)
                    is.close();
            } catch (IOException e)
            {
            }

            try
            {
                if (fos != null)
                    fos.close();
            } catch (IOException e)
            {
            }
        }

        return false;

    }
    private File getCache(String unipue)
    {
        File file=new File(Environment.getExternalStorageDirectory(),"ImageLoader");
        if(!file.exists())
        {
            file.mkdir();
        }
        return new File(file,unipue);
    }
}