package com.emery.test.ipc;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;

import java.util.List;

public class MainActivity extends Activity {
    private final int BOOK_ARRIVED = 0;
    private IBookManager mIBookManager;
    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            System.out.println("-----binder死亡------");
            if (mIBookManager != null) {
                mIBookManager.asBinder().unlinkToDeath(mDeathRecipient, 0);
                mIBookManager = null;
            }
            //重新绑定
            if (mIntent != null && mMyConnection != null) {
                bindService(mIntent, mMyConnection, BIND_AUTO_CREATE);
            }
        }
    };
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case BOOK_ARRIVED:
                    Book book = (Book) msg.obj;
                    System.out.println("client收到" + book.getName() + "--" + book.getAuthor());
                    break;

                default:
                    break;
            }
        }
    };
    private IOnNewBookArrivedListener mIOnNewBookArrivedListener = new IOnNewBookArrivedListener
            .Stub() {

        @Override
        public void onNewBookArrived(Book book) throws RemoteException {
            mHandler.obtainMessage(BOOK_ARRIVED, book).sendToTarget();
        }


    };
    private Intent mIntent;
    private MyConnection mMyConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIntent = new Intent(this, IBookManagerService.class);
        mMyConnection = new MyConnection();
        bindService(mIntent, mMyConnection, BIND_AUTO_CREATE);
    }

    class MyConnection implements ServiceConnection {


        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mIBookManager = IBookManager.Stub.asInterface(service);
            if (mIBookManager == null) {
                System.out.println("连接失败。。。");
                return;
            }

            try {
                service.linkToDeath(mDeathRecipient, 0);//设置死亡代理

                List<Book> bookList = mIBookManager.getBookList();
                System.out.println("client查询有" + bookList.size() + "本书");
                mIBookManager.addBook(new Book("新书", "王五"));
                System.out.println("client添加了一本书后client查询有" + mIBookManager.getBookList().size()
                        + "本书");
                mIBookManager.registerListener(mIOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mIBookManager = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mIBookManager != null && mIBookManager.asBinder().isBinderAlive()) {
            try {
                //解除监听
                mIBookManager.unregisterListener(mIOnNewBookArrivedListener);
                unbindService(mMyConnection);

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}

