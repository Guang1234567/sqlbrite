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

import com.example.sqlbrite.todo.di.model.remote.NetModule;
import com.example.sqlbrite.todo.di.model.remote.TodoApiModule.GitHubApiInterface;
import com.example.sqlbrite.todo.model.users.LoginManager;
import com.example.sqlbrite.todo.model.users.UserManager;
import com.example.sqlbrite.todo.schedulers.SchedulerProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        includes = {
                NetModule.class
        }
)
public final class AppScopeModelModule {

    @Provides
    @Singleton
    public LoginManager provideLoginManager(GitHubApiInterface gitHubApiInterface, SchedulerProvider schedulerProvider) {
        return new LoginManager.LoginManagerImpl(gitHubApiInterface, schedulerProvider);
    }

    @Provides
    @Singleton
    public UserManager provideUserManager(LoginManager loginManager, SchedulerProvider schedulerProvider) {
        return new UserManager.UserManagerImpl(loginManager, schedulerProvider);
    }
}
