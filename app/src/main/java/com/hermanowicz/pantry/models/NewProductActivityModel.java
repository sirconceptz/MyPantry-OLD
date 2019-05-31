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

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.db.Product;

import java.util.ArrayList;
import java.util.List;

public class NewProductActivityModel {

    private Resources resources;
    private String name = "";
    private String typeOfProduct = "";
    private String productFeatures = "";
    private String expirationDate = "";
    private String productionDate = "";
    private String composition = "";
    private String healingProperties = "";
    private String dosage = "";
    private String taste = "";
    private int quantity, volume, weight;
    private boolean hasSugar, hasSalt, isSweet, isSour, isSweetAndSour, isBitter, isSalty;

    public NewProductActivityModel(Resources resources) {
        this.resources = resources;
    }

    public List<Product> buildProductsList() {
        setTaste();
        List<Product> productsList = new ArrayList<>();
        for (int counter = 1; counter <= quantity; counter++) {
            if(!isExpirationDateValid())
                expirationDate="-";

            Product product = new Product();
            product.setName(name);
            product.setTypeOfProduct(typeOfProduct);
            product.setProductFeatures(productFeatures);
            product.setExpirationDate(expirationDate);
            product.setProductionDate(productionDate);
            product.setComposition(composition);
            product.setHealingProperties(healingProperties);
            product.setDosage(dosage);
            product.setVolume(volume);
            product.setWeight(weight);
            product.setHasSugar(hasSugar);
            product.setHasSalt(hasSalt);
            product.setTaste(taste);
            product.setHashCode(String.valueOf(product.hashCode()));
            productsList.add(product);
        }
        return productsList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTypeOfProduct(String typeOfProduct) {
        this.typeOfProduct = typeOfProduct;
    }

    public void setProductFeatures(String productFeatures) {
        this.productFeatures = productFeatures;
    }

    public void setExpirationDate(String expirationDate) {
        try {
            String[] expirationDateArray = expirationDate.split("\\.");
            int year = Integer.valueOf(expirationDateArray[2]);
            int month = Integer.valueOf(expirationDateArray[1]);
            int day = Integer.valueOf(expirationDateArray[0]);
            this.expirationDate = year + "-" + month + "-" + day;
        } catch (ArrayIndexOutOfBoundsException e) {
            this.expirationDate = "";
        }
    }

    public void setProductionDate(String productionDate) {
        try {
            String[] productionDateArray = productionDate.split("\\.");
            int year = Integer.valueOf(productionDateArray[2]);
            int month = Integer.valueOf(productionDateArray[1]);
            int day = Integer.valueOf(productionDateArray[0]);
            this.productionDate = year + "-" + month + "-" + day;
        } catch (ArrayIndexOutOfBoundsException e) {
            this.productionDate = "";
        }
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public void setHealingProperties(String healingProperties) {
        this.healingProperties = healingProperties;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public void setTaste(String taste) {
        this.taste = taste;
    }

    public void setHasSugar(Boolean hasSugar) {
        this.hasSugar = hasSugar;
    }

    public void setHasSalt(Boolean hasSalt) {
        this.hasSalt = hasSalt;
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

    public void parseVolumeProduct(String volume) {
        try {
            this.volume = Integer.valueOf(volume);
        } catch (NumberFormatException e) {
            this.volume = 0;
        }
        if (this.quantity < 0)
            this.quantity = 0;
    }

    public void parseWeightProduct(String weight) {
        try {
            this.weight = Integer.valueOf(weight);
        } catch (NumberFormatException e) {
            this.weight = 0;
        }
        if (this.quantity < 0)
            this.quantity = 0;
    }

    public String[] getExpirationDateArrayList() {
        String[] expirationDateArray = expirationDate.split("-");
        return expirationDateArray;
    }

    public String[] getProductionDateArrayList() {
        String[] productionDateArray = productionDate.split("-");
        return productionDateArray;
    }

    public void setIsSweet(boolean isSweet) {
        this.isSweet = isSweet;
    }

    public void setIsSour(boolean isSour) {
        this.isSour = isSour;
    }

    public void setIsSweetAndSour(boolean isSweetAndSour) {
        this.isSweetAndSour = isSweetAndSour;
    }

    public void setIsBitter(boolean isBitter) {
        this.isBitter = isBitter;
    }

    public void setIsSalty(boolean isSalty) {
        this.isSalty = isSalty;
    }

    private void setTaste() {
        String[] filterTasteArray = resources.getStringArray(R.array.ProductDetailsActivity_taste_array);
        if (!isSweet && !isSour && !isSweetAndSour && !isBitter && !isSalty) {
            taste = resources.getString(R.string.ProductDetailsActivity_not_selected);
        } else if (isSweet) {
            taste = filterTasteArray[1];
        } else if (isSour) {
            taste = filterTasteArray[2];
        } else if (isSweetAndSour) {
            taste = filterTasteArray[3];
        } else if (isBitter) {
            taste = filterTasteArray[4];
        } else {
            taste = filterTasteArray[5];
        }
    }

    public String getOnProductAddStatement() {
        String statementToShow;
        if (quantity < 2)
            statementToShow = resources.getString(R.string.NewProductActivity_product_added_successful);
        else
            statementToShow = resources.getString(R.string.NewProductActivity_products_added_successful);
        return statementToShow;
    }

    public boolean isProductNameValid() {
        boolean correctProductName = false;
        if (name.length() > 0)
            correctProductName = true;
        return !correctProductName;
    }

    public boolean isTypeOfProductValid() {
        String[] typeOfProductsArray = resources.getStringArray(R.array.ProductDetailsActivity_type_of_product_array);
        boolean correctTypeOfProduct = false;
        if (!typeOfProduct.equals(typeOfProductsArray[0]))
            correctTypeOfProduct = true;
        return correctTypeOfProduct;
    }

    public boolean isExpirationDateValid() {
        boolean correctExpirationDate = false;
        if (expirationDate.length() > 0)
            correctExpirationDate = true;
        return correctExpirationDate;
    }
}