package com.example.sqlbrite.todo.di;

import android.arch.lifecycle.ViewModelProvider;

import com.example.sqlbrite.todo.di.controler.UserScopeViewModelFactory;
import com.example.sqlbrite.todo.model.local.preferences.UserPrefs;
import com.example.sqlbrite.todo.schedulers.SchedulerProvider;

import javax.inject.Named;

import dagger.Component;

@UserScope
@Component(modules = UserScopeModule.class, dependencies = AppScopeComponent.class)
public interface UserScopeComponent {

    SchedulerProvider schedulerProvider();

    ViewModelProvider.Factory userScopeviewModelProviderFactory();

    @Named("AppScope")
    ViewModelProvider.Factory appScopeviewModelProviderFactory();

    UserPrefs userPrefs();
}
