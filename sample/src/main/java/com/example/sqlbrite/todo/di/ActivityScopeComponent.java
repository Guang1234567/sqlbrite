package com.example.sqlbrite.todo.di;

import android.app.Activity;

import com.example.sqlbrite.todo.di.controler.UserScopeViewModelFactory;
import com.example.sqlbrite.todo.schedulers.SchedulerProvider;
import com.example.sqlbrite.todo.ui.MainActivity;
import com.example.sqlbrite.todo.ui.SystemSettingActivity;

import dagger.Component;

/**
 * @author Guang1234567
 * @date 2018/3/14 13:10
 */

@ActivityScope
@Component(modules = ActivityScopeModule.class, dependencies = UserScopeComponent.class)
public interface ActivityScopeComponent {

    Activity activity();

    SchedulerProvider schedulerProvider();

    /*ViewModelProvider.Factory viewModelProviderFactory();*/

    UserScopeViewModelFactory viewModelProviderFactory();

    void inject(MainActivity activity);

    void inject(SystemSettingActivity activity);
}
