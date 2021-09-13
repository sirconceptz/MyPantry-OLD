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

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import com.hermanowicz.pantry.db.product.Product;
import com.hermanowicz.pantry.db.product.ProductDb;
import com.hermanowicz.pantry.interfaces.NewProductView;
import com.hermanowicz.pantry.model.AppSettingsModel;
import com.hermanowicz.pantry.model.GroupProducts;
import com.hermanowicz.pantry.model.NewProductModel;
import com.hermanowicz.pantry.util.PremiumAccess;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <h1>NewProductPresenter</h1>
 * Presenter for NewProductActivity
 *
 * @author  Mateusz Hermanowicz
 */

public class NewProductPresenter {

    private final NewProductView view;
    private final NewProductModel model;
    private final Context context;
    private final Calendar calendar = Calendar.getInstance();
    private final DateFormat dateFormat = DateFormat.getDateInstance();
    private PremiumAccess premiumAccess;

    public NewProductPresenter(@NonNull NewProductView view, @NonNull Context context) {
        this.view = view;
        this.model = new NewProductModel(context);
        this.context = context;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        AppSettingsModel appSettingsModel = new AppSettingsModel(sharedPreferences);
        model.setDatabaseMode(appSettingsModel.getDatabaseMode());
    }

    public void setPremiumAccess(@NonNull PremiumAccess premiumAccess){
        this.premiumAccess = premiumAccess;
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

    public void setTaste(@Nullable RadioButton selectedTasteButton){
        model.setTaste(selectedTasteButton);
    }

    public void setQuantity(@NonNull String quantity){
        model.parseQuantityProducts(quantity);
    }

    public void addProducts(@NonNull Product product) {
        if (model.isProductNameNotValid(product))
            view.showErrorNameNotSet();
        else if (!model.isTypeOfProductValid(product))
            view.showErrorCategoryNotSelected();
        else {
            model.createProductsList(product);
            model.addProducts();
            List<Product> productList = model.getProductList();
            List<Product> allProductList = model.getAllProductList();
            view.reCreateNotifications();
            view.showStatementOnAreProductsAdded(model.getOnProductAddStatement());
            view.navigateToPrintQRCodesActivity(productList, allProductList);
        }
    }

    public void updateProductFeaturesAdapter(@NonNull String typeOfProductSpinnerValue) {
        view.updateProductFeaturesAdapter(typeOfProductSpinnerValue);
    }

    public String[] getOwnCategoryArray(){
        return model.getOwnCategoriesArray();
    }

    public String[] getStorageLocationsArray(){
        return model.getStorageLocationsArray();
    }

    public int[] getExpirationDateArray() {
        return model.getExpirationDateArray();
    }

    public int[] getProductionDateArray() {
        return model.getProductionDateArray();
    }

    public void navigateToMainActivity() {
        view.navigateToMainActivity();
    }

    public void onClickAddProduct() {
        view.onClickAddProduct();
    }

    public boolean isFormNotFilled() {
        return view.isFormNotFilled();
    }

    public void showCancelProductAddDialog() {
        view.showCancelProductAddDialog();
    }

    public boolean isPremium(){
        return premiumAccess.isPremium();
    }

    public void setBarcode(String barcode) {
        model.setBarcode(barcode);
    }

    public void setProductList(List<Product> productList) {
        model.setProductList(productList);
        List<GroupProducts> groupProductsList = model.getGroupProductList();
        int groupProductsListSize = groupProductsList.size();
        if(groupProductsListSize == 1)
            view.setProductData(productList.get(0));
        else if(groupProductsListSize > 1) {
            String[] namesOfProducts = model.getNamesProductList();
            view.chooseProductToCopy(namesOfProducts);
        }
    }

    public void setSelectedProductToCopy(int position) {
        List<GroupProducts> groupProductsList = model.getGroupProductList();
        GroupProducts groupProducts = groupProductsList.get(position);
        Product product = groupProducts.getProduct();
        view.setProductData(product);
    }

    public boolean isOfflineDb() {
        return model.getDatabaseMode().equals("local");
    }

    public void setAllProductList(List<Product> allProductList) {
        if(isOfflineDb()) {
            ProductDb db = ProductDb.getInstance(context);
            allProductList = db.productsDao().getAllProductsList();
        }
        model.setAllProductList(allProductList);
    }
}