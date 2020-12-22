package com.hermanowicz.pantry.models;

import com.hermanowicz.pantry.db.StorageLocation;

import java.util.List;

public class StorageLocationModel {

    public final int MAX_CHAR_STORAGE_LOCATION_NAME = 30;
    public final int MAX_CHAR_STORAGE_LOCATION_DESCRIPTION = 200;

    private final DatabaseOperations db;

    public StorageLocationModel(DatabaseOperations databaseOperations){
        this.db = databaseOperations;
    }

    public StorageLocation getStorageLocation(int id) {
        return db.getStorageLocation(id);
    }

    public void updateStorageLocation(StorageLocation storageLocation) {
        db.updateStorageLocation(storageLocation);
    }

    public void deleteStorageLocation(int id) {
        db.deleteStorageLocation(id);
    }

    public List<StorageLocation> getStorageLocationList(){
        return db.getStorageLocationList();
    }

    public boolean addStorageLocation(StorageLocation storageLocation){
        List<StorageLocation> storageLocationList = db.getStorageLocationList();
        boolean correct = true;
        for(StorageLocation storageLocation1 : storageLocationList){
            if (storageLocation1.getName().equals(storageLocation.getName())) {
                correct = false;
                break;
            }
        }
        if(correct) {
            db.addStorageLocation(storageLocation);
            return true;
        }
        else
            return false;
    }

    public boolean isStorageLocationNameNotCorrect(String storageLocationName) {
        return (storageLocationName.length() > MAX_CHAR_STORAGE_LOCATION_NAME || storageLocationName.length() < 1);
    }

    public boolean isStorageLocationDescriptionNotCorrect(String storageLocationDescription) {
        return storageLocationDescription.length() > MAX_CHAR_STORAGE_LOCATION_DESCRIPTION;
    }
}