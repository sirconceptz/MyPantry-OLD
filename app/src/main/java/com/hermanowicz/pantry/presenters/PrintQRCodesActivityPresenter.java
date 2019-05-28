/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry.presenters;

import android.graphics.Bitmap;

import androidx.appcompat.app.AppCompatActivity;

import com.hermanowicz.pantry.interfaces.IPrintQRCodesActivityPresenter;
import com.hermanowicz.pantry.interfaces.IPrintQRCodesActivityView;
import com.hermanowicz.pantry.models.PrintQRCodesActivityModel;

import java.util.ArrayList;

public class PrintQRCodesActivityPresenter implements IPrintQRCodesActivityPresenter {

    private IPrintQRCodesActivityView view;
    private PrintQRCodesActivityModel model;

    private ArrayList<String> textToQRCodeArray, namesOfProductsArray, expirationDatesArray;

    public PrintQRCodesActivityPresenter(IPrintQRCodesActivityView view, PrintQRCodesActivityModel model) {
        this.view = view;
        this.model = model;
    }

    public void onDestroy() {
        view = null;
        model = null;
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

    @Override
    public void setTextToQRCodeArray(ArrayList<String> textToQRCodeArray) {
        this.textToQRCodeArray = textToQRCodeArray;
    }

    @Override
    public void setNamesOfProductsArray(ArrayList<String> namesOfProductsArray) {
        this.namesOfProductsArray = namesOfProductsArray;
    }

    @Override
    public void setExpirationDatesArray(ArrayList<String> expirationDatesArray) {
        this.expirationDatesArray = expirationDatesArray;
    }

    @Override
    public void setActivity(AppCompatActivity activity) {
        model.setActivity(activity);
    }

    @Override
    public void showQRCodeImage() {
        Bitmap qrCodeImage = model.generateQRCode(textToQRCodeArray.get(0));
        view.showQRCodeImage(qrCodeImage);
    }

    @Override
    public void onClickPrintQRCodes() {
        if (createAndSavePDF())
            view.openPDF();
    }

    @Override
    public void onClickSendPdfByEmail() {
        if (createAndSavePDF())
            view.sendPDFByEmail();
    }

    @Override
    public void onClickSkipButton() {
        view.navigateToNewProductActivity();
    }

    @Override
    public void navigateToMainActivity() {
        view.navigateToMainActivity();
    }
}
