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

import com.virtualrainbowllc.demotracker.data.mapper.DemoMapper;
import com.virtualrainbowllc.demotracker.data.model.Demo;
import com.virtualrainbowllc.demotracker.data.model.realmmodels.DemoRealm;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;

public class DemoRepository implements Repository.DemoRepository {

    private final Realm realm;

    private final DemoMapper mapper;

    public DemoRepository(final Realm realm) {
        this.realm = realm;
        mapper = new DemoMapper();
    }

    @Override
    public Realm getRealm() {
        return realm;
    }

    @Override
    public Observable<List<Demo>> getAll() {
        return realm.where(DemoRealm.class)
                .findAllAsync()
                .asObservable()
                .filter(RealmResults::isLoaded)
                .filter(RealmResults::isValid)
                .map(mapper::transform);
    }

    @Override
    public void add(Demo demo, ServiceCallback callback) {
        realm.executeTransactionAsync(realm -> {
            DemoRealm demoRealm = realm.createObject(DemoRealm.class);
            demoRealm.setId(demo.getId());
            demoRealm.setCheckedIn(demo.isCheckedIn());
            demoRealm.setYear(demo.getYear());
            demoRealm.setMake(demo.getMake());
            demoRealm.setModel(demo.getModel());
            demoRealm.setPrice(demo.getPrice());
            demoRealm.setDateOut(demo.getDateOut());
            demoRealm.setMileageOut(demo.getMileageOut());

            if (demo.isCheckedIn()) {
                demoRealm.setDateIn(demo.getDateIn());
                demoRealm.setMileageIn(demo.getMileageIn());
            }
        }, callback::onSuccess, callback::onError);
    }

    @Override
    public void update(Demo demo, ServiceCallback callback) {
        realm.executeTransactionAsync(realm -> {
            DemoRealm demoRealm = realm.where(DemoRealm.class)
                    .equalTo("id", demo.getId()).findFirst();
            if (demoRealm != null) {
                demoRealm.setCheckedIn(demo.isCheckedIn());
                demoRealm.setYear(demo.getYear());
                demoRealm.setMake(demo.getMake());
                demoRealm.setModel(demo.getModel());
                demoRealm.setPrice(demo.getPrice());
                demoRealm.setDateOut(demo.getDateOut());
                demoRealm.setMileageOut(demo.getMileageOut());

                if (demo.isCheckedIn()) {
                    demoRealm.setDateIn(demo.getDateOut());
                    demoRealm.setMileageIn(demo.getMileageIn());
                }
            }
        }, callback::onSuccess, callback::onError);
    }

    @Override
    public void remove(Demo demo, ServiceCallback callback) {
        realm.executeTransactionAsync(realm -> {
            DemoRealm demoRealm = realm.where(DemoRealm.class)
                    .equalTo("id", demo.getId()).findFirst();
            if (demoRealm != null) {
                demoRealm.deleteFromRealm();
            }
        }, callback::onSuccess, callback::onError);
    }

    @Override
    public Observable<Demo> query(String field, String criteria) {
        return realm.where(DemoRealm.class).equalTo(field, criteria)
                .findFirstAsync()
                .<DemoRealm>asObservable()
                .filter(demo -> demo.isLoaded())
                .filter(demo -> demo.isValid())
                .map(mapper::transform);
    }

    @Override
    public void closeRealm() {
        realm.close();
    }
}
