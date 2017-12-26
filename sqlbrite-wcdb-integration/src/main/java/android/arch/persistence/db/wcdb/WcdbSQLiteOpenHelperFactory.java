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

package android.arch.persistence.db.wcdb;

import android.arch.persistence.db.SupportSQLiteOpenHelper;

import com.tencent.wcdb.database.SQLiteCipherSpec;

/**
 * Implements {@link SupportSQLiteOpenHelper.Factory} using the SQLite implementation in the
 * framework.
 */
@SuppressWarnings("unused")
public final class WcdbSQLiteOpenHelperFactory implements SupportSQLiteOpenHelper.Factory {
    @Override
    public SupportSQLiteOpenHelper create(SupportSQLiteOpenHelper.Configuration configuration) {
        return new WcdbSQLiteOpenHelper(
                configuration.context, configuration.name, configuration.callback);
    }

    public SupportSQLiteOpenHelper create(SupportSQLiteOpenHelper.Configuration configuration, String password) {
        SQLiteCipherSpec cipher = new SQLiteCipherSpec()  // 加密描述对象
                .setPageSize(1024)        // SQLCipher 默认 Page size 为 1024
                .setSQLCipherVersion(3);  // 1,2,3 分别对应 1.x, 2.x, 3.x 创建的 SQLCipher 数据库
        return new WcdbSQLiteOpenHelper(
                configuration.context, configuration.name, configuration.callback, password, cipher);
    }
}
