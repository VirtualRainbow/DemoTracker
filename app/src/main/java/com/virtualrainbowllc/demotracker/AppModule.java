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

package com.virtualrainbowllc.demotracker;

import android.content.SharedPreferences;

import com.virtualrainbowllc.demotracker.tools.ApplicationScope;

import dagger.Module;
import dagger.Provides;

import static android.content.Context.MODE_PRIVATE;

@ApplicationScope
@Module
public class AppModule {

    private final DemoTrackerApplication app;

    AppModule(DemoTrackerApplication app) {
        this.app = app;
    }

    @ApplicationScope
    @Provides
    DemoTrackerApplication provideApp() {
        return app;
    }

    @ApplicationScope
    @Provides
    SharedPreferences provideSharedPreferences(DemoTrackerApplication app) {
        return app.getSharedPreferences(app.getString(R.string.app_name), MODE_PRIVATE);
    }
}