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

import com.virtualrainbowllc.demotracker.data.DemoRepository;
import com.virtualrainbowllc.demotracker.data.TripRepository;
import com.virtualrainbowllc.demotracker.tools.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
class ViewDemoPresenterModule {

    private final ViewDemoContract.ViewDemoView viewDemoView;

    ViewDemoPresenterModule(ViewDemoContract.ViewDemoView viewDemoView) {
        this.viewDemoView = viewDemoView;
    }

    @Provides
    @ActivityScope
    ViewDemoContract.ViewDemoView provideView() {
        return viewDemoView;
    }

    @Provides
    @ActivityScope
    ViewDemoContract.ViewDemoPresenter providePresenter(ViewDemoContract.ViewDemoView viewDemoView,
                                                        DemoRepository demoRepository,
                                                        TripRepository tripRepository) {
        return new ViewDemoPresenterImpl(viewDemoView, demoRepository, tripRepository);
    }
}