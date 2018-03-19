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

    private final BriteDatabase mDatabase;

    private final Class mEntityClazz;

    private final String mTableName;

    private final String SQL_QUERY_BY_ID;

    public BriteDaoSupport(BriteDatabase database) {
        mDatabase = database;

        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        mEntityClazz = (Class) type.getActualTypeArguments()[0];

        mTableName = toTableName(mEntityClazz);

        SQL_QUERY_BY_ID = new StringBuilder("SELECT * FROM ")
                .append(mTableName)
                .append(" WHERE ")
                .append(BaseColumns._ID)
                .append("=?")
                .toString();
    }

    final public long insert(ENTITY t) {
        ContentValues cvs = toContentValues(t);
        return insert(CONFLICT_NONE, cvs);
    }

    final protected long insert(int conflictAlgorithm, ContentValues cvs) {
        cvs.remove(BaseColumns._ID);
        return mDatabase.insert(mTableName, conflictAlgorithm, cvs);
    }

    final public int deleteById(long rowId) {
        return mDatabase.delete(mTableName, BaseColumns._ID + " = " + rowId);
    }

    final protected int delete(@NonNull final String table, @Nullable String whereClause,
                               @Nullable String... whereArgs) {
        return mDatabase.delete(table, whereClause, whereArgs);
    }

    final public int update(ENTITY t) {
        return mDatabase.update(mTableName,
                CONFLICT_NONE,
                toContentValues(t),
                BaseColumns._ID + " = ?",
                new String[]{String.valueOf(toRowId(t))});
    }

    protected int update(@NonNull final String table, int conflictAlgorithm,
                         @NonNull ContentValues values, @Nullable String whereClause, @Nullable String... whereArgs) {
        return mDatabase.update(table,
                conflictAlgorithm,
                values,
                whereClause,
                whereArgs);
    }

    @CheckResult
    @Nullable
    final public ENTITY queryById(final long rowId) {
        ENTITY e = null;
        Cursor cursor = mDatabase.query(SQL_QUERY_BY_ID, new String[]{String.valueOf(rowId)});
        if (cursor != null && !cursor.isClosed()) {
            if (cursor.moveToFirst()) {
                e = createFromCursor(cursor);
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
                    result.add(createFromCursor(c));
                }
            }
            c.close();
        }
        return result;
    }

    @CheckResult
    @NonNull
    final protected QueryObservable createQuery(@NonNull final String table, @NonNull String sql,
                                                @NonNull Object... args) {
        return mDatabase.createQuery(table, sql, args);
    }

    @CheckResult
    @NonNull
    final protected QueryObservable createQuery(@NonNull final Iterable<String> tables, @NonNull String sql,
                                                @NonNull Object... args) {
        return mDatabase.createQuery(tables, sql, args);
    }

    final protected Class getEntityClazz() {
        return mEntityClazz;
    }

    final protected String getTableName() {
        return mTableName;
    }

    protected abstract ContentValues toContentValues(ENTITY e);

    protected abstract ENTITY createFromCursor(Cursor cursor);

    protected abstract long toRowId(ENTITY e);

    protected abstract String toTableName(Class<ENTITY> clazz);
}
