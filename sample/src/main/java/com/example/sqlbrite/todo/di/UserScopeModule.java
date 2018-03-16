package com.example.sqlbrite.todo.di;

import com.example.sqlbrite.todo.di.controler.UserScopeViewModelModule;
import com.example.sqlbrite.todo.di.model.UserScopeModelModule;

import dagger.Module;

@Module(includes = {UserScopeModelModule.class, UserScopeViewModelModule.class,})
public abstract class UserScopeModule {

    public UserScopeModule() {
    }
}
