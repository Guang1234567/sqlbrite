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
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.example.sqlbrite.todo.di.UserScope;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

@UserScope
public class UserScopeViewModelFactory implements ViewModelProvider.Factory {

    private final Map<Class<? extends ViewModel>, Provider<ViewModel>> mCreators;

    private final Map<Class<? extends ShareViewModel>, ShareViewModel> mShareCache;

    @Inject
    public UserScopeViewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> creators) {
        mCreators = creators;
        mShareCache = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        Provider<? extends ViewModel> creator = mCreators.get(modelClass);
        if (creator == null) {
            for (Map.Entry<Class<? extends ViewModel>, Provider<ViewModel>> entry : mCreators.entrySet()) {
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
    private ViewModel provideShareViewModel(Class<? extends ShareViewModel> modelClass, Provider<? extends ShareViewModel> creator) {
        ShareViewModel shareVM;
        if (mShareCache.containsKey(modelClass)) {
            shareVM = mShareCache.get(modelClass);
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
                        mShareCache.put(modelClass, tmp);
                    }
                });
                shareVM.setOnShareCleared(new Runnable() {
                    @Override
                    public void run() {
                        mShareCache.remove(modelClass);
                    }
                });
            }
        }
        ViewModel vm = null;
        if (shareVM != null) {
            vm = new ViewModelSmartPointer(shareVM);
        }
        return vm;
    }

    public final <VM extends ViewModel> VM provide(@NonNull FragmentActivity activity,
                                                   @NonNull Class<VM> modelClass) {

        VM result = ViewModelProviders.of(activity, this).get(modelClass);
        if (ShareViewModel.class.isAssignableFrom(modelClass)) {
            ViewModelSmartPointer<ShareViewModel> sp = (ViewModelSmartPointer<ShareViewModel>) result;
            result = (VM) sp.ref();
        }
        return result;
    }

    /**
     * Smart pointer fro {@link ShareViewModel}
     *
     * @param <T>
     */
    private final static class ViewModelSmartPointer<T extends ShareViewModel> extends ViewModel {

        private T mRef;

        public ViewModelSmartPointer(T ref) {
            if (ref == null) {
                throw new RuntimeException("requireNonNull !");
            }
            ref.incRefCount();
            mRef = ref;
        }

        @Override
        protected final void onCleared() {
            super.onCleared();
            mRef.decRefCount();
        }

        @NonNull
        public final T ref() {
            return mRef;
        }
    }
}
