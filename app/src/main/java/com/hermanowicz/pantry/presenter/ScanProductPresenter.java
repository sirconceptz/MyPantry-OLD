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

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.hermanowicz.pantry.interfaces.ScanProductView;
import com.hermanowicz.pantry.model.AppSettingsModel;
import com.hermanowicz.pantry.model.ScanProductModel;

import java.util.List;

public class ScanProductPresenter {

    private final ScanProductView view;
    private final ScanProductModel model = new ScanProductModel();
    private final AppSettingsModel appSettingsModel;

    public ScanProductPresenter(@NonNull ScanProductView view, @NonNull SharedPreferences sharedPreferences) {
        this.view = view;
        this.appSettingsModel = new AppSettingsModel(sharedPreferences);
    }

    public void onScanResult(@NonNull String scanResult) {
        List<Integer> decodedQRCodeAsList = model.decodeScanResult(scanResult);
        if (decodedQRCodeAsList.size() > 1) {
            if (appSettingsModel.getScannerVibrationMode())
                view.onVibration();
            view.navigateToProductDetailsActivity(decodedQRCodeAsList);
        }
        else {
            view.showErrorProductNotFound();
            view.navigateToMainActivity();
        }
    }

    public void initQrScanner(){
        view.setQrScanner(appSettingsModel.getScannerSoundMode());
    }

    public int getSelectedCamera(){
        return appSettingsModel.getSelectedScanCamera();
    }

    public void showErrorProductNotFound() {
        view.showErrorProductNotFound();
    }

    public void navigateToMainActivity() {
        view.navigateToMainActivity();
    }
}