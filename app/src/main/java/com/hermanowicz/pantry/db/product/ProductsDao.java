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

package com.hermanowicz.pantry.db.product;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * <h1>ProductsDao</h1>
 * Products dao needed to support database.
 *
 * @author  Mateusz Hermanowicz
 */

@Dao
public interface ProductsDao {
    @Query("SELECT * FROM products WHERE id = (:id)")
    Product getProduct(int id);

    @Query("SELECT * FROM products WHERE barcode = (:barcode)")
    List<Product> getProductListWithBarcode(String barcode);

    @Query("SELECT * FROM products ORDER BY expirationDate ASC")
    LiveData<List<Product>> getAllProductsLivedata();

    @Query("SELECT * FROM products ORDER BY expirationDate ASC")
    List<Product> getAllProductsList();

    @Insert
    void addProducts(List<Product> products);

    @Delete
    void deleteProducts(List<Product> product);

    @Query("DELETE FROM products")
    void clearDb();

    @Query("SELECT id FROM products ORDER BY id DESC")
    int getIdLastProduct();

    @Update
    void updateProduct(Product... products);
}