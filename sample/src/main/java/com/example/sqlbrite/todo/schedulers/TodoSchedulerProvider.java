package com.example.sqlbrite.todo.schedulers;

import android.support.annotation.NonNull;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Provides different types of schedulers.
 */
public class TodoSchedulerProvider implements SchedulerProvider {

    @Override
    @NonNull
    public Scheduler computation() {
        return Schedulers.computation();
    }

    @Override
    @NonNull
    public Scheduler single() {
        return Schedulers.single();
    }

    @Override
    @NonNull
    public Scheduler newThread() {
        return Schedulers.newThread();
    }

    @Override
    @NonNull
    public Scheduler trampoline() {
        return Schedulers.trampoline();
    }

    @Override
    @NonNull
    public Scheduler io() {
        return Schedulers.io();
    }

    @Override
    @NonNull
    public Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }

    @Override
    @NonNull
    public Scheduler viewModel() {
        return Schedulers.single();
    }

    @Override
    @NonNull
    public Scheduler database() {
        return Schedulers.io();
    }
}
