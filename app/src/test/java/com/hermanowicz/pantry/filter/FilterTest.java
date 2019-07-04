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

import com.hermanowicz.pantry.db.Product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
class FilterTest {

    private List<Product> productList;
    private FilterModel filterProduct1 = new FilterModel();
    private FilterModel filterProduct2 = new FilterModel();
    private int sizeOfProductList = 0;
    private String name1 = "Strawberry";
    private String name2 = "Apple";
    private String name3 = "Banana";
    private String date1 = "1990-01-01";
    private String date2 = "2000-02-02";
    private String date3 = "2005-03-05";
    private String typeOfProduct1 = "Ready meals";
    private String typeOfProduct2 = "Herbs";
    private String productFeatures1 = "Dried";
    private String productFeatures2 = "Fresh";
    private String productFeatures3 = "Ready meals";
    private String taste1 = "Sweet";
    private String taste2 = "Sour";
    private String taste3 = "Salty";

    @BeforeEach
    void setUp() {
        productList = new ArrayList<>();
        Product product = new Product();
        product.setName(name1);
        product.setTypeOfProduct(typeOfProduct1);
        product.setProductFeatures(productFeatures3);
        product.setExpirationDate(date1);
        product.setProductionDate(date2);
        product.setVolume(100);
        product.setWeight(200);
        product.setHasSugar(true);
        product.setHasSalt(false);
        product.setTaste(taste3);
        productList.add(product);

        product = new Product();
        product.setName(name2);
        product.setTypeOfProduct(typeOfProduct2);
        product.setProductFeatures(productFeatures2);
        product.setExpirationDate(date2);
        product.setProductionDate(date3);
        product.setVolume(300);
        product.setWeight(300);
        product.setHasSugar(false);
        product.setHasSalt(true);
        product.setTaste(taste2);
        productList.add(product);

        product = new Product();
        product.setName(name3);
        product.setTypeOfProduct(typeOfProduct1);
        product.setProductFeatures(productFeatures3);
        product.setExpirationDate(date3);
        product.setProductionDate(date1);
        product.setVolume(250);
        product.setWeight(350);
        product.setHasSugar(true);
        product.setHasSalt(true);
        product.setTaste(taste2);
        productList.add(product);

        product = new Product();
        product.setName(name1);
        product.setTypeOfProduct(typeOfProduct2);
        product.setProductFeatures(productFeatures1);
        product.setExpirationDate(date2);
        product.setProductionDate(date2);
        product.setVolume(400);
        product.setWeight(500);
        product.setHasSugar(false);
        product.setHasSalt(false);
        product.setTaste(taste3);
        productList.add(product);

        product = new Product();
        product.setName(name1);
        product.setTypeOfProduct(typeOfProduct1);
        product.setProductFeatures(productFeatures3);
        product.setExpirationDate(date3);
        product.setProductionDate(date3);
        product.setVolume(0);
        product.setWeight(0);
        product.setHasSugar(true);
        product.setHasSalt(false);
        product.setTaste(taste3);
        productList.add(product);

        product = new Product();
        product.setName(name2);
        product.setTypeOfProduct(typeOfProduct2);
        product.setExpirationDate(date3);
        product.setProductionDate(date1);
        product.setVolume(1000);
        product.setWeight(1000);
        product.setTaste(taste1);
        product.setHasSugar(false);
        product.setHasSalt(false);
        productList.add(product);

        product = new Product();
        product.setName(name3);
        product.setTypeOfProduct(typeOfProduct1);
        product.setExpirationDate(date3);
        product.setProductionDate(date2);
        product.setVolume(30);
        product.setWeight(40);
        product.setHasSugar(true);
        product.setHasSalt(true);
        product.setTaste(taste2);
        productList.add(product);


    }

    @Test
    void isFilterByProductCorrect() {
    }
}