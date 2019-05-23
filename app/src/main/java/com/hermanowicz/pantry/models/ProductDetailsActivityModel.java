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

public class ProductDetailsActivityModel {

    private ProductEntity product;
    private String hashCode;

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
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