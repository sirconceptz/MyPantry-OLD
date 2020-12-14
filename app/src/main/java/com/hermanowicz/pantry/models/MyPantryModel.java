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

package com.hermanowicz.pantry.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.hermanowicz.pantry.db.CategoryDb;
import com.hermanowicz.pantry.db.Product;
import com.hermanowicz.pantry.db.ProductDb;
import com.hermanowicz.pantry.filter.Filter;
import com.hermanowicz.pantry.filter.FilterModel;

import java.util.ArrayList;
import java.util.List;

public class MyPantryModel {

    private LiveData<List<Product>> productLiveData;
    private MutableLiveData<FilterModel> product = new MutableLiveData<>();
    private FilterModel filterProduct = new FilterModel();
    private Filter filter;
    private List<Product> productList;
    private List<Product> selectedProductList = new ArrayList<>();
    private List<GroupProducts> groupProductsList = new ArrayList<>();
    private boolean isMultiSelect = false;
    private DatabaseOperations databaseOperations;

    public MyPantryModel(ProductDb productDb, CategoryDb categoryDb){
        databaseOperations = new DatabaseOperations(productDb, categoryDb);
    }

    private void groupProducts(List<Product> productList){
        List<GroupProducts> toAddGroupProductsList = new ArrayList<>();
        List<GroupProducts> toRemoveGroupProductsList = new ArrayList<>();

        for (Product product: productList){
            boolean productOnList = false;
            GroupProducts testedGroupProducts = new GroupProducts(product, 1);
            for (GroupProducts groupProducts : groupProductsList) {
                if (groupProducts.getProduct().getName().equals(product.getName())
                        && groupProducts.getProduct().getExpirationDate().equals(product.getExpirationDate())
                        && groupProducts.getProduct().getProductFeatures().equals(product.getProductFeatures())
                        && groupProducts.getProduct().getComposition().equals(product.getComposition())
                        && groupProducts.getProduct().getHealingProperties().equals(product.getHealingProperties())
                        && groupProducts.getProduct().getDosage().equals(product.getDosage())
                        && groupProducts.getProduct().getVolume() == product.getVolume()
                        && groupProducts.getProduct().getWeight() == product.getWeight()
                        && groupProducts.getProduct().getTypeOfProduct().equals(product.getTypeOfProduct())
                        && groupProducts.getProduct().getProductFeatures().equals(product.getProductFeatures())
                        && groupProducts.getProduct().getHasSalt() == product.getHasSalt()
                        && groupProducts.getProduct().getHasSugar() == product.getHasSugar()
                        && groupProducts.getProduct().getTaste().equals(product.getTaste())) {
                    productOnList = true;
                    testedGroupProducts = groupProducts;
                    break;
                }
            }
            if(productOnList) {
                toRemoveGroupProductsList.add(testedGroupProducts);
                testedGroupProducts.setQuantity(testedGroupProducts.getQuantity() + 1);
                toAddGroupProductsList.add(testedGroupProducts);
            }
            else {
                GroupProducts newGroupProduct = new GroupProducts(product, 1);
                toAddGroupProductsList.add(newGroupProduct);
            }
            groupProductsList.removeAll(toRemoveGroupProductsList);
            groupProductsList.addAll(toAddGroupProductsList);
            toAddGroupProductsList.clear();
            toRemoveGroupProductsList.clear();
        }
    }

    public void deleteSelectedProducts(){
        databaseOperations.deleteProductsFromList(getAllSelectedProductList());
    }

    public LiveData<List<Product>> getProductLiveData() {
        return productLiveData;
    }

    public List<GroupProducts> getGroupProductsList(){
        return groupProductsList;
    }

    public void setIsMultiSelect(boolean state){
        this.isMultiSelect = state;
    }

    public boolean getIsMultiSelect(){
        return this.isMultiSelect;
    }

    public List<Product> getGroupsSelectedProductList(){
        return selectedProductList;
    }

    public List<Product> getAllSelectedProductList(){
        List<Product> productList = new ArrayList<>();
        for (Product product : selectedProductList){
            List<Product> similarProducts = databaseOperations.getSimilarProductsList(product);
            productList.addAll(similarProducts);
        }
        return productList;
    }

    public void clearSelectList(){
        this.selectedProductList = new ArrayList<>();
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
        groupProductsList.clear();
        filter = new Filter(this.productList);
        groupProducts(productList);
    }

    public void setAllProductsList(){
        this.productList = databaseOperations.getAllProducts();
        groupProducts(productList);
    }

    public List<Product> getProductList(){
        return this.productList;
    }

    public void setProductsLiveData(){
        this.productLiveData = databaseOperations.getProductLiveData();
    }

    public void addMultiSelect(int position) {
        if (selectedProductList.contains(productList.get(position)))
            selectedProductList.remove(productList.get(position));
        else
            selectedProductList.add(productList.get(position));
    }

    public void clearFilters(){
        filterProduct = new FilterModel();
    }

    public void filterProductListByName(String fltrName) {
        filterProduct.setName(fltrName);
        productLiveData = Transformations.switchMap(product, filter::filterByProduct);
        product.setValue(filterProduct);
    }

    public void filterProductListByTypeOfProduct(String fltrTypeOfProduct, String fltrProductFeatures) {
        filterProduct.setTypeOfProduct(fltrTypeOfProduct);
        filterProduct.setProductFeatures(fltrProductFeatures);
        productLiveData = Transformations.switchMap(product, filter::filterByProduct);
        product.setValue(filterProduct);
    }

    public void filterProductListByExpirationDate(String fltrExpirationDateSince, String fltrExpirationDateFor) {
        filterProduct.setExpirationDateSince(fltrExpirationDateSince);
        filterProduct.setExpirationDateFor(fltrExpirationDateFor);
        productLiveData = Transformations.switchMap(product, filter::filterByProduct);
        product.setValue(filterProduct);
    }

    public void filterProductListByProductionDate(String fltrProductionDateSince, String fltrProductionDateFor) {
        filterProduct.setProductionDateSince(fltrProductionDateSince);
        filterProduct.setProductionDateFor(fltrProductionDateFor);
        productLiveData = Transformations.switchMap(product, filter::filterByProduct);
        product.setValue(filterProduct);
    }

    public void filterProductListByVolume(int fltrVolumeSince, int fltrVolumeFor) {
        filterProduct.setVolumeSince(fltrVolumeSince);
        filterProduct.setVolumeFor(fltrVolumeFor);
        productLiveData = Transformations.switchMap(product, filter::filterByProduct);
        product.setValue(filterProduct);
    }

    public void filterProductListByWeight(int fltrWeightSince, int fltrWeightFor) {
        filterProduct.setWeightSince(fltrWeightSince);
        filterProduct.setWeightFor(fltrWeightFor);
        productLiveData = Transformations.switchMap(product, filter::filterByProduct);
        product.setValue(filterProduct);
    }

    public void filterProductListBySugarAndSalt(Filter.Set fltrHasSugar, Filter.Set fltrHasSalt) {
        filterProduct.setHasSugar(fltrHasSugar);
        filterProduct.setHasSalt(fltrHasSalt);
        productLiveData = Transformations.switchMap(product, filter::filterByProduct);
        product.setValue(filterProduct);
    }

    public void filterProductListByTaste(String fltrTaste){
        filterProduct.setTaste(fltrTaste);
        productLiveData = Transformations.switchMap(product, filter::filterByProduct);
        product.setValue(filterProduct);
    }

    public FilterModel getFilterProduct(){
        return filterProduct;
    }
}