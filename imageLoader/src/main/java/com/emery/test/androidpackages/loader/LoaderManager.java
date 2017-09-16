package com.emery.test.androidpackages.loader;

import com.emery.test.androidpackages.loader.Interface.ILoader;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MyPC on 2017/2/12.
 *
 * 管理支持的加载图片的协议
 */

public class LoaderManager {

    //缓存所有支持的loader
    private Map<String,ILoader> loaders=new HashMap<>();
    private static LoaderManager instance=new LoaderManager();
    private LoaderManager(){
        regist("http",new UrlLoader() );
        regist("https",new UrlLoader());
        regist("file",new LocalLoader());
    }
    public static LoaderManager getInstance(){
        return instance;
    }
    private void regist(String type,ILoader loader){
        loaders.put(type,loader);
    }
    public ILoader getLoader(String type){
        if(loaders.containsKey(type)){
            return loaders.get(type);
        }else{
            return new NullLoader();
        }
    }

}
