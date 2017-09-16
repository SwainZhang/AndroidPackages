// IOnNewBookArrivedListener.aidl
package com.emery.test.ipc;
import com.emery.test.ipc.Book;
// Declare any non-default types here with import statements

interface IOnNewBookArrivedListener {
   void onNewBookArrived(in Book book);
}
