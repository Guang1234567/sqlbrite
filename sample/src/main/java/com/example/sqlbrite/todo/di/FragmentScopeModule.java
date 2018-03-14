package com.example.sqlbrite.todo.di;

import android.support.v4.app.Fragment;

import dagger.Module;
import dagger.Provides;

/**
 * @author Administrator
 * @date 2018/3/14 13:11
 */

@Module
public class FragmentScopeModule {
    private final Fragment mFragment;

    public FragmentScopeModule(Fragment fragment) {
        mFragment = fragment;
    }

    @Provides
    @FragmentScope
    Fragment provideFragment() {
        return mFragment;
    }
}
