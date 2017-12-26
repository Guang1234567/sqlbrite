/*
 * Copyright (C) 2016 The Android Open Source Project
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

package android.arch.persistence.db.sqlcipher;

import android.arch.persistence.db.SupportSQLiteOpenHelper;

/**
 * Implements {@link SupportSQLiteOpenHelper.Factory} using the SQLite implementation in the
 * framework.
 */
@SuppressWarnings("unused")
public final class SqlcipherSQLiteOpenHelperFactory implements SupportSQLiteOpenHelper.Factory {
    private static final String DEFAULT_PWD = "Password_1234567";

    @Override
    public SupportSQLiteOpenHelper create(SupportSQLiteOpenHelper.Configuration configuration) {
        return new SqlcipherSQLiteOpenHelper(
                configuration.context, configuration.name, configuration.callback, DEFAULT_PWD);
    }

    public SupportSQLiteOpenHelper create(SupportSQLiteOpenHelper.Configuration configuration, String password) {
        return new SqlcipherSQLiteOpenHelper(
                configuration.context, configuration.name, configuration.callback, password);
    }
}
