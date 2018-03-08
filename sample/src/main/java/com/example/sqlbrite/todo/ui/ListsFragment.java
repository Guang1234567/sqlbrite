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
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sqlbrite.todo.R;
import com.example.sqlbrite.todo.TodoApp;
import com.example.sqlbrite.todo.controler.MainViewModel;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import static android.support.v4.view.MenuItemCompat.SHOW_AS_ACTION_IF_ROOM;
import static android.support.v4.view.MenuItemCompat.SHOW_AS_ACTION_WITH_TEXT;

public final class ListsFragment extends Fragment {
    private static final String TAG = "ListsFragment";

    interface Listener {
        void onListClicked(long id);

        void onNewListClicked();
    }

    static ListsFragment newInstance() {
        return new ListsFragment();
    }

/*
    @Inject
    BriteDatabase db;

    @Inject
    ListsItemDao mListsItemDao;
*/

    @Inject
    ViewModelProvider.Factory mViewModelFactory;

    //@Inject
    MainViewModel mViewModel;

    @BindView(android.R.id.list)
    ListView listView;
    @BindView(android.R.id.empty)
    View emptyView;
    @BindView(R.id.export_db)
    Button btnExportDB;

    private Listener listener;
    private ListsAdapter adapter;
    private Disposable disposable;

    @Override
    public void onAttach(Activity activity) {
        if (!(activity instanceof Listener)) {
            throw new IllegalStateException("Activity must implement fragment Listener.");
        }

        super.onAttach(activity);
        TodoApp.getComponent(activity).inject(this);
        setHasOptionsMenu(true);

        listener = (Listener) activity;
        adapter = new ListsAdapter(activity);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem item = menu.add(R.string.new_list)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        listener.onNewListClicked();
                        return true;
                    }
                });
        MenuItemCompat.setShowAsAction(item, SHOW_AS_ACTION_IF_ROOM | SHOW_AS_ACTION_WITH_TEXT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.lists, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        listView.setEmptyView(emptyView);
        listView.setAdapter(adapter);
    }

    @OnClick(R.id.export_db)
    void onExportDB(View btn) {
        try {
            File dstFile = mViewModel.exportDecryption();
            Toast.makeText(getContext(), "导出数据库成功!\n" + dstFile.getPath(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "导出数据库失败!", e);
            Toast.makeText(getContext(), "导出数据库失败!", Toast.LENGTH_SHORT).show();
        }
    }

    @OnItemClick(android.R.id.list)
    void listClicked(long listId) {
        listener.onListClicked(listId);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity(), mViewModelFactory).get(MainViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().setTitle("To-Do");

    /*disposable = db.createQuery(ListsItem.TABLES, ListsItem.QUERY)
        .mapToList(ListsItem.MAPPER) // 耗内存
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(adapter);*/


        disposable = mViewModel.createQueryListsItems() // 省内存
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(adapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        disposable.dispose();
    }
}
