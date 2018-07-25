/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.sqlbrite.todo.di.controler;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.Nullable;

import com.example.sqlbrite.todo.di.UserScope;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

@UserScope
public class UserScopeViewModelFactory implements ViewModelProvider.Factory {
    private final Map<Class<? extends ViewModel>, Provider<ViewModel>> creators;

    @Inject
    public UserScopeViewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> creators) {
        this.creators = creators;
    }

    /*
    @SuppressWarnings("unchecked")
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        Provider<? extends ViewModel> creator = creators.get(modelClass);
        if (creator == null) {
            for (Map.Entry<Class<? extends ViewModel>, Provider<ViewModel>> entry : creators.entrySet()) {
                if (modelClass.isAssignableFrom(entry.getKey())) {
                    creator = entry.getValue();
                    break;
                }
            }
        }
        if (creator == null) {
            throw new IllegalArgumentException("unknown model class " + modelClass);
        }
        try {
            return (T) creator.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    */

    private static final Map<Class<? extends ShareViewModel>, ShareViewModel> sShareCache = new HashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        Provider<? extends ViewModel> creator = creators.get(modelClass);
        if (creator == null) {
            for (Map.Entry<Class<? extends ViewModel>, Provider<ViewModel>> entry : creators.entrySet()) {
                if (modelClass.isAssignableFrom(entry.getKey())) {
                    creator = entry.getValue();
                    break;
                }
            }
        }

        if (creator == null) {
            throw new IllegalArgumentException("unknown model class " + modelClass);
        }

        if (ShareViewModel.class.isAssignableFrom(modelClass)) {
            return (T) provideShareViewModel((Class<ShareViewModel>) modelClass, (Provider<ShareViewModel>) creator);
        }

        try {
            return (T) creator.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    private ShareViewModel provideShareViewModel(Class<? extends ShareViewModel> modelClass, Provider<? extends ShareViewModel> creator) {
        ShareViewModel shareVM;
        if (sShareCache.containsKey(modelClass)) {
            shareVM = sShareCache.get(modelClass);
        } else {
            try {
                shareVM = creator.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (shareVM != null) {
                final ShareViewModel tmp = shareVM;
                shareVM.setOnShareCreated(new Runnable() {
                    @Override
                    public void run() {
                        sShareCache.put(modelClass, tmp);
                    }
                });
                shareVM.setOnShareCleared(new Runnable() {
                    @Override
                    public void run() {
                        sShareCache.remove(modelClass);
                    }
                });
            }
        }

        if (shareVM != null) {
            shareVM.incRefCount();
        }
        return shareVM;
    }
}
