package com.hermanowicz.pantry.interfaces;

import com.hermanowicz.pantry.db.StorageLocation;

public interface StorageLocationDetailsView {
    void showErrorOnUpdateStorageLocation();

    void showStorageLocationUpdated();

    void showStorageLocationDetails(StorageLocation storageLocation);

    void showStorageLocationNameError();

    void showStorageLocationDescriptionError();

    void updateNameCharCounter(int charCounter, int maxChar);

    void updateDescriptionCharCounter(int charCounter, int maxChar);

    void navigateToStorageLocationActivity();
}