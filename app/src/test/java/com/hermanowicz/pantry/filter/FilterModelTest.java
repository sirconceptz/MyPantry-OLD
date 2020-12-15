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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FilterModelTest {

    private FilterModel filterProduct = new FilterModel();

    @Test
    void canIGetCorrectName() {
        String name = "Raspberry juice";
        filterProduct.setName(name);
        assertEquals(name, filterProduct.getName());
    }

    @Test
    void canIGetCorrectTypeOfProduct() {
        String typeOfProduct = "Fruit";
        filterProduct.setTypeOfProduct(typeOfProduct);
        assertEquals(typeOfProduct, filterProduct.getTypeOfProduct());
    }

    @Test
    void canIGetCorrectProductFeatures() {
        String productFeatures = "Juice";
        filterProduct.setProductCategory(productFeatures);
        assertEquals(productFeatures, filterProduct.getProductCategory());
    }

    @Test
    void canIGetCorrectExpirationDateSince() {
        String expirationDateSince = "2010-05-15";
        filterProduct.setExpirationDateSince(expirationDateSince);
        assertEquals(expirationDateSince, filterProduct.getExpirationDateSince());
    }

    @Test
    void canIGetCorrectExpirationDateFor() {
        String expirationDateFor = "2015-08-18";
        filterProduct.setExpirationDateFor(expirationDateFor);
        assertEquals(expirationDateFor, filterProduct.getExpirationDateFor());
    }

    @Test
    void canIGetCorrectProductionDateSince() {
        String productionDateSince = "2009-02-24";
        filterProduct.setProductionDateSince(productionDateSince);
        assertEquals(productionDateSince, filterProduct.getProductionDateSince());
    }

    @Test
    void canIGetCorrectProductionDateFor() {
        String productionDateFor = "2011-07-29";
        filterProduct.setProductionDateFor(productionDateFor);
        assertEquals(productionDateFor, filterProduct.getProductionDateFor());
    }

    @Test
    void canIGetCorrectComposition() {
        String composition = "Raspberries";
        filterProduct.setComposition(composition);
        assertEquals(composition, filterProduct.getComposition());
    }

    @Test
    void canIGetCorrectHealingProperties() {
        String healingProperties = "No";
        filterProduct.setHealingProperties(healingProperties);
        assertEquals(healingProperties, filterProduct.getHealingProperties());
    }

    @Test
    void canIGetCorrectDosage() {
        String dosage = null;
        filterProduct.setDosage(dosage);
        assertEquals(dosage, filterProduct.getDosage());
    }

    @Test
    void canIGetCorrectVolumeSince() {
        int volumeSince = 15;
        filterProduct.setVolumeSince(volumeSince);
        assertEquals(volumeSince, filterProduct.getVolumeSince());
    }

    @Test
    void canIGetCorrectVolumeFor() {
        int volumeFor = 100;
        filterProduct.setVolumeFor(volumeFor);
        assertEquals(volumeFor, filterProduct.getVolumeFor());
    }

    @Test
    void canIGetCorrectWeightSince() {
        int weightSince = 25;
        filterProduct.setWeightSince(weightSince);
        assertEquals(weightSince, filterProduct.getWeightSince());
    }

    @Test
    void canIGetCorrectWeightFor() {
        int weightFor = 300;
        filterProduct.setWeightFor(weightFor);
        assertEquals(weightFor, filterProduct.getWeightFor());
    }

    @Test
    void canIGetCorrectHasSugar() {
        Filter.Set hasSugar = Filter.Set.YES;
        filterProduct.setHasSugar(hasSugar);
        assertEquals(hasSugar, filterProduct.getHasSugar());
    }

    @Test
    void canIGetCorrectHasSalt() {
        Filter.Set hasSalt = Filter.Set.DISABLED;
        filterProduct.setHasSalt(hasSalt);
        assertEquals(hasSalt, filterProduct.getHasSalt());
    }

    @Test
    void canIGetCorrectTaste() {
        String taste = "Sweet";
        filterProduct.setTaste(taste);
        assertEquals(taste, filterProduct.getTaste());
    }
}