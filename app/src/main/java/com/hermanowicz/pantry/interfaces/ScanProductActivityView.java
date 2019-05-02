/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry.interfaces;

import java.util.List;

public interface ScanProductActivityView {
    void navigateToProductDetailsActivity(List<Integer> decodedScanResultAsList);

    void showErrorProductNotFound();

    void navigateToMainActivity();
}
