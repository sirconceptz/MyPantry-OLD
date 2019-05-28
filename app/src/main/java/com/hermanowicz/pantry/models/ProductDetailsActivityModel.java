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

import com.hermanowicz.pantry.db.Product;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductDetailsActivityModel {

    private Product product;
    private String hashCode;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public boolean compareHashCode() {
        boolean result;
        result = hashCode.equals(product.getHashCode());
        return result;
    }

    public ArrayList<String> getTextToQRCodeList() {
        ArrayList<String> textToQRCodeList = new ArrayList<>();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("product_id", product.getId());
            jsonObject.put("hash_code", product.getHashCode());
            textToQRCodeList.add(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return textToQRCodeList;
    }

    public ArrayList<String> getNamesOfProductsList() {
        ArrayList<String> namesOfProductsList = new ArrayList<>();

        String productName = product.getName();
        if (productName.length() > 15) {
            namesOfProductsList.add(productName.substring(0, 14) + ".");
        } else {
            namesOfProductsList.add(productName);
        }

        return namesOfProductsList;
    }

    public ArrayList<String> getExpirationDatesList() {
        ArrayList<String> expirationDatesList = new ArrayList<>();

        expirationDatesList.add(product.getExpirationDate());

        return expirationDatesList;
    }
}