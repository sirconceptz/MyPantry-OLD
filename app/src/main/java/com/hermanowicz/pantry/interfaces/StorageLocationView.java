package com.hermanowicz.pantry.interfaces;

import com.hermanowicz.pantry.db.StorageLocation;

import java.util.List;

public interface StorageLocationView {
    void showEmptyStorageLocationListStatement(boolean visible);

    void updateStorageLocationList(List<StorageLocation> storageLocationList);

    void onSuccessAddNewStorageLocation();

    void onErrorAddNewStorageLocation();

    void navigateToMainActivity();
}