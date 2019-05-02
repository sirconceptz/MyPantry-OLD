/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry.presenters;

import com.hermanowicz.pantry.interfaces.ProductDetailsActivityView;
import com.hermanowicz.pantry.models.Product;
import com.hermanowicz.pantry.models.ProductDetailsActivityModel;

import java.util.ArrayList;

public class ProductDetailsActivityPresenter implements com.hermanowicz.pantry.interfaces.IProductDetailsActivityPresenter {

    private ProductDetailsActivityView view;
    private ProductDetailsActivityModel model;

    public ProductDetailsActivityPresenter(ProductDetailsActivityView view, ProductDetailsActivityModel model) {
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
    public void setHashCode(int hashCode) {
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