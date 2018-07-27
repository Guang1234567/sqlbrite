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
package com.example.sqlbrite.todo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.sqlbrite.todo.R;
import com.example.sqlbrite.todo.controler.DemoShareViewModel;
import com.example.sqlbrite.todo.controler.MainViewModel;
import com.example.sqlbrite.todo.di.ActivityScopeComponent;
import com.example.sqlbrite.todo.model.users.UserSession;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public final class MainActivity extends BaseViewModelActivity<MainViewModel>
        implements ListsFragment.Listener, ItemsFragment.Listener {

    private static final String TAG = "MainActivity";

    private DemoShareViewModel mDemoShareViewModel;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, ListsFragment.newInstance())
                    .commit();
        }


        TypeToken typeToken = new TypeToken<List<Integer>>() {
        };

        typeToken.getRawType();
        typeToken.getType();

        Observable.range(1, 3).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.e("lll", String.valueOf(integer));
            }
        });

        mDemoShareViewModel = mViewModelFactory.provide(this, DemoShareViewModel.class);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Obtain currrent login user to do sth.
        // No login no trigger
        getViewModel().currentLoginUserSession()
                .compose(this.<UserSession>bindToLifecycle())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<UserSession>() {
                    @Override
                    public void accept(UserSession userSession) throws Exception {
                        Toast.makeText(MainActivity.this, "当前用户 : " + userSession.user().id(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void injectOnCreate(ActivityScopeComponent component) {
        component.inject(this);
    }

    @Override
    public void onListClicked(long id) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left,
                        R.anim.slide_out_right)
                .replace(android.R.id.content, ItemsFragment.newInstance(id))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onNewListClicked() {
        NewListFragment.newInstance().show(getSupportFragmentManager(), "new-list");
    }

    @Override
    public void onNewItemClicked(long listId) {
        NewItemFragment.newInstance(listId).show(getSupportFragmentManager(), "new-item");
    }

    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().findFragmentById(android.R.id.content) instanceof ListsFragment) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        } else {
            super.onBackPressed();
        }
    }
}
