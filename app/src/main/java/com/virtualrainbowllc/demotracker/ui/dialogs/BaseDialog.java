/*
 * Copyright 2016 Virtual Rainbow, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.virtualrainbowllc.demotracker.ui.dialogs;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialog;
import android.view.WindowManager;

import com.virtualrainbowllc.demotracker.R;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

abstract class BaseDialog extends AppCompatDialog {

    private final CompositeSubscription compositeSubscription = new CompositeSubscription();

    BaseDialog(Context context) {
        super(context, R.style.AppTheme_Dialog);
    }

    @CallSuper
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ViewDataBinding binding = getLayoutBinding();
        setBinding(binding);
        setContentView(binding.getRoot());

        getWindow().setAttributes(lp);
    }

    protected abstract ViewDataBinding getLayoutBinding();

    protected abstract void setBinding(@NonNull ViewDataBinding binding);

    @CallSuper
    @Override
    protected void onStop() {
        compositeSubscription.unsubscribe();
        super.onStop();
    }

    protected final void addToAutoUnsubscribe(@NonNull final Subscription subscription) {
        compositeSubscription.add(subscription);
    }
}
