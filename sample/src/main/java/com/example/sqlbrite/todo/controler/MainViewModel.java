package com.example.sqlbrite.todo.controler;

import com.example.sqlbrite.todo.model.MainDataSource;
import com.example.sqlbrite.todo.model.local.db.TodoItem;
import com.example.sqlbrite.todo.model.users.UserManager;
import com.example.sqlbrite.todo.model.users.UserSession;
import com.example.sqlbrite.todo.schedulers.SchedulerProvider;
import com.example.sqlbrite.todo.ui.ListsItem;
import com.gg.rxbase.controller.RxBaseViewModel;
import com.gg.rxbase.lifecycle.ViewModelEvent;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * @author Guang1234567
 * @date 2018/3/6 14:40
 */

public class MainViewModel extends RxBaseViewModel {

    private final MainDataSource mDataSource;
    private final Lazy<Observable<UserManager>> mUserManager;
    private final SchedulerProvider mSchedulerProvider;

    private UserSession mUserSession = UserSession.FAIL;

    private Date mLastCreateTime;

    @Inject
    public MainViewModel(MainDataSource dataSource,
                         Lazy<Observable<UserManager>> userManager,
                         SchedulerProvider schedulerProvider) {
        mDataSource = dataSource;
        mUserManager = userManager;
        mSchedulerProvider = schedulerProvider;

        mLastCreateTime = new Date();
    }

    public Date getLastCreateTime() {
        return mLastCreateTime;
    }

    public Observable<List<ListsItem>> createQueryListsItems() {
        return mDataSource.createQueryListsItems(100)// 省内存
                .compose(this.<List<ListsItem>>bindToLifecycle()); // 等价 bindUntilEvent(ViewModelEvent.DESTROY)
    }

    public boolean complete(long id, boolean complete) {
        return mDataSource.completeTodoitem(id, complete);
    }

    public Observable<Integer> createQueryItemCount(long listId) {
        return mDataSource.createQueryItemCount(listId)
                .compose(this.<Integer>bindUntilEvent(ViewModelEvent.DESTROY));
    }

    public Observable<List<TodoItem>> createQueryTodoItemsByListId(long listId) {
        return mDataSource.createQueryTodoItemsByListId(listId)
                .compose(this.<List<TodoItem>>bindUntilEvent(ViewModelEvent.DESTROY));
    }

    public Observable<String> createQueryListName(long listId) {
        return mDataSource.createQueryListName(listId)
                .compose(this.<String>bindUntilEvent(ViewModelEvent.DESTROY));
    }

    public long createNewOneTodoList(String name) {
        return mDataSource.createNewOneTodoList(name);
    }

    public long createNewOneTodoItem(long listId, String description) {
        return mDataSource.createNewOneTodoItem(listId, description);
    }

    public File exportDecryption() throws Exception {
        return mDataSource.exportDecryption();
    }

    public Observable<UserSession> login(final String userId, final String password) {
        return mUserManager.get()
                .observeOn(mSchedulerProvider.viewModel())
                .filter(new Predicate<UserManager>() {
                    @Override
                    public boolean test(UserManager userManager) throws Exception {
                        return UserSession.FAIL.equals(mUserSession);
                    }
                })
                .map(new Function<UserManager, UserSession>() {
                    @Override
                    public UserSession apply(UserManager userManager) throws Exception {
                        return userManager.login(userId, password);
                    }
                })
                .doOnNext(new Consumer<UserSession>() {
                    @Override
                    public void accept(UserSession userSession) throws Exception {
                        mUserSession = userSession;
                    }
                });
    }

    public Observable<UserSession> isUserSessionAlive() {
        return Observable
                .create(new ObservableOnSubscribe<UserSession>() {
                    @Override
                    public void subscribe(ObservableEmitter<UserSession> e) throws Exception {
                        e.onNext(mUserSession);
                        if (!e.isDisposed()) {
                            e.onComplete();
                        }
                    }
                })
                .observeOn(mSchedulerProvider.viewModel())
                .filter(new Predicate<UserSession>() {
                    @Override
                    public boolean test(UserSession session) throws Exception {
                        return mUserSession.isAlive();
                    }
                });
    }

    public void logout() {
        if (!UserSession.FAIL.equals(mUserSession)) {
            mUserSession.logout();
            mUserSession = UserSession.FAIL;
        }
    }
}
