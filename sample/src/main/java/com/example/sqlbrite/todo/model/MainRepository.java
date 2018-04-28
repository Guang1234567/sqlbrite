package com.example.sqlbrite.todo.model;

import com.example.sqlbrite.todo.model.local.db.TodoItem;
import com.example.sqlbrite.todo.ui.ListsItem;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author Guang1234567
 * @date 2018/3/9 13:45
 */

public class MainRepository implements MainDataSource {

    private final DbMainRepository mDbMainRepository;

    private final MemoryMainRepository mMemoryMainRepository;

    private final Observable mCreateQueryListsItems;

    @Inject
    public MainRepository(final DbMainRepository dbMainRepository, final MemoryMainRepository memoryMainRepository) {
        mDbMainRepository = dbMainRepository;
        mMemoryMainRepository = memoryMainRepository;

        mCreateQueryListsItems = dbMainRepository.createQueryListsItems(Long.MAX_VALUE)
                .doOnNext(new Consumer<List<ListsItem>>() {
                    @Override
                    public void accept(List<ListsItem> listsItems) throws Exception {
                        memoryMainRepository.refreshListsItemsCache(listsItems);
                    }
                })
                .share();
    }

    @Override
    public Observable<List<ListsItem>> createQueryListsItems(final long max) {
        Observable<List<ListsItem>> memory = mMemoryMainRepository.createQueryListsItems(max);
        final Disposable[] outterDisposable = new Disposable[1];
        return memory
                .doOnLifecycle(
                        new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (!disposable.isDisposed()) {
                                    outterDisposable[0] = mCreateQueryListsItems.subscribe();
                                }
                            }
                        },
                        new Action() {
                            @Override
                            public void run() throws Exception {
                                if (outterDisposable[0] != null
                                        && !outterDisposable[0].isDisposed()) {
                                    outterDisposable[0].dispose();
                                }
                            }
                        }
                );
    }

    @Override
    public Observable<Integer> createQueryItemCount(long listId) {
        return mMemoryMainRepository.createQueryItemCount(listId)
                .switchIfEmpty(mDbMainRepository.createQueryItemCount(listId));
    }

    @Override
    public Observable<List<TodoItem>> createQueryTodoItemsByListId(long listId) {
        return mMemoryMainRepository.createQueryTodoItemsByListId(listId)
                .switchIfEmpty(
                        mDbMainRepository.createQueryTodoItemsByListId(listId)
                                .doOnNext(new Consumer<List<TodoItem>>() {
                                    @Override
                                    public void accept(List<TodoItem> todoItems) throws Exception {
                                        if (todoItems != null) {
                                            mMemoryMainRepository.putTodoItemsCache(listId, todoItems);
                                        }
                                    }
                                })
                );
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
