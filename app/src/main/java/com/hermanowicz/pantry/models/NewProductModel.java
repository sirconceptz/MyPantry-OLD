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

import android.content.res.Resources;
import android.widget.RadioButton;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.db.Product;
import com.hermanowicz.pantry.db.ProductDb;

import java.util.ArrayList;
import java.util.List;

public class NewProductModel {

    private Resources resources;
    private ProductDb productDb;
    private String expirationDate = "-";
    private String productionDate = "-";
    private String taste;
    private int quantity;

    public NewProductModel(Resources resources, ProductDb productDb) {
        this.resources = resources;
        this.productDb = productDb;
    }

    public List<Product> buildProductsList(Product product) {
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
        return productDb.productsDao().getIdOfLastProduct();
    }

    public void addProducts(List<Product> products){
        productDb.productsDao().insertProductsToDB(products);
    }

    public void setExpirationDate(int year, int month, int day) {
        this.expirationDate = year + "-" + month + "-" + day;
    }

    public void setProductionDate(int year, int month, int day) {
        this.productionDate = year + "-" + month + "-" + day;
    }

    public void setTaste(RadioButton selectedTasteButton){
        String[] tasteArray = resources.getStringArray(R.array.ProductDetailsActivity_taste_array);
        if(selectedTasteButton == null)
            this.taste = tasteArray[0];
        else
            this.taste = selectedTasteButton.getText().toString();
    }

    public void parseQuantityProducts(String quantity) {
        try {
            this.quantity = Integer.valueOf(quantity);
        } catch (NumberFormatException e) {
            this.quantity = 1;
        }
        if (this.quantity < 1)
            this.quantity = 1;
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

    public boolean isProductNameNotValid(Product product) {
        boolean correctProductName = false;
        if (product.getName().length() > 0)
            correctProductName = true;
        return !correctProductName;
    }

    public boolean isTypeOfProductValid(Product product) {
        String[] typeOfProductsArray = resources.getStringArray(R.array.ProductDetailsActivity_type_of_product_array);
        boolean correctTypeOfProduct = false;
        if (!product.getTypeOfProduct().equals(typeOfProductsArray[0]))
            correctTypeOfProduct = true;
        return correctTypeOfProduct;
    }
}