package com.example.sqlbrite.todo.di;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProvider;

import com.example.sqlbrite.todo.schedulers.SchedulerProvider;
import com.example.sqlbrite.todo.ui.MainActivity;

import dagger.Component;

/**
 * @author Administrator
 * @date 2018/3/14 13:10
 */

@ActivityScope
@Component(modules = ActivityScopeModule.class, dependencies = {UserScopeComponent.class})
public interface ActivityScopeComponent {

    Activity activity();

    SchedulerProvider schedulerProvider();

    ViewModelProvider.Factory viewModelProviderFactory();

    void inject(MainActivity activity);
}
