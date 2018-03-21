package com.example.sqlbrite.todo.model.local.preferences;

import com.f2prateek.rx.preferences2.Preference;
import com.f2prateek.rx.preferences2.RxSharedPreferences;

/**
 * @author Guang1234567
 * @date 2018/3/21 12:36
 */

public class UserPrefs {

    private final RxSharedPreferences mPreferences;

    public UserPrefs(RxSharedPreferences preferences) {
        mPreferences = preferences;
    }
}
