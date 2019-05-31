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

    List<Product> getProductList();

    void clearSelectList();

    List<Product> getSelectList();

    void setIsMultiSelect(boolean state);

    boolean getIsMultiSelect();

    void addMultiSelectProduct(int position);

    void deleteSelectedProducts();

    void printSelectedProducts();

    void navigateToMainActivity();
}
