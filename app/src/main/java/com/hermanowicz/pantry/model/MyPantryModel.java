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

package com.hermanowicz.pantry.model;

import android.content.Context;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hermanowicz.pantry.db.category.Category;
import com.hermanowicz.pantry.db.product.Product;
import com.hermanowicz.pantry.db.product.ProductDb;
import com.hermanowicz.pantry.filter.Filter;
import com.hermanowicz.pantry.filter.FilterModel;

import java.util.ArrayList;
import java.util.List;

@Keep
public class MyPantryModel {

    private final ProductDb productDb;
    private LiveData<List<Product>> productLiveData;
    private final MutableLiveData<FilterModel> product = new MutableLiveData<>();
    private FilterModel filterProduct = new FilterModel();
    private Filter filter;
    private List<Product> allProductList = new ArrayList<>();
    private List<Product> selectedProductsGroupList = new ArrayList<>();
    private List<GroupProducts> groupProductsList = new ArrayList<>();
    private boolean isMultiSelect = false;
    private String databaseMode = "";
    private String[] allCategoryNameList = new String[0];

    public MyPantryModel(@NonNull Context context) {
        this.productDb = ProductDb.getInstance(context);
    }

    public void deleteSelectedProducts() {
        if (databaseMode.equals("local"))
            productDb.productsDao().deleteProducts(getAllSelectedProductList());
        else {
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference ref = db.getReference().child("products/" + FirebaseAuth.getInstance().getUid());
            for (Product product : selectedProductsGroupList) {
                List<Product> productToRemove = Product.getSimilarProductsList(product, allProductList);
                for (Product product1 : productToRemove) {
                    ref.child(String.valueOf(product1.getId())).removeValue();
                }
            }
        }
    }

    public LiveData<List<Product>> getProductLiveData() {
        return productLiveData;
    }

    public List<GroupProducts> getGroupProductsList() {
        return groupProductsList;
    }

    public void setIsMultiSelect(boolean state){
        this.isMultiSelect = state;
    }

    public boolean getIsMultiSelect(){
        return this.isMultiSelect;
    }

    public List<Product> getGroupsSelectedProductList(){
        return selectedProductsGroupList;
    }

    public List<Product> getAllSelectedProductList(){
        List<Product> productList = new ArrayList<>();
        for (Product product : selectedProductsGroupList){
            List<Product> similarProducts = Product.getSimilarProductsList(product, allProductList);
            productList.addAll(similarProducts);
        }
        return productList;
    }

    public void clearSelectList(){
        this.selectedProductsGroupList = new ArrayList<>();
    }

    public void setProductList(@NonNull List<Product> productList) {
        filter = new Filter(productList);
        groupProductsList = GroupProducts.getGroupProducts(productList);
    }

    public void setAllProductsList(@NonNull List<Product> productList){
        allProductList = productList;
    }

    public void setOfflineDbProductsList(){
        List<Product> productList = productDb.productsDao().getAllProductsList();
        setProductList(productList);
        setAllProductsList(productList);
    }

    public List<Product> getAllProductsList(){
        return allProductList;
    }

    public void setProductsLiveData() {
        if(databaseMode.equals("local"))
            setProductsOfflineLiveData();
        else
            setProductsOnlineLiveData();
    }

    public void setProductsOnlineLiveData() {
        MutableLiveData<List<Product>> productListMutableLiveData = new MutableLiveData<>();
        productListMutableLiveData.setValue(allProductList);
        productLiveData = productListMutableLiveData;
    }

    public void setProductsOfflineLiveData() {
        this.productLiveData = productDb.productsDao().getAllProductsLivedata();
    }

    public void addMultiSelect(int position) {
        if (selectedProductsGroupList.contains(groupProductsList.get(position).getProduct()))
            selectedProductsGroupList.remove(groupProductsList.get(position).getProduct());
        else
            selectedProductsGroupList.add(groupProductsList.get(position).getProduct());
    }

    public void clearFilters(){
        filterProduct = new FilterModel();
    }

    public void filterProductListByName(String fltrName) {
        filterProduct.setName(fltrName);
        product.setValue(filterProduct);
        productLiveData = Transformations.switchMap(product, filter::filterByProduct);
    }

    public void filterProductListByTypeOfProduct(String fltrTypeOfProduct, String fltrProductCategory) {
        filterProduct.setTypeOfProduct(fltrTypeOfProduct);
        filterProduct.setProductCategory(fltrProductCategory);
        product.setValue(filterProduct);
        productLiveData = Transformations.switchMap(product, filter::filterByProduct);
    }

    public void filterProductListByExpirationDate(String fltrExpirationDateSince, String fltrExpirationDateFor) {
        filterProduct.setExpirationDateSince(fltrExpirationDateSince);
        filterProduct.setExpirationDateFor(fltrExpirationDateFor);
        product.setValue(filterProduct);
        productLiveData = Transformations.switchMap(product, filter::filterByProduct);
    }

    public void filterProductListByProductionDate(String fltrProductionDateSince, String fltrProductionDateFor) {
        filterProduct.setProductionDateSince(fltrProductionDateSince);
        filterProduct.setProductionDateFor(fltrProductionDateFor);
        product.setValue(filterProduct);
        productLiveData = Transformations.switchMap(product, filter::filterByProduct);
    }

    public void filterProductListByVolume(int fltrVolumeSince, int fltrVolumeFor) {
        filterProduct.setVolumeSince(fltrVolumeSince);
        filterProduct.setVolumeFor(fltrVolumeFor);
        product.setValue(filterProduct);
        productLiveData = Transformations.switchMap(product, filter::filterByProduct);
    }

    public void filterProductListByWeight(int fltrWeightSince, int fltrWeightFor) {
        filterProduct.setWeightSince(fltrWeightSince);
        filterProduct.setWeightFor(fltrWeightFor);
        product.setValue(filterProduct);
        productLiveData = Transformations.switchMap(product, filter::filterByProduct);
    }

    public void filterProductListByProductFeatures(Filter.Set fltrHasSugar, Filter.Set fltrHasSalt,
                                                   Filter.Set fltrIsBio, Filter.Set fltrIsVege) {
        filterProduct.setHasSugar(fltrHasSugar);
        filterProduct.setHasSalt(fltrHasSalt);
        filterProduct.setIsBio(fltrIsBio);
        filterProduct.setIsVege(fltrIsVege);
        product.setValue(filterProduct);
        productLiveData = Transformations.switchMap(product, filter::filterByProduct);
    }

    public void filterProductListByTaste(String fltrTaste){
        filterProduct.setTaste(fltrTaste);
        product.setValue(filterProduct);
        productLiveData = Transformations.switchMap(product, filter::filterByProduct);
    }

    public FilterModel getFilterProduct(){
        return filterProduct;
    }

    public void setDatabaseMode(@NonNull String databaseMode) {
        this.databaseMode = databaseMode;
    }

    public String getDatabaseMode() {
        if (databaseMode != null)
            return databaseMode;
        else
            return "";
    }

    public String[] getAllCategoryNameList() {
        if (allCategoryNameList != null)
            return allCategoryNameList;
        else
            return new String[0];
    }

    public void setAllCategoryNameList(List<Category> categoryList) {
        int categoryListSize = categoryList.size();
        allCategoryNameList = new String[categoryListSize];
        for (int counter = 0; counter <= categoryListSize; counter++) {
            Category category = categoryList.get(counter);
            String categoryName = category.getName();
            allCategoryNameList[counter] = categoryName;
        }
    }
}