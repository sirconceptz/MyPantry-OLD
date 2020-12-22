package com.hermanowicz.pantry.db;

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