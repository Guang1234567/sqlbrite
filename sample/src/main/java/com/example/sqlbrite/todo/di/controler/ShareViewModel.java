package com.example.sqlbrite.todo.di.controler;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.gg.rxbase.lifecycle.RxViewModelLifecycleProviderImpl;
import com.gg.rxbase.lifecycle.ViewModelEvent;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Observable;

public abstract class ShareViewModel extends ViewModel implements LifecycleProvider<ViewModelEvent> {

    public static final String TAG = "ShareViewModel";

    private final AtomicInteger mRefCounter;

    private Runnable mOnShareCreated;

    private Runnable mOnShareCleared;

    private final RxViewModelLifecycleProviderImpl mLifecycleProvider;

    protected ShareViewModel() {
        mRefCounter = new AtomicInteger(0);
        mLifecycleProvider = new RxViewModelLifecycleProviderImpl();
    }

    void setOnShareCreated(Runnable hook) {
        mOnShareCreated = hook;
    }

    void setOnShareCleared(Runnable hook) {
        mOnShareCleared = hook;
    }

    @Override
    protected final void onCleared() {
        super.onCleared();
        decRefCount();
    }

    protected void onFirstRef() {

    }

    protected void onLastRef() {

    }

    protected void onShareCleared() {
    }

    public final int incRefCount() {
        int counter = mRefCounter.incrementAndGet();
        if (counter == 1) {
            onFirstRef();
            if (mOnShareCreated != null) {
                mOnShareCreated.run();
                mOnShareCreated = null;
            }
            mLifecycleProvider.onNext(ViewModelEvent.CREATE);
        }
        return counter;
    }

    public final int decRefCount() {
        int counter = mRefCounter.decrementAndGet();
        if (counter == 0) {
            mLifecycleProvider.onNext(ViewModelEvent.DESTROY);
            onShareCleared();
            if (mOnShareCleared != null) {
                mOnShareCleared.run();
                mOnShareCleared = null;
            }
            onLastRef();
        } else if (counter < 0) {
            Log.e(TAG, "too many decRefCount() call!", new Exception());

            mRefCounter.set(0);
            mOnShareCleared = null;
            counter = 0;
        }
        return counter;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ShareViewModel{");
        sb.append("mRefCounter=").append(mRefCounter);
        sb.append(", mOnShareCleared=").append(mOnShareCleared);
        sb.append('}');
        return sb.toString();
    }

    @NonNull
    @Override
    public final Observable<ViewModelEvent> lifecycle() {
        return mLifecycleProvider.lifecycle();
    }

    @NonNull
    @Override
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ViewModelEvent event) {
        return mLifecycleProvider.bindUntilEvent(event);
    }

    @NonNull
    @Override
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return mLifecycleProvider.bindToLifecycle();
    }
}
