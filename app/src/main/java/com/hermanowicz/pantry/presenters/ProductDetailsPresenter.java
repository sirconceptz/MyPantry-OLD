/*
 * Copyright (c) 2020
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

import android.content.res.Resources;

import com.hermanowicz.pantry.db.Product;
import com.hermanowicz.pantry.db.ProductDb;
import com.hermanowicz.pantry.interfaces.ProductDetailsView;
import com.hermanowicz.pantry.models.GroupProducts;
import com.hermanowicz.pantry.models.ProductDataModel;
import com.hermanowicz.pantry.utils.PrintQRData;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailsPresenter {

    private ProductDetailsView view;
    private ProductDataModel model;

    public ProductDetailsPresenter(ProductDetailsView view, ProductDb db, Resources resources) {
        this.view = view;
        this.model = new ProductDataModel(db, resources);
    }

    public void setProductList(int productId) {
        model.setProductList(productId);
    }

    public void showProductDetails(String hashCode) {
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
        //Todo: Zrobić model.deleteSimilarProducts(productId);
        view.onDeletedProduct();
        view.navigateToMyPantryActivity();
    }

    public void printQRCode() {
        ArrayList<String> textToQRCodeList, namesOfProductsList, expirationDatesList;
        List<Product> productList = model.getProductList();

        textToQRCodeList = PrintQRData.getTextToQRCodeList(productList, 0);
        namesOfProductsList = PrintQRData.getNamesOfProductsList(productList);
        expirationDatesList = PrintQRData.getExpirationDatesList(productList);

        view.onPrintQRCode(textToQRCodeList, namesOfProductsList, expirationDatesList);
    }

    public void editProduct(int productId){
        view.navigateToEditProductActivity(productId);
    }

    public void navigateToMyPantryActivity() {
        view.navigateToMyPantryActivity();
    }
}