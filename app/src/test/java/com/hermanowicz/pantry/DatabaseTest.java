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

package com.hermanowicz.pantry;

import android.content.Context;

import androidx.room.Room;
import androidx.test.InstrumentationRegistry;

import com.hermanowicz.pantry.db.Product;
import com.hermanowicz.pantry.db.ProductDb;
import com.hermanowicz.pantry.db.ProductsDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class DatabaseTest {

    private ProductsDao mProductsDao;
    private ProductDb mProductDb;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        mProductDb = Room.inMemoryDatabaseBuilder(context, ProductDb.class).build();
        mProductsDao = mProductDb.productsDao();
    }

    @Test
    public void writeProductAndReadInList() throws Exception {

        mProductDb.productsDao().clearDb();

        List<Product> productList = mProductsDao.getAllProductsAsList();
        assertThat(productList.size(), equalTo(0));

        for(int i = 0; 3 > i; i++)
        {
            Product product = new Product();
            product.setName("Name " + i);
            product.setTypeOfProduct("Type of product");
            product.setProductFeatures("Product features");
            product.setExpirationDate("2020-01-01");
            product.setProductionDate("2019-12-31");
            product.setComposition("Composition " + i);
            product.setHealingProperties("Healing properties " + i);
            product.setDosage("Dosage " + i);
            product.setVolume(500);
            product.setWeight(1000);
            product.setHasSugar(true);
            product.setHasSalt(false);
            product.setTaste("Taste " + i);
            product.setHashCode(String.valueOf(product.hashCode()));
            productList.add(product);
        }
        mProductDb.productsDao().insertProductsToDB(productList);

        productList = mProductsDao.getAllProductsAsList();
        assertThat(productList.size(), equalTo(3));
    }

    @After
    public void closeDb() throws IOException {
        mProductDb.close();
    }
}