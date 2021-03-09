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

package com.hermanowicz.pantry.presenter;

import androidx.annotation.NonNull;

import com.hermanowicz.pantry.db.storagelocation.StorageLocation;
import com.hermanowicz.pantry.interfaces.NewStorageLocationView;
import com.hermanowicz.pantry.model.StorageLocationModel;

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