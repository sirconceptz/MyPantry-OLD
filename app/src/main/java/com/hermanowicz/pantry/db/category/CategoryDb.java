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

package com.hermanowicz.pantry.db.category;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

/**
 * <h1>CategoryDb/h1>
 * Category database class
 *
 * @author  Mateusz Hermanowicz
 */

@Database(entities = {Category.class}, version = 1)
public abstract class CategoryDb extends RoomDatabase {

    public abstract CategoryDao categoryDao();

    private static CategoryDb INSTANCE;
    private static final Object sLock = new Object();

    public static CategoryDb getInstance(@NonNull Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        CategoryDb.class, "CategoryDb.db")
                        .allowMainThreadQueries()
                        .addCallback(new Callback() {
                            @Override
                            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                super.onCreate(db);
                            }
                        })
                        .build();
            }
            return INSTANCE;
        }
    }
}