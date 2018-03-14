package com.example.sqlbrite.todo.di;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.example.sqlbrite.todo.TodoApp;

/**
 * @author Administrator
 * @date 2018/3/14 17:34
 */

public class InjectHelper {

    public static FragmentScopeComponent createFragmentScopeComponent(Context context, Activity activity, Fragment fragment) {
        context = context.getApplicationContext();
        return DaggerFragmentScopeComponent.builder()
                .activityScopeComponent(
                        createActivityScopeComponent(context, activity)
                )
                .fragmentScopeModule(new FragmentScopeModule(fragment))
                .build();
    }

    public static ActivityScopeComponent createActivityScopeComponent(Context context, Activity activity) {
        context = context.getApplicationContext();
        return DaggerActivityScopeComponent.builder()
                .userScopeComponent(createUserScopeComponent(context))
                .activityScopeModule(new ActivityScopeModule(activity))
                .build();
    }

    public static UserScopeComponent createUserScopeComponent(Context context) {
        context = context.getApplicationContext();
        return DaggerUserScopeComponent.builder().appScopeComponent(createAppScopeComponent(context)).build();
    }

    public static AppScopeComponent createAppScopeComponent(Context context) {
        context = context.getApplicationContext();
        return TodoApp.getAppScopeComponent(context);
    }
}
