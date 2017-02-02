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

package com.virtualrainbowllc.demotracker.data.mapper;

import com.virtualrainbowllc.demotracker.data.model.Trip;
import com.virtualrainbowllc.demotracker.data.model.realmmodels.TripRealm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TripMapper {

    public TripMapper() {
    }

    public Trip transform(TripRealm tripRealm) {
        Trip trip = null;
        if (tripRealm != null) {
            trip = new Trip();
            trip.setTripId(tripRealm.getTripId());
            trip.setVehicleId(tripRealm.getVehicleId());
            trip.setDateOut(tripRealm.getDateOut());
            trip.setDateIn(tripRealm.getDateIn());
            trip.setTotalBusinessMiles(tripRealm.getTotalBusinessMiles());
        }
        return trip;
    }

    public List<Trip> transform(Collection<TripRealm> tripRealmCollection) {
        final List<Trip> tripList = new ArrayList<>(20);
        for (TripRealm tripRealm : tripRealmCollection) {
            final Trip trip = transform(tripRealm);
            if (trip != null) {
                tripList.add(trip);
            }
        }
        return tripList;
    }
}
