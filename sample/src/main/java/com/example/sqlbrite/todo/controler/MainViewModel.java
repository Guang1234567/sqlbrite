package com.example.sqlbrite.todo.controler;

import com.example.sqlbrite.todo.model.MainDataSource;
import com.example.sqlbrite.todo.model.local.db.TodoItem;
import com.example.sqlbrite.todo.schedulers.SchedulerProvider;
import com.example.sqlbrite.todo.ui.ListsItem;
import com.gg.rxbase.controller.RxBaseViewModel;
import com.gg.rxbase.lifecycle.ViewModelEvent;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * @author Guang1234567
 * @date 2018/3/6 14:40
 */

public class MainViewModel extends RxBaseViewModel {

    private final SchedulerProvider mSchedulerProvider;
    private final MainDataSource mDataSource;
    private Date mLastCreateTime;

    @Inject
    public MainViewModel(MainDataSource dataSource,
                         SchedulerProvider schedulerProvider) {
        mDataSource = dataSource;
        mSchedulerProvider = schedulerProvider;
        mLastCreateTime = new Date();
    }

    public Date getLastCreateTime() {
        return mLastCreateTime;
    }

    public Observable<List<ListsItem>> createQueryListsItems() {
        return mDataSource.createQueryListsItems(100)// 省内存
                .compose(this.<List<ListsItem>>bindToLifecycle()); // 等价 bindUntilEvent(ViewModelEvent.DESTROY)
    }

    public boolean complete(long id, boolean complete) {
        return mDataSource.completeTodoitem(id, complete);
    }

    public Observable<Integer> createQueryItemCount(long listId) {
        return mDataSource.createQueryItemCount(listId)
                .compose(this.<Integer>bindUntilEvent(ViewModelEvent.DESTROY));
    }

    public Observable<List<TodoItem>> createQueryTodoItemsByListId(long listId) {
        return mDataSource.createQueryTodoItemsByListId(listId)
                .compose(this.<List<TodoItem>>bindUntilEvent(ViewModelEvent.DESTROY));
    }

    public Observable<String> createQueryListName(long listId) {
        return mDataSource.createQueryListName(listId)
                .compose(this.<String>bindUntilEvent(ViewModelEvent.DESTROY));
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
