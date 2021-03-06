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

import com.virtualrainbowllc.demotracker.AppComponent;
import com.virtualrainbowllc.demotracker.data.DataModule;
import com.virtualrainbowllc.demotracker.tools.ActivityScope;

import dagger.Component;

@ActivityScope
@Component(
        dependencies = AppComponent.class,
        modules = {ViewDemoPresenterModule.class, DataModule.class}
)
interface ViewDemoComponent {
    void inject(ViewDemoActivity activity);

    ViewDemoContract.ViewDemoPresenter getEditVehiclePresenter();

    ViewDemoContract.ViewDemoView getEditVehicleView();
}
