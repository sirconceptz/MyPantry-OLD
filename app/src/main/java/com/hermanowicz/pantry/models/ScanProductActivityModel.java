/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

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