/*
 * Copyright (c) 2019-2021
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

package com.hermanowicz.pantry.presenter;

import androidx.annotation.NonNull;

import com.hermanowicz.pantry.db.product.Product;
import com.hermanowicz.pantry.interfaces.ProductDetailsView;
import com.hermanowicz.pantry.model.GroupProducts;
import com.hermanowicz.pantry.model.ProductDataModel;

import java.util.List;

public class ProductDetailsPresenter {

    private final ProductDetailsView view;
    private final ProductDataModel model;

    public ProductDetailsPresenter(@NonNull ProductDetailsView view, @NonNull ProductDataModel productDataModel) {
        this.view = view;
        this.model = productDataModel;
    }

    public void setProductId(int productId) {
        model.setProductId(productId);
    }

    public void showProductDetails(@NonNull String hashCode) {
        if (model.isProductListEmpty()) {
            view.showErrorWrongData();
            view.navigateToMyPantryActivity();
        } else if(model.isCorrectHashCode(hashCode)) {
            GroupProducts groupProducts = model.getGroupProducts();
            view.showProductDetails(groupProducts);
        } else {
            view.showErrorWrongData();
            view.navigateToMyPantryActivity();
        }
    }

    public void deleteProduct(int productId) {
        model.deleteSimilarProducts(productId);
        view.onDeletedProduct();
        view.navigateToMyPantryActivity();
    }

    public void printQRCode() {
        List<Product> productList = model.getProductList();

        view.onPrintQRCode(productList);
    }

    public void editProduct(int productId){
        view.navigateToEditProductActivity(productId);
    }

    public void navigateToMyPantryActivity() {
        view.navigateToMyPantryActivity();
    }

    public void addPhoto() {
        view.navigateToAddPhoto(model.getProductList());
    }
}