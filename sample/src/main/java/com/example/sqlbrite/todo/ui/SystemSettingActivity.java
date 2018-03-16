package com.example.sqlbrite.todo.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sqlbrite.todo.R;
import com.example.sqlbrite.todo.controler.SystemSettingViewModel;
import com.example.sqlbrite.todo.di.ActivityScopeComponent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SystemSettingActivity extends BaseViewModelActivity<SystemSettingViewModel> {

    @BindView(R.id.btn_logout)
    Button mBtnLogout;

    @Override
    protected void injectOnCreate(ActivityScopeComponent component) {
        component.inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_setting);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_logout)
    void listClicked(View v) {
        getViewModel().logout();
    }
}
