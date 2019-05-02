/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry.interfaces;

public interface IMainActivityPresenter {
    void navigateToMyPantryActivity();

    void navigateToScanProductActivity();

    void navigateToNewProductActivity();

    void navigateToAppSettingsActivity();
}
