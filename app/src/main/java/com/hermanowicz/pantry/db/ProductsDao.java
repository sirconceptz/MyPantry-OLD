/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProductsDao {
    @Query("SELECT * FROM products WHERE id = (:id)")
    Product getProductById(int id);

    @Query("SELECT * FROM products ORDER BY expirationDate ASC")
    LiveData<List<Product>> getAllProducts();

    @Query("SELECT * FROM products ORDER BY expirationDate ASC")
    List<Product> getAllProductsAsList();

    @Insert
    void insertProductToDB(List<Product> productsArrayList);

    @Query("DELETE FROM products WHERE id = (:id)")
    void deleteProductById(int id);

    @Query("DELETE FROM products")
    void clearDb();
}