package com.example.sqlbrite.todo.controler;

import com.example.sqlbrite.todo.model.users.UserManager;
import com.example.sqlbrite.todo.model.users.UserSession;
import com.example.sqlbrite.todo.schedulers.SchedulerProvider;

import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @author Guang1234567
 * @date 2018/3/6 14:40
 */

public class LoginViewControler {

    private final UserManager mUserManager;
    private final SchedulerProvider mSchedulerProvider;

    private UserSession mUserSession = null;

    @Inject
    public LoginViewControler(UserManager userManager,
                              SchedulerProvider schedulerProvider) {
        mUserManager = userManager;
        mSchedulerProvider = schedulerProvider;
    }

    public Single<UserSession> login(final String userId, final String password) {
        if (mUserSession != null) {
            throw new AssertionError("Must logout firstly!");
        }

        return mUserManager.createSessionForUser(userId)
                .subscribeOn(mSchedulerProvider.viewModel())
                .observeOn(mSchedulerProvider.viewModel())
                .flatMap(new Function<UserSession, SingleSource<UserSession>>() {
                    @Override
                    public SingleSource<UserSession> apply(UserSession userSession) throws Exception {
                        return userSession.login(password);
                    }
                })
                .doOnSuccess(new Consumer<UserSession>() {
                    @Override
                    public void accept(UserSession userSession) throws Exception {
                        mUserSession = userSession;
                    }
                });
    }

    public Observable<UserSession> currentAliveUserSession() {
        return Observable
                .create(new ObservableOnSubscribe<UserSession>() {
                    @Override
                    public void subscribe(ObservableEmitter<UserSession> e) throws Exception {
                        if (mUserSession != null && mUserSession.isAlive()) {
                            e.onNext(mUserSession);
                        }
                    }
                })
                .subscribeOn(mSchedulerProvider.viewModel())
                .observeOn(mSchedulerProvider.viewModel());
    }

    public Completable logout() {
        if (mUserSession == null) {
            throw new AssertionError("Must login firstly!");
        }
        return Single
                .fromCallable(new Callable<UserSession>() {
                    @Override
                    public UserSession call() throws Exception {
                        return mUserSession;
                    }
                })
                .subscribeOn(mSchedulerProvider.viewModel())
                .observeOn(mSchedulerProvider.viewModel())
                .flatMapCompletable(new Function<UserSession, CompletableSource>() {
                    @Override
                    public CompletableSource apply(UserSession userSession) throws Exception {
                        return userSession.logout();
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        mUserSession = null;
                    }
                });
    }
}
