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

import android.graphics.Bitmap;

import androidx.appcompat.app.AppCompatActivity;

import com.hermanowicz.pantry.interfaces.PrintQRCodesView;
import com.hermanowicz.pantry.models.PrintQRCodesModel;

import java.util.ArrayList;

public class PrintQRCodesPresenter {

    private final PrintQRCodesView view;
    private final PrintQRCodesModel model= new PrintQRCodesModel();

    private ArrayList<String> textToQRCodeArray, namesOfProductsArray, expirationDatesArray;

    public PrintQRCodesPresenter(PrintQRCodesView view) {
        this.view = view;
    }

    private boolean createAndSavePDF() {
        boolean result = model.checkWritePermission();
        model.setTextToQRCodeArray(textToQRCodeArray);
        model.setNamesOfProductsArray(namesOfProductsArray);
        model.setExpirationDatesArray(expirationDatesArray);
        if (result) {
            model.createAndSavePDF();
        } else {
            view.showPermissionsError();
            model.requestWritePermission();
        }
        return result;
    }

    public void setTextToQRCodeArray(ArrayList<String> textToQRCodeArray) {
        this.textToQRCodeArray = textToQRCodeArray;
    }

    public void setNamesOfProductsArray(ArrayList<String> namesOfProductsArray) {
        this.namesOfProductsArray = namesOfProductsArray;
    }

    public void setExpirationDatesArray(ArrayList<String> expirationDatesArray) {
        this.expirationDatesArray = expirationDatesArray;
    }

    public void setActivity(AppCompatActivity activity) {
        model.setActivity(activity);
    }

    public void showQRCodeImage() {
        Bitmap qrCodeImage = model.generateQRCode(textToQRCodeArray.get(0));
        view.showQRCodeImage(qrCodeImage);
    }

    public void onClickPrintQRCodes() {
        if (createAndSavePDF())
            view.openPDF();
    }

    public void onClickSendPdfByEmail() {
        if (createAndSavePDF())
            view.sendPDFByEmail();
    }

    public void onClickSkipButton() {
        view.navigateToMainActivity();
    }

    public void navigateToMainActivity() {
        view.navigateToMainActivity();
    }
}
