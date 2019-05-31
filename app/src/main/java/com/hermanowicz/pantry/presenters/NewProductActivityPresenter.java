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
import com.hermanowicz.pantry.interfaces.INewProductActivityPresenter;
import com.hermanowicz.pantry.interfaces.INewProductActivityView;
import com.hermanowicz.pantry.models.NewProductActivityModel;
import com.hermanowicz.pantry.utils.PrintQRData;

import java.util.ArrayList;
import java.util.List;

public class NewProductActivityPresenter implements INewProductActivityPresenter {

    private INewProductActivityView view;
    private NewProductActivityModel model;

    public NewProductActivityPresenter(INewProductActivityView view, NewProductActivityModel model) {
        this.view = view;
        this.model = model;
    }

    public void onDestroy() {
        view = null;
        model = null;
    }

    @Override
    public void setName(String name) {
        model.setName(name);
        if (model.isProductNameValid())
            view.showErrorNameNotSet();
    }

    @Override
    public void setTypeOfProduct(String typeOfProduct) {
        model.setTypeOfProduct(typeOfProduct);
    }

    @Override
    public void setProductFeatures(String productFeatures) {
        model.setProductFeatures(productFeatures);
    }

    @Override
    public void setExpirationDate(String expirationDate) {
        model.setExpirationDate(expirationDate);
    }

    @Override
    public void setProductionDate(String productionDate) {
        model.setProductionDate(productionDate);
    }

    @Override
    public void showExpirationDate(int day, int month, int year) {
        view.showExpirationDate(day, month, year);
    }

    @Override
    public void showProductionDate(int day, int month, int year) {
        view.showProductionDate(day, month, year);
    }

    @Override
    public void setComposition(String composition) {
        model.setComposition(composition);
    }

    @Override
    public void setHealingProperties(String healingProperties) {
        model.setHealingProperties(healingProperties);
    }

    @Override
    public void setDosage(String dosage) {
        model.setDosage(dosage);
    }

    @Override
    public void setHasSugar(boolean hasSugar) {
        model.setHasSugar(hasSugar);
    }

    @Override
    public void setHasSalt(boolean hasSalt) {
        model.setHasSalt(hasSalt);
    }

    @Override
    public void setIsSweet(boolean isSweet) {
        model.setIsSweet(isSweet);
    }

    @Override
    public void setIsSour(boolean isSour) {
        model.setIsSour(isSour);
    }

    @Override
    public void setIsSweetAndSour(boolean isSweetAndSour) {
        model.setIsSweetAndSour(isSweetAndSour);
    }

    @Override
    public void setIsBitter(boolean isBitter) {
        model.setIsBitter(isBitter);
    }

    @Override
    public void setIsSalty(boolean isSalty) {
        model.setIsSalty(isSalty);
    }

    @Override
    public void parseQuantity(String quantity) {
        model.parseQuantityProducts(quantity);
    }

    @Override
    public void parseVolume(String volume) {
        model.parseVolumeProduct(volume);
    }

    @Override
    public void parseWeight(String weight) {
        model.parseWeightProduct(weight);
    }

    @Override
    public void addProducts() {
        if (model.isProductNameValid())
            view.showErrorNameNotSet();
        else if (!model.isTypeOfProductValid())
            view.showErrorCategoryNotSelected();
        else {
            List<Product> productsArrayList = model.buildProductsList();
            view.isAddProductsSuccess(productsArrayList);
            ArrayList<String> textToQRCodeList, namesOfProductsList, expirationDatesList;

            textToQRCodeList = PrintQRData.getTextToQRCodeList(productsArrayList);
            namesOfProductsList = PrintQRData.getNamesOfProductsList(productsArrayList);
            expirationDatesList = PrintQRData.getExpirationDatesList(productsArrayList);

            view.showStatementOnAreProductsAdded(model.getOnProductAddStatement());
            view.navigateToPrintQRCodesActivity(textToQRCodeList, namesOfProductsList, expirationDatesList);
        }
    }

    @Override
    public void updateProductFeaturesAdapter(String typeOfProductSpinnerValue) {
        view.updateProductFeaturesAdapter(typeOfProductSpinnerValue);
    }

    @Override
    public String[] getExpirationDateArray() {
        String[] expirationDateArray = model.getExpirationDateArrayList();
        return expirationDateArray;
    }

    @Override
    public String[] getProductionDateArray() {
        String[] productionDateArray = model.getProductionDateArrayList();
        return productionDateArray;
    }

    @Override
    public void navigateToMainActivity() {
        view.navigateToMainActivity();
    }
}