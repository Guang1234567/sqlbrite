package com.example.sqlbrite.todo.di;

import com.example.sqlbrite.todo.di.controler.ViewModelModule;
import com.example.sqlbrite.todo.di.model.UserScopeModelModule;

import dagger.Module;

@Module(includes = {UserScopeModelModule.class, ViewModelModule.class,})
public final class UserScopeModule {

    public UserScopeModule() {
    }
}
