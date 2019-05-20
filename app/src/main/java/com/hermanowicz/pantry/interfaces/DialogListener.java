/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry.interfaces;

/**
 * <h1>DialogListener</h1>
 * Interface to set values from dialog windows.
 *
 * @author Mateusz Hermanowicz
 * @version 1.0
 * @since 1.0
 */
public interface DialogListener {
    void setFilterName(String filterName);

    void setFilterExpirationDate(String filterExpirationDateSince, String filterExpirationDateFor);

    void setFilterProductionDate(String filterProductionDateSince, String filterProductionDateFor);

    void setFilterTypeOfProduct(String filterTypeOfProduct, String filterProductFeatures);

    void setFilterVolume(int filterVolumeSince, int filterVolumeFor);

    void setFilterWeight(int filterWeightSince, int filterWeightFor);

    void setFilterTaste(String filterTaste);

    void setProductFeatures(int filterHasSugar, int filterHasSalt);

    void clearFilterName();

    void clearFilterExpirationDate();

    void clearFilterProductionDate();

    void clearFilterTypeOfProduct();

    void clearFilterVolume();

    void clearFilterWeight();

    void clearFilterTaste();

    void clearProductFeatures();
}