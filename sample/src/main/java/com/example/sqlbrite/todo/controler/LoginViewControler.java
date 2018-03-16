package com.example.sqlbrite.todo.controler;

import com.example.sqlbrite.todo.model.users.UserManager;
import com.example.sqlbrite.todo.model.users.UserSession;
import com.example.sqlbrite.todo.schedulers.SchedulerProvider;
import com.gg.rxbase.controller.RxBaseViewModel;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

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
        return mUserManager.startSessionForUser(userId)
                .observeOn(mSchedulerProvider.viewModel())
                .doOnSuccess(new Consumer<UserSession>() {
                    @Override
                    public void accept(UserSession userSession) throws Exception {
                        mUserSession = userSession;
                    }
                });
    }

    public boolean isUserSessionAlive() {
        return mUserSession != null;
    }

    public Completable logout() {
        return mUserManager.closeUserSession(mUserSession)
                .observeOn(mSchedulerProvider.viewModel())
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        mUserSession = null;
                    }
                });
    }
}
