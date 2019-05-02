/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry.interfaces;

import java.util.ArrayList;

public interface NewProductActivityView {
    void onAddProduct(ArrayList<String> textToQRCodeList, ArrayList<String> namesOfProductsList, ArrayList<String> expirationDatesList);

    void onIsProductsAdded(String statementToShow);

    void navigateToMainActivity();
}