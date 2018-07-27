package com.example.sqlbrite.todo.di;

import com.example.sqlbrite.todo.di.controler.UserScopeViewModelFactory;
import com.example.sqlbrite.todo.model.local.preferences.UserPrefs;
import com.example.sqlbrite.todo.schedulers.SchedulerProvider;

import dagger.Component;

@UserScope
@Component(modules = UserScopeModule.class, dependencies = AppScopeComponent.class)
public interface UserScopeComponent {

    SchedulerProvider schedulerProvider();

    UserScopeViewModelFactory viewModelProviderFactory();

    UserPrefs userPrefs();
}
