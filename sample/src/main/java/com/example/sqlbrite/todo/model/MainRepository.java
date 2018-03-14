package com.example.sqlbrite.todo.model;

import android.arch.persistence.db.wcdb.WcdbUtils;
import android.os.Environment;

import com.example.sqlbrite.todo.model.local.db.TodoItem;
import com.example.sqlbrite.todo.model.local.db.TodoItemDao;
import com.example.sqlbrite.todo.model.local.db.TodoListDao;
import com.example.sqlbrite.todo.ui.ListsItem;
import com.example.sqlbrite.todo.ui.ListsItemDao;
import com.jakewharton.rx.ReplayingShare;
import com.squareup.sqlbrite3.BriteDatabase;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;

/**
 * @author Guang1234567
 * @date 2018/3/9 13:45
 */

public class MainRepository implements MainDataSource {

    private BriteDatabase mBriteDatabaseb;

    private final ListsItemDao mListsItemDao;

    private final TodoListDao mTodoListDao;

    private final TodoItemDao mTodoItemDao;

    private final Observable<List<ListsItem>> QueryListsItems;

    @Inject
    public MainRepository(BriteDatabase briteDatabaseb, ListsItemDao listsItemDao, TodoListDao todoListDao, TodoItemDao todoItemDao) {
        mBriteDatabaseb = briteDatabaseb;
        mListsItemDao = listsItemDao;
        mTodoListDao = todoListDao;
        mTodoItemDao = todoItemDao;

        QueryListsItems = mListsItemDao
                .createQueryListsItems(Long.MAX_VALUE)
                .doOnNext(new Consumer<List<ListsItem>>() {
                    @Override
                    public void accept(List<ListsItem> listsItems) throws Exception {
                        int i = 1;
                    }
                })
                .compose(ReplayingShare.<List<ListsItem>>instance()); // use ReplayingShare Transformer to memory cache, avoid expensive manipulate.
    }

    @Override
    public Observable<List<ListsItem>> createQueryListsItems(final long max) {
        return QueryListsItems.take(max);
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
