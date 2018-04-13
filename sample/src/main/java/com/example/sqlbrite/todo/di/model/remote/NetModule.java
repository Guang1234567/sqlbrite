/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.sqlbrite.todo.di.model.remote;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.sqlbrite.todo.controler.TodoApiThrowableHandler;
import com.example.sqlbrite.todo.schedulers.SchedulerProvider;
import com.gg.rxbase.net.retrofit.ApiErrorHandlingTransformer;
import com.gg.rxbase.net.retrofit.ApiTransformerCallAdapterFactory;
import com.gg.rxbase.net.retrofit.ObserveOnCallAdapterFactory;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.functions.Consumer;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(
        includes = {
                TodoApiModule.class
        }
)
public final class NetModule {
    private final static String TAG = "NetModule";

    String mBaseUrl;

    // Constructor needs one parameter to instantiate.
    public NetModule(String baseUrl) {
        this.mBaseUrl = baseUrl;
    }

    // Dagger will only look for methods annotated with @Provides
    @Provides
    @Singleton
    // Application reference must come from AppScopeModule.class
    SharedPreferences providesSharedPreferences(Application application) {
        return application.getSharedPreferences(TAG, Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    Cache provideOkHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }

    @Provides
    @Singleton
    TypeAdapterFactory provideTypeAdapterFactory() {
        return MyGsonTypeAdapterFactory.create();
    }

    @Provides
    @Singleton
    Gson provideGson(TypeAdapterFactory typeAdapterFactory) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        gsonBuilder.registerTypeAdapterFactory(typeAdapterFactory);
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    @Named("cached")
    OkHttpClient provideOkHttpClient(Cache cache) {
        return new OkHttpClient.Builder().cache(cache).build();
    }

    @Provides
    @Singleton
    @Named("non_cached")
    OkHttpClient provideOkHttpClientWithoutCache() {
        return new OkHttpClient.Builder().build();
    }

    @Provides
    Consumer<Throwable> provideApiThrowableHandler(Application application) {
        return new TodoApiThrowableHandler(application);
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson,
                             @Named("cached") OkHttpClient okHttpClient,
                             Application application,
                             Consumer<Throwable> apiThrowableHandler,
                             SchedulerProvider schedulerProvider) {
        ApiErrorHandlingTransformer errorHandlingTransformer = new ApiErrorHandlingTransformer(application, apiThrowableHandler);

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(ObserveOnCallAdapterFactory.create(schedulerProvider.io()))
                .addCallAdapterFactory(ApiTransformerCallAdapterFactory.create(errorHandlingTransformer,
                        errorHandlingTransformer,
                        errorHandlingTransformer,
                        errorHandlingTransformer,
                        errorHandlingTransformer))
                .addCallAdapterFactory(ObserveOnCallAdapterFactory.create(schedulerProvider.ui()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .build();
        return retrofit;
    }
}
