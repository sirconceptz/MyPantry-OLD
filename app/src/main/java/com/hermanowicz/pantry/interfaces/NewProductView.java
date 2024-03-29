/*
 * Copyright (c) 2019-2022
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

import com.hermanowicz.pantry.db.product.Product;

import java.util.List;

public interface NewProductView {
    void navigateToPrintQRCodesActivity(List<Product> productList, List<Product> allProductList);

    void onClickAddProduct();

    void reCreateNotifications();

    void updateProductFeaturesAdapter(String typeOfProductSpinnerValue);

    void updateProductCategoryAdapter(String[] categoryArray);

    void updateStorageLocationAdapter(String[] storageLocationArray);

    void showStatementOnAreProductsAdded(String statementToShow);

    void showExpirationDate(String date);

    void showProductionDate(String date);

    void showErrorNameNotSet();

    void showErrorCategoryNotSelected();

    void navigateToMainActivity();

    void showCancelProductAddDialog();

    boolean isFormNotFilled();

    void setProductData(Product product);

    void chooseProductToCopy(String[] namesProductList);
}