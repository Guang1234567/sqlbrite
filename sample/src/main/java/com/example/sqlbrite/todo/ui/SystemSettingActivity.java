package com.example.sqlbrite.todo.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sqlbrite.todo.R;
import com.example.sqlbrite.todo.TodoApp;
import com.example.sqlbrite.todo.controler.DemoShareViewModel;
import com.example.sqlbrite.todo.controler.SystemSettingViewModel;
import com.example.sqlbrite.todo.di.ActivityScopeComponent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SystemSettingActivity extends BaseViewModelActivity<SystemSettingViewModel> {

    private static final String TAG = "SystemSettingActivity";
    @BindView(R.id.btn_logout)
    Button mBtnLogout;

    private DemoShareViewModel mDemoShareViewModel;

    @Override
    protected void injectOnCreate(ActivityScopeComponent component) {
        component.inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_setting);

        ButterKnife.bind(this);

        mDemoShareViewModel = mViewModelFactory.provide(this, DemoShareViewModel.class);
    }

    @OnClick(R.id.btn_logout)
    void listClicked(View v) {
        getViewModel().logout()
                .observeOn(getSchedulerProvider().ui())
                .subscribe(
                        () -> {
                            Toast.makeText(SystemSettingActivity.this, "注销成功!", Toast.LENGTH_SHORT).show();

                            TodoApp.getApplication(this).exit();
                        },

                        error -> {
                            Toast.makeText(SystemSettingActivity.this, "注销失败!", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "注销失败!", error);
                        }
                );
    }
}
