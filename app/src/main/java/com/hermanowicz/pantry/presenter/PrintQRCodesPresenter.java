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
import com.hermanowicz.pantry.interfaces.PrintQRCodesView;
import com.hermanowicz.pantry.model.PrintQRCodesModel;

import java.util.List;

public class PrintQRCodesPresenter {

    private final PrintQRCodesView view;
    private final PrintQRCodesModel model = new PrintQRCodesModel();

    public PrintQRCodesPresenter(@NonNull PrintQRCodesView view) {
        this.view = view;
    }

    private boolean createAndSavePDF() {
        boolean result = model.isWritePermission();
        if (result)
            model.createAndSavePDF();
        else
            model.requestWritePermission();
        return result;
    }

    public void setProductList(@NonNull List<Product> productList){
        model.setProductList(productList);
    }

    public void setActivity(@NonNull AppCompatActivity activity) {
        model.setActivity(activity);
    }

    public void showQRCodeImage() {
        Bitmap qrCodeImage = model.getThumb();
        view.showQRCodeImage(qrCodeImage);
    }

    public void onClickPrintQRCodes() {
        if (createAndSavePDF())
            view.openPDF(model.getPdfFileName());
    }

    public void onClickAddPhoto(@NonNull List<Product> productList) {
        view.navigateToAddPhoto(productList);
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
}
