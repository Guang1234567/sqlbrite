package com.example.sqlbrite.todo.ui;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;

import com.example.sqlbrite.todo.schedulers.SchedulerProvider;
import com.gg.rxbase.ui.RxBaseActivity;

import java.lang.reflect.ParameterizedType;

import javax.inject.Inject;

/**
 * @author Administrator
 * @date 2018/3/10 11:21
 */

public abstract class BaseViewModelActivity<VIEWMODEL extends ViewModel> extends RxBaseActivity {

    @Inject
    SchedulerProvider mSchedulerProvider;

    @Inject
    ViewModelProvider.Factory mViewModelFactory;

    private VIEWMODEL mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toInject(this);

        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        Class viewModelClazz = (Class) type.getActualTypeArguments()[0];
        mViewModel = (VIEWMODEL) ViewModelProviders.of(this, mViewModelFactory).get(viewModelClazz);
    }

    protected abstract void toInject(BaseViewModelActivity<VIEWMODEL> self);

    protected VIEWMODEL getViewModel() {
        return mViewModel;
    }

    public SchedulerProvider getSchedulerProvider() {
        return mSchedulerProvider;
    }
}