package com.example.sqlbrite.todo.di.controler;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.example.sqlbrite.todo.controler.LoginFlowViewModel;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class AppScopeViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LoginFlowViewModel.class)
    abstract ViewModel bindLoginFlowViewModel(LoginFlowViewModel viewModel);

    @Named("AppScope")
    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(AppScopeViewModelFactory factory);
}
