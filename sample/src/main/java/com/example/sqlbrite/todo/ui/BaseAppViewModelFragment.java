package com.example.sqlbrite.todo.ui;import android.content.Context;import android.os.Bundle;import android.support.annotation.Nullable;import com.example.sqlbrite.todo.di.AppFragmentScopeComponent;import com.example.sqlbrite.todo.di.InjectHelper;import com.example.sqlbrite.todo.schedulers.SchedulerProvider;import com.gg.rxbase.ui.RxBaseFragment;import javax.inject.Inject;/** * @author Guang1234567 * @date 2018/3/9 10:03 */public abstract class BaseAppViewModelFragment extends RxBaseFragment implements AppFragmentScopeComponent.Injectable {    private AppFragmentScopeComponent mFragmentScopeComponent;    @Inject    SchedulerProvider mSchedulerProvider;    @Override    public void onAttach(Context context) {        super.onAttach(context);        if (!(getActivity() instanceof BaseAppViewModelActivity)) {            throw new IllegalStateException("Activity must extends " + BaseAppViewModelActivity.class);        }    }    @SuppressWarnings("unchecked")    @Override    public void onActivityCreated(@Nullable Bundle savedInstanceState) {        super.onActivityCreated(savedInstanceState);        mFragmentScopeComponent = InjectHelper.instance().createAppFragmentScopeComponent(                this,                ((BaseAppViewModelActivity) getActivity()).getActivityScopeComponent());        inject(mFragmentScopeComponent);    }    public SchedulerProvider getSchedulerProvider() {        return mSchedulerProvider;    }}