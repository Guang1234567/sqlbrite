package com.example.sqlbrite.todo.model;

import com.example.sqlbrite.todo.model.local.db.TodoItem;
import com.example.sqlbrite.todo.ui.ListsItem;
import com.jakewharton.rxrelay2.BehaviorRelay;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @author Guang1234567
 * @date 2018/3/9 13:45
 */

public class MemoryMainRepository implements MainDataSource {

    private final BehaviorRelay<List<ListsItem>> mListsItems;

    @Inject
    public MemoryMainRepository() {
        mListsItems = BehaviorRelay.create();
    }

    public Consumer<List<ListsItem>> updateListsItems() {
        return mListsItems;
    }

    @Override
    public Observable<List<ListsItem>> createQueryListsItems(long max) {
        return mListsItems.hide();
    }

    @Override
    public Observable<Integer> createQueryItemCount(long listId) {
        return mListsItems.flatMap(new Function<List<ListsItem>, ObservableSource<Integer>>() {
            @Override
            public ObservableSource<Integer> apply(List<ListsItem> cache) throws Exception {
                Iterator<ListsItem> it = cache.iterator();
                while (it.hasNext()) {
                    ListsItem listsItem = it.next();
                    if (listsItem != null && listsItem.id() == listId) {
                        return Observable.just(listsItem.itemCount());
                    }
                }
                return Observable.empty();
            }
        });
    }

    @Override
    public Observable<List<TodoItem>> createQueryTodoItemsByListId(long listId) {
        return null;
    }

    @Override
    public Observable<String> createQueryListName(long listId) {
        return null;
    }

    @Override
    public boolean completeTodoitem(long itemId, boolean complete) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long createNewOneTodoList(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long createNewOneTodoItem(long listId, String description) {
        throw new UnsupportedOperationException();
    }

    @Override
    public File exportDecryption() throws Exception {
        throw new UnsupportedOperationException();
    }
}
