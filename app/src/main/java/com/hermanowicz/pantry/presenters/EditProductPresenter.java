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

import android.content.res.Resources;
import android.widget.RadioButton;

import com.hermanowicz.pantry.db.Product;
import com.hermanowicz.pantry.db.ProductDb;
import com.hermanowicz.pantry.interfaces.EditProductView;
import com.hermanowicz.pantry.interfaces.ProductDataView;
import com.hermanowicz.pantry.models.ProductDataModel;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditProductPresenter {

    private EditProductView view;

    private ProductDataModel model;
    private ProductDataView productDataView;

    private Calendar calendar = Calendar.getInstance();
    private final DateFormat dateFormat = DateFormat.getDateInstance();

    public EditProductPresenter(EditProductView view, ProductDataView productDataView, ProductDb db, Resources resources){
        this.model = new ProductDataModel(db, resources);
        this.view = view;
        this.productDataView = productDataView;
    }

    public void setProduct(int productId){
        model.setProduct(productId);
        Product product = model.getProduct();
        int productTypeSpinnerPosition = model.getProductTypeSpinnerPosition();
        int productFeaturesSpinnerPosition = model.getProductFeaturesSpinnerPosition(productTypeSpinnerPosition);
        view.setSpinnerSelections(productTypeSpinnerPosition, productFeaturesSpinnerPosition);
        view.showProductData(product);
    }

    public Product getProduct(){
        return model.getProduct();
    }

    public void setTaste(RadioButton selectedTasteButton){
        model.setTaste(selectedTasteButton);
    }

    public void saveProduct(Product product){
        if(model.isProductNameNotValid(product)){
            productDataView.showErrorNameNotSet();
        }
        else if (!model.isTypeOfProductValid(product))
            productDataView.showErrorCategoryNotSelected();
        else {
            model.updateProduct(product);
            view.onSavedProduct();
            view.navigateToMyPantryActivity();
        }
    }

    public void setExpirationDate(int year, int month, int day) {
        model.setExpirationDate(model.formatDate(year, month, day));
    }

    public void setProductionDate(int year, int month, int day) {
        model.setProductionDate(model.formatDate(year, month, day));
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

    public void navigateToMyPantryActivity(){
        view.navigateToMyPantryActivity();
    }
}