package com.squareup.sqlbrite3.support.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.squareup.sqlbrite3.BriteDatabase;
import com.squareup.sqlbrite3.QueryObservable;

import java.lang.reflect.ParameterizedType;
import java.util.LinkedList;
import java.util.List;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_NONE;

/**
 * @author Guang1234567
 * @date 2018/3/5 15:21
 */

public abstract class BriteDaoSupport<ENTITY> {
    private BriteDatabase mDatabase;

    private Class mEntityClazz;

    private String mTableName;

    private final String SQL_QUERY;

    public BriteDaoSupport(BriteDatabase database) {
        mDatabase = database;

        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        mEntityClazz = (Class) type.getActualTypeArguments()[0];

        mTableName = getTableName(mEntityClazz);

        SQL_QUERY = new StringBuilder("SELECT * FROM ").append(mTableName).toString();
    }

    final public long insert(ENTITY t) {
        ContentValues cvsWithoutRowId = toContentValues(t);
        cvsWithoutRowId.remove(BaseColumns._ID);
        return mDatabase.insert(mTableName, CONFLICT_NONE, cvsWithoutRowId);
    }

    final public int deleteById(long rowId) {
        return mDatabase.delete(mTableName, BaseColumns._ID + " = " + rowId);
    }

    final protected int delete(@Nullable String whereClause,
                               @Nullable String... whereArgs) {
        return mDatabase.delete(mTableName, whereClause, whereArgs);
    }

    final public int update(ENTITY t) {
        return mDatabase.update(mTableName,
                CONFLICT_NONE,
                toContentValues(t),
                BaseColumns._ID + " = ?",
                new String[]{String.valueOf(getRowId(t))});
    }

    protected int update(int conflictAlgorithm,
                         @NonNull ContentValues values, @Nullable String whereClause, @Nullable String... whereArgs) {
        return mDatabase.update(mTableName,
                conflictAlgorithm,
                values,
                whereClause,
                whereArgs);
    }

    @CheckResult
    @Nullable
    final public ENTITY queryById(long rowId) {
        ENTITY e = null;
        Cursor cursor = mDatabase.query(SQL_QUERY);
        if (cursor != null && !cursor.isClosed()) {
            if (cursor.moveToFirst()) {
                e = toEntity(cursor);
            }
            cursor.close();
        }
        return e;
    }

    @NonNull
    final protected List<ENTITY> query(@NonNull String sql, @NonNull Object... args) {
        List<ENTITY> result = new LinkedList<>();
        Cursor c = mDatabase.query(sql, args);
        if (c != null && !c.isClosed()) {
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    result.add(toEntity(c));
                }
            }
            c.close();
        }
        return result;
    }

    @CheckResult
    @NonNull
    final protected QueryObservable createQuery(@NonNull String sql,
                                                @NonNull Object... args) {
        return mDatabase.createQuery(mTableName, sql, args);
    }

    @CheckResult
    @NonNull
    final protected QueryObservable createQuery(@NonNull final Iterable<String> tables, @NonNull String sql,
                                                @NonNull Object... args) {
        return mDatabase.createQuery(tables, sql, args);
    }

    protected abstract ContentValues toContentValues(ENTITY e);

    protected abstract ENTITY toEntity(Cursor cursor);

    protected abstract long getRowId(ENTITY e);

    protected abstract String getTableName(Class<ENTITY> clazz);
}
