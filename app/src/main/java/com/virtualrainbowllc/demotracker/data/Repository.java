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

package com.virtualrainbowllc.demotracker.data;

import com.virtualrainbowllc.demotracker.data.model.Demo;
import com.virtualrainbowllc.demotracker.data.model.Trip;

import java.util.List;

import io.realm.Realm;
import rx.Observable;

interface Repository {

    interface DemoRepository {
        Realm getRealm();

        Observable<List<Demo>> getAll();

        void add(Demo demo, ServiceCallback callback);

        void update(Demo demo, ServiceCallback callback);

        void remove(Demo demo, ServiceCallback callback);

        Observable<Demo> query(String field, String criteria);

        void closeRealm();
    }

    interface TripRepository {
        Realm getRealm();

        void add(Trip trip, ServiceCallback callback);

        void update(Trip trip, ServiceCallback callback);

        void remove(Trip trip, ServiceCallback callback);

        void remove(List<Trip> trips, ServiceCallback callback);

        Observable<List<Trip>> query(String field, String criteria);

        void closeRealm();
    }
}
