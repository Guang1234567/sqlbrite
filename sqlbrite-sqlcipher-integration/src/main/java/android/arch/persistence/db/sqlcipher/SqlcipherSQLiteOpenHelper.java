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

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import net.sqlcipher.DatabaseErrorHandler;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

class SqlcipherSQLiteOpenHelper implements SupportSQLiteOpenHelper {
    private final OpenHelper mDelegate;

    SqlcipherSQLiteOpenHelper(Context context, String name,
                              Callback callback, String password) {
        mDelegate = createDelegate(context, name, callback, password);
    }

    private OpenHelper createDelegate(Context context, String name, Callback callback, String password) {
        final SqlcipherSQLiteDatabase[] dbRef = new SqlcipherSQLiteDatabase[1];
        return new OpenHelper(context, name, dbRef, callback, password);
    }

    @Override
    public String getDatabaseName() {
        throw new RuntimeException("Stub!");
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setWriteAheadLoggingEnabled(boolean enabled) {
        throw new RuntimeException("Stub!");
    }

    @Override
    public SupportSQLiteDatabase getWritableDatabase() {
        return mDelegate.getWritableSupportDatabase();
    }

    @Override
    public SupportSQLiteDatabase getReadableDatabase() {
        return mDelegate.getReadableSupportDatabase();
    }

    @Override
    public void close() {
        mDelegate.close();
    }

    static class OpenHelper extends SQLiteOpenHelper {
        private static boolean loadLibs = true;

        /**
         * This is used as an Object reference so that we can access the wrapped database inside
         * the constructor. SQLiteOpenHelper requires the error handler to be passed in the
         * constructor.
         */
        final SqlcipherSQLiteDatabase[] mDbRef;
        final Callback mCallback;

        final String mPassword;

        OpenHelper(Context context, String name, final SqlcipherSQLiteDatabase[] dbRef,
                   final Callback callback, final String password) {
            super(context, name, null, callback.version, null,
                    new DatabaseErrorHandler() {
                        @Override
                        public void onCorruption(SQLiteDatabase dbObj) {
                            SqlcipherSQLiteDatabase db = dbRef[0];
                            if (db != null) {
                                callback.onCorruption(db);
                            }
                        }
                    });

            if (loadLibs) {
                net.sqlcipher.database.SQLiteDatabase.loadLibs(context);
                loadLibs = false; // only load once
            }

            mCallback = callback;
            mDbRef = dbRef;

            mPassword = password;
        }

        SupportSQLiteDatabase getWritableSupportDatabase() {
            SQLiteDatabase db = super.getWritableDatabase(mPassword);
            return getWrappedDb(db);
        }

        SupportSQLiteDatabase getReadableSupportDatabase() {
            SQLiteDatabase db = super.getReadableDatabase(mPassword);
            return getWrappedDb(db);
        }

        SqlcipherSQLiteDatabase getWrappedDb(SQLiteDatabase sqLiteDatabase) {
            SqlcipherSQLiteDatabase dbRef = mDbRef[0];
            if (dbRef == null) {
                dbRef = new SqlcipherSQLiteDatabase(sqLiteDatabase);
                mDbRef[0] = dbRef;
            }
            return mDbRef[0];
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            mCallback.onCreate(getWrappedDb(sqLiteDatabase));
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            mCallback.onUpgrade(getWrappedDb(sqLiteDatabase), oldVersion, newVersion);
        }


        public void onConfigure(SQLiteDatabase db) {
            mCallback.onConfigure(getWrappedDb(db));
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            mCallback.onDowngrade(getWrappedDb(db), oldVersion, newVersion);
        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            mCallback.onOpen(getWrappedDb(db));
        }

        @Override
        public synchronized void close() {
            super.close();
            mDbRef[0] = null;
        }
    }
}
