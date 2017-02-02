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

package com.virtualrainbowllc.demotracker.ui.common;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.virtualrainbowllc.demotracker.AppComponent;
import com.virtualrainbowllc.demotracker.DemoTrackerApplication;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

public abstract class BaseActivity extends AppCompatActivity {

    private final CompositeSubscription compositeSubscription = new CompositeSubscription();
    private boolean orientationChange = false;

    @CallSuper
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupComponent(DemoTrackerApplication.get(this).component());
        if (orientationChange) {
            orientationChange = false;
        }
        onViewCreated(savedInstanceState);
    }

    protected void onViewCreated(@Nullable final Bundle savedInstanceState) {

    }

    @CallSuper
    @Override
    protected void onSaveInstanceState(@Nullable Bundle outState) {
        super.onSaveInstanceState(outState);
        orientationChange = true;
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        if (!orientationChange) {
            compositeSubscription.unsubscribe();
            closeRealm();
        }
        super.onDestroy();
    }

    protected final void addToAutoUnsubscribe(@NonNull final Subscription subscription) {
        compositeSubscription.add(subscription);
    }

    protected final void showSnackbar(@Nullable View view, @NonNull final String message) {
        if (view != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
        }
    }

    protected final void showMaterialTapTarget(@NonNull Activity activity, @NonNull View view,
                                               @StringRes int primaryText, @StringRes int secondaryText) {
        new MaterialTapTargetPrompt.Builder(activity)
                .setTarget(view)
                .setPrimaryText(getString(primaryText))
                .setSecondaryText(getString(secondaryText))
                .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener() {
                    @Override
                    public void onHidePrompt(MotionEvent event, boolean tappedTarget) {
                        editTapTargetFlags();
                    }

                    @Override
                    public void onHidePromptComplete() {

                    }
                })
                .show();
    }

    protected abstract void setupComponent(AppComponent component);

    protected abstract void closeRealm();

    protected abstract void editTapTargetFlags();
}
