package com.example.sqlbrite.todo.model.users;

import android.app.Application;
import android.support.annotation.NonNull;

import com.example.sqlbrite.todo.schedulers.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * @author Administrator
 * @date 2018/3/14 12:31
 */

public interface UserManager {

    UserSession login(String userId, String password);

    void logout();

    UserSession curSession();

    Observable<UserManager> toObservable();

    class UserManagerImpl implements UserManager {

        private final SchedulerProvider mSchedulerProvider;

        private UserSession mUserSession;

        @Inject
        public UserManagerImpl(Application application, SchedulerProvider schedulerProvider) {
            mSchedulerProvider = schedulerProvider;

            mUserSession = UserSession.FAIL;
        }

        @NonNull
        @Override
        public UserSession login(String userId, String password) {
            if (UserSession.FAIL.equals(mUserSession)) {
                // TODO login here
                mUserSession = UserSession.login(userId, password, this);
            }
            return mUserSession;
        }

        @Override
        public void logout() {
            mUserSession = UserSession.FAIL;
        }

        @NonNull
        @Override
        public UserSession curSession() {
            return mUserSession;
        }

        @Override
        public Observable<UserManager> toObservable() {
            return Observable.create(new ObservableOnSubscribe<UserManager>() {
                @Override
                public void subscribe(ObservableEmitter<UserManager> e) throws Exception {
                    e.onNext(UserManagerImpl.this);
                    if (!e.isDisposed()) {
                        e.onComplete();
                    }
                }
            });
        }
    }
}
