package com.hermanowicz.pantry.presenters;

import androidx.annotation.NonNull;

import com.hermanowicz.pantry.db.StorageLocation;
import com.hermanowicz.pantry.interfaces.StorageLocationDetailsView;
import com.hermanowicz.pantry.models.StorageLocationModel;

public class StorageLocationsDetailsPresenter {

    private final StorageLocationModel model;
    private final StorageLocationDetailsView view;

    public StorageLocationsDetailsPresenter(@NonNull StorageLocationDetailsView view, @NonNull StorageLocationModel model) {
        this.model = model;
        this.view = view;
    }

    public void setStorageLocationId(int id) {
        StorageLocation storageLocation = model.getStorageLocation(id);
        view.showStorageLocationDetails(storageLocation);
    }

    public StorageLocation getStorageLocation(int id){
        return model.getStorageLocation(id);
    }

    public void deleteStorageLocation(int id) {
        model.deleteStorageLocation(id);
        view.navigateToStorageLocationActivity();
    }

    public void updateStorageLocation(@NonNull StorageLocation storageLocation) {
        if(model.isStorageLocationNameNotCorrect(storageLocation.getName()) || model.isStorageLocationDescriptionNotCorrect(storageLocation.getDescription()))
            view.showErrorOnUpdateStorageLocation();
        else {
            model.updateStorageLocation(storageLocation);
            view.showStorageLocationUpdated();
            view.navigateToStorageLocationActivity();
        }
    }

    public void isStorageLocationNameCorrect(@NonNull String storageLocationName) {
        if(model.isStorageLocationNameNotCorrect(storageLocationName))
            view.showStorageLocationNameError();
        view.updateNameCharCounter(storageLocationName.length(), model.MAX_CHAR_STORAGE_LOCATION_NAME);
    }

    public void isStorageLocationDescriptionCorrect(@NonNull String storageLocationDescription) {
        if(model.isStorageLocationDescriptionNotCorrect(storageLocationDescription))
            view.showStorageLocationDescriptionError();
        view.updateDescriptionCharCounter(storageLocationDescription.length(), model.MAX_CHAR_STORAGE_LOCATION_DESCRIPTION);
    }

    public void navigateToStorageLocationActivity() {
        view.navigateToStorageLocationActivity();
    }
}