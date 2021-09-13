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
import android.content.res.Resources;

import androidx.annotation.NonNull;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.db.product.Product;
import com.hermanowicz.pantry.db.product.ProductDb;
import com.hermanowicz.pantry.interfaces.ScanProductView;
import com.hermanowicz.pantry.model.AppSettingsModel;
import com.hermanowicz.pantry.model.GroupProducts;
import com.hermanowicz.pantry.model.ScanProductModel;

import java.util.List;

/**
 * <h1>ScanProductPresenter</h1>
 * Presenter for ScanProductActivity
 *
 * @author  Mateusz Hermanowicz
 */

public class ScanProductPresenter {

    private final ScanProductView view;
    private final ScanProductModel model;
    private final AppSettingsModel appSettingsModel;

    public ScanProductPresenter(@NonNull ScanProductView view, @NonNull SharedPreferences sharedPreferences, @NonNull ProductDb productDb) {
        this.view = view;
        this.model = new ScanProductModel(productDb);
        this.appSettingsModel = new AppSettingsModel(sharedPreferences);
        model.setDatabaseMode(appSettingsModel.getDatabaseMode());
    }

    public void onScanResult(@NonNull String scanResult) {
        model.setScanResult(scanResult);
        if(model.isBarcodeScan()){
            List<Product> productList = model.getProductListWithBarcode(scanResult);
            view.navigateToNewProductActivity(scanResult, productList);
        }
        else {
            List<Integer> decodedQRCodeAsList = model.decodeScanResult(scanResult);
            if (decodedQRCodeAsList.size() > 1) {
                if (appSettingsModel.getScannerVibrationMode())
                    view.onVibration();
                view.navigateToProductDetailsActivity(decodedQRCodeAsList);
            } else {
                view.showErrorProductNotFound();
                view.navigateToMainActivity();
            }
        }
    }

    public void initScanner(boolean isBarcodeScan, Resources resources){
        model.setIsBarcodeScan(isBarcodeScan);
        String message;
        if(isBarcodeScan) {
            message = resources.getString(R.string.ScanProductActivity_scan_barcode);
        }
        else
            message = resources.getString(R.string.ScanProductActivity_scan_qr_code);
        view.setScanner(appSettingsModel.getScannerSoundMode(), message);
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

    public void setSelectedProductToCopy(int position) {
        List<Product> productList = model.getProductListWithBarcode(model.getScanResult());
        List<GroupProducts> groupProductList = GroupProducts.getGroupProducts(productList);
        GroupProducts groupProduct = groupProductList.get(position);
        Product product = groupProduct.getProduct();
        view.navigateToNewProductActivity(product);
    }

    public void enterBarcodeManually() {
        view.onSelectedEnterBarcodeManually();
    }

    public void onSetBarcodeManually(String barcode) {
        model.setIsBarcodeScan(true);
        onScanResult(barcode);
    }

    public void setProductList(List<Product> productList) {
        model.setProductList(productList);
    }

    public boolean isOfflineDb() {
        return model.getDatabaseMode().equals("local");
    }

    public void setOfflineAllProductList() {
        model.setOfflineAllProductList();
    }
}