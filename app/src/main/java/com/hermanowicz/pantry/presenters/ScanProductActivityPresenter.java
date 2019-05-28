/*
 * Copyright (c) 2019
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

package com.hermanowicz.pantry.presenters;

import com.hermanowicz.pantry.interfaces.IScanProductActivityPresenter;
import com.hermanowicz.pantry.interfaces.IScanProductActivityView;
import com.hermanowicz.pantry.models.ScanProductActivityModel;

import java.util.List;

public class ScanProductActivityPresenter implements IScanProductActivityPresenter {

    private IScanProductActivityView view;
    private ScanProductActivityModel model;

    public ScanProductActivityPresenter(IScanProductActivityView view, ScanProductActivityModel model) {
        this.view = view;
        this.model = model;
    }

    public void onDestroy() {
        view = null;
        model = null;
    }

    @Override
    public void onScanResult(String scanResult) {
        List<Integer> decodedQRCodeAsList = model.decodeScanResult(scanResult);
        if (decodedQRCodeAsList == null)
            view.navigateToProductDetailsActivity(decodedQRCodeAsList);
        else {
            view.showErrorProductNotFound();
            view.navigateToMainActivity();
        }
    }

    @Override
    public void showErrorProductNotFound() {
        view.showErrorProductNotFound();
    }


    @Override
    public void navigateToMainActivity() {
        view.navigateToMainActivity();
    }
}