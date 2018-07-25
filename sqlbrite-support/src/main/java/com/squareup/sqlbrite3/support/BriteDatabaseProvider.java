package com.squareup.sqlbrite3.support;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.squareup.sqlbrite3.BriteDatabase;
import com.squareup.sqlbrite3.SqlBrite;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;

/**
 * @author Guang1234567
 * @date 2018/3/8 15:12
 */
class BriteDatabaseProvider {

    private final SqlBrite mSqlBrite;
    private final SupportSQLiteOpenHelper mHelper;
    private final Scheduler mScheduler;

    private final AtomicInteger mDbRefCounter;
    private BriteDatabase mDb;

    public BriteDatabaseProvider(@NonNull SqlBrite sqlBrite,
                                 @NonNull SupportSQLiteOpenHelper helper,
                                 @NonNull Scheduler scheduler) {
        mSqlBrite = sqlBrite;
        mHelper = helper;
        mScheduler = scheduler;

        mDbRefCounter = new AtomicInteger(0);
        mDb = null;
    }

    private synchronized BriteDatabase openBriteDatabase() {
        if (mDbRefCounter.incrementAndGet() == 1) {
            mDb = mSqlBrite.wrapDatabaseHelper(mHelper, mScheduler);
        }
        return mDb;
    }

    private synchronized void closeBriteDatabase() {
        int counter = mDbRefCounter.decrementAndGet();
        if (counter == 0) {
            mDb.close();
            mDb = null;
        } else if (counter < 0) {
            mDbRefCounter.set(0);
        }
    }

    public Observable<BriteDatabase> get() {
        return Observable.create(new ObservableOnSubscribe<BriteDatabase>() {
            @Override
            public void subscribe(ObservableEmitter<BriteDatabase> e) throws Exception {
                e.setDisposable(new Disposable() {
                    final AtomicBoolean mIsDispose = new AtomicBoolean(false);

                    @Override
                    public void dispose() {
                        if (mIsDispose.compareAndSet(false, true)) {
                            BriteDatabaseProvider.this.closeBriteDatabase();
                        }
                    }

                    @Override
                    public boolean isDisposed() {
                        return mIsDispose.get();
                    }
                });

                BriteDatabase briteDatabase = openBriteDatabase();
                e.onNext(briteDatabase);

                if (!e.isDisposed()) {
                    e.onComplete();
                }
            }
        }).subscribeOn(mScheduler);
    }
}
