package com.example.sqlbrite.todo.di.controler;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class ShareViewModel extends ViewModel {

    public static final String TAG = "ShareViewModel";

    private final AtomicInteger mRefCounter;

    private Runnable mOnShareCreated;

    private Runnable mOnShareCleared;

    protected ShareViewModel() {
        mRefCounter = new AtomicInteger(0);
    }

    void setOnShareCreated(Runnable hook) {
        mOnShareCreated = hook;
    }

    void setOnShareCleared(Runnable hook) {
        mOnShareCleared = hook;
    }

    @Override
    protected final void onCleared() {
        decRefCount();
    }

    protected void onFirstRef() {

    }

    protected void onLastRef() {

    }

    protected abstract void onShareCleared();

    public final int incRefCount() {
        int counter = mRefCounter.incrementAndGet();
        if (counter == 1) {
            onFirstRef();
            if (mOnShareCreated != null) {
                mOnShareCreated.run();
                mOnShareCreated = null;
            }
        }
        return counter;
    }

    public final int decRefCount() {
        int counter = mRefCounter.decrementAndGet();
        if (counter == 0) {
            onLastRef();
            if (mOnShareCleared != null) {
                mOnShareCleared.run();
                mOnShareCleared = null;
            }
            onShareCleared();
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
}
