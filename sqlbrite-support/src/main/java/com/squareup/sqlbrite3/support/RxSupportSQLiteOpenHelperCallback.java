package com.squareup.sqlbrite3.support;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;

/**
 * @author Administrator
 * @date 2018/4/13 16:15
 */

public class RxSupportSQLiteOpenHelperCallback extends SupportSQLiteOpenHelper.Callback {

    public static final String TAG = "RxSupportSQLiteOpenHelperCallback";

    private final PublishSubject<DbVersion> mVersionEmitter;

    private final int mFirstVersion;
    private final int mLastVersion;

    public RxSupportSQLiteOpenHelperCallback(int firstVersion, int lastVersion) {
        super(lastVersion);
        mVersionEmitter = PublishSubject.create();
        mFirstVersion = firstVersion;
        mLastVersion = lastVersion;
    }

    @Override
    final public void onCreate(SupportSQLiteDatabase db) {
        Log.i(TAG, "┏ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ┓");
        Log.i(TAG, "┃            Start Create Database");
        Log.i(TAG, "┗ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ┛");
        Log.i(TAG, "db = " + db);
        doEachVer(db, mFirstVersion, mLastVersion);
        Log.i(TAG, "┏ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ┓");
        Log.i(TAG, "┃              End Create Database");
        Log.i(TAG, "┗ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ┛");
    }

    @Override
    final public void onUpgrade(SupportSQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "┏ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ┓");
        Log.i(TAG, "┃            Start Upgrade Database");
        Log.i(TAG, "┗ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ┛");
        Log.i(TAG, "db = " + db + "   (@" + db.hashCode() + ")");
        Log.i(TAG, "oldVersion = " + oldVersion);
        Log.i(TAG, "newVersion = " + newVersion);
        doEachVer(db, oldVersion + 1, newVersion);
        Log.i(TAG, "┏ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ┓");
        Log.i(TAG, "┃              End Upgrade Database");
        Log.i(TAG, "┗ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ━ ┛");
    }

    private void doEachVer(SupportSQLiteDatabase db, int fromVersion, int toVersion) {
        for (int toVer = fromVersion; toVer <= toVersion; toVer++) {
            mVersionEmitter.onNext(new DbVersion(db, toVer));
        }
        mVersionEmitter.onComplete();
    }

    public Observable<SupportSQLiteDatabase> upgrateToVersion(final int version) {
        return mVersionEmitter
                .filter(new Predicate<DbVersion>() {
                    @Override
                    public boolean test(DbVersion ver) throws Exception {
                        return ver.mCode == version;
                    }
                }).map(new Function<DbVersion, SupportSQLiteDatabase>() {
                    @Override
                    public SupportSQLiteDatabase apply(DbVersion ver) throws Exception {
                        return ver.mDb;
                    }
                });
    }

    private static class DbVersion {
        SupportSQLiteDatabase mDb;
        int mCode;

        DbVersion(SupportSQLiteDatabase db, int code) {
            mDb = db;
            mCode = code;
        }
    }
}
