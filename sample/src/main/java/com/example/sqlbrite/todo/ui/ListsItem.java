/*
 * Copyright (C) 2015 Square, Inc.
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
package com.example.sqlbrite.todo.ui;

import android.database.Cursor;
import android.os.Parcelable;

import com.example.sqlbrite.todo.model.autovalue.DateCursorTypeAdapter;
import com.example.sqlbrite.todo.model.local.db.Db;
import com.example.sqlbrite.todo.model.local.db.TodoItem;
import com.example.sqlbrite.todo.model.local.db.TodoList;
import com.gabrielittner.auto.value.cursor.ColumnAdapter;
import com.gabrielittner.auto.value.cursor.ColumnName;
import com.google.auto.value.AutoValue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import io.reactivex.functions.Function;

@AutoValue
public abstract class ListsItem implements Parcelable {

    public final static String ITEM_COUNT = "item_count";

    public final static String ALIAS_LIST = "list";
    public final static String ALIAS_ITEM = "item";

    public final static String LIST_ID = ALIAS_LIST + "." + TodoList.ID;
    public final static String LIST_NAME = ALIAS_LIST + "." + TodoList.NAME;
    public final static String LIST_CREATE_TIMESTAMP = ALIAS_LIST + "." + TodoList.CREATE_TIMESTAMP;
    public final static String ITEM_ID = ALIAS_ITEM + "." + TodoItem.ID;
    public final static String ITEM_LIST_ID = ALIAS_ITEM + "." + TodoItem.LIST_ID;

    public static Collection<String> TABLES = Arrays.asList(TodoList.TABLE, TodoItem.TABLE);

    @ColumnName(TodoList.ID)
    public abstract long id();

    @ColumnName(TodoList.NAME)
    public abstract String name();

    @ColumnName(TodoList.CREATE_TIMESTAMP)
    @ColumnAdapter(DateCursorTypeAdapter.class)
    public abstract Date createTimestamp();

    @ColumnName(ITEM_COUNT)
    public abstract int itemCount();

    // Optional: if your project includes RxJava 2 the extension will generate a Function<Cursor, User>
    public static Function<Cursor, ListsItem> MAPPER_FUNCTION() {
        return AutoValue_ListsItem.MAPPER_FUNCTION;
    }
}
