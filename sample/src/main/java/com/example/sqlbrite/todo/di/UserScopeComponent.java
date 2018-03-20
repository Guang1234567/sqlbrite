package com.example.sqlbrite.todo.di;

import android.arch.lifecycle.ViewModelProvider;

import com.example.sqlbrite.todo.controler.LoginViewControler;
import com.example.sqlbrite.todo.di.model.remote.TodoApiModule.GitHubApiInterface;
import com.example.sqlbrite.todo.model.users.LoginManager;
import com.example.sqlbrite.todo.model.users.UserManager;
import com.example.sqlbrite.todo.schedulers.SchedulerProvider;

import dagger.Component;

@UserScope
@Component(modules = UserScopeModule.class, dependencies = AppScopeComponent.class)
public interface UserScopeComponent {

    SchedulerProvider schedulerProvider();

    ViewModelProvider.Factory viewModelProviderFactory();

    GitHubApiInterface gitHubApiInterface();

    UserManager userManager();

    LoginManager loginManager();

    LoginViewControler loginViewControler();
}
