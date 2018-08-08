package com.example.sqlbrite.todo.model;

import android.util.Log;

import com.example.sqlbrite.todo.model.local.db.TodoItem;
import com.example.sqlbrite.todo.ui.ListsItem;
import com.jakewharton.rx.ReplayingShare;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableConverter;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @author Guang1234567
 * @date 2018/3/9 13:45
 */

public class MainRepository implements MainDataSource {

    private final MemoryMainRepository mMemoryMainRepository;

    private final DbMainRepository mDbMainRepository;

    private final Observable<List<ListsItem>> mListItems;

    @Inject
    public MainRepository(final DbMainRepository dbMainRepository, final MemoryMainRepository memoryMainRepository) {
        mMemoryMainRepository = memoryMainRepository;
        mDbMainRepository = dbMainRepository;

        //mListItems = mDbMainRepository.createQueryListsItems(100).replay(1).refCount(); // as same as below line
        //mListItems = mDbMainRepository.createQueryListsItems(100).compose(ReplayingShare.instance());
        mListItems = mDbMainRepository.createQueryListsItems(100)
                .doOnNext(mMemoryMainRepository.updateListsItems())
                .share()
                .compose(new ObservableTransformer<List<ListsItem>, List<ListsItem>>() {
                    @Override
                    public ObservableSource<List<ListsItem>> apply(Observable<List<ListsItem>> upstream) {
                        return new Observable<List<ListsItem>>() {
                            @Override
                            protected void subscribeActual(Observer<? super List<ListsItem>> downstream) {
                                upstream.subscribe(new Observer<List<ListsItem>>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {
                                        downstream.onSubscribe(d);
                                    }

                                    @Override
                                    public void onNext(List<ListsItem> listsItems) {
                                        downstream.onNext(listsItems);
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        downstream.onError(e);
                                    }

                                    @Override
                                    public void onComplete() {
                                        downstream.onComplete();
                                    }
                                });
                            }
                        };
                    }
                });
    }

    @Override
    public Observable<List<ListsItem>> createQueryListsItems(final long max) {
        return mListItems;
    }

    @Override
    public Observable<Integer> createQueryItemCount(long listId) {
        return mMemoryMainRepository.createQueryItemCount(listId)
                .switchIfEmpty(mDbMainRepository.createQueryItemCount(listId));
    }

    @Override
    public Observable<List<TodoItem>> createQueryTodoItemsByListId(long listId) {
        return mDbMainRepository.createQueryTodoItemsByListId(listId);
    }

    @Override
    public Observable<String> createQueryListName(long listId) {
        return mMemoryMainRepository.createQueryListName(listId)
                .switchIfEmpty(mDbMainRepository.createQueryListName(listId));
    }

    @Override
    public boolean completeTodoitem(long itemId, boolean complete) {
        return mDbMainRepository.completeTodoitem(itemId, complete);
    }

    @Override
    public long createNewOneTodoList(String name) {
        return mDbMainRepository.createNewOneTodoList(name);
    }

    @Override
    public long createNewOneTodoItem(long listId, String description) {
        return mDbMainRepository.createNewOneTodoItem(listId, description);
    }

    @Override
    public File exportDecryption() throws Exception {
        return mDbMainRepository.exportDecryption();
    }
}
