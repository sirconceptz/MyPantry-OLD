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

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.hermanowicz.pantry.db.product.Product;
import com.hermanowicz.pantry.db.product.ProductDb;
import com.hermanowicz.pantry.interfaces.PrintQRCodesView;
import com.hermanowicz.pantry.model.AppSettingsModel;
import com.hermanowicz.pantry.model.PrintQRCodesModel;
import com.hermanowicz.pantry.util.PremiumAccess;

import java.util.List;

/**
 * <h1>PrintQRCodesPresenter</h1>
 * Presenter for PrintQRCodesActivity
 *
 * @author  Mateusz Hermanowicz
 */

public class PrintQRCodesPresenter {

    private final PrintQRCodesView view;
    private final PrintQRCodesModel model;
    private final AppSettingsModel appSettingsModel;
    private final AppCompatActivity activity;
    private PremiumAccess premiumAccess;

    public PrintQRCodesPresenter(@NonNull PrintQRCodesView view,
                                 @NonNull AppSettingsModel appSettingsModel,
                                 @NonNull AppCompatActivity activity) {
        this.view = view;
        this.activity = activity;
        this.model = new PrintQRCodesModel(activity);
        this.appSettingsModel = appSettingsModel;
    }

    public void setPremiumAccess(@NonNull PremiumAccess premiumAccess){
        this.premiumAccess = premiumAccess;
    }

    private boolean createAndSavePDF() {
        boolean result = model.isWritePermission();
        if (result)
            model.createAndSavePDF();
        else
            model.requestWritePermission();
        return result;
    }

    public void showQRCodeImage() {
        Bitmap qrCodeImage = model.getThumb();
        view.showQRCodeImage(qrCodeImage);
    }

    public void onClickPrintQRCodes() {
        if (createAndSavePDF())
            view.openPDF(model.getPdfFileName());
    }

    public void onClickSendPdfByEmail() {
        if (createAndSavePDF())
            view.sendPDFByEmail(model.getPdfFileName());
    }

    public void onClickSkipButton() {
        view.navigateToMainActivity();
    }

    public void navigateToMainActivity() {
        view.navigateToMainActivity();
    }

    public boolean isPremium(){
        return premiumAccess.isPremium();
    }

    public boolean isOfflineDb() {
        return appSettingsModel.getDatabaseMode().equals("local");
    }

    public void setAllProductList(List<Product> productList) {
        if(isOfflineDb()) {
            ProductDb db = ProductDb.getInstance(activity);
            productList = db.productsDao().getAllProductsList();
        }
        model.setAllProductList(productList);
    }

    public void setProductList(List<Product> productList) {
        model.setProductList(productList);
    }
}
