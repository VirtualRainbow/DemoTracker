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

package com.virtualrainbowllc.demotracker.ui.log;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.virtualrainbowllc.demotracker.data.DemoRepository;
import com.virtualrainbowllc.demotracker.data.ServiceCallback;
import com.virtualrainbowllc.demotracker.data.model.Demo;
import com.virtualrainbowllc.demotracker.ui.common.BasePresenterImpl;

import timber.log.Timber;

class LogPresenterImpl extends BasePresenterImpl implements
        LogContract.LogPresenter, ServiceCallback {

    @NonNull
    private final DemoRepository demoRepository;
    @Nullable
    private LogContract.LogView logView;

    LogPresenterImpl(@Nullable LogContract.LogView logView,
                     @NonNull DemoRepository demoRepository) {
        this.logView = logView;
        this.demoRepository = demoRepository;
    }

    @Override
    public void onViewAttached() {
        super.onViewAttached();
        if (logView != null) {
            logView.setupBaseViews();
        }
    }

    @Override
    public void onViewDetached() {
        super.onViewDetached();
        if (logView == null) {
            throw new IllegalStateException("View is already detached");
        }
        logView = null;
    }

    @Override
    public void closeRealm() {
        demoRepository.closeRealm();
    }

    @Override
    public void firstRun() {
        if (logView != null) {
            logView.showFirstRun();
        }
    }

    @Override
    public void loadData() {
        if (logView != null) {
            addToAutoUnsubscribe(demoRepository.getAll()
                    .doOnNext(items -> logView.setItems(items))
                    .doOnError(throwable -> Timber.e(throwable.getMessage()))
                    .subscribe());
        }
    }

    @Override
    public void fabClicked() {
        if (logView != null) {
            logView.startCheckOutDialog();
        }
    }

    @Override
    public void itemClicked(String id) {
        if (logView != null) {
            logView.startViewDemoActivity(id);
        }
    }

    @Override
    public void checkOutDemo(Demo demo) {
        if (logView != null) {
            demoRepository.add(demo, this);
        }
    }

    @Override
    public void onSuccess() {
        if (logView != null) {
            logView.checkOutDemoSuccess();
            loadData();
        }
    }

    @Override
    public void onError(Throwable e) {
        if (logView != null) {
            logView.checkOutDemoError();
            Timber.e(e.getMessage());
        }
    }
}
