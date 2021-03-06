package com.example.sqlbrite.todo.model.users;

import com.example.sqlbrite.todo.schedulers.SchedulerProvider;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * @author Guang1234567
 * @date 2018/3/14 12:31
 */

public interface UserManager {

    Single<UserSession> startSessionForUser(String userId);

    Completable closeUserSession(UserSession userSession);

    class UserManagerImpl implements UserManager {

        private final LoginManager mLoginManager;

        private final SchedulerProvider mSchedulerProvider;

        private List<UserSession> mAliveSessions;

        @Inject
        public UserManagerImpl(LoginManager loginManager, SchedulerProvider schedulerProvider) {
            mLoginManager = loginManager;
            mSchedulerProvider = schedulerProvider;

            mAliveSessions = new LinkedList<>();
        }

        @Override
        public Single<UserSession> startSessionForUser(final String userId) {
            return Single
                    .<UserSession>create(e -> {
                        UserSession us = UserSession.create(userId, UserManagerImpl.this, mLoginManager);
                        onStartUserSession(us);
                        e.onSuccess(us);
                    })
                    .subscribeOn(mSchedulerProvider.single());
        }

        @Override
        public Completable closeUserSession(final UserSession userSession) {
            return Completable
                    .create(e -> {
                        onCloseUserSession(userSession);
                        e.onComplete();
                    })
                    .subscribeOn(mSchedulerProvider.single());
        }

        private void onStartUserSession(UserSession userSession) {
            mAliveSessions.add(userSession);
        }

        public void onCloseUserSession(UserSession userSession) {
            mAliveSessions.remove(userSession);
        }
    }
}
