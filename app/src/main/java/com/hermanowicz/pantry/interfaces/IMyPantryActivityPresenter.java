/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry.interfaces;

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

    void initRecyclerViewData();
    void navigateToMainActivity();
}
