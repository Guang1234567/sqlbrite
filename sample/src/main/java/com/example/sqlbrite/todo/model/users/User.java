package com.example.sqlbrite.todo.model.users;

import android.os.Parcelable;

import com.example.sqlbrite.todo.model.autovalue.DateParcelTypeAdapter;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.ryanharter.auto.value.parcel.ParcelAdapter;

import java.util.Date;

@AutoValue
public abstract class User implements Parcelable {

    @SerializedName("id")
    public abstract String id();

    @SerializedName("name")
    public abstract String name();

    @SerializedName("timestamp")
    @ParcelAdapter(DateParcelTypeAdapter.class)
    public abstract Date timestamp();

    public static Builder builder() {
        return new AutoValue_User.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(String id);

        public abstract Builder name(String name);

        public abstract Builder timestamp(Date timestamp);

        public abstract User build();
    }

    public static User create(String id, String name, Date timestamp) {
        return new AutoValue_User(id, name, timestamp);
    }

    // The public static method returning a TypeAdapter<Foo> is what
    // tells auto-value-gson to create a TypeAdapter for Foo.
    public static TypeAdapter<User> typeAdapter(Gson gson) {
        return new AutoValue_User.GsonTypeAdapter(gson)
                .setDefaultId("id_NULL")
                .setDefaultName("name_NULL");
    }

}