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

package com.hermanowicz.pantry.db.storagelocation;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StorageLocationDao {
    @Query("SELECT * FROM storage_locations WHERE id = (:id)")
    StorageLocation getStorageLocation(int id);

    @Query("SELECT * FROM storage_locations")
    List<StorageLocation> getAllStorageLocations();

    @Query("SELECT name FROM storage_locations")
    String[] getAllStorageLocationsArray();

    @Insert
    void addStorageLocation(StorageLocation storageLocation);

    @Delete
    void deleteStorageLocation(StorageLocation storageLocation);

    @Update
    void updateStorageLocation(StorageLocation storageLocation);
}