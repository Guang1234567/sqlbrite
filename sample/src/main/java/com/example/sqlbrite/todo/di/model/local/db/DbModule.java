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
package com.example.sqlbrite.todo.di.model.local.db;

import android.app.Application;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Configuration;
import android.arch.persistence.db.wcdb.WcdbSQLiteOpenHelperFactory;

import com.example.sqlbrite.todo.di.UserScope;
import com.example.sqlbrite.todo.model.local.db.DbCallback;
import com.example.sqlbrite.todo.model.local.db.TodoItemDao;
import com.example.sqlbrite.todo.model.local.db.TodoListDao;
import com.example.sqlbrite.todo.schedulers.SchedulerProvider;
import com.example.sqlbrite.todo.ui.ListsItemDao;
import com.squareup.sqlbrite3.BriteDatabase;
import com.squareup.sqlbrite3.SqlBrite;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import timber.log.Timber;

@Module
public final class DbModule {

    private final String mDatabaseName;

    public DbModule(String databaseName) {
        mDatabaseName = databaseName;
    }

    @Provides
    @UserScope
    @Named("database_name")
    String provideDatabaseName() {
        return mDatabaseName;
    }

    @Provides
    @UserScope
    SqlBrite provideSqlBrite() {
        return new SqlBrite.Builder()
                .logger(new SqlBrite.Logger() {
                    @Override
                    public void log(String message) {
                        Timber.tag("Database").v(message);
                    }
                })
                .build();
    }

    @Provides
    @UserScope
    SupportSQLiteOpenHelper provideSupportSQLiteOpenHelper(Application application, @Named("database_name") String databaseName) {
        // 1) android native sqlite, no cipher
        /*
        Configuration configuration = Configuration.builder(application)
            .name("todo.db")
            .callback(new DbCallback())
            .build();

        Factory factory = new FrameworkSQLiteOpenHelperFactory();
        SupportSQLiteOpenHelper helper = factory.create(configuration);

        BriteDatabase db = sqlBrite.wrapDatabaseHelper(helper, Schedulers.io());
        db.setLoggingEnabled(true);
        //*/


        // 2) SQLCipher is an SQLite extension that provides 256 bit AES encryption of database files.
        /*
        Configuration configuration_sqlcipher = Configuration.builder(application)
                .name("todo_sqlcipher.db")
                .callback(new DbCallback())
                .build();

        SqlcipherSQLiteOpenHelperFactory factory_sqlcipher = new SqlcipherSQLiteOpenHelperFactory();
        SupportSQLiteOpenHelper helper_sqlcipher = factory_sqlcipher.create(configuration_sqlcipher, "Passsword_1234567");

        BriteDatabase db_sqlcipher = sqlBrite.wrapDatabaseHelper(helper_sqlcipher, Schedulers.io());
        db_sqlcipher.setLoggingEnabled(true);
        //*/


        // 3) wcdb base on SQLCipher, no cipher
        /*
        Configuration configuration_wcdb = Configuration.builder(application)
                .name("todo_wcdb.db")
                .callback(new DbCallback())
                .build();

        WcdbSQLiteOpenHelperFactory factory_wcdb = new WcdbSQLiteOpenHelperFactory();
        SupportSQLiteOpenHelper helper_wcdb = factory_wcdb.create(configuration_wcdb);

        BriteDatabase db_wcdb = sqlBrite.wrapDatabaseHelper(helper_wcdb, Schedulers.io());
        db_wcdb.setLoggingEnabled(true);
        //*/


        // 4) wcdb base on SQLCipher, has cipher
        Configuration configuration_wcdb_cipher = Configuration.builder(application)
                .name("todo_wcdb_cipher_" + databaseName + ".db")
                .callback(new DbCallback())
                .build();

        WcdbSQLiteOpenHelperFactory factory_wcdb_cipher = new WcdbSQLiteOpenHelperFactory();
        return factory_wcdb_cipher.create(configuration_wcdb_cipher, "Passsword_7654321");
    }

    @Provides
    @UserScope
    BriteDatabase provideDatabase(SqlBrite sqlBrite, SupportSQLiteOpenHelper helper, SchedulerProvider schedulerProvider) {
        BriteDatabase briteDatabase = sqlBrite.wrapDatabaseHelper(helper, schedulerProvider.database());
        briteDatabase.setLoggingEnabled(true);
        return briteDatabase;
    }

    /*@Provides
    @UserScope
    BriteDatabaseProvider provideDatabaseProvider(SqlBrite sqlBrite, SupportSQLiteOpenHelper helper, SchedulerProvider schedulerProvider) {
        return new BriteDatabaseProvider(sqlBrite, helper, schedulerProvider.database());
    }*/

    @Provides
    @UserScope
    TodoListDao provideTodoListDao(BriteDatabase db) {
        return new TodoListDao(db);
    }

    @Provides
    @UserScope
    TodoItemDao provideTodoItemDao(BriteDatabase db) {
        return new TodoItemDao(db);
    }

    @Provides
    @UserScope
    ListsItemDao provideListsItemDao(BriteDatabase db) {
        return new ListsItemDao(db);
    }
}
