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

import android.widget.RadioButton;

import androidx.annotation.NonNull;

import com.hermanowicz.pantry.interfaces.EditProductView;
import com.hermanowicz.pantry.interfaces.ProductDataView;
import com.hermanowicz.pantry.models.GroupProducts;
import com.hermanowicz.pantry.models.ProductDataModel;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditProductPresenter {

    private final EditProductView view;
    private final ProductDataModel model;
    private final ProductDataView productDataView;

    private final Calendar calendar = Calendar.getInstance();
    private final DateFormat dateFormat = DateFormat.getDateInstance();

    public EditProductPresenter(@NonNull EditProductView view, @NonNull ProductDataView productDataView, @NonNull ProductDataModel model){
        this.model = model;
        this.view = view;
        this.productDataView = productDataView;
    }

    public void setProduct(int productId){
        model.setProductList(productId);
        GroupProducts groupProducts = model.getGroupProducts();
        int productTypeSpinnerPosition = model.getProductTypeSpinnerPosition();
        int productFeaturesSpinnerPosition = model.getProductFeaturesSpinnerPosition(productTypeSpinnerPosition);
        view.setSpinnerSelections(productTypeSpinnerPosition, productFeaturesSpinnerPosition);
        view.showProductData(groupProducts);
    }

    public GroupProducts getGroupProducts(){
        return model.getGroupProducts();
    }

    public void setTaste(@NonNull RadioButton selectedTasteButton){
        model.setTaste(selectedTasteButton);
    }

    public void saveProduct(@NonNull GroupProducts groupProducts){
        if(model.isProductNameNotValid(groupProducts.getProduct())){
            productDataView.showErrorNameNotSet();
        }
        else if (!model.isTypeOfProductValid(groupProducts.getProduct()))
            productDataView.showErrorCategoryNotSelected();
        else {
            model.updateDatabase(groupProducts);
            view.onSavedProduct();
            view.navigateToMyPantryActivity();
        }
    }

    public void setExpirationDate(int year, int month, int day) {
        model.setExpirationDate(model.formatDate(year, month+1, day));
    }

    public void setProductionDate(int year, int month, int day) {
        model.setProductionDate(model.formatDate(year, month+1, day));
    }

    public void showExpirationDate(int day, int month, int year) {
        calendar.set(year, month, day);
        Date date = calendar.getTime();
        productDataView.showExpirationDate(dateFormat.format(date));
    }

    public void showProductionDate(int day, int month, int year) {
        calendar.set(year, month, day);
        Date date = calendar.getTime();
        productDataView.showProductionDate(dateFormat.format(date));
    }

    public void updateProductFeaturesAdapter(String typeOfProductSpinnerValue) {
        productDataView.updateProductFeaturesAdapter(typeOfProductSpinnerValue);
    }

    public int[] getExpirationDateArray() {
        return model.getExpirationDateArray();
    }

    public int[] getProductionDateArray() {
        return model.getProductionDateArray();
    }

    public String[] getOwnCategoryArray(){
        return model.getOwnCategoriesArray();
    }

    public void navigateToMyPantryActivity(){
        view.navigateToMyPantryActivity();
    }
}