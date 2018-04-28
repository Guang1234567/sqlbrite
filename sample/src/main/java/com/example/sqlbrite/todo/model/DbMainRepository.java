package com.example.sqlbrite.todo.model;

import android.arch.persistence.db.wcdb.WcdbUtils;
import android.os.Environment;

import com.example.sqlbrite.todo.model.local.db.TodoItem;
import com.example.sqlbrite.todo.model.local.db.TodoItemDao;
import com.example.sqlbrite.todo.model.local.db.TodoListDao;
import com.example.sqlbrite.todo.ui.ListsItem;
import com.example.sqlbrite.todo.ui.ListsItemDao;
import com.squareup.sqlbrite3.BriteDatabase;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * @author Guang1234567
 * @date 2018/3/9 13:45
 */

public class DbMainRepository implements MainDataSource {

    private BriteDatabase mBriteDatabaseb;

    private final ListsItemDao mListsItemDao;

    private final TodoListDao mTodoListDao;

    private final TodoItemDao mTodoItemDao;

    @Inject
    public DbMainRepository(BriteDatabase briteDatabaseb, ListsItemDao listsItemDao, TodoListDao todoListDao, TodoItemDao todoItemDao) {
        mBriteDatabaseb = briteDatabaseb;
        mListsItemDao = listsItemDao;
        mTodoListDao = todoListDao;
        mTodoItemDao = todoItemDao;
    }

    @Override
    public Observable<List<ListsItem>> createQueryListsItems(final long max) {
        return mListsItemDao
                .createQueryListsItems(500)
                .doOnNext(new Consumer<List<ListsItem>>() {
                    @Override
                    public void accept(List<ListsItem> listsItems) throws Exception {
                        int i = 1;
                    }
                })
                .take(max);
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
    public boolean completeTodoitem(long itemId, boolean complete) {
        return mTodoItemDao.complete(itemId, complete);
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
