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

public interface IMyPantryActivityView {
    void openDialog(String typeOfDialog);

    void setFilterIcon(int position);

    void clearFilterIcon(int position);

    void showEmptyPantryStatement();

    void clearFilterIcons();

    void navigateToMainActivity();

    void updateRecyclerViewAdapter();

    LiveData<List<Product>> getProductLiveData();
}
