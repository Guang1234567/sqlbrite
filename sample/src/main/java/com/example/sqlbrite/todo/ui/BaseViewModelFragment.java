package com.example.sqlbrite.todo.ui;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.example.sqlbrite.todo.TodoApp;
import com.example.sqlbrite.todo.di.DaggerActivityScopeComponent;
import com.example.sqlbrite.todo.di.DaggerFragmentScopeComponent;
import com.example.sqlbrite.todo.di.FragmentScopeComponent;
import com.example.sqlbrite.todo.di.FragmentScopeModule;
import com.example.sqlbrite.todo.di.InjectHelper;
import com.example.sqlbrite.todo.schedulers.SchedulerProvider;
import com.gg.rxbase.ui.RxBaseFragment;

import java.lang.reflect.ParameterizedType;

import javax.inject.Inject;

/**
 * @author Guang1234567
 * @date 2018/3/9 10:03
 */

public abstract class BaseViewModelFragment<VIEWMODEL extends ViewModel> extends RxBaseFragment {

    @Inject
    SchedulerProvider mSchedulerProvider;

    @Inject
    ViewModelProvider.Factory mViewModelFactory;

    private VIEWMODEL mViewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        toInject(
                InjectHelper.createFragmentScopeComponent(context, getActivity(), this)
        );
    }

    protected abstract void toInject(FragmentScopeComponent component);

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        Class viewModelClazz = (Class) type.getActualTypeArguments()[0];
        mViewModel = (VIEWMODEL) ViewModelProviders.of(getActivity(), mViewModelFactory).get(viewModelClazz);
    }

    protected VIEWMODEL getViewModel() {
        return mViewModel;
    }

    public SchedulerProvider getSchedulerProvider() {
        return mSchedulerProvider;
    }
}
