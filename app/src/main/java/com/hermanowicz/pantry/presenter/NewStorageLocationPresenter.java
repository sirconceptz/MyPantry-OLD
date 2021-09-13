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

/**
 * <h1>NewStorageLocationPresenter</h1>
 * Presenter for NewStorageLocationDialog
 *
 * @author  Mateusz Hermanowicz
 */

public class NewStorageLocationPresenter {

    private final NewStorageLocationView view;
    private final StorageLocationModel model;

    public NewStorageLocationPresenter(@NonNull NewStorageLocationView view, @NonNull StorageLocationModel model){
        this.view = view;
        this.model = model;
    }

    public void isStorageLocationNameCorrect(@NonNull String storageLocationName){
        if(model.isStorageLocationNameNotCorrect(storageLocationName))
            view.showNameFieldError();
    }

    public void isStorageLocationDescriptionCorrect(@NonNull String storageLocationDescription){
        if(model.isStorageLocationDescriptionNotCorrect(storageLocationDescription))
            view.showDescriptionFieldError();
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