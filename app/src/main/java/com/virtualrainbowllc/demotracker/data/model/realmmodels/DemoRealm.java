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

package com.virtualrainbowllc.demotracker.data.model.realmmodels;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by James on 1/27/2017.
 */

public class DemoRealm extends RealmObject {

    private String id;

    private String year;
    private String price;
    private String mileageOut;
    private String mileageIn;
    private Date dateOut;
    private Date dateIn;
    private String make;
    private String model;
    private boolean checkedIn;

    public DemoRealm() {

    }

    public DemoRealm(final String id, final boolean checkedIn, final String year, final String make,
                     final String model, final String price, final Date dateOut, final String mileageOut) {
        this.id = id;
        this.checkedIn = checkedIn;
        this.year = year;
        this.make = make;
        this.model = model;
        this.price = price;
        this.dateOut = dateOut;
        this.mileageOut = mileageOut;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public boolean isCheckedIn() {
        return checkedIn;
    }

    public void setCheckedIn(final boolean checkedIn) {
        this.checkedIn = checkedIn;
    }

    public String getYear() {
        return year;
    }

    public void setYear(final String year) {
        this.year = year;
    }

    public String getMake() {
        return make;
    }

    public void setMake(final String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(final String model) {
        this.model = model;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(final String price) {
        this.price = price;
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

    public String getMileageOut() {
        return mileageOut;
    }

    public void setMileageOut(final String mileageOut) {
        this.mileageOut = mileageOut;
    }

    public String getMileageIn() {
        return mileageIn;
    }

    public void setMileageIn(final String mileageIn) {
        this.mileageIn = mileageIn;
    }
}

