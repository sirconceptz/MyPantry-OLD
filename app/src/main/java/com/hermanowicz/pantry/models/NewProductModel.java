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

package com.hermanowicz.pantry.models;

import android.content.res.Resources;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.db.Product;

import java.util.ArrayList;
import java.util.List;

public class NewProductModel {

    private final Resources resources;
    private String expirationDate = "-";
    private String productionDate = "-";
    private String taste;
    private int quantity;
    private final DatabaseOperations databaseOperations;

    public NewProductModel(@NonNull Resources resources, @NonNull DatabaseOperations databaseOperations) {
        this.resources = resources;
        this.databaseOperations = databaseOperations;
    }

    public List<Product> createProductsList(@NonNull Product product) {
        List<Product> productsList = new ArrayList<>();
        for (int counter = 1; counter <= quantity; counter++) {
            product.setTaste(taste);
            product.setExpirationDate(expirationDate);
            product.setProductionDate(productionDate);
            product.setHashCode(String.valueOf(product.hashCode()));
            productsList.add(product);
        }
        return productsList;
    }

    public int getIdOfLastProductInDb() {
        return databaseOperations.getIdOfLastProductInDb();
    }

    public void addProducts(@NonNull List<Product> productList){
        databaseOperations.addProducts(productList);
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
        return databaseOperations.getOwnCategoriesArray();
    }

    public String[] getStorageLocationsArray() {
        return databaseOperations.getStorageLocationsArray();
    }
}