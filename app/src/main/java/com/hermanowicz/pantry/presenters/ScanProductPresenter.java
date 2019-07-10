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

import com.hermanowicz.pantry.interfaces.ScanProductView;
import com.hermanowicz.pantry.models.ScanProductModel;

import java.util.List;

public class ScanProductPresenter {

    private ScanProductView view;
    private ScanProductModel model = new ScanProductModel();

    public ScanProductPresenter(ScanProductView view) {
        this.view = view;
    }

    public void onScanResult(String scanResult) {
        List<Integer> decodedQRCodeAsList = model.decodeScanResult(scanResult);
        if (decodedQRCodeAsList.size() > 1)
            view.navigateToProductDetailsActivity(decodedQRCodeAsList);
        else {
            view.showErrorProductNotFound();
            view.navigateToMainActivity();
        }
    }

    public void showErrorProductNotFound() {
        view.showErrorProductNotFound();
    }

    public void navigateToMainActivity() {
        view.navigateToMainActivity();
    }
}