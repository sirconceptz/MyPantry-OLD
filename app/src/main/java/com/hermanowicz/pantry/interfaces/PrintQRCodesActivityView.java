/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry.interfaces;

import android.graphics.Bitmap;

public interface PrintQRCodesActivityView {
    void showPermissionsError();

    void showQRCodeImage(Bitmap qrCodeImage);

    void openPDF();

    void sendPDFByEmail();

    void navigateToNewProductActivity();

    void navigateToMainActivity();
}
