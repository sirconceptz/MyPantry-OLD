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

package com.hermanowicz.pantry.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.hermanowicz.pantry.db.Product;
import com.hermanowicz.pantry.filter.Filter;
import com.hermanowicz.pantry.filter.FilterModel;

import java.util.List;

public class MyPantryActivityModel {

    private LiveData<List<Product>> productLiveData;
    private MutableLiveData<FilterModel> product = new MutableLiveData<>();
    private FilterModel filterProduct = new FilterModel();
    private Filter filter;

    public LiveData<List<Product>> getProductLiveData() {
        return productLiveData;
    }

    public void setProductList(List<Product> productList) {
        filter = new Filter(productList);
    }

    public void setProductLiveData(LiveData<List<Product>> productLiveData) {
        this.productLiveData = productLiveData;
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

    public void filterProductListBySugarAndSalt(int fltrHasSugar, int fltrHasSalt) {
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

    public String getFilterName(){
        return filterProduct.getName();
    }

    public String getFilterTypeOfProduct(){
        return filterProduct.getTypeOfProduct();
    }

    public String getFilterProductFeatures(){
        return filterProduct.getProductFeatures();
    }

    public String getFilterExpirationDateSince(){
        return filterProduct.getExpirationDateSince();
    }

    public String getFilterExpirationDateFor(){
        return filterProduct.getExpirationDateFor();
    }

    public String getFilterProductionDateSince(){
        return filterProduct.getProductionDateSince();
    }

    public String getFilterProductionDateFor(){
        return filterProduct.getProductionDateFor();
    }

    public int getFilterVolumeSince(){
        return filterProduct.getVolumeSince();
    }

    public int getFilterVolumeFor(){
        return filterProduct.getVolumeFor();
    }

    public int getFilterWeightSince(){
        return filterProduct.getWeightSince();
    }

    public int getFilterWeightFor(){
        return filterProduct.getWeightFor();
    }

    public int getFilterHasSugar(){
        return filterProduct.getHasSugar();
    }

    public int getFilterHasSalt(){
        return filterProduct.getHasSalt();
    }

    public String getFilterTaste(){
        return filterProduct.getTaste();
    }
}