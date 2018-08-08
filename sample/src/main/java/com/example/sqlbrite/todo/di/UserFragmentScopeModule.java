package com.example.sqlbrite.todo.di;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.Fragment;

import com.example.sqlbrite.todo.controler.MainViewModel;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * @author Guang1234567
 * @date 2018/3/14 13:11
 */

@Module
public class UserFragmentScopeModule {
    private final Fragment mFragment;

    public UserFragmentScopeModule(Fragment fragment) {
        mFragment = fragment;
    }

    @Provides
    @FragmentScope
    Fragment provideFragment() {
        return mFragment;
    }

    @Provides
    @FragmentScope
    @Named("FragmentScope")
    MainViewModel provideMainViewModel(Fragment fragment, ViewModelProvider.Factory factory) {
        return ViewModelProviders.of(fragment, factory).get(MainViewModel.class);
    }
}
