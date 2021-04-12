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
import com.hermanowicz.pantry.model.DatabaseOperations;
import com.hermanowicz.pantry.model.GroupProducts;
import com.hermanowicz.pantry.model.PhotoModel;
import com.hermanowicz.pantry.model.ProductDataModel;

import java.util.List;

public class ProductDetailsPresenter {

    private final ProductDetailsView view;
    private final ProductDataModel model;
    private final PhotoModel photoModel;

    public ProductDetailsPresenter(@NonNull ProductDetailsView view, @NonNull ProductDataModel productDataModel, @NonNull DatabaseOperations databaseOperations) {
        this.view = view;
        this.model = productDataModel;
        this.photoModel = new PhotoModel(databaseOperations);
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
            if(groupProducts.getProduct().getPhotoName() != null){
                photoModel.setPhotoFile(groupProducts.getProduct().getPhotoName());
                view.showPhoto(photoModel.getPhotoBitmap());
            }
        } else {
            view.showErrorWrongData();
            view.navigateToMyPantryActivity();
        }
    }

    public void onClickDeleteProduct(int productId) {
        model.deleteSimilarProducts(productId);
        view.onDeletedProduct();
        view.navigateToMyPantryActivity();
    }

    public void onClickPrintQRCodes() {
        List<Product> productList = model.getProductList();
        view.navigateToPrintQRCodeActivity(productList);
    }

    public void onClickEditProduct(int productId){
        view.navigateToEditProductActivity(productId);
    }

    public void onClickTakePhoto() {
        view.navigateToAddPhotoActivity(model.getProductList());
    }

    public void navigateToMyPantryActivity() {
        view.navigateToMyPantryActivity();
    }
}