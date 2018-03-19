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
package com.example.sqlbrite.todo.model.local.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcelable;
import android.provider.BaseColumns;

import com.gabrielittner.auto.value.cursor.ColumnName;
import com.google.auto.value.AutoValue;

import io.reactivex.functions.Function;

// Note: normally I wouldn't prefix table classes but I didn't want 'List' to be overloaded.
@AutoValue
public abstract class TodoList implements Parcelable, toContentValuesAble {
    public static final String TABLE = "todo_list";

    public static final String ID = BaseColumns._ID;
    public static final String NAME = "name";
    public static final String ARCHIVED = "archived";

    @ColumnName(ID)
    public abstract long id();

    @ColumnName(NAME)
    public abstract String name();

    @ColumnName(ARCHIVED)
    public abstract boolean archived();

    public static final class ContentValuesBuilder {
        private final ContentValues values = new ContentValues();

        public ContentValuesBuilder id(long id) {
            values.put(ID, id);
            return this;
        }

        public ContentValuesBuilder name(String name) {
            values.put(NAME, name);
            return this;
        }

        public ContentValuesBuilder archived(boolean archived) {
            values.put(ARCHIVED, archived);
            return this;
        }

        public ContentValues build() {
            return values; // TODO defensive copy?
        }
    }

    // Optional: if your project includes RxJava 2 the extension will generate a Function<Cursor, User>
    public static Function<Cursor, TodoList> MAPPER_FUNCTION() {
        return AutoValue_TodoList.MAPPER_FUNCTION;
    }

    public static TodoList createFromCursor(Cursor cursor) {
        return AutoValue_TodoList.createFromCursor(cursor);
    }
}
