package com.example.sqlbrite.todo.model.users;

import com.example.sqlbrite.todo.di.InjectHelper;
import com.example.sqlbrite.todo.di.UserScopeComponent;

import io.reactivex.Completable;
import io.reactivex.Single;
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

    private boolean mIsLogin;

    private UserScopeComponent mUserScopeComponent;

    UserSession(
            User user,
            UserManager userManager,
            LoginManager loginManager) {
        mUser = user;
        mUserManager = userManager;
        mLoginManager = loginManager;
        mIsLogin = false;

        mUserScopeComponent = InjectHelper.instance().createUserScopeComponent(user.id());
    }

    public User user() {
        return mUser;
    }

    public UserScopeComponent getUserScopeComponent() {
        return mUserScopeComponent;
    }

    static UserSession create(String userId, UserManager userManager, LoginManager loginManager) {
        UserSession s = new UserSession(User.builder().id(userId).build(), userManager, loginManager);
        return s;
    }

    public Single<UserSession> login(final String password) {
        if (mIsLogin) {
            throw new AssertionError("Must logout firstly!");
        }

        return mLoginManager.login(mUser.id(), password)
                .map(new Function<User, UserSession>() {
                    @Override
                    public UserSession apply(User user) throws Exception {
                        UserSession.this.mUser = user;
                        mIsLogin = true;
                        return UserSession.this;
                    }
                });
    }

    public boolean isLogin() {
        return mIsLogin;
    }

    public Completable logout() {
        if (!mIsLogin) {
            throw new AssertionError("Must login firstly!");
        }

        return mLoginManager.logout(user().id())
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        mIsLogin = false;
                    }
                });
    }
}
