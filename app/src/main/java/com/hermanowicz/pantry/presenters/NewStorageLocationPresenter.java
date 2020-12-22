package com.hermanowicz.pantry.presenters;

import androidx.annotation.NonNull;

import com.hermanowicz.pantry.db.StorageLocation;
import com.hermanowicz.pantry.interfaces.NewStorageLocationView;
import com.hermanowicz.pantry.models.StorageLocationModel;

public class NewStorageLocationPresenter {

    private final NewStorageLocationView view;
    private final StorageLocationModel model;

    public NewStorageLocationPresenter(@NonNull NewStorageLocationView view, @NonNull StorageLocationModel model){
        this.view = view;
        this.model = model;
    }

    public void initCharCounters(){
        view.updateNameCharCounter(0, model.MAX_CHAR_STORAGE_LOCATION_NAME);
        view.updateDescriptionCharCounter(0, model.MAX_CHAR_STORAGE_LOCATION_DESCRIPTION);
    }

    public void isStorageLocationNameCorrect(@NonNull String storageLocationName){
        if(model.isStorageLocationNameNotCorrect(storageLocationName))
            view.showNameFieldError();
        view.updateNameCharCounter(storageLocationName.length(), model.MAX_CHAR_STORAGE_LOCATION_NAME);
    }

    public void isStorageLocationDescriptionCorrect(@NonNull String storageLocationDescription){
        if(model.isStorageLocationDescriptionNotCorrect(storageLocationDescription))
            view.showDescriptionFieldError();
        view.updateDescriptionCharCounter(storageLocationDescription.length(), model.MAX_CHAR_STORAGE_LOCATION_DESCRIPTION);
    }

    public void onPressAddNewStorageLocation(@NonNull StorageLocation storageLocation) {
        if(model.isStorageLocationNameNotCorrect(storageLocation.getName()))
            view.showNameFieldError();
        else if(model.isStorageLocationDescriptionNotCorrect(storageLocation.getDescription()))
            view.showDescriptionFieldError();
        else
            view.onAddStorageLocation(storageLocation);
    }
}