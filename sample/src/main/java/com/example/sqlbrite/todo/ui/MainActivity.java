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

import android.os.Bundle;
import android.widget.Toast;

import com.example.sqlbrite.todo.R;
import com.example.sqlbrite.todo.controler.MainViewModel;
import com.example.sqlbrite.todo.di.ActivityScopeComponent;
import com.example.sqlbrite.todo.model.users.UserSession;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public final class MainActivity extends BaseViewModelActivity<MainViewModel>
        implements ListsFragment.Listener, ItemsFragment.Listener {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getViewModel()
                .login("id_user_lucy", "pwd_1234567")
                .compose(this.<UserSession>bindUntilEvent(ActivityEvent.DESTROY))
                .flatMap(new Function<UserSession, Observable<UserSession>>() {
                    @Override
                    public Observable<UserSession> apply(UserSession userSession) throws Exception {
                        return UserSession.FAIL.equals(userSession) ?
                                Observable.<UserSession>error(new AssertionError())
                                : Observable.just(userSession);
                    }
                })
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<UserSession>() {
                               @Override
                               public void accept(UserSession userSession) throws Exception {
                                   Toast.makeText(MainActivity.this, "登陆成功!\n进入界面!", Toast.LENGTH_SHORT).show();

                                   if (savedInstanceState == null) {
                                       getSupportFragmentManager().beginTransaction()
                                               .add(android.R.id.content, ListsFragment.newInstance())
                                               .commit();
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Toast.makeText(MainActivity.this, "登陆失败!", Toast.LENGTH_SHORT).show();
                            }
                        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            getViewModel().logout();
        }
    }

    @Override
    protected void toInject(ActivityScopeComponent component) {
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
}
