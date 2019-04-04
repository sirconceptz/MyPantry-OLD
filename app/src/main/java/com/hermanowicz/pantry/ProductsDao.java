/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

@Dao
public interface ProductsDao {
    @RawQuery
    List<ProductEntity> getProductsFromDB(SupportSQLiteQuery query);

    @Query("SELECT id FROM 'products' WHERE id = (:id)")
    int idOFLastProductInDB(int id);

    @Query("SELECT * FROM 'products'")
    ProductEntity getAllProducts();

    @Insert
    void insertProductToDB(ProductEntity... product);

    @Query("DELETE FROM 'products' WHERE id = (:id)")
    int deleteProductById(int id);
}