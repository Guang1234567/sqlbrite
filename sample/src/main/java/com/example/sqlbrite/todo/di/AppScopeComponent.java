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
import android.arch.lifecycle.ViewModelProvider;

import com.example.sqlbrite.todo.TodoApp;
import com.example.sqlbrite.todo.model.LoginFlowRepository;
import com.example.sqlbrite.todo.di.model.remote.TodoApiModule.GitHubApiInterface;
import com.example.sqlbrite.todo.model.local.preferences.AppPrefs;
import com.example.sqlbrite.todo.model.users.LoginManager;
import com.example.sqlbrite.todo.model.users.UserManager;
import com.example.sqlbrite.todo.schedulers.SchedulerProvider;
import com.google.gson.Gson;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = AppScopeModule.class)
public interface AppScopeComponent {

    Application application();

    SchedulerProvider schedulerProvider();

    @Named("AppScope")
    ViewModelProvider.Factory appScopeviewModelProviderFactory();

    Gson gson();

    AppPrefs appPrefs();

    GitHubApiInterface gitHubApiInterface();

    UserManager userManager();

    LoginManager loginManager();

    LoginFlowRepository loginFlowRepository();

    void inject(TodoApp app);

    interface Injectable {
        void inject(AppScopeComponent component);
    }
}
