package com.example.sqlbrite.todo.di;

import android.app.Activity;

import com.example.sqlbrite.todo.di.model.UserScopeModelModule;
import com.example.sqlbrite.todo.di.model.remote.NetModule;

import dagger.Module;
import dagger.Provides;

/**
 * @author Administrator
 * @date 2018/3/14 13:11
 */

@Module
public class ActivityScopeModule {
    private final Activity mActivity;

    public ActivityScopeModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    @ActivityScope
    Activity provideActivity() {
        return mActivity;
    }
}
