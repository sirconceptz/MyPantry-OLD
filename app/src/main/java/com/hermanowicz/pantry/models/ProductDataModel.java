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

import java.util.List;

public class ProductDataModel {

    private ProductDb db;
    private Resources resources;
    private Product product;
    private String taste;
    private String productionDate = "-";
    private String expirationDate = "-";

    public ProductDataModel(ProductDb db, Resources resources){
        this.db = db;
        this.resources = resources;
    }

    public String formatDate(int year, int month, int day) {
        return year + "-" + month + "-" + day;
    }

    public void setProduct(int productId){
        product = getProductFromDb(productId);
        expirationDate = product.getExpirationDate();
        productionDate = product.getProductionDate();
    }

    public Product getProduct(){
        return product;
    }

    public int getProductTypeSpinnerPosition(){
        String[] productTypesArray = resources.getStringArray(R.array.ProductDetailsActivity_type_of_product_array);
        int selection = 0;
        for(int counter = 0; productTypesArray.length > counter; counter++){
            if(product.getTypeOfProduct().equals(productTypesArray[counter]))
                selection = counter;
        }
        return selection;
    }

    public int getProductFeaturesSpinnerPosition(int productTypeSpinnerPosition){
        String[] productFeaturesArray;
        int selection = 0;

        if(productTypeSpinnerPosition == 1)
            productFeaturesArray = resources.getStringArray(R.array.ProductDetailsActivity_store_products_array);
        else if(productTypeSpinnerPosition == 2)
            productFeaturesArray = resources.getStringArray(R.array.ProductDetailsActivity_ready_meals_array);
        else if(productTypeSpinnerPosition == 3)
            productFeaturesArray = resources.getStringArray(R.array.ProductDetailsActivity_vegetables_array);
        else if(productTypeSpinnerPosition == 4)
            productFeaturesArray = resources.getStringArray(R.array.ProductDetailsActivity_fruits_array);
        else if(productTypeSpinnerPosition == 5)
            productFeaturesArray = resources.getStringArray(R.array.ProductDetailsActivity_herbs_array);
        else if(productTypeSpinnerPosition == 6)
            productFeaturesArray = resources.getStringArray(R.array.ProductDetailsActivity_liqueurs_array);
        else if(productTypeSpinnerPosition == 7)
            productFeaturesArray = resources.getStringArray(R.array.ProductDetailsActivity_wines_type_array);
        else if(productTypeSpinnerPosition == 8)
            productFeaturesArray = resources.getStringArray(R.array.ProductDetailsActivity_mushrooms_array);
        else if(productTypeSpinnerPosition == 9)
            productFeaturesArray = resources.getStringArray(R.array.ProductDetailsActivity_vinegars_array);
        else if(productTypeSpinnerPosition == 10)
            productFeaturesArray = resources.getStringArray(R.array.ProductDetailsActivity_chemical_products_array);
        else
            productFeaturesArray = resources.getStringArray(R.array.ProductDetailsActivity_other_products_array);

        for(int counter = 0; productFeaturesArray.length > counter; counter++){
            if(product.getProductFeatures().equals(productFeaturesArray[counter]))
                selection = counter;
        }
        return selection;
    }

    public void setTaste(RadioButton selectedTasteButton){
        String[] tasteArray = resources.getStringArray(R.array.ProductDetailsActivity_taste_array);
        if(selectedTasteButton == null)
            taste = tasteArray[0];
        else
            taste = selectedTasteButton.getText().toString();
    }

    public void setExpirationDate(String date){
        this.expirationDate = date;
    }

    public void setProductionDate(String date){
        this.productionDate = date;
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

    public void addProducts(List<Product> products){
        db.productsDao().insertProductsToDB(products);
    }

    public void updateProduct(Product product){
        product.setTaste(taste);
        product.setProductionDate(productionDate);
        product.setExpirationDate(expirationDate);
        db.productsDao().updateProduct(product);
    }

    public Product getProductFromDb(int productId){
        return db.productsDao().getProductById(productId);
    }
}