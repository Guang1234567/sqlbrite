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

import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.sqlbrite.todo.R;
import com.example.sqlbrite.todo.controler.MainViewModel;
import com.example.sqlbrite.todo.di.InjectHelper;
import com.example.sqlbrite.todo.schedulers.SchedulerProvider;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.trello.rxlifecycle2.components.support.RxAppCompatDialogFragment;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

import static butterknife.ButterKnife.findById;

public final class NewListFragment extends RxAppCompatDialogFragment {
    public static NewListFragment newInstance() {
        return new NewListFragment();
    }

    private final PublishSubject<String> createClicked = PublishSubject.create();


    @Inject
    SchedulerProvider mSchedulerProvider;

    @Inject
    ViewModelProvider.Factory mViewModelFactory;

    //@Inject
    MainViewModel mViewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        InjectHelper.instance().createFragmentScopeComponent(getActivity(), this).inject(this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(getActivity(), mViewModelFactory).get(MainViewModel.class);

        final Context context = getActivity();
        View view = LayoutInflater.from(context).inflate(R.layout.new_list, null);

        EditText name = findById(view, android.R.id.input);
        Observable.combineLatest(createClicked, RxTextView.textChanges(name),
                new BiFunction<String, CharSequence, String>() {
                    @Override
                    public String apply(String ignored, CharSequence text) {
                        return text.toString();
                    }
                }) //
                .observeOn(mSchedulerProvider.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String name) {
                        mViewModel.createNewOneTodoList(name);
                    }
                });

        return new AlertDialog.Builder(context)
                .setTitle(R.string.new_list)
                .setView(view)
                .setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createClicked.onNext("clicked");
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                    }
                })
                .create();
    }
}
