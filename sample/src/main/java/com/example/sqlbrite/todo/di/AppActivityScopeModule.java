package com.example.sqlbrite.todo.di;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentActivity;

import com.example.sqlbrite.todo.controler.DemoShareViewModel;
import com.example.sqlbrite.todo.controler.LoginFlowViewModel;
import com.example.sqlbrite.todo.controler.MainViewModel;
import com.example.sqlbrite.todo.controler.SystemSettingViewModel;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * @author Guang1234567
 * @date 2018/3/14 13:11
 */

@Module
public class AppActivityScopeModule {
    private final FragmentActivity mActivity;

    public AppActivityScopeModule(FragmentActivity activity) {
        mActivity = activity;
    }

    @Provides
    @ActivityScope
    FragmentActivity provideActivity() {
        return mActivity;
    }

    @Provides
    @ActivityScope
    LoginFlowViewModel provideLoginFlowViewModel(FragmentActivity activity, @Named("AppScope") ViewModelProvider.Factory factory) {
        return ViewModelProviders.of(activity, factory).get(LoginFlowViewModel.class);
    }
}
