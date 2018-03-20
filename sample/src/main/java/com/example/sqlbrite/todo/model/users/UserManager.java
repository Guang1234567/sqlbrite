package com.example.sqlbrite.todo.model.users;

import com.example.sqlbrite.todo.di.InjectHelper;
import com.example.sqlbrite.todo.di.UserScopeComponent;
import com.example.sqlbrite.todo.schedulers.SchedulerProvider;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * @author Guang1234567
 * @date 2018/3/14 12:31
 */

public interface UserManager {

    Single<UserSession> createSessionForUser(String userId);

    Single<UserSession> startSessionForUser(UserSession userSession);

    UserScopeComponent getUserScopeComponent();

    Completable closeUserSession(UserSession userSession);

    class UserManagerImpl implements UserManager {

        private final LoginManager mLoginManager;

        private final SchedulerProvider mSchedulerProvider;

        private List<UserSession> mAliveSessions;

        private UserScopeComponent mUserScopeComponent;

        @Inject
        public UserManagerImpl(LoginManager loginManager, SchedulerProvider schedulerProvider) {
            mLoginManager = loginManager;
            mSchedulerProvider = schedulerProvider;
            mAliveSessions = new LinkedList<>();
        }

        @Override
        public Single<UserSession> createSessionForUser(final String userId) {
            return Single.<UserSession>create(new SingleOnSubscribe<UserSession>() {
                @Override
                public void subscribe(SingleEmitter<UserSession> e) throws Exception {
                    e.onSuccess(UserSession.create(userId, UserManagerImpl.this, mLoginManager));
                }
            });
        }

        @Override
        public Single<UserSession> startSessionForUser(UserSession session) {
            return Single.just(session)
                    .doOnSuccess(new Consumer<UserSession>() {
                        @Override
                        public void accept(UserSession userSession) throws Exception {
                            onStartUserSession(userSession);
                        }
                    });
        }

        @Override
        public UserScopeComponent getUserScopeComponent() {
            return mUserScopeComponent;
        }

        @Override
        public Completable closeUserSession(final UserSession userSession) {
            return Single.just(userSession)
                    .doOnSuccess(new Consumer<UserSession>() {
                        @Override
                        public void accept(UserSession userSession) throws Exception {
                            onCloseUserSession(userSession);
                        }
                    })
                    .toCompletable();
        }

        private void onStartUserSession(UserSession userSession) {
            mUserScopeComponent = InjectHelper.instance().createUserScopeComponent(userSession.user().id());
            mAliveSessions.add(userSession);
        }

        public void onCloseUserSession(UserSession userSession) {
            mAliveSessions.remove(userSession);
            mUserScopeComponent = null;
        }
    }
}
