/*
 * Copyright (c) 2019-2022
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

package com.hermanowicz.pantry.presenter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import com.hermanowicz.pantry.db.storagelocation.StorageLocation;
import com.hermanowicz.pantry.interfaces.StorageLocationDetailsView;
import com.hermanowicz.pantry.model.AppSettingsModel;
import com.hermanowicz.pantry.model.StorageLocationModel;

import java.util.List;

/**
 * <h1>StorageLocationDetailsPresenter</h1>
 * Presenter for StorageLocationDetailsActivity
 *
 * @author Mateusz Hermanowicz
 */

public class StorageLocationDetailsPresenter {

    private final StorageLocationModel model;
    private final StorageLocationDetailsView view;

    public StorageLocationDetailsPresenter(@NonNull StorageLocationDetailsView view,
                                           @NonNull Context context) {
        this.model = new StorageLocationModel(context);
        this.view = view;
        AppSettingsModel appSettingsModel = new AppSettingsModel(PreferenceManager.
                getDefaultSharedPreferences(context));
        model.setDatabaseMode(appSettingsModel.getDatabaseMode());
    }

    public StorageLocation getStorageLocation(){
        return model.getStorageLocation();
    }

    public void setStorageLocation(@NonNull StorageLocation storageLocation){
        model.setStorageLocation(storageLocation);
        view.showStorageLocationDetails(storageLocation);
    }

    public void deleteStorageLocation() {
        if(model.getDatabaseMode().equals("local"))
            model.deleteOfflineDbStorageLocation(model.getStorageLocation());
        else
            model.deleteOnlineStorageLocation(model.getStorageLocation());
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
    }

    public void isStorageLocationDescriptionCorrect(@NonNull String storageLocationDescription) {
        if(model.isStorageLocationDescriptionNotCorrect(storageLocationDescription))
            view.showStorageLocationDescriptionError();
    }

    public void onResponse(List<StorageLocation> storageLocationList) {
        model.setStorageLocation(storageLocationList.get(0));
    }
}