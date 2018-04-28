package com.example.sqlbrite.todo.model;

import com.example.sqlbrite.todo.model.local.db.TodoItem;
import com.example.sqlbrite.todo.ui.ListsItem;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * @author Guang1234567
 * @date 2018/3/9 13:44
 */

public interface MainDataSource {

    Observable<List<ListsItem>> createQueryListsItems(final long max);

    Observable<Integer> createQueryItemCount(long listId);

    Observable<List<TodoItem>> createQueryTodoItemsByListId(long listId);

    Observable<String> createQueryListName(long listId);

    boolean completeTodoitem(long itemId, boolean complete);

    long createNewOneTodoList(String name);

    long createNewOneTodoItem(long listId, String description);

    File exportDecryption() throws Exception ;
}
