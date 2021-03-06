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
package com.example.sqlbrite.todo.di.model.local.preferences;

import android.app.Application;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.example.sqlbrite.todo.model.local.preferences.AppPrefs;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.google.gson.Gson;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public final class AppScopePreferencesModule {

    @Provides
    @Singleton
    @Named("app_scope")
    public SharedPreferences provideSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    @Named("app_scope")
    public RxSharedPreferences provideRxSharedPreferences(@Named("app_scope") SharedPreferences sharedPreferences) {
        return RxSharedPreferences.create(sharedPreferences);
    }

    @Provides
    @Singleton
    public AppPrefs provideAppPrefs(@Named("app_scope") RxSharedPreferences sharedPreferences, Gson gson) {
        return new AppPrefs(sharedPreferences, gson);
    }
}
