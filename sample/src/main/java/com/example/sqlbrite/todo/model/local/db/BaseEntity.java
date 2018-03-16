package com.example.sqlbrite.todo.model.local.db;

import android.content.ContentValues;
import android.database.Cursor;

import io.reactivex.functions.Function;

/**
 * 所有数据实体的基类
 *
 * @author lihanguang
 * @date 2016/2/23 17:47
 */
abstract class BaseEntity {
    // Optional: When you include an abstract method that returns ContentValues and doesn't have
    // any parameters the extension will implement it for you
    abstract ContentValues toContentValues();

}
