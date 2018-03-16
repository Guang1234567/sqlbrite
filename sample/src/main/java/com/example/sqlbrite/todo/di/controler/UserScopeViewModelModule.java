package com.example.sqlbrite.todo.di.controler;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.example.sqlbrite.todo.controler.MainViewModel;
import com.example.sqlbrite.todo.controler.SystemSettingViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class UserScopeViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    abstract ViewModel bindMainViewModel(MainViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SystemSettingViewModel.class)
    abstract ViewModel bindSystemSettingViewModel(SystemSettingViewModel viewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(UserScopeViewModelFactory factory);
}
