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

package com.hermanowicz.pantry.utils;

import com.hermanowicz.pantry.db.Product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PrintQRDataTest {

    private List<Product> productList = new ArrayList<>();
    private String date1 = "1990-01-01";
    private String date2 = "2000-02-02";
    private String date3 = "2005-03-05";
    private String name1 = "Strawberry";
    private String name2 = "Apple";
    private String name3 = "Banana";

    @BeforeEach
    void setUp()
    {
        productList = new ArrayList<>();
        Product product = new Product();
        product.setName(name1);
        product.setExpirationDate(date1);
        productList.add(product);

        product = new Product();
        product.setName(name2);
        product.setExpirationDate(date2);
        productList.add(product);

        product = new Product();
        product.setName(name3);
        product.setExpirationDate(date3);
        productList.add(product);
    }

    @Test
    void canIGetCorrectTextToQRCodeList() {
        ArrayList<String> textToQRCodeList = PrintQRData.getTextToQRCodeList(productList, 0);
        assertEquals(3, textToQRCodeList.size());
        System.out.println(textToQRCodeList.get(0));
        System.out.println(textToQRCodeList.get(1));
        System.out.println(textToQRCodeList.get(2));
    }

    @Test
    void canIGetCorrectNamesOfProductsList() {
        ArrayList<String> namesOfProductsList = PrintQRData.getNamesOfProductsList(productList);

        assertEquals(name1, namesOfProductsList.get(0));
        assertEquals(name2, namesOfProductsList.get(1));
        assertEquals(name3, namesOfProductsList.get(2));

        System.out.println(name1 + " " + namesOfProductsList.get(0));
        System.out.println(name2 + " " + namesOfProductsList.get(1));
        System.out.println(name3 + " " + namesOfProductsList.get(2));
    }

    @Test
    void canIGetCorrectExpirationDatesList() {
        ArrayList<String> expirationDatesOfProducts = PrintQRData.getExpirationDatesList(productList);

        assertEquals(date1, expirationDatesOfProducts.get(0));
        assertEquals(date2, expirationDatesOfProducts.get(1));
        assertEquals(date3, expirationDatesOfProducts.get(2));

        System.out.println(date1 + " " + expirationDatesOfProducts.get(0));
        System.out.println(date2 + " " + expirationDatesOfProducts.get(1));
        System.out.println(date3 + " " + expirationDatesOfProducts.get(2));
    }
}