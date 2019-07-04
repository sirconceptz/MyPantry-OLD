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

package com.hermanowicz.pantry.db;

import android.content.Context;

import androidx.room.Room;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class DatabaseTest {

    private ProductsDao productsDao;
    private ProductDb productDb;

    @Before
    public void createDb() {
        Context context = RuntimeEnvironment.systemContext;
        productDb = Room.inMemoryDatabaseBuilder(context, ProductDb.class).allowMainThreadQueries().build();
        productsDao = productDb.productsDao();
    }

    @Test
    public void canIWriteProductsAndReadInList() {
        productDb.productsDao().clearDb();

        List<Product> productList = productsDao.getAllProductsAsList();
        assertThat(productList.size(), equalTo(0));

        for(int counter = 0; 3 > counter; counter++)
        {
            Product product = new Product();

            productList.add(product);
        }
        productDb.productsDao().insertProductsToDB(productList);

        productList = productsDao.getAllProductsAsList();
        assertThat(productList.size(), equalTo(3));
    }

    @Test
    public void canIClearDatabase() {
        canIWriteProductsAndReadInList();
        productDb.productsDao().clearDb();
        int sizeOfDatabase = productDb.productsDao().getAllProductsAsList().size();
        assertEquals(0, sizeOfDatabase);
    }

        @After
    public void closeDb() throws IOException {
        productDb.close();
    }
}