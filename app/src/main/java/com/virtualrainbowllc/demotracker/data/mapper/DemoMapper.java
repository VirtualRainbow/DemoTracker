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

import com.virtualrainbowllc.demotracker.data.model.Demo;
import com.virtualrainbowllc.demotracker.data.model.realmmodels.DemoRealm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DemoMapper {

    public DemoMapper() {
    }

    public Demo transform(DemoRealm demoRealm) {
        Demo demo = null;
        if (demoRealm != null) {
            demo = new Demo();
            demo.setId(demoRealm.getId());
            demo.setCheckedIn(demoRealm.isCheckedIn());
            demo.setYear(demoRealm.getYear());
            demo.setMake(demoRealm.getMake());
            demo.setModel(demoRealm.getModel());
            demo.setPrice(demoRealm.getPrice());
            demo.setDateOut(demoRealm.getDateOut());
            demo.setMileageOut(demoRealm.getMileageOut());

            if (demoRealm.isCheckedIn()) {
                demo.setDateIn(demoRealm.getDateIn());
                demo.setMileageIn(demoRealm.getMileageIn());
            }
        }
        return demo;
    }

    public List<Demo> transform(Collection<DemoRealm> demoRealmCollection) {
        final List<Demo> demoList = new ArrayList<>(20);
        for (DemoRealm demoRealm : demoRealmCollection) {
            final Demo demo = transform(demoRealm);
            if (demo != null) {
                demoList.add(demo);
            }
        }
        return demoList;
    }
}