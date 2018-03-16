package com.example.sqlbrite.todo.di.model.remote;

import com.google.gson.TypeAdapterFactory;
import com.ryanharter.auto.value.gson.GsonTypeAdapterFactory;

/**
 * @author Guang1234567
 * @date 2017/5/4 19:22:20
 */

@GsonTypeAdapterFactory
abstract class MyGsonTypeAdapterFactory implements TypeAdapterFactory {

    public static TypeAdapterFactory create() {
        return new AutoValueGson_MyGsonTypeAdapterFactory();
    }
}
