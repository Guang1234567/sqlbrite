package com.example.sqlbrite.todo.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.sqlbrite.todo.ui.ListsItem;
import com.squareup.sqlbrite3.BriteDatabase;
import com.squareup.sqlbrite3.SqlBrite;
import com.squareup.sqlbrite3.support.dao.BriteDaoSupport;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

/**
 * @author Administrator
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
    protected long getRowId(TodoList e) {
        return e.id();
    }

    @Override
    protected String getTableName(Class<TodoList> clazz) {
        return TodoList.TABLE;
    }


    //------------------------------------------------------------
    // 业务逻辑
    //------------------------------------------------------------

    public Observable<List<ListsItem>> createListsItemsQuery(final int max) {
        return createQuery(ListsItem.TABLES, ListsItem.QUERY)
                .flatMapSingle(new Function<SqlBrite.Query, SingleSource<List<ListsItem>>>() {
                    @Override
                    public SingleSource<List<ListsItem>> apply(SqlBrite.Query query) throws Exception {
                        return query.asRows(ListsItem.MAPPER)
                                .take(max)
                                .toList();
                    }
                });
    }

    private static final String TITLE_QUERY =
            "SELECT " + TodoList.NAME + " FROM " + TodoList.TABLE + " WHERE " + TodoList.ID + " = ?";

    public Observable<String> listName(long listId) {
        return createQuery(TITLE_QUERY, listId).map(new Function<SqlBrite.Query, String>() {
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
}
