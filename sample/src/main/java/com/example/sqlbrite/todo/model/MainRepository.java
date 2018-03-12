package com.example.sqlbrite.todo.model;

import android.arch.persistence.db.wcdb.WcdbUtils;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.example.sqlbrite.todo.model.local.db.TodoItem;
import com.example.sqlbrite.todo.model.local.db.TodoItemDao;
import com.example.sqlbrite.todo.model.local.db.TodoListDao;
import com.example.sqlbrite.todo.ui.ListsItem;
import com.example.sqlbrite.todo.ui.ListsItemDao;
import com.squareup.sqlbrite3.BriteDatabase;
import com.squareup.sqlbrite3.SqlBrite;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * @author Guang1234567
 * @date 2018/3/9 13:45
 */

public class MainRepository implements MainDataSource {

    private BriteDatabase mBriteDatabaseb;

    private final ListsItemDao mListsItemDao;

    private final TodoListDao mTodoListDao;

    private final TodoItemDao mTodoItemDao;

    final BehaviorSubject<List<ListsItem>> memory = BehaviorSubject.create();

    @Inject
    public MainRepository(BriteDatabase briteDatabaseb, ListsItemDao listsItemDao, TodoListDao todoListDao, TodoItemDao todoItemDao) {
        mBriteDatabaseb = briteDatabaseb;
        mListsItemDao = listsItemDao;
        mTodoListDao = todoListDao;
        mTodoItemDao = todoItemDao;

        Observable<List<ListsItem>> disk = mListsItemDao.createQueryListsItems(Long.MAX_VALUE,
                new Predicate<SqlBrite.Query>() {
                    @Override
                    public boolean test(SqlBrite.Query query) throws Exception {
                        return !memory.hasValue() || memory.hasObservers();
                    }
                })
                .doOnNext(new Consumer<List<ListsItem>>() {
                    @Override
                    public void accept(List<ListsItem> listsItems) throws Exception {
                        memory.onNext(listsItems);
                    }
                });
        Disposable disposable = disk.subscribe();
    }

    @Override
    public Observable<List<ListsItem>> createQueryListsItems(final long max) {
        return memory.take(max);
    }

    @Override
    public boolean completeTodoitem(long itemId, boolean complete) {
        return mTodoItemDao.complete(itemId, complete);
    }

    @Override
    public Observable<Integer> createQueryItemCount(long listId) {
        return mTodoItemDao.createQueryItemCount(listId);
    }

    @Override
    public Observable<List<TodoItem>> createQueryTodoItemsByListId(long listId) {
        return mTodoItemDao.createQueryTodoItemsByListId(listId);
    }

    @Override
    public Observable<String> createQueryListName(long listId) {
        return mTodoListDao.createQueryListName(listId);
    }

    @Override
    public long createNewOneTodoList(String name) {
        return mTodoListDao.createNewOne(name);
    }

    @Override
    public long createNewOneTodoItem(long listId, String description) {
        return mTodoItemDao.createNewOne(listId, description);
    }

    @Override
    public File exportDecryption() throws Exception {
        File dir = Environment.getExternalStorageDirectory();
        File dstFile = new File(dir, "todo-list-backup.db");
        if (!dstFile.exists()) {
            dstFile.createNewFile();
        }
        WcdbUtils.exportDecryption(mBriteDatabaseb.getReadableDatabase(), dstFile);
        return dstFile;
    }
}
