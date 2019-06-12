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

import com.hermanowicz.pantry.db.Product;
import com.hermanowicz.pantry.db.ProductDb;
import com.hermanowicz.pantry.interfaces.NewProductView;
import com.hermanowicz.pantry.models.NewProductModel;
import com.hermanowicz.pantry.utils.PrintQRData;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewProductPresenter {

    private NewProductView view;
    private NewProductModel model;

    private Calendar calendar = Calendar.getInstance();
    private final DateFormat dateFormat = DateFormat.getDateInstance();

    public NewProductPresenter(NewProductView view, Resources resources, ProductDb productDb) {
        this.view = view;
        this.model = new NewProductModel(resources, productDb);
    }

    public void setExpirationDate(int year, int month, int day) {
        model.setExpirationDate(year, month, day);
    }

    public void setProductionDate(int year, int month, int day) {
        model.setProductionDate(year, month, day);
    }

    public void showExpirationDate(int day, int month, int year) {
        calendar.set(year, month, day);
        Date date = calendar.getTime();
        view.showExpirationDate(dateFormat.format(date));
    }

    public void showProductionDate(int day, int month, int year) {
        calendar.set(year, month, day);
        Date date = calendar.getTime();
        view.showProductionDate(dateFormat.format(date));
    }

    public void setQuantity(String quantity){
        model.parseQuantityProducts(quantity);
    }

    public void addProducts(Product product) {
        if (model.isProductNameValid(product))
            view.showErrorNameNotSet();
        else if (!model.isTypeOfProductValid(product))
            view.showErrorCategoryNotSelected();
        else {
            List<Product> products = model.buildProductsList(product);
            model.addProducts(products);
            view.onProductsAdd(products);
            ArrayList<String> textToQRCodeList, namesOfProductsList, expirationDatesList;

            textToQRCodeList = PrintQRData.getTextToQRCodeList(products);
            namesOfProductsList = PrintQRData.getNamesOfProductsList(products);
            expirationDatesList = PrintQRData.getExpirationDatesList(products);

            view.showStatementOnAreProductsAdded(model.getOnProductAddStatement());
            view.navigateToPrintQRCodesActivity(textToQRCodeList, namesOfProductsList, expirationDatesList);
        }
    }

    public void updateProductFeaturesAdapter(String typeOfProductSpinnerValue) {
        view.updateProductFeaturesAdapter(typeOfProductSpinnerValue);
    }

    public int[] getExpirationDateArray() {
        int[] expirationDateArray = model.getExpirationDateArray();
        return expirationDateArray;
    }

    public int[] getProductionDateArray() {
        int[] productionDateArray = model.getProductionDateArray();
        return productionDateArray;
    }

    public void navigateToMainActivity() {
        view.navigateToMainActivity();
    }
}