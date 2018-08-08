package com.example.sqlbrite.todo.di;

import dagger.Component;

@FragmentScope
@Component(modules = AppFragmentScopeModule.class, dependencies = AppActivityScopeComponent.class)
public interface AppFragmentScopeComponent {

    interface Injectable {
        void inject(AppFragmentScopeComponent component);
    }
}
