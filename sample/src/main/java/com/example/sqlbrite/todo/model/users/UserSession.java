package com.example.sqlbrite.todo.model.users;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

/**
 * @author Guang1234567
 * @date 2018/3/14 18:42
 */
public final class UserSession {

    private User mUser;

    private UserManager mUserManager;

    private LoginManager mLoginManager;

    private boolean mIsAlive;

    UserSession(
            User user,
            UserManager userManager,
            LoginManager loginManager) {
        if (user == null) {
            throw new NullPointerException("Null user");
        }
        mUser = user;

        if (userManager == null) {
            throw new NullPointerException("Null userManager");
        }
        mUserManager = userManager;

        if (loginManager == null) {
            throw new NullPointerException("Null loginManager");
        }
        mLoginManager = loginManager;

        mIsAlive = false;
    }

    public User user() {
        return mUser;
    }

    static UserSession create(String userId, UserManager userManager, LoginManager loginManager) {
        UserSession s = new UserSession(User.builder().id(userId).build(), userManager, loginManager);
        return s;
    }

    UserManager userManager() {
        return mUserManager;
    }

    LoginManager loginManager() {
        return mLoginManager;
    }

    public Single<UserSession> login(final String password) {
        if (mIsAlive) {
            throw new AssertionError("Must logout firstly!");
        }

        return userManager().startSessionForUser(UserSession.this)
                .flatMap(new Function<UserSession, SingleSource<User>>() {
                    @Override
                    public SingleSource<User> apply(UserSession userSession) throws Exception {
                        return loginManager().login(userSession.user().id(), password);
                    }
                })
                .map(new Function<User, UserSession>() {
                    @Override
                    public UserSession apply(User user) throws Exception {
                        UserSession.this.mUser = user;
                        mIsAlive = true;
                        return UserSession.this;
                    }
                });
    }

    public boolean isAlive() {
        return mIsAlive;
    }

    public Completable logout() {
        if (!mIsAlive) {
            throw new AssertionError("Must login firstly!");
        }

        return loginManager().logout(user().id())
                .andThen(userManager().closeUserSession(this))
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        mIsAlive = false;
                    }
                });
    }
}
