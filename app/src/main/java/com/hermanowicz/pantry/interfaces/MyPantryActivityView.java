/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry.interfaces;

public interface MyPantryActivityView {
    void openDialog(String typeOfDialog);

    void setFilterIcon(int position);

    void clearFilterIcon(int position);
    void showEmptyPantryStatement();
    void clearFilterIcons();

    void initData(String pantryQuery);

    void refreshListView(String pantryQuery);
    void navigateToMainActivity();
}
