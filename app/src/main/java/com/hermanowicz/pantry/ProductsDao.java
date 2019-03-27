/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry;

import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;

import java.util.List;

@Dao
public interface ProductsDao {
    @RawQuery
    List<ProductEntity> getProductsFromDB(SupportSQLiteQuery query);

    @Query("SELECT id FROM 'products' WHERE id = (:id)")
    int idOFLastProductInDB(int id);

    @Insert
    void insertProductToDB(ProductEntity... product);

    @Query("DELETE FROM 'products' WHERE id = (:id)")
    int deleteById(int id);
}