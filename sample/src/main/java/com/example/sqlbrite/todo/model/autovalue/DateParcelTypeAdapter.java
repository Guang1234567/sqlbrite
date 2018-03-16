package com.example.sqlbrite.todo.model.autovalue;

import android.os.Parcel;

import com.ryanharter.auto.value.parcel.TypeAdapter;

import java.util.Date;

/**
 * @author Guang1234567
 * @date 2017/5/5 00:45
 */

public class DateParcelTypeAdapter implements TypeAdapter<Date> {
    public Date fromParcel(Parcel in) {
        return new Date(in.readLong());
    }

    public void toParcel(Date value, Parcel dest) {
        dest.writeLong(value.getTime());
    }
}
