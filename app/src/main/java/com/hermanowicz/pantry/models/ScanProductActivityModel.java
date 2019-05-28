/*
 * Copyright (c) 2019
 * Mateusz Hermanowicz - All rights reserved.
 * My Pantry
 * https://www.mypantry.eu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hermanowicz.pantry.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ScanProductActivityModel {

    public List<Integer> decodeScanResult(String scanResult) {
        List<Integer> decodedQRCodeAsList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(scanResult);
            decodedQRCodeAsList.add(jsonObject.getInt("product_id"));
            decodedQRCodeAsList.add(jsonObject.getInt("hash_code"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return decodedQRCodeAsList;
    }
}