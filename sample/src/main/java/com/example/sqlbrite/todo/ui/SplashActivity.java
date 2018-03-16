package com.example.sqlbrite.todo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.sqlbrite.todo.R;
import com.example.sqlbrite.todo.controler.LoginViewControler;
import com.example.sqlbrite.todo.di.AppScopeComponent;
import com.example.sqlbrite.todo.di.InjectHelper;
import com.example.sqlbrite.todo.model.users.UserSession;
import com.example.sqlbrite.todo.schedulers.SchedulerProvider;
import com.gg.rxbase.ui.RxBaseActivity;

import javax.inject.Inject;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class SplashActivity extends RxBaseActivity {

    private static final String TAG = "SplashActivity";

    @Inject
    LoginViewControler mViewModel;
    @Inject SchedulerProvider mSchedulerProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        AppScopeComponent appScopeComponent = InjectHelper.instance().getAppScopeComponent();
        appScopeComponent.inject(this);

        mViewModel
                .login("user_id_lucy", "pwd_1234567")
                .compose(this.<UserSession>bindToLifecycle())
                .observeOn(mSchedulerProvider.ui())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        finish();
                    }
                })
                .subscribe(new Consumer<UserSession>() {
                               @Override
                               public void accept(UserSession userSession) throws Exception {
                                   Toast.makeText(SplashActivity.this, "登陆成功!\n进入界面!", Toast.LENGTH_SHORT).show();

                                   NaviToMain();
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable e) throws Exception {
                                Toast.makeText(SplashActivity.this, "登陆失败!", Toast.LENGTH_SHORT).show();

                                Log.e(TAG, "登陆失败!", e);
                            }
                        });

    }

    private void NaviToMain() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
