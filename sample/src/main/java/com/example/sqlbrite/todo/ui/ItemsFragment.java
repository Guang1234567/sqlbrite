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

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sqlbrite.todo.R;
import com.example.sqlbrite.todo.TodoApp;
import com.example.sqlbrite.todo.controler.MainViewModel;
import com.example.sqlbrite.todo.di.FragmentScopeComponent;
import com.example.sqlbrite.todo.model.local.db.TodoItem;
import com.jakewharton.rxbinding2.widget.AdapterViewItemClickEvent;
import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

import static android.support.v4.view.MenuItemCompat.SHOW_AS_ACTION_IF_ROOM;
import static android.support.v4.view.MenuItemCompat.SHOW_AS_ACTION_WITH_TEXT;

public final class ItemsFragment extends BaseViewModelFragment<MainViewModel> {
    private static final String KEY_LIST_ID = "list_id";

    public interface Listener {

        void onNewItemClicked(long listId);
    }

    public static ItemsFragment newInstance(long listId) {
        Bundle arguments = new Bundle();
        arguments.putLong(KEY_LIST_ID, listId);

        ItemsFragment fragment = new ItemsFragment();
        fragment.setArguments(arguments);
        return fragment;
    }


    @BindView(android.R.id.list)
    ListView listView;

    @BindView(android.R.id.empty)
    View emptyView;
    private Listener listener;

    private ItemsAdapter adapter;
    //private CompositeDisposable disposables;

    private long getListId() {
        return getArguments().getLong(KEY_LIST_ID);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = getActivity();
        if (!(activity instanceof Listener)) {
            throw new IllegalStateException("Activity must implement fragment Listener.");
        }

        setHasOptionsMenu(true);

        listener = (Listener) activity;
        adapter = new ItemsAdapter(activity);
    }

    @Override
    protected void injectOnAttach(FragmentScopeComponent component) {
        component.inject(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem item = menu.add(R.string.new_item)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        listener.onNewItemClicked(getListId());
                        return true;
                    }
                });
        MenuItemCompat.setShowAsAction(item, SHOW_AS_ACTION_IF_ROOM | SHOW_AS_ACTION_WITH_TEXT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.items, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        listView.setEmptyView(emptyView);
        listView.setAdapter(adapter);

        RxAdapterView.itemClickEvents(listView)
                .observeOn(getSchedulerProvider().io())
                .subscribe(new Consumer<AdapterViewItemClickEvent>() {
                    @Override
                    public void accept(AdapterViewItemClickEvent event) {
                        boolean newValue = !adapter.getItem(event.position()).complete();
                        getViewModel().complete(event.id(), newValue);
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        long listId = getListId();

        //disposables = new CompositeDisposable();

        Observable<Integer> itemCount = getViewModel().createQueryItemCount(listId);
        Observable<String> listName = getViewModel().createQueryListName(listId);

        //disposables.add(
        Observable.combineLatest(listName, itemCount, new BiFunction<String, Integer, String>() {
            @Override
            public String apply(String listName, Integer itemCount) {
                return listName + " (" + itemCount + ")";
            }
        })
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String title) throws Exception {
                        getActivity().setTitle(title);
                    }
                })
        //)
        ;

        //disposables.add(
        getViewModel().createQueryTodoItemsByListId(listId)
                .compose(this.<List<TodoItem>>bindUntilEvent(FragmentEvent.PAUSE))
                .observeOn(getSchedulerProvider().ui())
                .subscribe(adapter)
        //)
        ;

        CharSequence content = "请切换成横屏!\nViewModel 仍是同一个!\n创建时间 : " + String.valueOf(getViewModel().getLastCreateTime());
        Toast.makeText(getContext(),
                content,
                Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onPause() {
        super.onPause();
        //disposables.dispose();
    }
}
