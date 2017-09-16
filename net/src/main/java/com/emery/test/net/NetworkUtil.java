package com.emery.test.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by MyPC on 2017/2/22.
 */

public class NetworkUtil {
    /**
     * 是否有网络连接
     *
     * @param context
     * @return
     */

    public boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        // 获取当前连接可用的网络
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return activeNetworkInfo.isConnectedOrConnecting();
    }

    public static int getNetworkType(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();

        return 0;
    }
    public static NetworkInfo[] getAllNetworkInfo(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo[] allNetworkInfo = cm.getAllNetworkInfo();
        for (NetworkInfo networkInfo : allNetworkInfo) {
            System.out.println(networkInfo);
        }
        return allNetworkInfo;
    }

    /**
     * 判断当前连接网络的类型
     * @param context
     * @return
     */
    public static void connectionChanged(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        //获取当前连接可用的网络
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        int type = activeNetworkInfo.getType();
        if(ConnectivityManager.TYPE_WIFI==type){
            NetworkInfo.State state = activeNetworkInfo.getState();
            if(NetworkInfo.State.CONNECTED==state){
                System.out.println("wifi已连接");
            }else{
                System.out.println("wifi已断开");
            }
        }else if(ConnectivityManager.TYPE_MOBILE==type){
            NetworkInfo.State state = activeNetworkInfo.getState();
            if(NetworkInfo.State.CONNECTED==state){
                System.out.println("移动网络已连接");
            }else{
                System.out.println("移动网络已断开");
            }
        }
    }

    /**
     * 判断wifi是否可用
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        // 获取特定网络类型的链接状态信息
        NetworkInfo.State state = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if(NetworkInfo.State.CONNECTED==state){
            return true;
        }
        return false;
    }
    }


