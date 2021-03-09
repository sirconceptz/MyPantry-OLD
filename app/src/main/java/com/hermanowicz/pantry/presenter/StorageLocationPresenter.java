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
import com.hermanowicz.pantry.interfaces.StorageLocationView;
import com.hermanowicz.pantry.model.StorageLocationModel;

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