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

import android.graphics.Bitmap;

import com.hermanowicz.pantry.db.photo.Photo;
import com.hermanowicz.pantry.db.product.Product;
import com.hermanowicz.pantry.model.GroupProducts;

import java.util.List;

public interface ProductDetailsView {

    void showProductDetails(GroupProducts groupProducts);

    void showPhoto(Bitmap photo);

    void showErrorWrongData();

    void onDeletedProduct();

    void showDialogOnDeleteProduct();

    void navigateToPrintQRCodeActivity(List<Product> productList);

    void navigateToEditProductActivity(int productId, List<Product> productList, List<Product> allProductList);

    void navigateToMyPantryActivity();

    void navigateToAddPhotoActivity(List<Product> productList, List<Photo> photoList);

    void navigateToScanProductActivity(List<Product> productList);
}