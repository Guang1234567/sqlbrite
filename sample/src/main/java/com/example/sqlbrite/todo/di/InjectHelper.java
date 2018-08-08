package com.example.sqlbrite.todo.di;

import android.app.Application;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.example.sqlbrite.todo.di.model.local.db.DbModule;
import com.example.sqlbrite.todo.di.model.local.preferences.AppScopePreferencesModule;
import com.example.sqlbrite.todo.di.model.local.preferences.UserScopePreferencesModule;
import com.example.sqlbrite.todo.di.model.remote.NetModule;
import com.example.sqlbrite.todo.di.model.remote.TodoApiModule;

/**
 * @author Guang1234567
 * @date 2018/3/14 17:34
 */

public class InjectHelper {

    private static final InjectHelper INSTANCE = new InjectHelper();

    public static InjectHelper instance() {
        return INSTANCE;
    }

    private InjectHelper() {

    }

    private AppScopeComponent mAppScopeComponent;

    public AppScopeComponent init(Application application) {
        mAppScopeComponent = DaggerAppScopeComponent
                .builder()
                .appScopeModule(new AppScopeModule(application))
                .netModule(new NetModule("https://www.github.com"))
                .todoApiModule(new TodoApiModule())
                .appScopePreferencesModule(new AppScopePreferencesModule())
                .build();
        return mAppScopeComponent;
    }

    public AppScopeComponent getAppScopeComponent() {
        return mAppScopeComponent;
    }


    public UserScopeComponent createUserScopeComponent(String userId) {
        return DaggerUserScopeComponent.builder()
                .appScopeComponent(getAppScopeComponent())
                .dbModule(new DbModule(userId))
                .userScopePreferencesModule(new UserScopePreferencesModule(userId))
                .build();
    }

    public UserActivityScopeComponent createUserActivityScopeComponent(FragmentActivity activity, UserScopeComponent userScopeComponent) {
        return DaggerUserActivityScopeComponent.builder()
                .userScopeComponent(userScopeComponent)
                .userActivityScopeModule(new UserActivityScopeModule(activity))
                .build();
    }


    public UserFragmentScopeComponent createUserFragmentScopeComponent(Fragment fragment, UserActivityScopeComponent activityScopeComponent) {
        return DaggerUserFragmentScopeComponent.builder()
                .userActivityScopeComponent(activityScopeComponent)
                .userFragmentScopeModule(new UserFragmentScopeModule(fragment))
                .build();
    }

    public AppActivityScopeComponent createAppActivityScopeComponent(FragmentActivity activity) {
        return DaggerAppActivityScopeComponent.builder()
                .appScopeComponent(getAppScopeComponent())
                .appActivityScopeModule(new AppActivityScopeModule(activity))
                .build();
    }


    public AppFragmentScopeComponent createAppFragmentScopeComponent(Fragment fragment, AppActivityScopeComponent activityScopeComponent) {
        return DaggerAppFragmentScopeComponent.builder()
                .appActivityScopeComponent(activityScopeComponent)
                .appFragmentScopeModule(new AppFragmentScopeModule(fragment))
                .build();
    }
}
