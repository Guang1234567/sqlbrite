package com.example.sqlbrite.todo.model.local.preferences;

import android.graphics.Point;

import com.f2prateek.rx.preferences2.Preference;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.google.gson.Gson;

/**
 * @author Guang1234567
 * @date 2018/3/21 12:35
 */

public class AppPrefs {

    private final RxSharedPreferences mPreferences;

    private final Gson mGson;

    public AppPrefs(RxSharedPreferences preferences, Gson gson) {
        mPreferences = preferences;
        mGson = gson;
    }

    public Preference<String> lastLoginUserId() {
        return mPreferences.getString("last_login_userid");
    }

    public Preference<Point> lastPoint() {
        return mPreferences.getObject("last_point", new Point(0, 0), new PreferenceJsonConverter<Point>(mGson, Point.class));
    }
}
