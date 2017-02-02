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

import com.virtualrainbowllc.demotracker.R;

import java.util.HashMap;
import java.util.Map;

public class VehicleModels {

    private VehicleModels() {
    } // Prevents instantiation

    public static Map<String, Integer> getModels() {
        Map<String, Integer> models = new HashMap<>();
        models.put("acura", R.array.acura);
        models.put("audi", R.array.audi);
        models.put("bmw", R.array.bmw);
        models.put("buick", R.array.buick);
        models.put("cadillac", R.array.cadillac);
        models.put("chevrolet", R.array.chevrolet);
        models.put("chrysler", R.array.chrysler);
        models.put("dodge", R.array.dodge);
        models.put("fiat", R.array.fiat);
        models.put("ford", R.array.ford);
        models.put("gmc", R.array.gmc);
        models.put("honda", R.array.honda);
        models.put("hummer", R.array.hummer);
        models.put("hyundai", R.array.hyundai);
        models.put("infiniti", R.array.infiniti);
        models.put("jaguar", R.array.jaguar);
        models.put("jeep", R.array.jeep);
        models.put("kia", R.array.kia);
        models.put("landrover", R.array.landrover);
        models.put("lexus", R.array.lexus);
        models.put("lincoln", R.array.lincoln);
        models.put("mazda", R.array.mazda);
        models.put("mercedesbenz", R.array.mercedesbenz);
        models.put("mercury", R.array.mercury);
        models.put("mini", R.array.mini);
        models.put("mitsubishi", R.array.mitsubishi);
        models.put("nissan", R.array.nissan);
        models.put("pontiac", R.array.pontiac);
        models.put("porsche", R.array.porsche);
        models.put("saturn", R.array.saturn);
        models.put("scion", R.array.scion);
        models.put("smart", R.array.smart);
        models.put("subaru", R.array.subaru);
        models.put("toyota", R.array.toyota);
        models.put("volkswagen", R.array.volkswagen);
        models.put("volvo", R.array.volvo);
        return models;
    }
}
