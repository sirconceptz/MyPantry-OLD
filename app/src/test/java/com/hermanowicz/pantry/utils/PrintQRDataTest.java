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

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PrintQRDataTest {

    private List<Product> mProductList = new ArrayList<>();

    @Test
    void getTextToQRCodeList() {
        String date1 = "1990-01-01";
        String date2 = "2000-02-02";
        String date3 = "2005-03-05";
        String name1 = "Strawberry";
        String name2 = "Apple";
        String name3 = "Banana";

        Product product = new Product();
        product.setId(0);
        product.setName(name1);
        product.setExpirationDate(date1);
        mProductList.add(product);

        product = new Product();
        product.setId(1);
        product.setName(name2);
        product.setExpirationDate(date2);
        mProductList.add(product);

        product = new Product();
        product.setId(2);
        product.setName(name3);
        product.setExpirationDate(date3);
        mProductList.add(product);

        ArrayList<String> textToQRCodeList = PrintQRData.getTextToQRCodeList(mProductList);
        assertEquals(3, textToQRCodeList.size());
        System.out.println(textToQRCodeList.get(0));
        System.out.println(textToQRCodeList.get(1));
        System.out.println(textToQRCodeList.get(2));
    }

    @Test
    void getNamesOfProductsList() {
        String date1 = "1990-01-01";
        String date2 = "2000-02-02";
        String date3 = "2005-03-05";
        String name1 = "Strawberry";
        String name2 = "Apple";
        String name3 = "Banana";

        Product product = new Product();
        product.setId(0);
        product.setName(name1);
        product.setExpirationDate(date1);
        mProductList.add(product);

        product = new Product();
        product.setId(1);
        product.setName(name2);
        product.setExpirationDate(date2);
        mProductList.add(product);

        product = new Product();
        product.setId(2);
        product.setName(name3);
        product.setExpirationDate(date3);
        mProductList.add(product);

        ArrayList<String> namesOfProductsList = PrintQRData.getNamesOfProductsList(mProductList);

        assertEquals("Strawberry", namesOfProductsList.get(0));
        assertEquals("Apple", namesOfProductsList.get(1));
        assertEquals("Banana", namesOfProductsList.get(2));
    }

    @Test
    void getExpirationDatesList() {
        String date1 = "1990-01-01";
        String date2 = "2000-02-02";
        String date3 = "2005-03-05";
        String name1 = "Strawberry";
        String name2 = "Apple";
        String name3 = "Banana";

        Product product = new Product();
        product.setId(0);
        product.setName(name1);
        product.setExpirationDate(date1);
        mProductList.add(product);

        product = new Product();
        product.setId(1);
        product.setName(name2);
        product.setExpirationDate(date2);
        mProductList.add(product);

        product = new Product();
        product.setId(2);
        product.setName(name3);
        product.setExpirationDate(date3);
        mProductList.add(product);

        ArrayList<String> expirationDatesOfProducts = PrintQRData.getExpirationDatesList(mProductList);

        assertEquals("1990-01-01", expirationDatesOfProducts.get(0));
        assertEquals("2000-02-02", expirationDatesOfProducts.get(1));
        assertEquals("2005-03-05", expirationDatesOfProducts.get(2));
    }
}