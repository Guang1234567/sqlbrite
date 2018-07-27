package com.example.sqlbrite.todo.controler;

import com.example.sqlbrite.todo.di.controler.ShareViewModel;

import javax.inject.Inject;

public class DemoShareViewModel extends ShareViewModel {

    @Inject
    protected DemoShareViewModel() {
        super();
    }

    @Override
    protected void onShareCleared() {
        super.onShareCleared();
    }
}
