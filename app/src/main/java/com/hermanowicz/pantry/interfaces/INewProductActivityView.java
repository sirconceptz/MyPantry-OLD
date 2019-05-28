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
import java.util.List;

public interface INewProductActivityView {
    void navigateToPrintQRCodesActivity(ArrayList<String> textToQRCodeList, ArrayList<String> namesOfProductsList, ArrayList<String> expirationDatesList);

    void isAddProductsSuccess(List<Product> productArrayList);

    void updateProductFeaturesAdapter(String typeOfProductSpinnerValue);

    void showStatementOnAreProductsAdded(String statementToShow);

    void showExpirationDate(int day, int month, int year);

    void showProductionDate(int day, int month, int year);

    void showErrorNameNotSet();

    void showErrorCategoryNotSelected();

    void navigateToMainActivity();
}