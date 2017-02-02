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

import com.virtualrainbowllc.demotracker.data.mapper.TripMapper;
import com.virtualrainbowllc.demotracker.data.model.Trip;
import com.virtualrainbowllc.demotracker.data.model.realmmodels.TripRealm;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;

public class TripRepository implements Repository.TripRepository {

    private final Realm realm;

    private final TripMapper mapper;

    public TripRepository(final Realm realm) {
        this.realm = realm;
        mapper = new TripMapper();
    }

    @Override
    public Realm getRealm() {
        return realm;
    }

    @Override
    public void add(Trip trip, ServiceCallback callback) {
        realm.executeTransactionAsync(realm -> {
            TripRealm newTrip = realm.createObject(TripRealm.class);
            newTrip.setTripId(trip.getTripId());
            newTrip.setVehicleId(trip.getVehicleId());
            newTrip.setDateOut(trip.getDateOut());
            newTrip.setDateIn(trip.getDateIn());
            newTrip.setTotalBusinessMiles(trip.getTotalBusinessMiles());
        }, callback::onSuccess, callback::onError);
    }

    @Override
    public void update(Trip trip, ServiceCallback callback) {
        realm.executeTransactionAsync(realm -> {
            TripRealm tripRealm = realm.where(TripRealm.class)
                    .equalTo("tripId", trip.getTripId()).findFirst();
            if (tripRealm != null) {
                tripRealm.setDateOut(trip.getDateOut());
                tripRealm.setDateIn(trip.getDateIn());
                tripRealm.setTotalBusinessMiles(trip.getTotalBusinessMiles());
            }
        }, callback::onSuccess, callback::onError);
    }

    @Override
    public void remove(Trip trip, ServiceCallback callback) {
        realm.executeTransactionAsync(realm -> {
            TripRealm tripRealm = realm.where(TripRealm.class)
                    .equalTo("tripId", trip.getTripId()).findFirst();
            if (tripRealm != null) {
                tripRealm.deleteFromRealm();
            }
        }, callback::onSuccess, callback::onError);
    }

    @Override
    public void remove(List<Trip> trips, ServiceCallback callback) {
        realm.executeTransactionAsync(realm -> {
            for (int i = 0; i < trips.size(); i++) {
                TripRealm tripRealm = realm.where(TripRealm.class)
                        .equalTo("tripId", trips.get(i).getTripId()).findFirst();
                if (tripRealm != null) {
                    tripRealm.deleteFromRealm();
                }
            }
        }, callback::onSuccess, callback::onError);
    }
    public Observable<List<Trip>> query(String field, String criteria) {
        return realm.where(TripRealm.class).equalTo(field, criteria)
                .findAllAsync()
                .asObservable()
                .filter(RealmResults::isLoaded)
                .filter(RealmResults::isValid)
                .map(mapper::transform);
    }

    @Override
    public void closeRealm() {
        realm.close();
    }
}
