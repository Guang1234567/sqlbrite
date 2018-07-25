package com.example.sqlbrite.todo.model.local.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.squareup.sqlbrite3.BriteDatabase;
import com.squareup.sqlbrite3.SqlBrite;
import com.squareup.sqlbrite3.support.dao.BriteDaoSupport;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_NONE;

/**
 * @author Guang1234567
 * @date 2018/3/5 18:04
 */

public class TodoItemDao extends BriteDaoSupport<TodoItem> {

    public TodoItemDao(BriteDatabase database) {
        super(database);
    }

    @Override
    protected ContentValues toContentValues(TodoItem e) {
        return e.toContentValues();
    }

    @Override
    protected TodoItem createFromCursor(Cursor cursor) {
        return TodoItem.create(cursor);
    }

    @Override
    protected long toRowId(TodoItem e) {
        return e.id();
    }

    @Override
    protected String toTableName(Class<TodoItem> clazz) {
        return TodoItem.TABLE;
    }


    //------------------------------------------------------------
    // 业务逻辑
    //------------------------------------------------------------


    public boolean complete(long id, boolean complete) {
        return update(getTableName(), CONFLICT_NONE,
                new TodoItem.ContentValuesBuilder().complete(complete).build(), TodoItem.ID + " = ?",
                String.valueOf(id)) > 0;
    }

    private static final String COUNT_QUERY = "SELECT COUNT(*) FROM "
            + TodoItem.TABLE
            + " WHERE "
            + TodoItem.COMPLETE
            + " = "
            + Db.BOOLEAN_FALSE
            + " AND "
            + TodoItem.LIST_ID
            + " = ?";

    public Observable<Integer> createQueryItemCount(long listId) {
        return createQuery(getTableName(), COUNT_QUERY, listId) //
                .mapToOne(new Function<Cursor, Integer>() {
                    @Override
                    public Integer apply(Cursor cursor) throws Exception {
                        return cursor.getInt(0);
                    }
                });
    }

    private static final String LIST_QUERY = "SELECT * FROM "
            + TodoItem.TABLE
            + " WHERE "
            + TodoItem.LIST_ID
            + " = ? ORDER BY "
            + TodoItem.COMPLETE
            + " ASC";

    public Observable<List<TodoItem>> createQueryTodoItemsByListId(long listId) {
        return createQuery(getTableName(), LIST_QUERY, listId)
                .mapToList(TodoItem.MAPPER_FUNCTION());
    }

    public long createNewOne(long listId, String description) {
        return insert(CONFLICT_NONE,
                new TodoItem.ContentValuesBuilder()
                        .listId(listId)
                        .description(description)
                        .build());
    }
}
