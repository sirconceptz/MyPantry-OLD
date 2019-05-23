/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.hermanowicz.pantry.models.ProductEntity;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface ProductsDao {
    @Query("SELECT * FROM 'products' WHERE id = (:id)")
    ProductEntity getProductById(int id);

    @Query("SELECT * FROM 'products' ORDER BY expirationDate ASC")
    List<ProductEntity> getAllProducts();

    @Insert
    void insertProductToDB(ArrayList<ProductEntity> productsArrayList);

    @Query("DELETE FROM 'products' WHERE id = (:id)")
    void deleteProductById(int id);

    @Query("DELETE FROM 'products'")
    void clearDb();
}