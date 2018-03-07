package com.example.sqlbrite.todo.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.squareup.sqlbrite3.BriteDatabase;
import com.squareup.sqlbrite3.SqlBrite;
import com.squareup.sqlbrite3.support.dao.BriteDaoSupport;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_NONE;

/**
 * @author Guang1234567
 * @date 2018/3/5 17:22
 */

public class TodoListDao extends BriteDaoSupport<TodoList> {

    public TodoListDao(BriteDatabase database) {
        super(database);
    }

    @Override
    protected ContentValues toContentValues(TodoList e) {
        return e.toContentValues();
    }

    @Override
    protected TodoList toEntity(Cursor cursor) {
        return TodoList.toEntity(cursor);
    }

    @Override
    protected long toRowId(TodoList e) {
        return e.id();
    }

    @Override
    protected String toTableName(Class<TodoList> clazz) {
        return TodoList.TABLE;
    }


    //------------------------------------------------------------
    // 业务逻辑
    //------------------------------------------------------------

    private static final String TITLE_QUERY =
            "SELECT " + TodoList.NAME + " FROM " + TodoList.TABLE + " WHERE " + TodoList.ID + " = ?";

    public Observable<String> createQueryListName(long listId) {
        return createQuery(getTableName(), TITLE_QUERY, listId)
                .map(new Function<SqlBrite.Query, String>() {
                    @Override
                    public String apply(SqlBrite.Query query) {
                        Cursor cursor = query.run();
                        try {
                            if (!cursor.moveToNext()) {
                                throw new AssertionError("No rows");
                            }
                            return cursor.getString(0);
                        } finally {
                            cursor.close();
                        }
                    }
                });
    }

    public long createNewOne(String name) {
        return insert(CONFLICT_NONE, new TodoList.Builder().name(name).build());
    }
}
