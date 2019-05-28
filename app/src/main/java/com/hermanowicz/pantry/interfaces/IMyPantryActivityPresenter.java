/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry.interfaces;

import androidx.lifecycle.LiveData;

import com.hermanowicz.pantry.db.Product;

import java.util.List;

public interface IMyPantryActivityPresenter {
    String getFilterName();

    String getFilterExpirationDateSince();

    String getFilterExpirationDateFor();

    String getFilterProductionDateSince();

    String getFilterProductionDateFor();

    String getFilterTypeOfProduct();

    String getFilterProductFeatures();

    int getFilterVolumeSince();

    int getFilterVolumeFor();

    int getFilterWeightSince();

    int getFilterWeightFor();

    int getFilterHasSugar();

    int getFilterHasSalt();

    String getFilterTaste();

    void clearFilters();

    void openDialog(String typeOfDialog);

    void setProductLiveData(LiveData<List<Product>> productLiveData);

    LiveData<List<Product>> getProductLiveData();

    void setProductList(List<Product> productList);

    void navigateToMainActivity();
}
