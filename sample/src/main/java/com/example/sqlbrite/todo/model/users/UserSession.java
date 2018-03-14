package com.example.sqlbrite.todo.model.users;

import com.google.auto.value.AutoValue;

/**
 * @author Administrator
 * @date 2018/3/14 18:42
 */

@AutoValue
public abstract class UserSession {

    public static final UserSession FAIL = new NullUserSession();

    private boolean mIsAlive = false;

    public abstract String name();

    public abstract String token();

    abstract UserManager userManager();

    static UserSession login(String name, String token, UserManager userManager) {
        UserSession s =  new AutoValue_UserSession(name, token, userManager);
        s.mIsAlive = true;
        return s;
    }

    public void logout() {
        UserManager mgr = userManager();
        if (mgr != null) {
            mgr.logout();
        }

        mIsAlive = false;
    }

    public boolean isAlive() {
        return mIsAlive;
    }

    private static class NullUserSession extends UserSession {

        @Override
        public String name() {
            return "";
        }

        @Override
        public String token() {
            return "";
        }

        @Override
        UserManager userManager() {
            return null;
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj) || obj == null;
        }
    }
}
