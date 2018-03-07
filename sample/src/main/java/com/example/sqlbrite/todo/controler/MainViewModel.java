package com.example.sqlbrite.todo.controler;

import android.arch.lifecycle.ViewModel;
import android.arch.persistence.db.wcdb.WcdbUtils;
import android.os.Environment;

import com.example.sqlbrite.todo.db.TodoItem;
import com.example.sqlbrite.todo.db.TodoItemDao;
import com.example.sqlbrite.todo.db.TodoListDao;
import com.example.sqlbrite.todo.ui.ListsItem;
import com.example.sqlbrite.todo.ui.ListsItemDao;
import com.squareup.sqlbrite3.BriteDatabase;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * @author Guang1234567
 * @date 2018/3/6 14:40
 */

public class MainViewModel extends ViewModel {

    @Inject
    BriteDatabase mBriteDatabaseb;

    private final ListsItemDao mListsItemDao;

    private final TodoListDao mTodoListDao;

    private final TodoItemDao mTodoItemDao;

    @Inject
    public MainViewModel(ListsItemDao listsItemDao, TodoListDao todoListDao, TodoItemDao todoItemDao, BriteDatabase db) {
        mListsItemDao = listsItemDao;
        mTodoListDao = todoListDao;
        mTodoItemDao = todoItemDao;
        mBriteDatabaseb = db;
    }

    public Observable<List<ListsItem>> createQueryListsItems() {
        return mListsItemDao.createQueryListsItems(100); // 省内存
    }

    public boolean complete(long id, boolean complete) {
        return mTodoItemDao.complete(id, complete);
    }

    public Observable<Integer> createQueryItemCount(long listId) {
        return mTodoItemDao.createQueryItemCount(listId);
    }

    public Observable<List<TodoItem>> createQueryTodoItemsByListId(long listId) {
        return mTodoItemDao.createQueryTodoItemsByListId(listId);
    }

    public Observable<String> createQueryListName(long listId) {
        return mTodoListDao.createQueryListName(listId);
    }

    public long createNewOneTodoList(String name) {
        return mTodoListDao.createNewOne(name);
    }

    public long createNewOneTodoItem(long listId, String description) {
        return mTodoItemDao.createNewOne(listId, description);
    }

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
