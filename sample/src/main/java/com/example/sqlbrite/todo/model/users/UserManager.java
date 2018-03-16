package com.example.sqlbrite.todo.model.users;

import android.app.Application;

import com.example.sqlbrite.todo.di.InjectHelper;
import com.example.sqlbrite.todo.di.UserScopeComponent;
import com.example.sqlbrite.todo.di.model.remote.TodoApiModule.GitHubApiInterface;
import com.example.sqlbrite.todo.schedulers.SchedulerProvider;

import java.util.Date;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @author Guang1234567
 * @date 2018/3/14 12:31
 */

public interface UserManager {

    Single<UserSession> startSessionForUser(String username);

    UserScopeComponent getUserScopeComponent();

    Completable closeUserSession(UserSession userSession);

    class UserManagerImpl implements UserManager {

        private final GitHubApiInterface mGitHubApiInterface;

        private final SchedulerProvider mSchedulerProvider;

        private UserScopeComponent mUserScopeComponent;

        @Inject
        public UserManagerImpl(Application application, GitHubApiInterface gitHubApiInterface, SchedulerProvider schedulerProvider) {
            mGitHubApiInterface = gitHubApiInterface;
            mSchedulerProvider = schedulerProvider;
        }

        @Override
        public Single<UserSession> startSessionForUser(String userId) {
            return mGitHubApiInterface.login(userId)
                    .subscribeOn(mSchedulerProvider.net())
                    .onErrorReturnItem(
                            User.builder()
                                    .id(userId)
                                    .name("伪造的人")
                                    .timestamp(new Date())
                                    .build()
                    )
                    .observeOn(mSchedulerProvider.ui())
                    .flatMap(new Function<User, Single<UserSession>>() {
                        @Override
                        public Single<UserSession> apply(User user) throws Exception {
                            return Single.just(UserSession.create(user, UserManagerImpl.this));
                        }
                    })
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
            return mGitHubApiInterface.logout(userSession.user().id())
                    .subscribeOn(mSchedulerProvider.net())
                    .observeOn(mSchedulerProvider.ui())
                    .doOnComplete(new Action() {
                        @Override
                        public void run() throws Exception {
                            oncloseUserSession(userSession);
                        }
                    });
        }

        private void onStartUserSession(UserSession userSession) {
            mUserScopeComponent = InjectHelper.instance().createUserScopeComponent(userSession.user().id());
        }

        public void oncloseUserSession(UserSession userSession) {
            mUserScopeComponent = null;
        }
    }
}
