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

package com.hermanowicz.pantry.presenters;

import com.hermanowicz.pantry.db.Product;
import com.hermanowicz.pantry.interfaces.IProductDetailsActivityView;
import com.hermanowicz.pantry.models.ProductDetailsActivityModel;

import java.util.ArrayList;

public class ProductDetailsActivityPresenter implements com.hermanowicz.pantry.interfaces.IProductDetailsActivityPresenter {

    private IProductDetailsActivityView view;
    private ProductDetailsActivityModel model;

    public ProductDetailsActivityPresenter(IProductDetailsActivityView view, ProductDetailsActivityModel model) {
        this.view = view;
        this.model = model;
    }

    public void onDestroy() {
        view = null;
        model = null;
    }

    @Override
    public void setProduct(Product product) {
        model.setProduct(product);
    }

    @Override
    public void setHashCode(String hashCode) {
        model.setHashCode(hashCode);
    }

    @Override
    public void showProductDetails() {
        if (model.compareHashCode()) {
            Product product = model.getProduct();
            view.showProductDetails(product);
        } else {
            view.showErrorWrongData();
            view.navigateToMyPantryActivity();
        }
    }

    @Override
    public void deleteProduct(int productID) {
        view.onDeletedProduct(productID);
        view.navigateToMyPantryActivity();
    }

    @Override
    public void printQRCode() {
        ArrayList<String> textToQRCodeList, namesOfProductsList, expirationDatesList;

        textToQRCodeList = model.getTextToQRCodeList();
        namesOfProductsList = model.getNamesOfProductsList();
        expirationDatesList = model.getExpirationDatesList();

        view.onPrintQRCode(textToQRCodeList, namesOfProductsList, expirationDatesList);
    }

    @Override
    public void navigateToMyPantryActivity() {
        view.navigateToMyPantryActivity();
    }
}