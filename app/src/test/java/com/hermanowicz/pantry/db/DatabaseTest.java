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

    private CategoryDao categoryDao;
    private CategoryDb categoryDb;

    @Before
    public void createDb() {
        Context context = RuntimeEnvironment.systemContext;
        productDb = Room.inMemoryDatabaseBuilder(context, ProductDb.class).allowMainThreadQueries().build();
        categoryDb = Room.inMemoryDatabaseBuilder(context, CategoryDb.class).allowMainThreadQueries().build();

        productsDao = productDb.productsDao();
        categoryDao = categoryDb.categoryDao();
    }

    @Test
    public void canIWriteCategoryAndReadInList() {
        categoryDao.clearDb();

        List<Category> categoryList = categoryDao.getAllOwnCategories();
        assertThat(categoryList.size(), equalTo(0));

        Category category = new Category();
        category.setName("Example name");
        category.setName("Example description");
        categoryDao.addCategory(category);

        categoryList = categoryDao.getAllOwnCategories();
        assertEquals(categoryList.size(), 1);
    }

    @Test
    public void canIWriteProductsAndReadInList() {
        productsDao.clearDb();

        List<Product> productList = productsDao.getAllProductsList();
        assertThat(productList.size(), equalTo(0));

        for(int counter = 0; 3 > counter; counter++)
        {
            Product product = new Product();

            productList.add(product);
        }
        productDb.productsDao().addProducts(productList);

        productList = productsDao.getAllProductsList();
        assertThat(productList.size(), equalTo(3));
    }

    @Test
    public void canIClearCategoryDatabase() {
        canIWriteCategoryAndReadInList();
        categoryDao.clearDb();
        int sizeOfDatabase = categoryDao.getAllOwnCategories().size();
        assertEquals(0, sizeOfDatabase);
    }

    @Test
    public void canIClearProductDatabase() {
        canIWriteProductsAndReadInList();
        productsDao.clearDb();
        int sizeOfDatabase = productsDao.getAllProductsList().size();
        assertEquals(0, sizeOfDatabase);
    }

    @After
    public void closeDb() {
        categoryDb.close();
        productDb.close();
    }
}