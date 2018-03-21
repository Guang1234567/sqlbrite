package com.example.sqlbrite.todo.model.local.preferences;

import android.support.annotation.NonNull;
import android.util.Base64;

import com.f2prateek.rx.preferences2.Preference;
import com.google.gson.Gson;

import java.lang.reflect.Type;

public final class PreferenceJsonConverter<T> implements Preference.Converter<T> {

    private final Gson mGson;

    private final Type mTypeOfT;

    public PreferenceJsonConverter(Gson gson, Type typeOfT) {
        mGson = gson;
        mTypeOfT = typeOfT;
    }

    @NonNull
    @Override
    public T deserialize(@NonNull String serialized) {
        return mGson.fromJson(new String(Base64.decode(serialized, Base64.DEFAULT)), mTypeOfT);
    }

    @NonNull
    @Override
    public String serialize(@NonNull T value) {
        return Base64.encodeToString(mGson.toJson(value).getBytes(), Base64.DEFAULT);
    }
}
