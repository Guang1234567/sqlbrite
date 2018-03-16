package com.example.sqlbrite.todo.di;

import android.arch.lifecycle.ViewModelProvider;

import com.example.sqlbrite.todo.di.model.remote.TodoApiModule.GitHubApiInterface;
import com.example.sqlbrite.todo.schedulers.SchedulerProvider;

import dagger.Component;

@UserScope
@Component(modules = UserScopeModule.class, dependencies = AppScopeComponent.class)
public interface UserScopeComponent {

    SchedulerProvider schedulerProvider();

    GitHubApiInterface gitHubApiInterface();

    ViewModelProvider.Factory viewModelProviderFactory();
}
