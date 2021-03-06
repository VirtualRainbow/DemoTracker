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

import com.virtualrainbowllc.demotracker.data.model.Demo;
import com.virtualrainbowllc.demotracker.ui.common.BasePresenter;
import com.virtualrainbowllc.demotracker.ui.common.BaseView;

import java.util.List;

interface LogContract {

    interface LogPresenter extends BasePresenter {

        void firstRun();

        void loadData();

        void fabClicked();

        void itemClicked(String id);

        void checkOutDemo(Demo demo);
    }

    interface LogView extends BaseView<LogPresenter> {

        void showFirstRun();

        void setupBaseViews();

        void setItems(List<Demo> demo);

        void startCheckOutDialog();

        void startViewDemoActivity(String id);

        void checkOutDemoSuccess();

        void checkOutDemoError();
    }
}
