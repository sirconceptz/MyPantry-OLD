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

import android.content.Context;
import android.content.res.Resources;
import android.widget.RadioButton;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.db.category.CategoryDb;
import com.hermanowicz.pantry.db.product.Product;
import com.hermanowicz.pantry.db.product.ProductDb;
import com.hermanowicz.pantry.db.storagelocation.StorageLocationDb;

import java.util.ArrayList;
import java.util.List;

@Keep
public class NewProductModel {

    private final Resources resources;
    private final ProductDb productDb;
    private final CategoryDb categoryDb;
    private final StorageLocationDb storageLocationDb;
    private String expirationDate = "-";
    private String productionDate = "-";
    private String taste;
    private int quantity;
    private String barcode;
    private List<Product> productList = new ArrayList<>();
    private String databaseMode;
    private List<Product> allProductList;

    public NewProductModel(@NonNull Context context) {
        this.resources = context.getResources();
        this.productDb = ProductDb.getInstance(context);
        this.categoryDb = CategoryDb.getInstance(context);
        this.storageLocationDb = StorageLocationDb.getInstance(context);
    }

    public void createProductsList(@NonNull Product product) {
        for (int counter = 1; counter <= quantity; counter++) {
            product.setTaste(taste);
            product.setExpirationDate(expirationDate);
            product.setProductionDate(productionDate);
            product.setPhotoName("");
            product.setPhotoDescription("");
            if(barcode == null)
                product.setBarcode("");
            else
                product.setBarcode(barcode);
            product.setHashCode(String.valueOf(product.hashCode()));
            if(product.getStorageLocation().equals("null"))
                product.setStorageLocation("");
            if(product.getProductFeatures().equals(resources.getString(R.string.Product_choose)))
                product.setProductFeatures("");
            productList.add(product);
        }
    }

    public void setDatabaseMode(String databaseMode){
        this.databaseMode = databaseMode;
    }

    public void addProducts(){
        if(databaseMode.equals("online"))
            addProductsToOnlineDatabase();
        else
            addProductsToOfflineDatabase();
    }

    private void addProductsToOfflineDatabase(){
        productDb.productsDao().addProducts(productList);
        List<Product> allProductsList = productDb.productsDao().getAllProductsList();
        productList = Product.getSimilarProductsList(productList.get(0), allProductsList);
    }

    private void addProductsToOnlineDatabase() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference().child("products/" + FirebaseAuth.getInstance().getUid());
        int counter = 0;
        int onlineProductListSize = 0;
        if (allProductList != null)
            onlineProductListSize = allProductList.size();
        for (Product product : productList) {
            counter++;
            int nextId = counter;
            if (onlineProductListSize > 0)
                nextId = nextId + allProductList.get(onlineProductListSize - 1).getId();
            product.setId(nextId);
            ref.child(String.valueOf(nextId)).setValue(product);
        }
    }

    public String[] getNamesProductList(){
        List<GroupProducts> groupProducts = getGroupProductList();
        String[] namesProductList = new String[groupProducts.size()];
        for(int i = 0; i < groupProducts.size(); i++){
            namesProductList[i] = groupProducts.get(i).getProduct().getName();
        }
        return namesProductList;
    }

    public List<GroupProducts> getGroupProductList() {
        return GroupProducts.getGroupProducts(productList);
    }

    public void setExpirationDate(int year, int month, int day) {
        month = month + 1;
        this.expirationDate = year + "-" + month + "-" + day;
    }

    public void setProductionDate(int year, int month, int day) {
        month = month + 1;
        this.productionDate = year + "-" + month + "-" + day;
    }

    public void setTaste(@Nullable RadioButton selectedTasteButton){
        if(selectedTasteButton == null)
            this.taste = "";
        else
            this.taste = selectedTasteButton.getText().toString();
    }

    public void parseQuantityProducts(@NonNull String quantity) {
        try {
            this.quantity = Integer.parseInt(quantity);
        } catch (NumberFormatException e) {
            this.quantity = 1;
        }
        if (this.quantity < 1)
            this.quantity = 1;
        if (this.quantity > 1000)
            this.quantity = 1000;
    }

    public int[] getExpirationDateArray() {
        String[] stringArray = expirationDate.split("-");
        int[] expirationDateArray = new int[stringArray.length];
        for(int counter = 0; stringArray.length > counter; counter++){
            expirationDateArray[counter] = Integer.parseInt(stringArray[counter]);
        }
        return expirationDateArray;
    }

    public int[] getProductionDateArray() {
        String[] stringArray = productionDate.split("-");
        int[] productionDateArray = new int[stringArray.length];
        for (int counter = 0; stringArray.length > counter; counter++) {
            productionDateArray[counter] = Integer.parseInt(stringArray[counter]);
        }
        return productionDateArray;
    }

    public String getOnProductAddStatement() {
        String statementToShow;
        if (quantity < 2)
            statementToShow = resources.getString(R.string.NewProductActivity_product_added_successful);
        else
            statementToShow = resources.getString(R.string.NewProductActivity_products_added_successful);
        return statementToShow;
    }

    public boolean isProductNameNotValid(@NonNull Product product) {
        boolean correctProductName = false;
        if (product.getName().length() > 0)
            correctProductName = true;
        return !correctProductName;
    }

    public boolean isTypeOfProductValid(@NonNull Product product) {
        String[] typeOfProductsArray = resources.getStringArray(R.array.Product_type_of_product_array);
        boolean correctTypeOfProduct = false;
        if (!product.getTypeOfProduct().equals(typeOfProductsArray[0]))
            correctTypeOfProduct = true;
        return correctTypeOfProduct;
    }

    public String[] getOwnCategoriesArray() {
        return categoryDb.categoryDao().getAllCategoriesArray();
    }

    public String[] getStorageLocationsArray() {
        return storageLocationDb.storageLocationDao().getAllStorageLocationsArray();
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public String getDatabaseMode() {
        return databaseMode;
    }

    public void setAllProductList(List<Product> allProductList) {
        this.allProductList = allProductList;
    }

    public List<Product> getAllProductList() {
        return allProductList;
    }
}