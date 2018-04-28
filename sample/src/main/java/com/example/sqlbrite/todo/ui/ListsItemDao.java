package com.example.sqlbrite.todo.ui;

import com.example.sqlbrite.todo.model.local.db.TodoItem;
import com.example.sqlbrite.todo.model.local.db.TodoList;
import com.squareup.sqlbrite3.BriteDatabase;
import com.squareup.sqlbrite3.SqlBrite;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class ListsItemDao {

    private final BriteDatabase mDatabase;

    public ListsItemDao(BriteDatabase database) {
        mDatabase = database;
    }

    //------------------------------------------------------------
    // 业务逻辑
    //------------------------------------------------------------

    public static String QUERY = ""
            + "SELECT " + ListsItem.LIST_ID + ", " + ListsItem.LIST_NAME + ", COUNT(" + ListsItem.ITEM_ID + ") as " + ListsItem.ITEM_COUNT + ", " + ListsItem.LIST_CREATE_TIMESTAMP
            + " FROM " + TodoList.TABLE + " AS " + ListsItem.ALIAS_LIST
            + " LEFT OUTER JOIN " + TodoItem.TABLE + " AS " + ListsItem.ALIAS_ITEM + " ON " + ListsItem.LIST_ID + " = " + ListsItem.ITEM_LIST_ID
            + " GROUP BY " + ListsItem.LIST_ID;

    public Observable<List<ListsItem>> createQueryListsItems(final long max) {
        return mDatabase.createQuery(ListsItem.TABLES, QUERY)
                .flatMap(new Function<SqlBrite.Query, Observable<List<ListsItem>>>() {
                    @Override
                    public Observable<List<ListsItem>> apply(SqlBrite.Query query) throws Exception {
                        return query.asRows(ListsItem.MAPPER_FUNCTION())
                                .take(max)
                                .toList()
                                .toObservable();
                    }
                });
    }
}
