/*
 * Copyright (c) 2019
 * Mateusz Hermanowicz - All rights reserved.
 * My Pantry
 * https://www.mypantry.eu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

    void addProducts();

    void updateProductFeaturesAdapter(String typeOfProductSpinnerValue);

    String[] getExpirationDateArray();

    String[] getProductionDateArray();

    void navigateToMainActivity();
}