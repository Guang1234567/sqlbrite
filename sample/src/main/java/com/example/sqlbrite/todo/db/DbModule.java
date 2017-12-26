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
package com.example.sqlbrite.todo.db;

import android.app.Application;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Configuration;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Factory;
import android.arch.persistence.db.framework.FrameworkSQLiteOpenHelperFactory;
import android.arch.persistence.db.sqlcipher.SqlcipherSQLiteOpenHelperFactory;
import android.arch.persistence.db.wcdb.WcdbSQLiteOpenHelperFactory;

import com.squareup.sqlbrite3.BriteDatabase;
import com.squareup.sqlbrite3.SqlBrite;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;

import javax.inject.Singleton;

import timber.log.Timber;

@Module
public final class DbModule {
    @Provides
    @Singleton
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
    @Singleton
    BriteDatabase provideDatabase(SqlBrite sqlBrite, Application application) {

        // 原生
        /*Configuration configuration = Configuration.builder(application)
            .name("todo.db")
            .callback(new DbCallback())
            .build();

        Factory factory = new FrameworkSQLiteOpenHelperFactory();
        SupportSQLiteOpenHelper helper = factory.create(configuration);

        BriteDatabase db = sqlBrite.wrapDatabaseHelper(helper, Schedulers.io());
        db.setLoggingEnabled(true);*/


        // 加密
        /*Configuration configuration_sqlcipher = Configuration.builder(application)
                .name("todo_sqlcipher.db")
                .callback(new DbCallback())
                .build();

        SqlcipherSQLiteOpenHelperFactory factory_sqlcipher = new SqlcipherSQLiteOpenHelperFactory();
        SupportSQLiteOpenHelper helper_sqlcipher = factory_sqlcipher.create(configuration_sqlcipher, "Passsword_1234567");

        BriteDatabase db_sqlcipher = sqlBrite.wrapDatabaseHelper(helper_sqlcipher, Schedulers.io());
        db_sqlcipher.setLoggingEnabled(true);*/


        // wcdb
        /*Configuration configuration_wcdb = Configuration.builder(application)
                .name("todo_wcdb.db")
                .callback(new DbCallback())
                .build();

        WcdbSQLiteOpenHelperFactory factory_wcdb = new WcdbSQLiteOpenHelperFactory();
        SupportSQLiteOpenHelper helper_wcdb = factory_wcdb.create(configuration_wcdb);

        BriteDatabase db_wcdb = sqlBrite.wrapDatabaseHelper(helper_wcdb, Schedulers.io());
        db_wcdb.setLoggingEnabled(true);*/


        // wcdb cipher
        Configuration configuration_wcdb_cipher = Configuration.builder(application)
                .name("todo_wcdb_cipher.db")
                .callback(new DbCallback())
                .build();

        WcdbSQLiteOpenHelperFactory factory_wcdb_cipher = new WcdbSQLiteOpenHelperFactory();
        SupportSQLiteOpenHelper helper_wcdb_cipher = factory_wcdb_cipher.create(configuration_wcdb_cipher, "Passsword_7788");

        BriteDatabase db_wcdb_cipher = sqlBrite.wrapDatabaseHelper(helper_wcdb_cipher, Schedulers.io());
        db_wcdb_cipher.setLoggingEnabled(true);

        return db_wcdb_cipher;
    }
}
