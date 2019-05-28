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

import androidx.lifecycle.LiveData;

import com.hermanowicz.pantry.db.Product;
import com.hermanowicz.pantry.interfaces.IFilterDialogListener;
import com.hermanowicz.pantry.interfaces.IMyPantryActivityPresenter;
import com.hermanowicz.pantry.interfaces.IMyPantryActivityView;
import com.hermanowicz.pantry.models.MyPantryActivityModel;

import java.util.List;

public class MyPantryActivityPresenter implements IMyPantryActivityPresenter, IFilterDialogListener {

    private IMyPantryActivityView view;
    private MyPantryActivityModel model;

    public MyPantryActivityPresenter(IMyPantryActivityView view, MyPantryActivityModel model) {
        this.view = view;
        this.model = model;
    }

    public void onDestroy() {
        view = null;
        model = null;
    }

    @Override
    public void setProductList(List<Product> productList) {
        if (productList.size() == 0) {
            view.showEmptyPantryStatement();
        }
        model.setProductList(productList);
    }

    @Override
    public void clearFilters() {
        model.clearFilters();
        view.clearFilterIcons();
        model.setProductLiveData(view.getProductLiveData());
        view.updateRecyclerViewAdapter();
    }

    @Override
    public void setProductLiveData(LiveData<List<Product>> productLiveData) {
        model.setProductLiveData(productLiveData);
    }

    @Override
    public LiveData<List<Product>> getProductLiveData() {
        LiveData<List<Product>> productLiveData = model.getProductLiveData();
        return productLiveData;
    }

    @Override
    public String getFilterName() {
        String filterName = model.getFilterName();
        return filterName;
    }

    @Override
    public String getFilterExpirationDateSince() {
        String filterExpirationDateSince = model.getFilterExpirationDateSince();
        return filterExpirationDateSince;
    }

    @Override
    public String getFilterExpirationDateFor() {
        String filterExpirationDateFor = model.getFilterExpirationDateFor();
        return filterExpirationDateFor;
    }

    @Override
    public String getFilterProductionDateSince() {
        String filterProductionDateSince = model.getFilterProductionDateSince();
        return filterProductionDateSince;
    }

    @Override
    public String getFilterProductionDateFor() {
        String filterProductionDateFor = model.getFilterProductionDateFor();
        return filterProductionDateFor;
    }

    @Override
    public String getFilterTypeOfProduct() {
        String filterTypeOfProduct = model.getFilterTypeOfProduct();
        return filterTypeOfProduct;
    }

    @Override
    public String getFilterProductFeatures() {
        String filterProductFeatures = model.getFilterProductFeatures();
        return filterProductFeatures;
    }

    @Override
    public int getFilterVolumeSince() {
        int filterVolumeSince = model.getFilterVolumeSince();
        return filterVolumeSince;
    }

    @Override
    public int getFilterVolumeFor() {
        int filterVolumeFor = model.getFilterVolumeFor();
        return filterVolumeFor;
    }

    @Override
    public int getFilterWeightSince() {
        int filterWeightSince = model.getFilterWeightSince();
        return filterWeightSince;
    }

    @Override
    public int getFilterWeightFor() {
        int filterWeightFor = model.getFilterWeightFor();
        return filterWeightFor;
    }

    @Override
    public int getFilterHasSugar() {
        int filterHasSugar = model.getFilterHasSugar();
        return filterHasSugar;
    }

    @Override
    public int getFilterHasSalt() {
        int filterHasSalt = model.getFilterHasSalt();
        return filterHasSalt;
    }

    @Override
    public String getFilterTaste() {
        String filterTaste = model.getFilterTaste();
        return filterTaste;
    }

    @Override
    public void setFilterName(String filterName) {
        model.filterProductListByName(filterName);
        view.setFilterIcon(1);
        view.updateRecyclerViewAdapter();
    }

    @Override
    public void setFilterExpirationDate(String filterExpirationDateSince, String filterExpirationDateFor) {
        model.filterProductListByExpirationDate(filterExpirationDateSince, filterExpirationDateFor);
        view.setFilterIcon(2);
        view.updateRecyclerViewAdapter();
    }

    @Override
    public void setFilterProductionDate(String filterProductionDateSince, String filterProductionDateFor) {
        model.filterProductListByProductionDate(filterProductionDateSince, filterProductionDateFor);
        view.setFilterIcon(3);
        view.updateRecyclerViewAdapter();
    }

    @Override
    public void setFilterTypeOfProduct(String filterTypeOfProduct, String filterProductFeatures) {
        model.filterProductListByTypeOfProduct(filterTypeOfProduct, filterProductFeatures);
        view.setFilterIcon(4);
        view.updateRecyclerViewAdapter();
    }

    @Override
    public void setFilterVolume(int filterVolumeSince, int filterVolumeFor) {
        model.filterProductListByVolume(filterVolumeSince, filterVolumeFor);
        view.setFilterIcon(5);
        view.updateRecyclerViewAdapter();
    }

    @Override
    public void setFilterWeight(int filterWeightSince, int filterWeightFor) {
        model.filterProductListByWeight(filterWeightSince, filterWeightFor);
        view.setFilterIcon(6);
        view.updateRecyclerViewAdapter();
    }

    @Override
    public void setFilterTaste(String filterTaste) {
        model.filterProductListByTaste(filterTaste);
        view.setFilterIcon(7);
        view.updateRecyclerViewAdapter();
    }

    @Override
    public void setProductFeatures(int filterHasSugar, int filterHasSalt) {
        model.filterProductListBySugarAndSalt(filterHasSugar, filterHasSalt);
        view.setFilterIcon(8);
        view.updateRecyclerViewAdapter();
    }

    @Override
    public void clearFilterName() {
        model.filterProductListByName(null);
        view.clearFilterIcon(1);
        view.updateRecyclerViewAdapter();
    }

    @Override
    public void clearFilterExpirationDate() {
        model.filterProductListByExpirationDate(null, null);
        view.clearFilterIcon(2);
        view.updateRecyclerViewAdapter();
    }

    @Override
    public void clearFilterProductionDate() {
        model.filterProductListByProductionDate(null, null);
        view.clearFilterIcon(3);
        view.updateRecyclerViewAdapter();
    }

    @Override
    public void clearFilterTypeOfProduct() {
        model.filterProductListByTypeOfProduct(null, null);
        view.clearFilterIcon(4);
        view.updateRecyclerViewAdapter();
    }

    @Override
    public void clearFilterVolume() {
        model.filterProductListByVolume(-1, -1);
        view.clearFilterIcon(5);
        view.updateRecyclerViewAdapter();
    }

    @Override
    public void clearFilterWeight() {
        model.filterProductListByWeight(-1, -1);
        view.clearFilterIcon(6);
        view.updateRecyclerViewAdapter();
    }

    @Override
    public void clearFilterTaste() {
        model.filterProductListByTaste(null);
        view.clearFilterIcon(7);
        view.updateRecyclerViewAdapter();
    }

    @Override
    public void clearProductFeatures() {
        model.filterProductListBySugarAndSalt(-1, -1);
        view.clearFilterIcon(8);
        view.updateRecyclerViewAdapter();
    }

    @Override
    public void navigateToMainActivity() {
        view.navigateToMainActivity();
    }

    @Override
    public void openDialog(String typeOfDialog) {
        view.openDialog(typeOfDialog);
    }
}
