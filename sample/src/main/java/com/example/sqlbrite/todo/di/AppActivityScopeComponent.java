package com.example.sqlbrite.todo.di;

import android.arch.lifecycle.ViewModelProvider;
import android.support.v4.app.FragmentActivity;

import com.example.sqlbrite.todo.controler.LoginFlowViewModel;
import com.example.sqlbrite.todo.schedulers.SchedulerProvider;
import com.example.sqlbrite.todo.ui.SplashActivity;

import javax.inject.Named;

import dagger.Component;

/**
 * @author Guang1234567
 * @date 2018/3/14 13:10
 */

@ActivityScope
@Component(modules = AppActivityScopeModule.class, dependencies = AppScopeComponent.class)
public interface AppActivityScopeComponent {

    FragmentActivity activity();

    SchedulerProvider schedulerProvider();

    @Named("AppScope")
    ViewModelProvider.Factory appScopeviewModelProviderFactory();

    LoginFlowViewModel loginFlowViewModel();

    void inject(SplashActivity activity);

    interface Injectable {
        void inject(AppActivityScopeComponent component);
    }
}
