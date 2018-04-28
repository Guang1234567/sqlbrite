package com.example.sqlbrite.todo.model.local.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.squareup.sqlbrite3.BriteDatabase;
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
    protected TodoList createFromCursor(Cursor cursor) {
        return TodoList.create(cursor);
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
                .mapToOne(new Function<Cursor, String>() {
                    @Override
                    public String apply(Cursor cursor) throws Exception {
                        return cursor.getString(0);
                    }
                });
    }

    public long createNewOne(String name) {
        return insert(CONFLICT_NONE, new TodoList.ContentValuesBuilder().name(name).build());
    }
}
