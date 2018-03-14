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
package com.example.sqlbrite.todo.di.model;

import android.app.Application;

import com.example.sqlbrite.todo.model.users.UserManager;
import com.example.sqlbrite.todo.schedulers.SchedulerProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Observable;

@Module
public final class AppScopeModelModule {

    @Provides
    @Singleton
    public UserManager provideUserManager(Application application, SchedulerProvider schedulerProvider) {
        return new UserManager.UserManagerImpl(application, schedulerProvider);
    }

    @Provides
    @Singleton
    public Observable<UserManager> provideUserManagerObservable(UserManager userManager) {
        return userManager.toObservable();
    }
}
