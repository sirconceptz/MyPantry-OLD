package com.hermanowicz.pantry.presenters;

import androidx.annotation.NonNull;

import com.hermanowicz.pantry.db.StorageLocation;
import com.hermanowicz.pantry.interfaces.StorageLocationView;
import com.hermanowicz.pantry.models.StorageLocationModel;

import java.util.List;

public class StorageLocationPresenter {

    private final StorageLocationView view;
    private final StorageLocationModel model;

    public StorageLocationPresenter(@NonNull StorageLocationView view, @NonNull StorageLocationModel model){
        this.view = view;
        this.model = model;
    }

    public void updateStorageLocationList(){
        List<StorageLocation> storageLocationList = model.getStorageLocationList();
        view.updateStorageLocationList(storageLocationList);
        view.showEmptyStorageLocationListStatement(storageLocationList.size() == 0);
    }

    public void addStorageLocation(StorageLocation storageLocation) {
       if(model.isStorageLocationNameNotCorrect(storageLocation.getName()) || model.isStorageLocationDescriptionNotCorrect(storageLocation.getDescription()))
           view.onErrorAddNewStorageLocation();
        else if(model.addStorageLocation(storageLocation)) {
            view.onSuccessAddNewStorageLocation();
            view.updateStorageLocationList(model.getStorageLocationList());
            view.showEmptyStorageLocationListStatement(model.getStorageLocationList().size() == 0);
        }
        else
            view.onErrorAddNewStorageLocation();
    }

    public List<StorageLocation> getStorageLocationList() {
        return model.getStorageLocationList();
    }

    public void navigateToMainActivity() {
        view.navigateToMainActivity();
    }
}