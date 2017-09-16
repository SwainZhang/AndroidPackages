package com.emery.test.androidpackages.loader;

import com.emery.test.androidpackages.loader.Interface.ILoader;
import com.emery.test.androidpackages.request.BitmapRequest;

/**
 * Created by MyPC on 2017/2/12.
 *
 * 如何loaderManager不包含loader,那么返回这个加载器
 */

public class NullLoader implements ILoader {
    @Override
    public void loadImage(BitmapRequest request) {

    }
}
