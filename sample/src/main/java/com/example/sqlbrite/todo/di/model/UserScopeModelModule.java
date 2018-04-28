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

import com.example.sqlbrite.todo.di.UserScope;
import com.example.sqlbrite.todo.di.model.local.db.DbModule;
import com.example.sqlbrite.todo.di.model.local.preferences.UserScopePreferencesModule;
import com.example.sqlbrite.todo.model.DbMainRepository;
import com.example.sqlbrite.todo.model.MainDataSource;
import com.example.sqlbrite.todo.model.MainRepository;
import com.example.sqlbrite.todo.model.MemoryMainRepository;
import com.example.sqlbrite.todo.model.local.db.TodoItemDao;
import com.example.sqlbrite.todo.model.local.db.TodoListDao;
import com.example.sqlbrite.todo.ui.ListsItemDao;
import com.squareup.sqlbrite3.BriteDatabase;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module(
        includes = {
                DbModule.class,
                UserScopePreferencesModule.class,
        }
)
public final class UserScopeModelModule {

    @Provides
    @UserScope
    DbMainRepository provideDbMainRepository(BriteDatabase briteDatabaseb, ListsItemDao listsItemDao, TodoListDao todoListDao, TodoItemDao todoItemDao) {
        return new DbMainRepository(briteDatabaseb, listsItemDao, todoListDao, todoItemDao);
    }

    @Named("DbMainRepository")
    @Provides
    @UserScope
    MemoryMainRepository provideMemoryMainRepository() {
        return new MemoryMainRepository();
    }

    @Provides
    @UserScope
    MainDataSource provideMainDataSource(DbMainRepository dbMainRepository, MemoryMainRepository memoryMainRepository) {
        return new MainRepository(dbMainRepository, memoryMainRepository);
    }
}
