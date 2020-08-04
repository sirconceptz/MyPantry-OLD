/*
 * Copyright (c) 2020
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

package com.hermanowicz.pantry.utils;

import android.util.Log;

import com.hermanowicz.pantry.db.Product;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>PrintQRData</h1>
 * A class used to prepare data from a list of products
 * that needed to print stickers with QR codes.
 *
 * @author  Mateusz Hermanowicz
 * @version 1.0
 * @since   1.0
 */

public class PrintQRData {
    public static ArrayList<String> getTextToQRCodeList(List<Product> productList, int idOfLastProductInDb) {
        ArrayList<String> textToQRCodeList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        int productId;
        for (int counter = productList.size() - 1; counter >= 0; counter--) {
            try {
                if (productList.get(counter).getId() == 0)
                    productId = idOfLastProductInDb - counter;
                else
                    productId = productList.get(counter).getId();
                jsonObject.put("product_id", productId);
                jsonObject.put("hash_code", productList.get(counter).getHashCode());
                textToQRCodeList.add(jsonObject.toString());
            } catch (JSONException e) {
               Log.e("json", e.toString());
            }
        }
        return textToQRCodeList;
    }

    public static ArrayList<String> getNamesOfProductsList(List<Product> productsList) {
        ArrayList<String> namesOfProductsList = new ArrayList<>();

        for (Product product : productsList) {
            String productName = product.getName();
            if (productName.length() > 15) {
                namesOfProductsList.add(productName.substring(0, 14) + ".");
            } else {
                namesOfProductsList.add(productName);
            }
        }
        return namesOfProductsList;
    }

    public static ArrayList<String> getExpirationDatesList(List<Product> productsList) {
        ArrayList<String> expirationDatesList = new ArrayList<>();

        for (Product product : productsList) {
            expirationDatesList.add(product.getExpirationDate());
        }
        return expirationDatesList;
    }
}