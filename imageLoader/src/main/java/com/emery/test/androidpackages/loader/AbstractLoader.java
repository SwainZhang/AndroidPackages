package com.emery.test.androidpackages.loader;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.emery.test.androidpackages.cache.Interface.IBitmapCache;
import com.emery.test.androidpackages.config.DisplayConfig;
import com.emery.test.androidpackages.loader.Interface.ILoader;
import com.emery.test.androidpackages.request.BitmapRequest;

/**
 * Created by MyPC on 2017/2/11.
 * <p>
 * 抽象加载器，
 */

public abstract class AbstractLoader implements ILoader {

    /**
     * 用户配置的图片缓存策略；
     */
    private IBitmapCache mIBitmapCache = SimpleImageLoader.getInstance().getImageLoaderConfig()
            .getIBitmapCache();

    /**
     * 用户显示配置
     */
    private DisplayConfig mDisplayConfig = SimpleImageLoader.getInstance().getImageLoaderConfig()
            .getDisplayConfig();

    @Override
    public void loadImage(BitmapRequest request) {
        //取出缓存bitmap
        Bitmap bitmap = mIBitmapCache.get(request);
        //还没缓存
        if (bitmap == null) {
            //显示加载前加载的图片
            showLoadingImage(request);
            //真正加载的图片
            bitmap = onLoad(request);
            //缓存图片
            cacheBitmap(request, bitmap);
        }
        //加载图片
        delivery2UIThread(request, bitmap);
    }

    /**
     * 加载图片
     *
     * @param request
     * @param bitmap
     */
    private void delivery2UIThread(final BitmapRequest request, final Bitmap bitmap) {
        final ImageView imageView = request.getImageView();
        if (imageView != null)
            imageView.post(new Runnable() {
                @Override
                public void run() {
                    updateImageView(request, bitmap);
                }
            });
    }


    /**
     * 把默认显示的图片替换掉，或者显示错误的图片
     *
     * @param request
     * @param bitmap
     */
    private void updateImageView(BitmapRequest request, Bitmap bitmap) {
        ImageView imageView = request.getImageView();
        //防止图片错位
        if (imageView!=null&&imageView.getTag() != null) {
            if (bitmap != null && request.getImageUri().equals(imageView.getTag())) {
                imageView.setImageBitmap(bitmap);
            }
        }
        if (bitmap == null && mDisplayConfig != null && mDisplayConfig.mErrorImage != -1) {
            imageView.setImageResource(mDisplayConfig.mErrorImage);
        }

        //监听回调
        if (request.mImageListener != null) {
            request.mImageListener.onComplete(request.getImageView(), bitmap, request.getImageUri
                    ());

        }
    }

    /**
     * 缓存图片
     *
     * @param request
     * @param bitmap
     */
    private void cacheBitmap(BitmapRequest request, Bitmap bitmap) {
        if (request != null && bitmap != null) {
            //线程安全
            synchronized (AbstractLoader.class) {
                mIBitmapCache.put(request, bitmap);
            }
        }
    }

    /**
     * 子类实现该方法加载真正的图片；
     *
     * @param request
     * @return
     */
    protected abstract Bitmap onLoad(BitmapRequest request);

    /**
     * 加载前应该显示的图片
     *
     * @param request
     */
    protected void showLoadingImage(BitmapRequest request) {
        //指定了显示配置
        if (hasLoadingPlaceHolder()) {
            final ImageView imageView = request.getImageView();
            if (imageView != null) {
                //在主线程中才能更新ui
                imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageResource(mDisplayConfig.mDefaultImage);
                    }
                });
            }
        }
    }

    /**
     * 是否配置加载前显示的默认图片
     *
     * @return
     */
    private boolean hasLoadingPlaceHolder() {
        return mDisplayConfig != null && mDisplayConfig.mDefaultImage > 0;
    }

}
