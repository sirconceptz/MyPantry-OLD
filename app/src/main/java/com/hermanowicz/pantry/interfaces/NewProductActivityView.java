/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry.interfaces;

import com.hermanowicz.pantry.models.ProductEntity;

import java.util.ArrayList;

public interface NewProductActivityView {
    void navigateToPrintQRCodesActivity(ArrayList<String> textToQRCodeList, ArrayList<String> namesOfProductsList, ArrayList<String> expirationDatesList);

    void isAddProductsSuccess(ArrayList<ProductEntity> productEntityArrayList);

    void updateProductFeaturesAdapter(String typeOfProductSpinnerValue);

    void showStatementOnAreProductsAdded(String statementToShow);

    void showExpirationDate(int day, int month, int year);

    void showProductionDate(int day, int month, int year);

    void showErrorNameNotSet();

    void showErrorCategoryNotSelected();

    void showErrorExpirationDateNotSet();

    void navigateToMainActivity();
}