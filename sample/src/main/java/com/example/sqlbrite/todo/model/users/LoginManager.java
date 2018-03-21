package com.example.sqlbrite.todo.model.users;

import com.example.sqlbrite.todo.di.model.remote.TodoApiModule.GitHubApiInterface;
import com.example.sqlbrite.todo.model.local.preferences.AppPrefs;
import com.example.sqlbrite.todo.schedulers.SchedulerProvider;

import java.util.Date;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.functions.Consumer;

/**
 * @author Guang1234567
 * @date 2018/3/14 12:31
 */

public interface LoginManager {

    Single<User> login(String userId, String password);

    Completable logout(String userId);

    class LoginManagerImpl implements LoginManager {

        private final GitHubApiInterface mGitHubApiInterface;
        private final AppPrefs mAppPrefs;
        private final SchedulerProvider mSchedulerProvider;

        @Inject
        public LoginManagerImpl(GitHubApiInterface gitHubApiInterface,
                                AppPrefs appPrefs,
                                SchedulerProvider schedulerProvider) {
            mGitHubApiInterface = gitHubApiInterface;
            mAppPrefs = appPrefs;
            mSchedulerProvider = schedulerProvider;
        }

        @Override
        public Single<User> login(String userId, String password) {
            return mGitHubApiInterface.login(userId)
                    .subscribeOn(mSchedulerProvider.io())
                    .onErrorReturnItem(
                            User.builder()
                                    .id(userId + "_伪造")
                                    .name("伪造的人")
                                    .timestamp(new Date())
                                    .build()
                    ).doOnSuccess(new Consumer<User>() {
                        @Override
                        public void accept(User user) throws Exception {
                            mAppPrefs.lastLoginUserId().set(user.id());
                        }
                    });
        }

        @Override
        public Completable logout(String userId) {
            return mGitHubApiInterface.logout(userId)
                    .subscribeOn(mSchedulerProvider.io())
                    .onErrorComplete();
        }
    }
}
