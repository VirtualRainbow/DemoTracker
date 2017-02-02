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

package com.virtualrainbowllc.demotracker.data.model;

import java.util.Date;

public class Trip {

    private String tripId;
    private String vehicleId;

    private Date dateIn, dateOut;
    private String totalBusinessMiles;

    public Trip() {

    }

    public Trip(final String tripId, final String vehicleId, final Date dateOut, final Date dateIn,
                final String totalBusinessMiles) {
        this.tripId = tripId;
        this.vehicleId = vehicleId;
        this.dateOut = dateOut;
        this.dateIn = dateIn;
        this.totalBusinessMiles = totalBusinessMiles;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(final String tripId) {
        this.tripId = tripId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(final String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Date getDateOut() {
        return dateOut;
    }

    public void setDateOut(final Date dateOut) {
        this.dateOut = dateOut;
    }

    public Date getDateIn() {
        return dateIn;
    }

    public void setDateIn(final Date dateIn) {
        this.dateIn = dateIn;
    }

    public String getTotalBusinessMiles() {
        return totalBusinessMiles;
    }

    public void setTotalBusinessMiles(final String totalBusinessMiles) {
        this.totalBusinessMiles = totalBusinessMiles;
    }
}
