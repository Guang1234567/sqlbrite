package com.example.sqlbrite.todo.di;

import android.arch.lifecycle.ViewModelProvider;
import android.support.v4.app.FragmentActivity;

import com.example.sqlbrite.todo.controler.DemoShareViewModel;
import com.example.sqlbrite.todo.controler.MainViewModel;
import com.example.sqlbrite.todo.schedulers.SchedulerProvider;
import com.example.sqlbrite.todo.ui.MainActivity;
import com.example.sqlbrite.todo.ui.SystemSettingActivity;

import javax.inject.Named;

import dagger.Component;

/**
 * @author Guang1234567
 * @date 2018/3/14 13:10
 */

@ActivityScope
@Component(
        modules = UserActivityScopeModule.class,
        dependencies = UserScopeComponent.class
)
public interface UserActivityScopeComponent {

    FragmentActivity activity();

    SchedulerProvider schedulerProvider();

    ViewModelProvider.Factory userScopeviewModelProviderFactory();

    @Named("AppScope")
    ViewModelProvider.Factory appScopeviewModelProviderFactory();

    MainViewModel mainViewModel();

    DemoShareViewModel demoShareViewModel();

    void inject(MainActivity activity);

    void inject(SystemSettingActivity activity);

    interface Injectable {
        void inject(UserActivityScopeComponent component);
    }
}
