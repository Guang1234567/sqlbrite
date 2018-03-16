package com.example.sqlbrite.todo.ui;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;

import com.example.sqlbrite.todo.di.ActivityScopeComponent;
import com.example.sqlbrite.todo.di.InjectHelper;
import com.example.sqlbrite.todo.schedulers.SchedulerProvider;
import com.gg.rxbase.ui.RxBaseActivity;

import java.lang.reflect.ParameterizedType;

import javax.inject.Inject;

/**
 * @author Guang1234567
 * @date 2018/3/10 11:21
 */

public abstract class BaseViewModelActivity<VIEWMODEL extends ViewModel> extends RxBaseActivity {

    private ActivityScopeComponent mActivityScopeComponent;

    @Inject
    SchedulerProvider mSchedulerProvider;

    @Inject
    ViewModelProvider.Factory mViewModelFactory;

    private VIEWMODEL mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityScopeComponent = InjectHelper.instance().createActivityScopeComponent(this);
        injectOnCreate(mActivityScopeComponent);

        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        Class viewModelClazz = (Class) type.getActualTypeArguments()[0];
        mViewModel = (VIEWMODEL) ViewModelProviders.of(this, mViewModelFactory).get(viewModelClazz);
    }

    public ActivityScopeComponent getActivityScopeComponent() {
        return mActivityScopeComponent;
    }

    protected abstract void injectOnCreate(ActivityScopeComponent component);

    protected VIEWMODEL getViewModel() {
        return mViewModel;
    }

    public SchedulerProvider getSchedulerProvider() {
        return mSchedulerProvider;
    }
}
