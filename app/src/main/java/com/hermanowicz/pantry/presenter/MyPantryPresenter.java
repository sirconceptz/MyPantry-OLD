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

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.preference.PreferenceManager;

import com.hermanowicz.pantry.db.photo.Photo;
import com.hermanowicz.pantry.db.product.Product;
import com.hermanowicz.pantry.filter.Filter;
import com.hermanowicz.pantry.filter.FilterModel;
import com.hermanowicz.pantry.interfaces.MyPantryView;
import com.hermanowicz.pantry.model.AppSettingsModel;
import com.hermanowicz.pantry.model.GroupProducts;
import com.hermanowicz.pantry.model.MyPantryModel;
import com.hermanowicz.pantry.util.PremiumAccess;

import java.util.List;

/**
 * <h1>MyPantryPresenter</h1>
 * Presenter for MyPantryActivity
 *
 * @author  Mateusz Hermanowicz
 */

public class MyPantryPresenter {

    private final MyPantryView view;
    private final MyPantryModel model;
    private PremiumAccess premiumAccess;

    public MyPantryPresenter(@NonNull MyPantryView view, @NonNull Context context) {
        this.view = view;
        this.model = new MyPantryModel(context);
        AppSettingsModel appSettingsModel = new AppSettingsModel(PreferenceManager.getDefaultSharedPreferences(context));
        model.setDatabaseMode(appSettingsModel.getDatabaseMode());
    }

    public void setPremiumAccess(@NonNull PremiumAccess premiumAccess){
        this.premiumAccess = premiumAccess;
    }

    public void setProductList(List<Product> productList){
        model.setProductList(productList);
        setProductsLiveData();
        view.showProductsNotFound(productList.size() == 0);
    }

    public void setAllProductList(@NonNull List<Product> productList) {
        model.setAllProductsList(productList);
    }

    public void setAllProductsOfflineList(){
        model.setOfflineDbProductsList();
    }

    public List<GroupProducts> getGroupProductsList() {
        return model.getGroupProductsList();
    }

    public void clearMultiSelectList() {
        model.clearSelectList();
    }

    public List<Product> getGroupsProductsSelectList() {
        return model.getGroupsSelectedProductList();
    }

    public void setIsMultiSelect(boolean state) {
        model.setIsMultiSelect(state);
    }

    public boolean getIsMultiSelect() {
        return model.getIsMultiSelect();
    }

    public void addMultiSelectProduct(int position) {
        model.addMultiSelect(position);
        view.updateSelectsRecyclerViewAdapter();
    }

    public void deleteSelectedProducts() {
        List<Product> productList = model.getAllSelectedProductList();
        model.deleteSelectedProducts();
        view.onDeleteProducts(productList);
        clearFilters();
    }

    public void printSelectedProducts() {
        List<Product> productList = model.getAllSelectedProductList();
        List<Product> allProductList = model.getAllProductsList();
        view.onPrintProducts(productList, allProductList);
    }

    public void clearFilters() {
        model.clearFilters();
        view.clearFilterIcons();
        setProductsLiveData();
        view.updateProductsViewAdapter();
    }

    public void setProductsLiveData() {
        model.setProductsLiveData();
    }

    public LiveData<List<Product>> getProductLiveData() {
        return model.getProductLiveData();
    }

    public FilterModel getFilterProduct() {
        return model.getFilterProduct();
    }

    public void setFilterName(String filterName) {
        if(filterName == null) { //For disabled filter
            view.clearFilterIcon(1);
        } else {
            view.setFilterIcon(1);
        }
        setProductsLiveData();
        model.filterProductListByName(filterName);
        view.updateProductsViewAdapter();
    }

    public void setFilterExpirationDate(String filterExpirationDateSince, String filterExpirationDateFor) {
        if(filterExpirationDateSince == null && filterExpirationDateFor == null){ //For disabled filter
            view.clearFilterIcon(2);
        } else {
            view.setFilterIcon(2);
        }
        setProductsLiveData();
        model.filterProductListByExpirationDate(filterExpirationDateSince, filterExpirationDateFor);
        view.updateProductsViewAdapter();
    }

    public void setFilterProductionDate(String filterProductionDateSince, String filterProductionDateFor) {
        if(filterProductionDateSince == null && filterProductionDateFor == null){ //For disabled filter
            view.clearFilterIcon(3);
        } else {
            view.setFilterIcon(3);
        }
        setProductsLiveData();
        model.filterProductListByProductionDate(filterProductionDateSince, filterProductionDateFor);
        view.updateProductsViewAdapter();
    }

    public void setFilterTypeOfProduct(String filterTypeOfProduct, String filterProductFeatures) {
        if(filterTypeOfProduct == null && filterProductFeatures == null) { //For disabled filter
            view.clearFilterIcon(4);
        } else {
            view.setFilterIcon(4);
        }
        setProductsLiveData();
        model.filterProductListByTypeOfProduct(filterTypeOfProduct, filterProductFeatures);
        view.updateProductsViewAdapter();
    }

    public void setFilterVolume(int filterVolumeSince, int filterVolumeFor) {
        if(filterVolumeSince == -1 && filterVolumeFor == -1) { //For disabled filter
            view.clearFilterIcon(5);
        } else {
            view.setFilterIcon(5);
        }
        setProductsLiveData();
        model.filterProductListByVolume(filterVolumeSince, filterVolumeFor);
        view.updateProductsViewAdapter();
    }

    public void setFilterWeight(int filterWeightSince, int filterWeightFor) {
        if(filterWeightSince == -1 && filterWeightFor == -1){ //For disabled filter
            view.clearFilterIcon(6);
        } else {
            view.setFilterIcon(6);
        }
        setProductsLiveData();
        model.filterProductListByWeight(filterWeightSince, filterWeightFor);
        view.updateProductsViewAdapter();
    }

    public void setFilterTaste(String filterTaste) {
        if(filterTaste == null){ //For disabled filter
            view.clearFilterIcon(7);
        } else {
            view.setFilterIcon(7);
        }
        setProductsLiveData();
        model.filterProductListByTaste(filterTaste);
        view.updateProductsViewAdapter();
    }

    public void setFilterProductFeatures(Filter.Set filterHasSugar, Filter.Set filterHasSalt,
                                         Filter.Set filterIsBio, Filter.Set filterIsVege) {
        if(filterHasSugar == Filter.Set.DISABLED && filterHasSalt == Filter.Set.DISABLED
                && filterIsBio == Filter.Set.DISABLED && filterIsVege == Filter.Set.DISABLED){  //For disabled filter
            view.clearFilterIcon(8);
        } else {
            view.setFilterIcon(8);
        }
        setProductsLiveData();
        model.filterProductListByProductFeatures(filterHasSugar, filterHasSalt, filterIsBio, filterIsVege);
        view.updateProductsViewAdapter();
    }

    public void navigateToMainActivity() {
        view.navigateToMainActivity();
    }

    public void openDialog(int typeOfDialog) {
        view.openFilterDialog(typeOfDialog);
    }

    public boolean isPremium(){
        return premiumAccess.isPremium();
    }

    public boolean isOfflineDb() {
        return model.getDatabaseMode().equals("local");
    }

    public List<Product> getProductList() {
        return model.getAllProductsList();
    }

    public void setAllPhotoList(List<Photo> photoList) {
        model.setPhotoList(photoList);
    }

    public List<Photo> getPhotoList() {
        return model.getPhotoList();
    }
}