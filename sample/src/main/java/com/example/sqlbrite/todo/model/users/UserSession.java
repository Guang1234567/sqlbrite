package com.example.sqlbrite.todo.model.users;

import com.example.sqlbrite.todo.di.UserScopeComponent;
import com.google.auto.value.AutoValue;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * @author Guang1234567
 * @date 2018/3/14 18:42
 */

@AutoValue
public abstract class UserSession {

    public abstract User user();

    abstract UserManager userManager();

    public static UserSession create(User user, UserManager userManager) {
        UserSession s = new AutoValue_UserSession(user, userManager);
        return s;
    }
}
