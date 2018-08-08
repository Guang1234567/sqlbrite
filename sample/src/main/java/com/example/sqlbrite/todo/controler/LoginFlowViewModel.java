package com.example.sqlbrite.todo.controler;

import android.arch.lifecycle.ViewModel;

import com.example.sqlbrite.todo.model.LoginFlowRepository;
import com.example.sqlbrite.todo.model.users.UserSession;

import javax.inject.Inject;

import io.reactivex.Single;

public class LoginFlowViewModel extends ViewModel {

    private LoginFlowRepository mLoginFlowRepository;

    @Inject
    protected LoginFlowViewModel(LoginFlowRepository LoginFlowRepository) {
        super();

        mLoginFlowRepository = LoginFlowRepository;
    }

    public Single<UserSession> login(final String userId, final String password) {
        return mLoginFlowRepository.login(userId, password);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
