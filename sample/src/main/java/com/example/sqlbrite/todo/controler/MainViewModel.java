package com.example.sqlbrite.todo.controler;

import android.arch.lifecycle.ViewModel;

import com.example.sqlbrite.todo.model.MainDataSource;
import com.example.sqlbrite.todo.model.local.db.TodoItem;
import com.example.sqlbrite.todo.ui.ListsItem;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * @author Guang1234567
 * @date 2018/3/6 14:40
 */

public class MainViewModel extends ViewModel {

    private final MainDataSource mDataSource;

    @Inject
    public MainViewModel(MainDataSource dataSource) {
        mDataSource = dataSource;
    }

    public Observable<List<ListsItem>> createQueryListsItems() {
        return mDataSource.createQueryListsItems(100); // 省内存
    }

    public boolean complete(long id, boolean complete) {
        return mDataSource.completeTodoitem(id, complete);
    }

    public Observable<Integer> createQueryItemCount(long listId) {
        return mDataSource.createQueryItemCount(listId);
    }

    public Observable<List<TodoItem>> createQueryTodoItemsByListId(long listId) {
        return mDataSource.createQueryTodoItemsByListId(listId);
    }

    public Observable<String> createQueryListName(long listId) {
        return mDataSource.createQueryListName(listId);
    }

    public long createNewOneTodoList(String name) {
        return mDataSource.createNewOneTodoList(name);
    }

    public long createNewOneTodoItem(long listId, String description) {
        return mDataSource.createNewOneTodoItem(listId, description);
    }

    public File exportDecryption() throws Exception {
        return mDataSource.exportDecryption();
    }
}
