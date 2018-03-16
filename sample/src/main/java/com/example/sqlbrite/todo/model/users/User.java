package com.example.sqlbrite.todo.model.users;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class User {

    @SerializedName("id")
    public abstract String id();

    @SerializedName("name")
    public abstract String name();

    public static User create(String id, String name) {
        return new AutoValue_User(id, name);
    }

    // The public static method returning a TypeAdapter<Foo> is what
    // tells auto-value-gson to create a TypeAdapter for Foo.
    public static TypeAdapter<User> typeAdapter(Gson gson) {
        return new AutoValue_User.GsonTypeAdapter(gson)
                .setDefaultId("id_NULL")
                .setDefaultName("name_NULL");
    }

}