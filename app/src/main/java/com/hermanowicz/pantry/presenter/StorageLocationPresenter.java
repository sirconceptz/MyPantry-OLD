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

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import com.hermanowicz.pantry.db.storagelocation.StorageLocation;
import com.hermanowicz.pantry.interfaces.StorageLocationDbResponse;
import com.hermanowicz.pantry.interfaces.StorageLocationView;
import com.hermanowicz.pantry.model.AppSettingsModel;
import com.hermanowicz.pantry.model.StorageLocationModel;
import com.hermanowicz.pantry.util.PremiumAccess;

import java.util.List;

/**
 * <h1>StorageLocationPresenter</h1>
 * Presenter for StorageLocationActivity
 *
 * @author  Mateusz Hermanowicz
 */

public class StorageLocationPresenter implements StorageLocationDbResponse {

    private final StorageLocationView view;
    private final StorageLocationModel model;
    private final PremiumAccess premiumAccess;
    private final AppSettingsModel appSettingsModel;
    private final StorageLocationDbResponse dbResponse;

    public StorageLocationPresenter (@NonNull StorageLocationView view, @NonNull Context context,
                                     @NonNull StorageLocationDbResponse dbResponse){
        this.view = view;
        this.model = new StorageLocationModel(context);
        this.appSettingsModel = new AppSettingsModel(PreferenceManager.
                getDefaultSharedPreferences(context));
        this.premiumAccess = new PremiumAccess(context);
        this.dbResponse = dbResponse;

        model.setDatabaseMode(getDatabaseMode());
        if(getDatabaseMode().equals("local")){
            model.setOfflineDbStorageLocationList();
        }
    }

    public String getDatabaseMode(){
        return appSettingsModel.getDatabaseMode();
    }

    public void updateStorageLocationListView(){
        List<StorageLocation> storageLocationList = model.getStorageLocationList();
        view.updateStorageLocationViewAdapter(storageLocationList);
        view.showEmptyStorageLocationListStatement(storageLocationList.size() == 0);
    }

    public void addStorageLocation(@NonNull StorageLocation storageLocation) {
        if(model.addStorageLocation(storageLocation)) {
            view.onSuccessAddNewStorageLocation();
            view.setOnlineDbStorageLocationList(dbResponse);
            if(getDatabaseMode().equals("local")){
                model.setOfflineDbStorageLocationList();
            }
            updateStorageLocationListView();
        }
        else
            view.showErrorAddNewStorageLocation();
    }

    public void navigateToMainActivity() {
        view.navigateToMainActivity();
    }

    public boolean isPremium() {
        return premiumAccess.isPremium();
    }

    public List<StorageLocation> getStorageLocationList() {
        return model.getStorageLocationList();
    }

    public void setOnlineDbStorageLocationList(@NonNull List<StorageLocation> storageLocationList) {
        if (getDatabaseMode().equals("online")) {
            model.setStorageLocationList(storageLocationList);
            updateStorageLocationListView();
        }
    }

    @Override
    public void onResponse(List<StorageLocation> storageLocationList) {
        model.setStorageLocationList(storageLocationList);
    }
}