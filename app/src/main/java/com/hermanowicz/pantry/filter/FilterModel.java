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

package com.hermanowicz.pantry.filter;

/**
 * <h1>FilterModel</h1>
 * The filter model is used to store the data needed to find products in the pantry.
 *
 * @author  Mateusz Hermanowicz
 * @version 1.0
 * @since   1.0
 */
public class FilterModel {

    private String name = null;
    private String typeOfProduct = null;
    private String productFeatures = null;
    private String expirationDateSince = null;
    private String expirationDateFor = null;
    private String productionDateSince = null;
    private String productionDateFor = null;
    private String composition = null;
    private String healingProperties = null;
    private String dosage = null;
    private int volumeSince = -1;
    private int volumeFor = -1;
    private int weightSince = -1;
    private int weightFor = -1;
    private Filter.Set hasSugar = Filter.Set.DISABLED;
    private Filter.Set hasSalt = Filter.Set.DISABLED;
    private String taste = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getExpirationDateSince() {
        return expirationDateSince;
    }

    public void setExpirationDateSince(String expirationDateSince) {
        this.expirationDateSince = expirationDateSince;
    }

    public String getExpirationDateFor() {
        return expirationDateFor;
    }

    public void setExpirationDateFor(String expirationDateFor) {
        this.expirationDateFor = expirationDateFor;
    }

    public String getProductionDateSince() {
        return productionDateSince;
    }

    public void setProductionDateSince(String productionDateSince) {
        this.productionDateSince = productionDateSince;
    }

    public String getProductionDateFor() {
        return productionDateFor;
    }

    public void setProductionDateFor(String productionDateFor) {
        this.productionDateFor = productionDateFor;
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

    public int getVolumeSince() {
        return volumeSince;
    }

    public void setVolumeSince(int volumeSince) {
        this.volumeSince = volumeSince;
    }

    public int getVolumeFor() {
        return volumeFor;
    }

    public void setVolumeFor(int volumeFor) {
        this.volumeFor = volumeFor;
    }

    public int getWeightSince() {
        return weightSince;
    }

    public void setWeightSince(int weightSince) {
        this.weightSince = weightSince;
    }

    public int getWeightFor() {
        return weightFor;
    }

    public void setWeightFor(int weightFor) {
        this.weightFor = weightFor;
    }

    public Filter.Set getHasSugar() {
        return hasSugar;
    }

    public void setHasSugar(Filter.Set hasSugar) {
        this.hasSugar = hasSugar;
    }

    public Filter.Set getHasSalt() {
        return hasSalt;
    }

    public void setHasSalt(Filter.Set hasSalt) {
        this.hasSalt = hasSalt;
    }

    public String getTaste() {
        return taste;
    }

    public void setTaste(String taste) {
        this.taste = taste;
    }
}