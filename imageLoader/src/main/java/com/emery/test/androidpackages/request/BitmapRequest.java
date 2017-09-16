package com.emery.test.androidpackages.request;

import android.widget.ImageView;

import com.emery.test.androidpackages.config.DisplayConfig;
import com.emery.test.androidpackages.loader.SimpleImageLoader;
import com.emery.test.androidpackages.policy.Interface.ILoaderPolicy;
import com.emery.test.androidpackages.utils.MD5Utils;

import java.lang.ref.SoftReference;

/**
 * Created by MyPC on 2017/2/11.
 * <p>
 * 请求类（封装请求参数），按优先级排序，加入到请求队列中。所以要实现排序接口。
 */

public class BitmapRequest implements Comparable<BitmapRequest> {

    /*-----------------display()的请求参数---------begin-----------*/

    /**
     * display()的请求参数
     */
    private SoftReference<ImageView> imageViewSoft;
    /**
     * 图片路径
     */
    private String imageUri;

    /**
     * 图片UrlMD5名字

     */
    private String imageUrlMD5;
    /**
     * 下载图片后二次处理的接口
     */
    public SimpleImageLoader.ImageListener mImageListener;
    /**
     * 用户在加载的时候可以修改配置
     */
    private DisplayConfig mDisplayConfig;

    /*-----------------display()的请求参数---------end-----------*/

    /**
     * 加载策略，这里引用加载策略的原因是将排序交给加载策略去做。
     */
    private ILoaderPolicy mILoaderPolicy = SimpleImageLoader.getInstance().getImageLoaderConfig()
            .getILoaderPolicy();
    /**
     * 请求优先级编号
     */
    private int scacleNo;

    public BitmapRequest(ImageView imageview, String imageUrl,
                         SimpleImageLoader.ImageListener imageListener, DisplayConfig config) {
        this.imageViewSoft = new SoftReference<ImageView>(imageview);
        //设置图片的tag
        imageview.setTag(imageUrl);
        this.imageUri = imageUrl;
        this.imageUrlMD5= MD5Utils.toMD5(imageUrl);
        mImageListener = imageListener;
        if (config != null) {
            this.mDisplayConfig = config;
        }
    }

    public int getScacleNo() {
        return scacleNo;
    }

    public void setScacleNo(int scacleNo) {
        this.scacleNo = scacleNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        BitmapRequest request = (BitmapRequest) o;

        if (scacleNo != request.scacleNo)
            return false;
        return mILoaderPolicy.equals(request.mILoaderPolicy);

    }

    @Override
    public int hashCode() {
        int result = mILoaderPolicy.hashCode();
        result = 31 * result + scacleNo;
        return result;
    }

    /**
     * 取出imageView 以便操作
     * @return
     */
    public ImageView getImageView() {
        return imageViewSoft.get();
    }

    public String getImageUri() {
        return imageUri;
    }
    public String getImageUrlMD5() {
        return imageUrlMD5;
    }

    /**
     *
     * 按照请求优先级排序，具体排序交给加载策略去做。
     *
     * @param another
     * @return
     */

    @Override
    public int compareTo(BitmapRequest another) {
        return mILoaderPolicy.compareTo(another,this);
    }
}
