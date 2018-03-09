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

import com.example.sqlbrite.todo.model.local.db.Db;
import com.example.sqlbrite.todo.model.local.db.TodoList;
import com.google.auto.value.AutoValue;

import io.reactivex.functions.Function;

@AutoValue
public abstract class ListsItem implements Parcelable {

    public final static String ITEM_COUNT = "item_count";

    abstract long id();

    abstract String name();

    abstract int itemCount();

    public static Function<Cursor, ListsItem> MAPPER = new Function<Cursor, ListsItem>() {
        @Override
        public ListsItem apply(Cursor cursor) {
            long id = Db.getLong(cursor, TodoList.ID);
            String name = Db.getString(cursor, TodoList.NAME);
            int itemCount = Db.getInt(cursor, ITEM_COUNT);
            return new AutoValue_ListsItem(id, name, itemCount);
        }
    };
}
