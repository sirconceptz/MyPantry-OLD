/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry.presenters;

import com.hermanowicz.pantry.interfaces.INewProductActivityPresenter;
import com.hermanowicz.pantry.interfaces.NewProductActivityView;
import com.hermanowicz.pantry.models.NewProductActivityModel;
import com.hermanowicz.pantry.models.Product;

import java.util.ArrayList;

public class NewProductActivityPresenter implements INewProductActivityPresenter {

    private NewProductActivityView view;
    private NewProductActivityModel model;

    public NewProductActivityPresenter(NewProductActivityView view, NewProductActivityModel model) {
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
        if (!model.checkCorrectProductName())
            view.showErrorNameNotSet();
    }

    @Override
    public void setTypeOfProduct(String typeOfProduct) {
        if (typeOfProduct.length() > 1)
            model.setTypeOfProduct(typeOfProduct);
        else
            view.showErrorCategoryNotSelected();
    }

    @Override
    public void setProductFeatures(String productFeatures) {
        model.setProductFeatures(productFeatures);
    }

    @Override
    public void setExpirationDate(String expirationDate) {
        model.setExpirationDate(expirationDate);
        if (!model.checkCorrectExpirationDate())
            view.showErrorExpirationDateNotSet();
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
    public void setIdOfLastProductInDb(int idOfLastProductInDB) {
        model.setIdOfLastProductInDb(idOfLastProductInDB);
    }

    @Override
    public void addProducts() {
        if (!model.checkCorrectProductName())
            view.showErrorNameNotSet();
        else if (!model.checkCorrectExpirationDate())
            view.showErrorExpirationDateNotSet();
        else if (!model.checkCorrectTypeOfProduct())
            view.showErrorCategoryNotSelected();
        else {
            ArrayList<Product> productsArrayList = model.buildProductsList();
            if (view.isAddProductsSuccess(productsArrayList)) {
                ArrayList<String> textToQRCodeList, namesOfProductsList, expirationDatesList;

                textToQRCodeList = model.getTextToQRCodeList(productsArrayList);
                namesOfProductsList = model.getNamesOfProductsList(productsArrayList);
                expirationDatesList = model.getExpirationDatesList(productsArrayList);

                view.showStatementOnAreProductsAdded(model.getOnProductAddStatement());
                view.navigateToPrintQRCodesActivity(textToQRCodeList, namesOfProductsList, expirationDatesList);
            }
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