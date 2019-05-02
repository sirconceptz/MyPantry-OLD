/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry.interfaces;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.WriterException;

import java.util.ArrayList;

public interface IPrintQRCodesActivityPresenter {
    void setTextToQRCodeArray(ArrayList<String> textToQRCodeArray);

    void setNamesOfProductsArray(ArrayList<String> namesOfProductsArray);

    void setExpirationDatesArray(ArrayList<String> expirationDatesArray);

    void setActivity(AppCompatActivity activity);

    void showQRCodeImage() throws WriterException;

    void onClickPrintQRCodes() throws WriterException;

    void onClickSendPdfByEmail();

    void onClickSkipButton();

    void navigateToMainActivity();
}
