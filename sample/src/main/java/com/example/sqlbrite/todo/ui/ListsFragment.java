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
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sqlbrite.todo.R;
import com.example.sqlbrite.todo.controler.DemoShareViewModel;
import com.example.sqlbrite.todo.controler.MainViewModel;
import com.example.sqlbrite.todo.di.FragmentScopeComponent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;
import me.drakeet.multitype.ItemViewBinder;
import me.drakeet.multitype.MultiTypeAdapter;

public final class ListsFragment extends BaseViewModelFragment<MainViewModel> {
    private static final String TAG = "ListsFragment";

    interface Listener {
        void onListClicked(long id);

        void onNewListClicked();
    }

    static ListsFragment newInstance() {
        return new ListsFragment();
    }

    @BindView(android.R.id.list)
    RecyclerView listView;
    @BindView(android.R.id.empty)
    View emptyView;

    private Listener listener;
    private MultiTypeAdapter adapter;
    //private Disposable disposable;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity = getActivity();
        if (!(activity instanceof Listener)) {
            throw new IllegalStateException("Activity must implement fragment Listener.");
        }

        setHasOptionsMenu(true);

        listener = (Listener) activity;
        //adapter = new ListsAdapter(context);
        adapter = new MultiTypeAdapter();
        adapter.register(ListsItem.class, new ListsItemViewBinder(listener));
    }

    @Override
    protected void injectOnAttach(FragmentScopeComponent component) {
        component.inject(this);
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

        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        item = menu.add(R.string.export_db)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        exportDB();
                        return true;
                    }
                });

        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        item = menu.add(R.string.setting)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent = new Intent(getContext(), SystemSettingActivity.class);
                        startActivity(intent);
                        return true;
                    }
                });

        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        item = menu.add("Test")
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        getViewModel().createQueryListsItems() // 省内存
                                .compose(ListsFragment.this.<List<ListsItem>>bindUntilEvent(FragmentEvent.PAUSE))
                                .observeOn(getSchedulerProvider().ui())
                                .subscribe(new Consumer<List<ListsItem>>() {
                                    @Override
                                    public void accept(List<ListsItem> listsItems) throws Exception {
                                    }
                                });
                        return true;
                    }
                });

        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
    }

    private void exportDB() {
        try {
            File dstFile = getViewModel().exportDecryption();
            Toast.makeText(getContext(), "导出数据库成功!\n" + dstFile.getPath(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "导出数据库失败!", e);
            Toast.makeText(getContext(), "导出数据库失败!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.lists, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        listView.setLayoutManager(new LinearLayoutManager(listView.getContext()));
        listView.addItemDecoration(new DividerItemDecoration(listView.getContext(), DividerItemDecoration.VERTICAL));
        //listView.setEmptyView(emptyView);
        listView.setAdapter(adapter);

        DemoShareViewModel demoShareViewModel = ViewModelProviders.of(getActivity(), mViewModelFactory).get(DemoShareViewModel.class);
    }

    /*@OnItemClick(android.R.id.list)
    void listClicked(long listId) {
        listener.onListClicked(listId);
    }*/

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().setTitle("To-Do");

        /*
        disposable = db.createQuery(ListsItem.TABLES, ListsItem.QUERY)
        .mapToList(ListsItem.MAPPER) // 耗内存
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(adapter);*/


        /*disposable = */
        getViewModel().createQueryListsItems() // 省内存
                .compose(this.<List<ListsItem>>bindUntilEvent(FragmentEvent.PAUSE))
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<List<ListsItem>>() {
                    @Override
                    public void accept(List<ListsItem> listsItems) throws Exception {
                        adapter.setItems(listsItems);
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onPause() {
        super.onPause();
        //disposable.dispose();  // RxLifecycle instead of
    }


    private static class ListsItemViewBinder extends ItemViewBinder<ListsItem, ListsItemViewBinder.ListsItemHolder> {
        private Listener listener;

        public ListsItemViewBinder(Listener listener) {
            this.listener = listener;
        }

        static class ListsItemHolder extends RecyclerView.ViewHolder {

            @NonNull
            private final TextView text;

            ListsItemHolder(@NonNull View itemView) {
                super(itemView);
                this.text = (TextView) itemView;
            }
        }

        @NonNull
        @Override
        protected ListsItemHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
            View root = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ListsItemHolder(root);
        }

        @Override
        protected void onBindViewHolder(@NonNull ListsItemHolder holder, @NonNull ListsItem item) {
            holder.text.setText(item.name() + " (" + item.itemCount() + ") " + String.valueOf(item.createTimestamp()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onListClicked(item.id());
                }
            });
        }

        @Override
        protected void onViewRecycled(@NonNull ListsItemHolder holder) {
            super.onViewRecycled(holder);
            holder.text.setText(null);
            holder.itemView.setOnClickListener(null);
        }
    }
}
