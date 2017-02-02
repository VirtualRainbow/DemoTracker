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

import com.virtualrainbowllc.demotracker.data.model.Demo;
import com.virtualrainbowllc.demotracker.data.model.Trip;
import com.virtualrainbowllc.demotracker.ui.common.BasePresenter;
import com.virtualrainbowllc.demotracker.ui.common.BaseView;

import java.util.List;

interface ViewDemoContract {

    interface ViewDemoPresenter extends BasePresenter {

        void firstRun();

        void loadData(String id);

        void checkInButtonClicked();

        void checkInDemo(Demo demo);

        void editDemoClicked();

        void editDemo(Demo demo);

        void deleteDemoClicked();

        void deleteDemo(Demo demo);

        void addTripClicked();

        void saveTrip(Trip trip);

        void tripItemClicked(Trip trip);

        void editTripItemClicked(Trip trip);

        void editTripItem(Trip trip);

        void deleteTripItemClicked(Trip trip);

        void deleteTrip(Trip trip);
    }

    interface ViewDemoView extends BaseView<ViewDemoPresenter> {

        void showFirstRun();

        void setupBaseViews();

        void setupDemoViews(Demo demo);

        void setTripItems(List<Trip> trips);

        void startCheckInDialog();

        void startEditDialog();

        void startDeleteDialog();

        void startAddBusinessDialog();

        void startEditOrDeleteBusinessDialog(Trip trip);

        void startEditBusinessDialog(Trip trip);

        void startDeleteBusinessDialog(Trip trip);

        void saveSuccess();

        void saveError();
    }
}
