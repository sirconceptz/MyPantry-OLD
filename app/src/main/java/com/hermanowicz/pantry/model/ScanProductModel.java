/*
 * Copyright (c) 2019-2021
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

package com.hermanowicz.pantry.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hermanowicz.pantry.db.product.Product;
import com.hermanowicz.pantry.db.product.ProductDb;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ScanProductModel {

    private final ProductDb productDb;
    private String scanResult;
    private List<Product> productList;
    private String databaseMode;
    private List<Product> productListToAddBarcode;

    public ScanProductModel(@NonNull ProductDb productDb){
        this.productDb = productDb;
    }

    private boolean isBarcodeScan = false;

    public List<Product> getProductListWithBarcode(String barcode) {
        List<Product> productListWithBarcode = new ArrayList<>();
        for(Product product : productList){
            if(product.getBarcode().equals(barcode))
                productListWithBarcode.add(product);
        }
        return productListWithBarcode;
    }

    public void setIsBarcodeScan(boolean barcodeScan){
        isBarcodeScan = barcodeScan;
    }

    public boolean isBarcodeScan(){
        return isBarcodeScan;
    }

    public List<Integer> decodeScanResult(@NonNull String scanResult) {
        List<Integer> decodedQRCodeAsList = null;
        try {
            decodedQRCodeAsList = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(scanResult);
            decodedQRCodeAsList.add(jsonObject.getInt("product_id"));
            decodedQRCodeAsList.add(jsonObject.getInt("hash_code"));
        } catch (JSONException e) {
            Log.e("Decode scan result", e.toString());
        }
        return decodedQRCodeAsList;
    }

    public void setScanResult(String scanResult) {
        this.scanResult = scanResult;
    }

    public String getScanResult(){
        return scanResult;
    }

    public void setDatabaseMode(String databaseMode) {
        this.databaseMode = databaseMode;
    }

    public String getDatabaseMode() {
        return databaseMode;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public void setOfflineAllProductList() {
        this.productList = productDb.productsDao().getAllProductsList();
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductListToAddBarcode(List<Product> productListToAddBarcode) {
        this.productListToAddBarcode = productListToAddBarcode;
    }

    public List<Product> getProductListToAddBarcode() {
        return productListToAddBarcode;
    }

    public void setBarcodeToProductList(String barcode) {
        for(Product product : productListToAddBarcode) {
            product.setBarcode(barcode);
        }
    }

    public void updateProductListWithBarcode() {
        if(databaseMode.equals("local")) {
            for (Product product : productListToAddBarcode)
                productDb.productsDao().updateProduct(product);
        }
        else {
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference ref = db.getReference().child("products/" + FirebaseAuth.getInstance().getUid());
            for (Product product : productListToAddBarcode) {
                ref.child(String.valueOf(product.getId())).setValue(product);
            }
        }
    }
}