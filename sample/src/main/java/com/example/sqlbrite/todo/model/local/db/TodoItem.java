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

@AutoValue
public abstract class TodoItem implements Parcelable, toContentValuesAble {
    public static final String TABLE = "todo_item";

    public static final String ID = BaseColumns._ID;
    public static final String LIST_ID = "todo_list_id";
    public static final String DESCRIPTION = "description";
    public static final String COMPLETE = "complete";

    @ColumnName(ID)
    public abstract long id();

    @ColumnName(LIST_ID)
    public abstract long listId();

    @ColumnName(DESCRIPTION)
    public abstract String description();

    @ColumnName(COMPLETE)
    public abstract boolean complete();

    public static final class ContentValuesBuilder {
        private final ContentValues values = new ContentValues();

        public ContentValuesBuilder id(long id) {
            values.put(ID, id);
            return this;
        }

        public ContentValuesBuilder listId(long listId) {
            values.put(LIST_ID, listId);
            return this;
        }

        public ContentValuesBuilder description(String description) {
            values.put(DESCRIPTION, description);
            return this;
        }

        public ContentValuesBuilder complete(boolean complete) {
            values.put(COMPLETE, complete ? Db.BOOLEAN_TRUE : Db.BOOLEAN_FALSE);
            return this;
        }

        public ContentValues build() {
            return values; // TODO defensive copy?
        }
    }

    // Optional: if your project includes RxJava 2 the extension will generate a Function<Cursor, User>
    public static Function<Cursor, TodoItem> MAPPER_FUNCTION() {
        return AutoValue_TodoItem.MAPPER_FUNCTION;
    }

    public static TodoItem createFromCursor(Cursor cursor) {
        return AutoValue_TodoItem.createFromCursor(cursor);
    }
}
