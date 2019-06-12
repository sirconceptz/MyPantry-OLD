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

public class ProductModel {

    public ProductModel(Resources resources){
        this.taste = resources.getString(R.string.ProductDetailsActivity_not_selected);
        tasteArray = resources.getStringArray(R.array.ProductDetailsActivity_taste_array);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getTypeOfProduct() {
        return typeOfProduct;
    }

    public void setTypeOfProduct(String typeOfProduct) {
        this.typeOfProduct = typeOfProduct;
    }

    public String getProductFeatures() {
        return productFeatures;
    }

    public void setProductFeatures(String productFeatures) {
        this.productFeatures = productFeatures;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(String productionDate) {
        this.productionDate = productionDate;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public String getHealingProperties() {
        return healingProperties;
    }

    public void setHealingProperties(String healingProperties) {
        this.healingProperties = healingProperties;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public boolean isHasSugar() {
        return hasSugar;
    }

    public void setHasSugar(boolean hasSugar) {
        this.hasSugar = hasSugar;
    }

    public boolean isHasSalt() {
        return hasSalt;
    }

    public void setHasSalt(boolean hasSalt) {
        this.hasSalt = hasSalt;
    }

    public String getTaste() {
        return taste;
    }

    public void setIsSweet(boolean isSweet){
        if(isSweet) this.taste = tasteArray[1];
    }


    public void setIsSour(boolean isSour){
        if(isSour) this.taste = tasteArray[2];
    }


    public void setIsSweetAndSour(boolean isSweetAndSour){
        if(isSweetAndSour) this.taste = tasteArray[3];
    }

    public void setIsBitter(boolean isBitter){
        if(isBitter) this.taste = tasteArray[4];
    }


    public void setIsSalty(boolean isSalty){
        if(isSalty) this.taste = tasteArray[5];
    }

    private String[] tasteArray;
    private String name;
    private String hashCode;
    private String typeOfProduct;
    private String productFeatures;
    private String expirationDate;
    private String productionDate;
    private String composition;
    private String healingProperties;
    private String dosage;
    private String volume;
    private String weight;
    private boolean hasSugar;
    private boolean hasSalt;
    private String taste;
}