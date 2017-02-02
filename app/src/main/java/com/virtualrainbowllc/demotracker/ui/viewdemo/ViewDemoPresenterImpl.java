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

package com.virtualrainbowllc.demotracker.ui.viewdemo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.virtualrainbowllc.demotracker.data.DemoRepository;
import com.virtualrainbowllc.demotracker.data.ServiceCallback;
import com.virtualrainbowllc.demotracker.data.TripRepository;
import com.virtualrainbowllc.demotracker.data.model.Demo;
import com.virtualrainbowllc.demotracker.data.model.Trip;
import com.virtualrainbowllc.demotracker.ui.common.BasePresenterImpl;

import timber.log.Timber;

class ViewDemoPresenterImpl extends BasePresenterImpl implements
        ViewDemoContract.ViewDemoPresenter, ServiceCallback {

    @NonNull
    private final DemoRepository demoRepository;
    @NonNull
    private final TripRepository tripRepository;
    @Nullable
    private ViewDemoContract.ViewDemoView viewDemoView;

    ViewDemoPresenterImpl(@Nullable ViewDemoContract.ViewDemoView viewDemoView,
                          @NonNull DemoRepository demoRepository,
                          @NonNull TripRepository tripRepository) {
        this.viewDemoView = viewDemoView;
        this.demoRepository = demoRepository;
        this.tripRepository = tripRepository;
    }

    @Override
    public void onViewAttached() {
        super.onViewAttached();
        if (viewDemoView != null) {
            viewDemoView.setupBaseViews();
        }
    }

    @Override
    public void onViewDetached() {
        super.onViewDetached();
        if (viewDemoView == null) {
            throw new IllegalStateException("View is already detached");
        }
        viewDemoView = null;
    }

    @Override
    public void closeRealm() {
        demoRepository.closeRealm();
        tripRepository.closeRealm();
    }

    @Override
    public void firstRun() {
        if (viewDemoView != null) {
            viewDemoView.showFirstRun();
        }
    }

    @Override
    public void loadData(String id) {
        if (viewDemoView != null) {
            addToAutoUnsubscribe(demoRepository.query("id", id)
                    .doOnNext(demo -> viewDemoView.setupDemoViews(demo))
                    .doOnError(throwable -> Timber.e(throwable.getMessage()))
                    .subscribe());
            addToAutoUnsubscribe(tripRepository.query("vehicleId", id)
                    .doOnNext(trips -> viewDemoView.setTripItems(trips))
                    .doOnError(throwable -> Timber.e(throwable.getMessage()))
                    .subscribe());
        }
    }

    @Override
    public void checkInButtonClicked() {
        if (viewDemoView != null) {
            viewDemoView.startCheckInDialog();
        }
    }

    @Override
    public void checkInDemo(Demo demo) {
        if (viewDemoView != null) {
            demoRepository.update(demo, this);
        }
    }

    @Override
    public void editDemoClicked() {
        if (viewDemoView != null) {
            viewDemoView.startEditDialog();
        }
    }

    @Override
    public void editDemo(Demo demo) {
        if (viewDemoView != null) {
            demoRepository.update(demo, this);
        }
    }

    @Override
    public void deleteDemoClicked() {
        if (viewDemoView != null) {
            viewDemoView.startDeleteDialog();
        }
    }

    @Override
    public void deleteDemo(Demo demo) {
        if (viewDemoView != null) {
            demoRepository.remove(demo, this);
            tripRepository.query("vehicleId", demo.getId())
                    .subscribe(trips -> tripRepository.remove(trips, this));
        }
    }

    @Override
    public void addTripClicked() {
        if (viewDemoView != null) {
            viewDemoView.startAddBusinessDialog();
        }
    }

    @Override
    public void saveTrip(Trip trip) {
        if (viewDemoView != null) {
            tripRepository.add(trip, this);
        }
    }

    @Override
    public void tripItemClicked(Trip trip) {
        if (viewDemoView != null) {
            viewDemoView.startEditOrDeleteBusinessDialog(trip);
        }
    }

    @Override
    public void editTripItemClicked(Trip trip) {
        if (viewDemoView != null) {
            viewDemoView.startEditBusinessDialog(trip);
        }
    }

    @Override
    public void editTripItem(Trip trip) {
        if (viewDemoView != null) {
            tripRepository.update(trip, this);
        }
    }

    @Override
    public void deleteTripItemClicked(Trip trip) {
        if (viewDemoView != null) {
            viewDemoView.startDeleteBusinessDialog(trip);
        }
    }

    @Override
    public void deleteTrip(Trip trip) {
        if (viewDemoView != null) {
            tripRepository.remove(trip, this);
        }
    }

    @Override
    public void onSuccess() {
        if (viewDemoView != null) {
            viewDemoView.saveSuccess();
        }
    }

    @Override
    public void onError(Throwable e) {
        if (viewDemoView != null) {
            viewDemoView.saveError();
            Timber.e(e.getMessage());
        }
    }
}
