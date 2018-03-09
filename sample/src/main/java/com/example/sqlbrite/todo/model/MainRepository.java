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

import io.reactivex.Observable;

/**
 * @author Guang1234567
 * @date 2018/3/9 13:45
 */

public class MainRepository implements MainDataSource {

    BriteDatabase mBriteDatabaseb;

    private final ListsItemDao mListsItemDao;

    private final TodoListDao mTodoListDao;

    private final TodoItemDao mTodoItemDao;

    public MainRepository(BriteDatabase briteDatabaseb, ListsItemDao listsItemDao, TodoListDao todoListDao, TodoItemDao todoItemDao) {
        mBriteDatabaseb = briteDatabaseb;
        mListsItemDao = listsItemDao;
        mTodoListDao = todoListDao;
        mTodoItemDao = todoItemDao;
    }

    @Override
    public Observable<List<ListsItem>> createQueryListsItems(final int max) {
        return mListsItemDao.createQueryListsItems(max); // 省内存
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
