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
package com.example.sqlbrite.todo;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.example.sqlbrite.todo.di.AppScopeComponent;
import com.example.sqlbrite.todo.di.InjectHelper;

import timber.log.Timber;

public final class TodoApp extends MultiDexApplication {

    private AppScopeComponent mAppScopeComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        mAppScopeComponent = InjectHelper.instance()
                .init(this);
    }
}
