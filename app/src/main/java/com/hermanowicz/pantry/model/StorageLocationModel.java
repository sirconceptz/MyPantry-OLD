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

package com.hermanowicz.pantry.model;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hermanowicz.pantry.db.storagelocation.StorageLocation;
import com.hermanowicz.pantry.db.storagelocation.StorageLocationDb;

import java.util.ArrayList;
import java.util.List;

public class StorageLocationModel {

    public final int MAX_CHAR_STORAGE_LOCATION_NAME = 30;
    public final int MAX_CHAR_STORAGE_LOCATION_DESCRIPTION = 200;

    private final StorageLocationDb db;
    private StorageLocation storageLocation;
    private List<StorageLocation> storageLocationList = new ArrayList<>();
    private String databaseMode;

    public StorageLocationModel(@NonNull Context context) {
        this.db = StorageLocationDb.getInstance(context);
    }

    public StorageLocation getStorageLocation(){
        return this.storageLocation;
    }

    public List<StorageLocation> getStorageLocationList() {
        return storageLocationList;
    }

    public void setStorageLocation(@NonNull StorageLocation storageLocation) {
        this.storageLocation = storageLocation;
    }

    public void setStorageLocationList(@NonNull List<StorageLocation> storageLocationList) {
        this.storageLocationList = storageLocationList;
    }

    public void setOfflineDbStorageLocationList(){
        List<StorageLocation> storageLocationList = db.storageLocationDao().getAllStorageLocations();
        setStorageLocationList(storageLocationList);
    }

    public void updateStorageLocation(@NonNull StorageLocation storageLocation) {
        if(databaseMode.equals("local"))
            updateOfflineStorageLocation(storageLocation);
        else
            updateOnlineStorageLocation(storageLocation);
    }

    private void updateOnlineStorageLocation(@NonNull StorageLocation storageLocation) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("storage_locations/" + FirebaseAuth.getInstance().getUid());
        ref.child(String.valueOf(storageLocation.getId())).setValue(storageLocation);
    }

    public void updateOfflineStorageLocation(@NonNull StorageLocation storageLocation) {
        db.storageLocationDao().updateStorageLocation(storageLocation);
    }

    public void deleteOfflineDbStorageLocation(@NonNull StorageLocation storageLocation) {
        db.storageLocationDao().deleteStorageLocation(storageLocation);
    }

    public void deleteOnlineStorageLocation(@NonNull StorageLocation storageLocation) {
        DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference().child("storage_locations/" + FirebaseAuth.getInstance().getUid());
        ref.child(String.valueOf(storageLocation.getId())).removeValue();
    }

    public boolean addStorageLocation(@NonNull StorageLocation newStorageLocation){
        boolean result;
        if(databaseMode.equals("local"))
            result = addOfflineDbStorageLocation(newStorageLocation);
        else
            result = addOnlineDbStorageLocation(newStorageLocation);
        return result;
    }

    private boolean addOnlineDbStorageLocation(@NonNull StorageLocation newStorageLocation) {
        boolean result = false;
        if(checkIsStorageLocationValid(newStorageLocation, storageLocationList)){
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("storage_locations/" + FirebaseAuth.getInstance().getUid());
            int nextId;
            if (storageLocationList.size() == 0)
                nextId = 0;
            else
                nextId = storageLocationList.get(storageLocationList.size()-1).getId() + 1;
            newStorageLocation.setId(nextId);
            ref.child(String.valueOf(nextId)).setValue(newStorageLocation);
            result = true;
        }
        return result;
    }

    private boolean addOfflineDbStorageLocation(@NonNull StorageLocation newStorageLocation) {
        boolean result = false;
        if(checkIsStorageLocationValid(newStorageLocation, storageLocationList)){
            db.storageLocationDao().addStorageLocation(newStorageLocation);
            result = true;
        }
        return result;
    }

    private boolean checkIsStorageLocationValid(@NonNull StorageLocation newStorageLocation,
                                         @NonNull List<StorageLocation> storageLocationList){
        boolean correct = true;
        for (StorageLocation storageLocation1 : storageLocationList) {
            if (storageLocation1.getName().equals(newStorageLocation.getName())) {
                correct = false;
                break;
            }
        }
        return correct;
    }

    public boolean isStorageLocationNameNotCorrect(@NonNull String name) {
        return (name.length() > MAX_CHAR_STORAGE_LOCATION_NAME || name.length() < 1);
    }

    public boolean isStorageLocationDescriptionNotCorrect(@NonNull String description) {
        return description.length() > MAX_CHAR_STORAGE_LOCATION_DESCRIPTION;
    }

    public void setDatabaseMode(String databaseMode) {
        this.databaseMode = databaseMode;
    }

    public String getDatabaseMode() {
        return databaseMode;
    }
}