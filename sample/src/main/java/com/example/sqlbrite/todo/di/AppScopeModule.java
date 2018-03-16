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
package com.example.sqlbrite.todo.di;

import android.app.Application;
import android.content.Context;

import com.example.sqlbrite.todo.controler.LoginViewControler;
import com.example.sqlbrite.todo.di.model.AppScopeModelModule;
import com.example.sqlbrite.todo.di.schedulers.SchedulerModule;
import com.example.sqlbrite.todo.model.users.UserManager;
import com.example.sqlbrite.todo.schedulers.SchedulerProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        includes = {
                SchedulerModule.class,
                AppScopeModelModule.class
        }
)
public final class AppScopeModule {

    private final Application mApplication;

    public AppScopeModule(Application application) {
        this.mApplication = application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    public LoginViewControler provideLoginViewModel(UserManager userManager, SchedulerProvider schedulerProvider) {
        return new LoginViewControler(userManager, schedulerProvider);
    }
}
