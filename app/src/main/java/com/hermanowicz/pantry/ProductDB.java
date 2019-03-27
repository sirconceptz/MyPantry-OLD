/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {ProductEntity.class}, version = 1)
public abstract class ProductDB extends RoomDatabase {
    public abstract ProductsDao productsDao();
}