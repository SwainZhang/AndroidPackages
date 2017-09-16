package com.emery.test.androidpackages.config;

import com.emery.test.androidpackages.cache.Interface.IBitmapCache;
import com.emery.test.androidpackages.cache.MemoryCache;
import com.emery.test.androidpackages.policy.Interface.ILoaderPolicy;
import com.emery.test.androidpackages.policy.ResevealPolicy;

/**
 * Created by MyPC on 2017/2/11.
 *
 * 框架配置类。
 */

public class ImageLoaderConfig {
    /**
     * 缓存策略(默认双缓存）
     */
    private IBitmapCache mIBitmapCache=new MemoryCache();
    /**
     * 加载策略（默认倒序）
     */
    private ILoaderPolicy mILoaderPolicy=new ResevealPolicy();

    /**
     * 开启线程数(默认cpu处理器个数)
     */
    private int mThreadCount=Runtime.getRuntime().availableProcessors();

    /**
     * 显示图片的配置
     */
    private DisplayConfig mDisplayConfig=new DisplayConfig();

    private ImageLoaderConfig(){

    }

    /**
     * 建造者模式
     *
     * （外部类构造方法私有化，内部类持有外部类的引用,并在内部类的构造中实例化外部类,并且同
     * 过在内部类中set方法给外部类的属性赋值（已经持有了外部类的引用），最后builder（）方法
     * 返回持有的引用）
     *
     */
    public static class Builder {

        private ImageLoaderConfig mImageLoaderConfig;

        public Builder(){
            this.mImageLoaderConfig=new ImageLoaderConfig();
        }

        public Builder setIBitmapCache(IBitmapCache IBitmapCache) {
            mImageLoaderConfig.mIBitmapCache = IBitmapCache;
            return  this;
        }

        public Builder setILoaderPolicy(ILoaderPolicy ILoaderPolicy) {
            mImageLoaderConfig.mILoaderPolicy = ILoaderPolicy;
            return  this;
        }

        public Builder setThreadCount(int threadCount) {
            mImageLoaderConfig.mThreadCount = threadCount;
            return  this;
        }
        public Builder setDisplayDefaultImage(int defaultImageId) {
            mImageLoaderConfig.mDisplayConfig.mDefaultImage = defaultImageId;
            return  this;
        }
        public Builder setDisplayErrorImage(int errorImageId) {
            mImageLoaderConfig.mDisplayConfig.mErrorImage = errorImageId;
            return  this;
        }

        public ImageLoaderConfig builder(){
            return  mImageLoaderConfig;
        }
    }

    public IBitmapCache getIBitmapCache() {
        return mIBitmapCache;
    }

    public ILoaderPolicy getILoaderPolicy() {
        return mILoaderPolicy;
    }

    public int getThreadCount() {
        return mThreadCount;
    }

    public DisplayConfig getDisplayConfig() {
        return mDisplayConfig;
    }
}
