package com.example.sqlbrite.todo.controler;

import com.example.sqlbrite.todo.model.LoginFlowRepository;
import com.example.sqlbrite.todo.model.users.UserManager;
import com.example.sqlbrite.todo.schedulers.SchedulerProvider;
import com.gg.rxbase.controller.RxBaseViewModel;

import javax.inject.Inject;

import io.reactivex.Completable;

/**
 * @author Guang1234567
 * @date 2018/3/6 14:40
 */

public class SystemSettingViewModel extends RxBaseViewModel {

    private final UserManager mUserManager;
    private final SchedulerProvider mSchedulerProvider;
    private final LoginFlowRepository mloginViewModel;

    @Inject
    public SystemSettingViewModel(UserManager userManager,
                                  SchedulerProvider schedulerProvider,
                                  LoginFlowRepository loginViewModel) {
        mUserManager = userManager;
        mSchedulerProvider = schedulerProvider;
        mloginViewModel = loginViewModel;
    }


    public Completable logout() {
        return mloginViewModel.logout()
                .compose(this.bindToLifecycle());
    }
}
