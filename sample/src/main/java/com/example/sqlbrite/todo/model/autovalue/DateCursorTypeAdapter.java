package com.example.sqlbrite.todo.model.autovalue;

import android.content.ContentValues;
import android.database.Cursor;

import com.gabrielittner.auto.value.cursor.ColumnTypeAdapter;
import com.squareup.sqlbrite3.support.DbUtils;

import java.util.Date;

/**
 * @author lihanguang
 * @date 2017/5/5 00:45
 */

public class DateCursorTypeAdapter implements ColumnTypeAdapter<Date> {
    public Date fromCursor(Cursor cursor, String columnName) {
        long d = DbUtils.getLong(cursor, columnName);
        return new Date(d);
    }

    public void toContentValues(ContentValues values, String columnName, Date value) {
        // leave this empty when you don't use a "toContentValues()" method
        values.put(columnName, value.getTime());
    }
}
