package com.example.sqlbrite.todo.di;

import android.support.v4.app.Fragment;

import com.example.sqlbrite.todo.ui.ItemsFragment;
import com.example.sqlbrite.todo.ui.ListsFragment;
import com.example.sqlbrite.todo.ui.NewItemFragment;
import com.example.sqlbrite.todo.ui.NewListFragment;

import dagger.Component;

@FragmentScope
@Component(modules = UserFragmentScopeModule.class, dependencies = UserActivityScopeComponent.class)
public interface UserFragmentScopeComponent {

    Fragment fragment();

    void inject(ListsFragment fragment);

    void inject(ItemsFragment fragment);

    void inject(NewItemFragment fragment);

    void inject(NewListFragment fragment);

    interface Injectable {
        void inject(UserFragmentScopeComponent component);
    }
}
