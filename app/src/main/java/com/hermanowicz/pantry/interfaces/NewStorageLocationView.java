package com.hermanowicz.pantry.interfaces;

import com.hermanowicz.pantry.db.StorageLocation;

public interface NewStorageLocationView {
    void onAddStorageLocation(StorageLocation storageLocation);

    void updateNameCharCounter(int charCounter, int maxChar);

    void updateDescriptionCharCounter(int charCounter, int maxChar);

    void showNameFieldError();

    void showDescriptionFieldError();
}