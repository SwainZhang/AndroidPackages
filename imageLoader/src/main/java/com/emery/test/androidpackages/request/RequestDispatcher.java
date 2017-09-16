package com.emery.test.androidpackages.request;

import com.emery.test.androidpackages.loader.Interface.ILoader;
import com.emery.test.androidpackages.loader.LoaderManager;

import java.util.concurrent.BlockingQueue;

/**
 * Created by MyPC on 2017/2/11.
 *
 * 请求转发器(不断从请求队列中获取请求，是需要耗时的所以要开启线程）,也可以实现runnable+线程池，这里用thread+数组
 */

public class RequestDispatcher extends Thread{
    /**
     * 阻塞请求队列（外面传入，转发器才可以取出）
     */
    private BlockingQueue<BitmapRequest> mRequestQueue;
    public RequestDispatcher(BlockingQueue<BitmapRequest> mRequestQueue){
        this.mRequestQueue=mRequestQueue;
    }
    @Override
    public void run() {
        while (!isInterrupted()){
            try {
                //取出请求
                BitmapRequest bitmapRequest=mRequestQueue.take();
                //根据协议地址选择加载器
                String schema=parseSchema(bitmapRequest.getImageUri());
                ILoader loader = LoaderManager.getInstance().getLoader(schema);
                //交给加载器处理请求
                loader.loadImage(bitmapRequest);

            } catch (InterruptedException e) {
                e.printStackTrace();
                this.interrupt();
            }
        }
    }

    private String parseSchema(String uri) {
        if(uri.contains("://")){
            return  uri.split("://")[0];
        }else{
            throw new UnsupportedOperationException("暂时不支持该类型，可在RequestDispatch中扩展");
        }

    }
}
