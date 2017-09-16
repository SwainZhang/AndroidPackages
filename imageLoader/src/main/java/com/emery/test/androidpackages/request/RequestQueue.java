package com.emery.test.androidpackages.request;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by MyPC on 2017/2/11.
 */

public class RequestQueue {
    /**
     * 阻塞式队列，
     * 多线程共享，
     * 生产效率与消费效率差别太远
     * 使用优先级队列，优先级高的队列先被消费（每一个产品都有编号）。
     */
    private BlockingQueue<BitmapRequest> mRequestQueue = new PriorityBlockingQueue<BitmapRequest>();
    /**
     * 线程数目等于转发器数量
     */
    private int threadCount;
    /**
     * 转发器
     */
    private RequestDispatcher[] mDispatchers;
    /**
     * 线程安全的int
     */
    private AtomicInteger mAtomicInteger=new AtomicInteger(0);

    public RequestQueue(int threadCount) {
        this.threadCount = threadCount;
    }

    /**
     * 添加bitmapRequest
     */
    public void addBitmapRequest(BitmapRequest request){
        //判断请求队列是否包含请求
        if(!mRequestQueue.contains(request)){
            //给请求队列编号
            request.setScacleNo(mAtomicInteger.incrementAndGet());
            mRequestQueue.add(request);
        }else{
            System.out.println(request.getScacleNo()+"请求已经存在请求队列中");
        }
    }

    /**
     * 开启请求（开启转发器）
     */
    public void start(){
        //先停止
        stop();
        startDispatchers();
    }

    private void startDispatchers() {
        mDispatchers=new RequestDispatcher[threadCount];
        for(int i=0;i<threadCount;i++){
            RequestDispatcher dispather=new RequestDispatcher(mRequestQueue);
            mDispatchers[i]=dispather;
            mDispatchers[i].start();
        }
    }

    /**
     * 停止请求
     */
    public void stop(){

    }
}
