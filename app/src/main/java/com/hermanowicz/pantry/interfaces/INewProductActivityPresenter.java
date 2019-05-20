/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry.interfaces;

public interface INewProductActivityPresenter {
    void setName(String name);

    void setTypeOfProduct(String typeOfProduct);

    void setProductFeatures(String productFeatures);

    void setExpirationDate(String expirationDate);

    void setProductionDate(String productionDate);

    void showExpirationDate(int day, int month, int year);

    void showProductionDate(int day, int month, int year);

    void setComposition(String composition);

    void setHealingProperties(String healingProperties);

    void setDosage(String dosage);

    void setHasSugar(boolean hasSugar);

    void setHasSalt(boolean hasSalt);

    void setIsSweet(boolean isSweet);

    void setIsSour(boolean isSour);

    void setIsSweetAndSour(boolean isSweetAndSour);

    void setIsBitter(boolean isBitter);

    void setIsSalty(boolean isSalty);

    void parseQuantity(String quantity);

    void parseVolume(String volume);

    void parseWeight(String weight);

    void setIdOfLastProductInDb(int idOfLastProductInDB);

    void addProducts();

    void updateProductFeaturesAdapter(String typeOfProductSpinnerValue);

    String[] getExpirationDateArray();

    String[] getProductionDateArray();
    void navigateToMainActivity();
}