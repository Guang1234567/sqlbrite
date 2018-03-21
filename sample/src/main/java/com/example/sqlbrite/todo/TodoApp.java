/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.sqlbrite.todo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.MainThread;
import android.support.multidex.MultiDex;

import com.example.sqlbrite.todo.di.AppScopeComponent;
import com.example.sqlbrite.todo.di.InjectHelper;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public final class TodoApp extends Application {

    private AppScopeComponent mAppScopeComponent;

    @Inject
    ActivityMgr mActivityMgr;

    public static TodoApp getApplication(Context context) {
        return (TodoApp) context.getApplicationContext();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        mAppScopeComponent = InjectHelper.instance()
                .init(this);

        mAppScopeComponent.inject(this);

    }

    public void exit() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            exitInMainThread();
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    exitInMainThread();
                }
            });
        }
    }

    private void exitInMainThread() {
        mActivityMgr.finishAllActivity();
    }

    public final static class ActivityMgr {
        private List<Activity> mActivitys;

        public ActivityMgr(Application app) {
            mActivitys = new LinkedList<>();
            app.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                    mActivitys.add(activity);
                }

                @Override
                public void onActivityStarted(Activity activity) {
                }

                @Override
                public void onActivityResumed(Activity activity) {
                }

                @Override
                public void onActivityPaused(Activity activity) {
                }

                @Override
                public void onActivityStopped(Activity activity) {
                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                }

                @Override
                public void onActivityDestroyed(Activity activity) {
                    if (!mActivitys.isEmpty()) {
                        mActivitys.remove(activity);
                    }
                }
            });
        }

        @MainThread
        void finishAllActivity() {
            if (mActivitys.isEmpty()) {
                return;
            }
            Iterator<Activity> iterator = mActivitys.iterator();
            while (iterator.hasNext()) {
                Activity activity = iterator.next();
                activity.finish();
                iterator.remove();
            }
        }
    }
}
