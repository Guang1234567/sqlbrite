package com.example.sqlbrite.todo.schedulers;

import android.support.annotation.NonNull;

import io.reactivex.Scheduler;

/**
 * Allow providing different types of {@link Scheduler}s.
 */
public interface SchedulerProvider {

    @NonNull
    Scheduler computation();

    @NonNull
    Scheduler single();

    @NonNull
    Scheduler newThread();

    @NonNull
    Scheduler trampoline();

    @NonNull
    Scheduler io();

    @NonNull
    Scheduler ui();

    @NonNull
    Scheduler viewModel();

    @NonNull
    Scheduler database();

    @NonNull
    Scheduler net();
}
