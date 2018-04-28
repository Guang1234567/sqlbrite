package com.example.sqlbrite.todo.model;

import android.support.v4.util.LruCache;

import com.example.sqlbrite.todo.model.local.db.TodoItem;
import com.example.sqlbrite.todo.ui.ListsItem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;

/**
 * @author Guang1234567
 * @date 2018/3/9 13:45
 */

public class MemoryMainRepository implements MainDataSource {
    private static final int DEFAULT_CACHE_SIZE = (10 /* MiB */ * 1024 * 1024);

    private final Map<Long, ListsItem> mListsItemsCache;

    private final PublishSubject<List<ListsItem>> mListsItemsCacheTrigger;

    private final LruCache<Long, List<TodoItem>> mTodoItemsCache;

    private final PublishSubject<List<ListsItem>> mTodoItemsCacheTrigger;

    @Inject
    public MemoryMainRepository() {
        mListsItemsCache = new ConcurrentHashMap<>(100);
        mListsItemsCacheTrigger = PublishSubject.create();

        mTodoItemsCache = new LruCache<>(DEFAULT_CACHE_SIZE);
        mTodoItemsCacheTrigger = PublishSubject.create();
    }

    protected void refreshListsItemsCache(List<ListsItem> newData) {
        mListsItemsCache.clear();
        if (newData != null && !newData.isEmpty()) {
            Iterator<ListsItem> it = newData.iterator();
            while (it.hasNext()) {
                ListsItem item = it.next();
                if (item != null) {
                    mListsItemsCache.put(Long.valueOf(item.id()), item);
                }
            }

            if (mListsItemsCacheTrigger.hasObservers()) {
                mListsItemsCacheTrigger.onNext(newData);
            }
        }
    }

    protected void putTodoItemsCache(long listId, List<TodoItem> newData) throws IOException {
        mTodoItemsCache.put(Long.valueOf(listId), newData);
    }

    private List<TodoItem> getTodoItemsCache(long listId) {
        return mTodoItemsCache.get(Long.valueOf(listId));
    }

    @Override
    public Observable<List<ListsItem>> createQueryListsItems(final long max) {
        return mListsItemsCacheTrigger
                .startWith(Observable.fromCallable(new Callable<List<ListsItem>>() {
                    @Override
                    public List<ListsItem> call() throws Exception {
                        return new ArrayList<ListsItem>(mListsItemsCache.values())
                                .subList(0, (int) Math.min(max, mListsItemsCache.size()));
                    }
                }));
    }

    @Override
    public Observable<Integer> createQueryItemCount(long listId) {
        return mListsItemsCacheTrigger
                .flatMap(new Function<List<ListsItem>, ObservableSource<Integer>>() {
                    @Override
                    public ObservableSource<Integer> apply(List<ListsItem> newData) throws Exception {
                        ListsItem item = mListsItemsCache.get(Long.valueOf(listId));
                        if (item != null) {
                            return Observable.just(item.itemCount());
                        }
                        return Observable.empty();
                    }
                })
                .startWith(Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                        ListsItem item = mListsItemsCache.get(Long.valueOf(listId));
                        if (item != null) {
                            emitter.onNext(item.itemCount());
                        }
                        emitter.onComplete();
                    }
                }));
    }

    @Override
    public Observable<List<TodoItem>> createQueryTodoItemsByListId(long listId) {
        return Observable
                .create(new ObservableOnSubscribe<List<TodoItem>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<TodoItem>> emitter) throws Exception {
                        List<TodoItem> todoItems = getTodoItemsCache(listId);
                        if (todoItems != null) {
                            emitter.onNext(todoItems);
                        }
                        emitter.onComplete();
                    }
                });
    }

    @Override
    public Observable<String> createQueryListName(long listId) {
        return mListsItemsCacheTrigger
                .flatMap(new Function<List<ListsItem>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(List<ListsItem> newData) throws Exception {
                        ListsItem item = mListsItemsCache.get(Long.valueOf(listId));
                        if (item != null) {
                            return Observable.just(item.name());
                        }
                        return Observable.empty();
                    }
                })
                .startWith(Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                        ListsItem item = mListsItemsCache.get(Long.valueOf(listId));
                        if (item != null) {
                            emitter.onNext(item.name());
                        }
                        emitter.onComplete();
                    }
                }));
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
