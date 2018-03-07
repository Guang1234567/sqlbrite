package com.example.sqlbrite.todo.ui;

import com.example.sqlbrite.todo.db.TodoItem;
import com.example.sqlbrite.todo.db.TodoList;
import com.squareup.sqlbrite3.BriteDatabase;
import com.squareup.sqlbrite3.SqlBrite;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

public class ListsItemDao {

    private final BriteDatabase mDatabase;

    public ListsItemDao(BriteDatabase database) {
        mDatabase = database;
    }

    //------------------------------------------------------------
    // 业务逻辑
    //------------------------------------------------------------

    private static String ALIAS_LIST = "list";
    private static String ALIAS_ITEM = "item";

    private static String LIST_ID = ALIAS_LIST + "." + TodoList.ID;
    private static String LIST_NAME = ALIAS_LIST + "." + TodoList.NAME;
    private static String ITEM_ID = ALIAS_ITEM + "." + TodoItem.ID;
    private static String ITEM_LIST_ID = ALIAS_ITEM + "." + TodoItem.LIST_ID;

    public static Collection<String> TABLES = Arrays.asList(TodoList.TABLE, TodoItem.TABLE);
    public static String QUERY = ""
            + "SELECT " + LIST_ID + ", " + LIST_NAME + ", COUNT(" + ITEM_ID + ") as " + ListsItem.ITEM_COUNT
            + " FROM " + TodoList.TABLE + " AS " + ALIAS_LIST
            + " LEFT OUTER JOIN " + TodoItem.TABLE + " AS " + ALIAS_ITEM + " ON " + LIST_ID + " = " + ITEM_LIST_ID
            + " GROUP BY " + LIST_ID;

    public Observable<List<ListsItem>> createQueryListsItems(final int max) {
        return mDatabase.createQuery(TABLES, QUERY)
                .flatMapSingle(new Function<SqlBrite.Query, SingleSource<List<ListsItem>>>() {
                    @Override
                    public SingleSource<List<ListsItem>> apply(SqlBrite.Query query) throws Exception {
                        return query.asRows(ListsItem.MAPPER)
                                .take(max)
                                .toList();
                    }
                });
    }


}
