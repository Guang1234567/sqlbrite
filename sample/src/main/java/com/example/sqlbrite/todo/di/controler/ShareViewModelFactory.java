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
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class ShareViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private static final Map<Class<? extends ViewModel>, ViewModel> sShareCache = new HashMap<>();

    @NonNull
    @Override
    public <T extends ViewModel> T create(final @NonNull Class<T> modelClass) {
        if (ShareViewModel.class.isAssignableFrom(modelClass)) {
            ShareViewModel shareVM = null;
            if (sShareCache.containsKey(modelClass)) {
                shareVM = (ShareViewModel) sShareCache.get(modelClass);
            } else {
                shareVM = (ShareViewModel) super.create(modelClass);
                if (shareVM != null) {
                    shareVM.setOnShareCleared(new Runnable() {
                        @Override
                        public void run() {
                            sShareCache.remove(modelClass);
                        }
                    });
                    sShareCache.put(modelClass, shareVM);
                }
            }
            if (shareVM != null) {
                shareVM.incRefCount();
            }
            return (T) shareVM;
        }
        return super.create(modelClass);
    }
}
