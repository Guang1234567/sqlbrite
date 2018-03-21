package com.example.sqlbrite.todo.model.local.preferences;

import com.f2prateek.rx.preferences2.Preference;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.google.gson.Gson;

/**
 * @author Guang1234567
 * @date 2018/3/21 12:36
 */

public class UserPrefs {

    private final RxSharedPreferences mPreferences;

    private final Gson mGson;

    public UserPrefs(RxSharedPreferences preferences, Gson gson) {
        mPreferences = preferences;
        mGson = gson;
    }
}
