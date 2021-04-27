/*
 * Copyright (c) 2019-2021
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

import com.hermanowicz.pantry.db.category.Category;
import com.hermanowicz.pantry.db.category.CategoryDao;
import com.hermanowicz.pantry.db.category.CategoryDb;
import com.hermanowicz.pantry.db.product.Product;
import com.hermanowicz.pantry.db.product.ProductDb;
import com.hermanowicz.pantry.db.product.ProductsDao;
import com.hermanowicz.pantry.db.storagelocation.StorageLocation;
import com.hermanowicz.pantry.db.storagelocation.StorageLocationDao;
import com.hermanowicz.pantry.db.storagelocation.StorageLocationDb;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

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

    private StorageLocationDao storageLocationDao;
    private StorageLocationDb storageLocationDb;

    @Before
    public void createDb() {
        Context context = RuntimeEnvironment.systemContext;
        productDb = Room.inMemoryDatabaseBuilder(context, ProductDb.class).allowMainThreadQueries().build();
        categoryDb = Room.inMemoryDatabaseBuilder(context, CategoryDb.class).allowMainThreadQueries().build();
        storageLocationDb = Room.inMemoryDatabaseBuilder(context, StorageLocationDb.class).allowMainThreadQueries().build();

        productsDao = productDb.productsDao();
        categoryDao = categoryDb.categoryDao();
        storageLocationDao = storageLocationDb.storageLocationDao();
    }

    @Test
    public void canIWriteCategoryAndReadInList() {
        categoryDao.clearDb();

        List<Category> categoryList = categoryDao.getAllOwnCategories();
        assertThat(categoryList.size(), equalTo(0));

        Category category = new Category();
        category.setName("Example name");
        category.setDescription("Example description");
        categoryDao.addCategory(category);

        categoryList = categoryDao.getAllOwnCategories();
        assertEquals(categoryList.size(), 1);
    }

    @Test
    public void canIWriteStorageLocationAndReadInList() {
        storageLocationDao.clearDb();

        List<StorageLocation> storageLocationList = storageLocationDao.getAllStorageLocations();
        assertThat(storageLocationList.size(), equalTo(0));

        StorageLocation storageLocation = new StorageLocation();
        storageLocation.setName("Example name");
        storageLocation.setDescription("Example description");
        storageLocationDao.addStorageLocation(storageLocation);

        storageLocationList = storageLocationDao.getAllStorageLocations();
        assertEquals(storageLocationList.size(), 1);
    }

    @Test
    public void canIWriteProductsAndReadInList() {
        productsDao.clearDb();
        List<Product> productList = productsDao.getAllProductsList();
        assertThat(productList.size(), equalTo(0));
        for(int counter = 0; 3 > counter; counter++)
        {
            Product product = new Product();
            product.setName("Example name");
            product.setIsVege(true);
            product.setVolume(100);
            product.setWeight(200);
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

    @Test
    public void canIClearStorageLocationDatabase() {
        canIWriteStorageLocationAndReadInList();
        storageLocationDao.clearDb();
        int sizeOfDatabase = storageLocationDao.getAllStorageLocations().size();
        assertEquals(0, sizeOfDatabase);
    }

    @After
    public void closeDb() {
        categoryDb.close();
        productDb.close();
        storageLocationDb.close();
    }
}