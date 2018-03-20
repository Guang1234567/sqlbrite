package com.example.sqlbrite.todo.ui;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;

import com.example.sqlbrite.todo.di.FragmentScopeComponent;
import com.example.sqlbrite.todo.di.InjectHelper;
import com.example.sqlbrite.todo.schedulers.SchedulerProvider;
import com.trello.rxlifecycle2.components.support.RxAppCompatDialogFragment;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.inject.Inject;

/**
 * @author Guang1234567
 * @date 2018/3/9 10:03
 */

public abstract class BaseViewModelDialogFragment<VIEWMODEL extends ViewModel> extends RxAppCompatDialogFragment {

    private FragmentScopeComponent mFragmentScopeComponent;

    @Inject
    SchedulerProvider mSchedulerProvider;

    @Inject
    ViewModelProvider.Factory mViewModelFactory;

    private VIEWMODEL mViewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(getActivity() instanceof BaseViewModelActivity)) {
            throw new IllegalStateException("Activity must extends " + BaseViewModelActivity.class);
        }

        mFragmentScopeComponent = InjectHelper.instance().createFragmentScopeComponent(getActivity(), this);
        injectOnAttach(mFragmentScopeComponent);
    }

    protected FragmentScopeComponent getFragmentScopeComponent() {
        return mFragmentScopeComponent;
    }

    protected abstract void injectOnAttach(FragmentScopeComponent component);

    @SuppressWarnings("unchecked")
    @Override
    public LayoutInflater onGetLayoutInflater(@Nullable Bundle savedInstanceState) {

        Class<VIEWMODEL> viewModelClazz;
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
            viewModelClazz = (Class<VIEWMODEL>) actualTypeArguments[0];
        } else {
            viewModelClazz = (Class<VIEWMODEL>) genericSuperclass;
        }
        mViewModel = (VIEWMODEL) ViewModelProviders.of(getActivity(), mViewModelFactory).get(viewModelClazz);

        return super.onGetLayoutInflater(savedInstanceState);
    }

    protected VIEWMODEL getViewModel() {
        return mViewModel;
    }

    public SchedulerProvider getSchedulerProvider() {
        return mSchedulerProvider;
    }
}
