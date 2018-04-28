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

import com.squareup.sqlbrite3.support.RxSupportSQLiteOpenHelperCallback;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_FAIL;

public final class DbCallback extends RxSupportSQLiteOpenHelperCallback {
    private static final int FIRST_VERSION = 1; // 最初版本号是 1
    private static final int LAST_VERSION = 3;

    private static final String CREATE_LIST = ""
            + "CREATE TABLE " + TodoList.TABLE + "("
            + TodoList.ID + " INTEGER NOT NULL PRIMARY KEY,"
            + TodoList.NAME + " TEXT NOT NULL,"
            + TodoList.ARCHIVED + " INTEGER NOT NULL DEFAULT 0,"
            + TodoList.CREATE_TIMESTAMP + " INTEGER NOT NULL DEFAULT 0"
            + ")";
    private static final String CREATE_ITEM = ""
            + "CREATE TABLE " + TodoItem.TABLE + "("
            + TodoItem.ID + " INTEGER NOT NULL PRIMARY KEY,"
            + TodoItem.LIST_ID + " INTEGER NOT NULL REFERENCES " + TodoList.TABLE + "(" + TodoList.ID + "),"
            + TodoItem.DESCRIPTION + " TEXT NOT NULL,"
            + TodoItem.COMPLETE + " INTEGER NOT NULL DEFAULT 0"
            + ")";
    private static final String CREATE_ITEM_LIST_ID_INDEX =
            "CREATE INDEX item_list_id ON " + TodoItem.TABLE + " (" + TodoItem.LIST_ID + ")";

    public DbCallback() {
        super(FIRST_VERSION, LAST_VERSION);

        init();
    }

    private void init() {
        upgrateToVersion(FIRST_VERSION).subscribe(db -> {
            db.execSQL(CREATE_LIST);
            db.execSQL(CREATE_ITEM);
            db.execSQL(CREATE_ITEM_LIST_ID_INDEX);

            long groceryListId = db.insert(TodoList.TABLE, CONFLICT_FAIL, new TodoList.ContentValuesBuilder()
                    .name("Grocery List")
                    .build());
            db.insert(TodoItem.TABLE, CONFLICT_FAIL, new TodoItem.ContentValuesBuilder()
                    .listId(groceryListId)
                    .description("Beer")
                    .build());
            db.insert(TodoItem.TABLE, CONFLICT_FAIL, new TodoItem.ContentValuesBuilder()
                    .listId(groceryListId)
                    .description("Point Break on DVD")
                    .build());
            db.insert(TodoItem.TABLE, CONFLICT_FAIL, new TodoItem.ContentValuesBuilder()
                    .listId(groceryListId)
                    .description("Bad Boys 2 on DVD")
                    .build());

            long holidayPresentsListId = db.insert(TodoList.TABLE, CONFLICT_FAIL, new TodoList.ContentValuesBuilder()
                    .name("Holiday Presents")
                    .build());
            db.insert(TodoItem.TABLE, CONFLICT_FAIL, new TodoItem.ContentValuesBuilder()
                    .listId(holidayPresentsListId)
                    .description("Pogo Stick for Jake W.")
                    .build());
            db.insert(TodoItem.TABLE, CONFLICT_FAIL, new TodoItem.ContentValuesBuilder()
                    .listId(holidayPresentsListId)
                    .description("Jack-in-the-box for Alec S.")
                    .build());
            db.insert(TodoItem.TABLE, CONFLICT_FAIL, new TodoItem.ContentValuesBuilder()
                    .listId(holidayPresentsListId)
                    .description("Pogs for Matt P.")
                    .build());
            db.insert(TodoItem.TABLE, CONFLICT_FAIL, new TodoItem.ContentValuesBuilder()
                    .listId(holidayPresentsListId)
                    .description("Cola for Jesse W.")
                    .build());

            long workListId = db.insert(TodoList.TABLE, CONFLICT_FAIL, new TodoList.ContentValuesBuilder()
                    .name("Work Items")
                    .build());
            db.insert(TodoItem.TABLE, CONFLICT_FAIL, new TodoItem.ContentValuesBuilder()
                    .listId(workListId)
                    .description("Finish SqlBrite library")
                    .complete(true)
                    .build());
            db.insert(TodoItem.TABLE, CONFLICT_FAIL, new TodoItem.ContentValuesBuilder()
                    .listId(workListId)
                    .description("Finish SqlBrite sample app")
                    .build());
            db.insert(TodoItem.TABLE, CONFLICT_FAIL, new TodoItem.ContentValuesBuilder()
                    .listId(workListId)
                    .description("Publish SqlBrite to GitHub")
                    .build());

            long birthdayPresentsListId = db.insert(TodoList.TABLE, CONFLICT_FAIL, new TodoList.ContentValuesBuilder()
                    .name("Birthday Presents")
                    .archived(true)
                    .build());
            db.insert(TodoItem.TABLE, CONFLICT_FAIL, new TodoItem.ContentValuesBuilder().listId(birthdayPresentsListId)
                    .description("New car")
                    .complete(true)
                    .build());

            long bigDataListId = db.insert(TodoList.TABLE, CONFLICT_FAIL, new TodoList.ContentValuesBuilder()
                    .name("Big Data")
                    .build());
            for (int i = 1; i <= 100000; i++) {
                db.insert(TodoItem.TABLE, CONFLICT_FAIL, new TodoItem.ContentValuesBuilder().listId(bigDataListId)
                        .description("Data " + String.valueOf(i))
                        .complete(true)
                        .build());
            }
        });

        upgrateToVersion((2)).subscribe(db -> {
        });

        upgrateToVersion((3)).subscribe(db -> {
        });
    }
}
