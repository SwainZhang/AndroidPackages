package com.emery.test.androidpackages.loader;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.emery.test.androidpackages.config.DisplayConfig;
import com.emery.test.androidpackages.config.ImageLoaderConfig;
import com.emery.test.androidpackages.request.BitmapRequest;
import com.emery.test.androidpackages.request.RequestQueue;

/**
 * Created by MyPC on 2017/2/11.
 * <p>
 * 对外暴露的加载方法
 */

public class SimpleImageLoader {

    /**
     *   框架整体配置
     */
    private ImageLoaderConfig mImageLoaderConfig;
    /**
     *  请求队列
     */
    private RequestQueue mRequestQueue;
    /**
     * 单例
     */
    private static volatile SimpleImageLoader instance;

    private SimpleImageLoader(){

    }
    private SimpleImageLoader(ImageLoaderConfig imageLoaderConfig){
        mImageLoaderConfig=imageLoaderConfig;
        mRequestQueue=new RequestQueue(mImageLoaderConfig.getThreadCount());
        mRequestQueue.start();
    }

    /**
     * 初始化的时候调用此方法
     * @param config 配置管理类
     * @return
     */
    public static SimpleImageLoader getInstance(ImageLoaderConfig config){
        if(instance==null){
            synchronized (SimpleImageLoader.class){
                if(instance==null){
                    instance=new SimpleImageLoader(config);
                }
            }
        }
        return  instance;
    }

    /**
     * 初始化完成后调用此方法
     * @return
     */
    public static SimpleImageLoader getInstance(){
        if(instance==null){
            throw new UnsupportedOperationException("未初始化配置,调用getInstance()完成配置");
        }
        return instance;
    }

    /**
     * 供外界调用显示图片
     * @param imageView
     * @param uri
     *        类型有 url,http,https,file
     */
    public void display(ImageView imageView,String uri){
        this.display(imageView,uri,null,null);
    }

    /**
     *
     * 加载数据的时候加载
     * @param imageView
     * @param uri
     * @param config
     * @param imageListener
     */
    public void display(ImageView imageView, String uri, DisplayConfig config,ImageListener imageListener){
        //将参数封装到请求中
        BitmapRequest request=new BitmapRequest(imageView,uri,imageListener,config);
        //添加到队列中
        mRequestQueue.addBitmapRequest(request);
    }

    public static interface ImageListener{
        /**
         * 图片请求下来后，需要修改图片,调用此方法。
         */
        void onComplete(ImageView imageView,Bitmap bitmap,String uri);
    }

    /**
     * 得到ImageLoaderConfig全局配置
     * @return
     */
    public ImageLoaderConfig getImageLoaderConfig() {
        return mImageLoaderConfig;
    }
}
