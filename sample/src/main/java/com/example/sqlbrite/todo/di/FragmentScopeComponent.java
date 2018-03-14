package com.example.sqlbrite.todo.di;

import android.arch.lifecycle.ViewModelProvider;
import android.support.v4.app.Fragment;

import com.example.sqlbrite.todo.schedulers.SchedulerProvider;
import com.example.sqlbrite.todo.ui.ItemsFragment;
import com.example.sqlbrite.todo.ui.ListsFragment;
import com.example.sqlbrite.todo.ui.NewItemFragment;
import com.example.sqlbrite.todo.ui.NewListFragment;

import dagger.Component;

@FragmentScope
@Component(modules = FragmentScopeModule.class, dependencies = ActivityScopeComponent.class)
public interface FragmentScopeComponent {

    Fragment fragment();

    void inject(ListsFragment fragment);

    void inject(ItemsFragment fragment);

    void inject(NewItemFragment fragment);

    void inject(NewListFragment fragment);
}
