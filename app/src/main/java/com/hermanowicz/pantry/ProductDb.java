/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.hermanowicz.pantry.interfaces.ProductsDao;
import com.hermanowicz.pantry.models.ProductEntity;

@Database(entities = {ProductEntity.class}, version = 1)
public abstract class ProductDb extends RoomDatabase {
    public abstract ProductsDao productsDao();
}