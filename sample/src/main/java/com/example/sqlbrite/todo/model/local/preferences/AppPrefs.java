package com.example.sqlbrite.todo.model.local.preferences;

import com.f2prateek.rx.preferences2.Preference;
import com.f2prateek.rx.preferences2.RxSharedPreferences;

/**
 * @author Administrator
 * @date 2018/3/21 12:35
 */

public class AppPrefs {

    private final RxSharedPreferences mPreferences;

    public AppPrefs(RxSharedPreferences preferences) {
        mPreferences = preferences;
    }

    public Preference<String> lastLoginUserId() {
        return mPreferences.getString("last_login_userid");
    }
}
