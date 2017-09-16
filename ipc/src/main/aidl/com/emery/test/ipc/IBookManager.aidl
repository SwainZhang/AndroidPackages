// IBookManager.aidl
package com.emery.test.ipc;
// Declare any non-default types here with import statements
import com.emery.test.ipc.IOnNewBookArrivedListener;
import com.emery.test.ipc.Book;
interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
    void registerListener(IOnNewBookArrivedListener listener);
    void unregisterListener(IOnNewBookArrivedListener listener);

}
