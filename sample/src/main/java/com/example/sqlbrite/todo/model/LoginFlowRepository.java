package com.example.sqlbrite.todo.model;

import com.example.sqlbrite.todo.di.controler.ShareViewModel;
import com.example.sqlbrite.todo.model.local.preferences.AppPrefs;
import com.example.sqlbrite.todo.model.users.UserManager;
import com.example.sqlbrite.todo.model.users.UserSession;
import com.example.sqlbrite.todo.schedulers.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @author Guang1234567
 * @date 2018/3/6 14:40
 */

public class LoginFlowRepository {

    private final UserManager mUserManager;
    private final AppPrefs mAppPrefs;
    private final SchedulerProvider mSchedulerProvider;

    private UserSession mUserSession = null;

    @Inject
    public LoginFlowRepository(UserManager userManager,
                               AppPrefs appPrefs,
                               SchedulerProvider schedulerProvider) {
        mUserManager = userManager;
        mAppPrefs = appPrefs;
        mSchedulerProvider = schedulerProvider;
    }

    public Single<UserSession> login(final String userId, final String password) {
        if (mUserSession != null) {
            throw new AssertionError("Must logout firstly!");
        }

        return mUserManager.startSessionForUser(userId)
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
                        mAppPrefs.lastLoginUserId().set(userSession.user().id());
                    }
                });
    }

    public Observable<UserSession> currentLoginUserSession() {
        return Observable
                .<UserSession>fromCallable(() -> {
                    return mUserSession;
                })
                .filter(us -> {
                    return us != null && us.isLogin();
                });
    }

    public Completable logout() {
        if (mUserSession == null) {
            throw new AssertionError("Must login firstly!");
        }
        return Single
                .fromCallable(() -> {
                    return mUserSession;
                })
                .subscribeOn(mSchedulerProvider.viewModel())
                .observeOn(mSchedulerProvider.viewModel())
                .flatMapCompletable(userSession -> {
                    return userSession.logout();
                })
                .andThen(mUserManager.closeUserSession(mUserSession))
                .doOnComplete(() -> {
                    mUserSession = null;
                });
    }
}
