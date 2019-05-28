/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry.interfaces;

import com.hermanowicz.pantry.db.Product;

import java.util.ArrayList;

public interface IProductDetailsActivityView {
    void showProductDetails(Product product);

    void showErrorWrongData();

    void onDeletedProduct(int productID);

    void onPrintQRCode(ArrayList<String> textToQRCodeArray, ArrayList<String> namesOfProductsArray, ArrayList<String> expirationDatesArray);

    void navigateToMyPantryActivity();
}