package com.example.sqlbrite.todo.di;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import com.example.sqlbrite.todo.di.model.local.db.DbModule;
import com.example.sqlbrite.todo.di.model.local.preferences.AppScopePreferencesModule;
import com.example.sqlbrite.todo.di.model.local.preferences.UserScopePreferencesModule;
import com.example.sqlbrite.todo.di.model.remote.NetModule;
import com.example.sqlbrite.todo.di.model.remote.TodoApiModule;
import com.example.sqlbrite.todo.ui.BaseViewModelActivity;

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

    public ActivityScopeComponent createActivityScopeComponent(Activity activity) {
        return DaggerActivityScopeComponent.builder()
                .userScopeComponent(getAppScopeComponent().userManager().getUserScopeComponent())
                .activityScopeModule(new ActivityScopeModule(activity))
                .build();
    }


    public FragmentScopeComponent createFragmentScopeComponent(Activity activity, Fragment fragment) {
        ActivityScopeComponent activityScopeComponent = ((BaseViewModelActivity) activity).getActivityScopeComponent();
        return DaggerFragmentScopeComponent.builder()
                .activityScopeComponent(activityScopeComponent)
                .fragmentScopeModule(new FragmentScopeModule(fragment))
                .build();
    }
}
