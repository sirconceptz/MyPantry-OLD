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
import com.hermanowicz.pantry.interfaces.ProductDetailsView;
import com.hermanowicz.pantry.models.ProductDetailsModel;
import com.hermanowicz.pantry.utils.PrintQRData;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailsPresenter {

    private ProductDetailsView view;
    private ProductDetailsModel model = new ProductDetailsModel();

    public ProductDetailsPresenter(ProductDetailsView view) {
        this.view = view;
    }

    public void setProduct(Product product) {
        model.setProduct(product);
    }

    public void setHashCode(String hashCode) {
        model.setHashCode(hashCode);
    }

    public void showProductDetails() {
        if (model.productIsNull()) {
            view.showErrorWrongData();
            view.navigateToMyPantryActivity();
        } else if(model.compareHashCode()) {
            Product product = model.getProduct();
            view.showProductDetails(product);
        } else {
            view.showErrorWrongData();
            view.navigateToMyPantryActivity();
        }
    }

    public void deleteProduct(int productId) {
        view.onDeletedProduct(productId);
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