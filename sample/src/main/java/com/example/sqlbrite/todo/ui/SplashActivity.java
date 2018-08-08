package com.example.sqlbrite.todo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.sqlbrite.todo.R;
import com.example.sqlbrite.todo.controler.LoginFlowViewModel;
import com.example.sqlbrite.todo.di.AppActivityScopeComponent;
import com.example.sqlbrite.todo.model.users.UserSession;

import javax.inject.Inject;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class SplashActivity extends BaseAppViewModelActivity{

    private static final String TAG = "SplashActivity";

    @Inject
    LoginFlowViewModel mLoginFlowViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mLoginFlowViewModel
                .login("user_id_lucy", "pwd_1234567")
                .compose(this.<UserSession>bindToLifecycle())
                .observeOn(getSchedulerProvider().ui())
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void NaviToMain() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void inject(AppActivityScopeComponent component) {
        component.inject(this);
    }
}
