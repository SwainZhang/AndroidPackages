package com.emery.test.ipc;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by MyPC on 2017/2/24.
 */

public class IBookManagerService extends Service {
    private RemoteCallbackList<IOnNewBookArrivedListener> mListeners = new
            RemoteCallbackList<IOnNewBookArrivedListener>();
    private CopyOnWriteArrayList<Book> mBooks = new CopyOnWriteArrayList<>();
    private AtomicBoolean mIsServiceDestoryed = new AtomicBoolean();

    @Override
    public void onCreate() {
        super.onCreate();
        mBooks.add(new Book("new book", "李1"));
        mBooks.add(new Book("new book", "李2"));
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!mIsServiceDestoryed.get()) {
                    try {
                        Thread.sleep(3000);
                        onNewBookArrived(new Book("new book", "李" + mBooks.size() + "来了"));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 广播client有新书
     *
     * @param book
     */
    private void onNewBookArrived(Book book) {
        mBooks.add(book);
        System.out.println("server现在共有" + mBooks.size() + "本书");
        int N = mListeners.beginBroadcast();
        for (int i = 0; i < N; i++) {
            IOnNewBookArrivedListener broadcastItem = mListeners.getBroadcastItem(i);
            if (broadcastItem != null) {
                try {
                    broadcastItem.onNewBookArrived(book);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            mListeners.finishBroadcast();

        }
    }

    @Override
    public void onDestroy() {
        mIsServiceDestoryed.set(true);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        int code = checkCallingOrSelfPermission("com.emery.test.ipc.ACCESS_BOOK_MANAGER");
        if (code == PackageManager.PERMISSION_DENIED) {
            try {
                throw new Exception("没有" + "com.emery.test.ipc.ACCESS_BOOK_MANAGER" + "权限");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        return new IBookManager.Stub() {
            @Override
            public List<Book> getBookList() throws RemoteException {
                return mBooks;
            }

            @Override
            public void addBook(Book book) throws RemoteException {
                mBooks.add(book);//client调用
            }

            @Override
            public void registerListener(IOnNewBookArrivedListener listener) throws
                    RemoteException {
                try {
                    mListeners.register(listener);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }

            @Override
            public void unregisterListener(IOnNewBookArrivedListener listener) throws
                    RemoteException {

                try {
                    mListeners.unregister(listener);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        };

    }
}
