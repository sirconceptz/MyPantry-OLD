/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry.presenters;

import com.hermanowicz.pantry.interfaces.DialogListener;
import com.hermanowicz.pantry.interfaces.IMyPantryActivityPresenter;
import com.hermanowicz.pantry.interfaces.MyPantryActivityView;
import com.hermanowicz.pantry.models.MyPantryActivityModel;

public class MyPantryActivityPresenter implements IMyPantryActivityPresenter, DialogListener {

    private MyPantryActivityView view;
    private MyPantryActivityModel model;

    public MyPantryActivityPresenter(MyPantryActivityView view, MyPantryActivityModel model) {
        this.view = view;
        this.model = model;
    }

    public void onDestroy() {
        view = null;
        model = null;
    }

    @Override
    public String getFilterName() {
        String filterName = model.getFilterName();
        return filterName;
    }

    @Override
    public void setFilterName(String filterName) {
        model.setFilterNameOfProduct(filterName);
        view.setFilterIcon(1);
        view.refreshListView(model.buildPantryQuery());
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
    public void setFilterTaste(String filterTaste) {
        model.setFilterTaste(filterTaste);
        view.setFilterIcon(7);
        view.refreshListView(model.buildPantryQuery());
    }

    @Override
    public void clearFilters() {
        model.clearFilters();
        view.clearFilterIcons();
    }

    @Override
    public void navigateToMainActivity() {
        view.navigateToMainActivity();
    }

    @Override
    public void openDialog(String typeOfDialog) {
        view.openDialog(typeOfDialog);
    }

    @Override
    public void initRecyclerViewData() {
        view.initData(model.buildPantryQuery());
    }

    @Override
    public void setFilterExpirationDate(String filterExpirationDateSince, String filterExpirationDateFor) {
        model.setFilterExpirationDateSince(filterExpirationDateSince);
        model.setFilterExpirationDateFor(filterExpirationDateFor);
        view.setFilterIcon(2);
        view.refreshListView(model.buildPantryQuery());
    }

    @Override
    public void setFilterProductionDate(String filterProductionDateSince, String filterProductionDateFor) {
        model.setFilterProductionDateSince(filterProductionDateSince);
        model.setFilterProductionDateFor(filterProductionDateFor);
        view.setFilterIcon(3);
        view.refreshListView(model.buildPantryQuery());
    }

    @Override
    public void setFilterTypeOfProduct(String filterTypeOfProduct, String filterProductFeatures) {
        model.setFilterTypeOfProduct(filterTypeOfProduct);
        model.setFilterProductFeatures(filterProductFeatures);
        view.setFilterIcon(4);
        view.refreshListView(model.buildPantryQuery());
    }

    @Override
    public void setFilterVolume(int filterVolumeSince, int filterVolumeFor) {
        model.setFilterVolumeSince(filterVolumeSince);
        model.setFilterVolumeFor(filterVolumeFor);
        view.setFilterIcon(5);
        view.refreshListView(model.buildPantryQuery());
    }

    @Override
    public void setFilterWeight(int filterWeightSince, int filterWeightFor) {
        model.setFilterWeightSince(filterWeightSince);
        model.setFilterWeightFor(filterWeightFor);
        view.setFilterIcon(6);
        view.refreshListView(model.buildPantryQuery());
    }

    @Override
    public void setProductFeatures(int filterHasSugar, int filterHasSalt) {
        model.setFilterHasSugar(filterHasSugar);
        model.setFilterHasSalt(filterHasSalt);
        view.setFilterIcon(8);
        view.refreshListView(model.buildPantryQuery());
    }

    @Override
    public void clearFilterName() {
        model.setFilterNameOfProduct(null);
        view.clearFilterIcon(1);
        view.refreshListView(model.buildPantryQuery());
    }

    @Override
    public void clearFilterExpirationDate() {
        model.setFilterExpirationDateSince(null);
        model.setFilterExpirationDateFor(null);
        view.clearFilterIcon(2);
        view.refreshListView(model.buildPantryQuery());
    }

    @Override
    public void clearFilterProductionDate() {
        model.setFilterProductionDateSince(null);
        model.setFilterProductionDateFor(null);
        view.clearFilterIcon(3);
        view.refreshListView(model.buildPantryQuery());
    }

    @Override
    public void clearFilterTypeOfProduct() {
        model.setFilterTypeOfProduct(null);
        model.setFilterProductFeatures(null);
        view.clearFilterIcon(4);
        view.refreshListView(model.buildPantryQuery());
    }

    @Override
    public void clearFilterVolume() {
        model.setFilterVolumeSince(-1);
        model.setFilterVolumeFor(-1);
        view.clearFilterIcon(5);
        view.refreshListView(model.buildPantryQuery());
    }

    @Override
    public void clearFilterWeight() {
        model.setFilterWeightSince(-1);
        model.setFilterWeightFor(-1);
        view.clearFilterIcon(6);
        view.refreshListView(model.buildPantryQuery());
    }

    @Override
    public void clearFilterTaste() {
        model.setFilterTaste(null);
        view.clearFilterIcon(7);
        view.refreshListView(model.buildPantryQuery());
    }

    @Override
    public void clearProductFeatures() {
        model.setFilterHasSugar(-1);
        model.setFilterHasSalt(-1);
        view.clearFilterIcon(8);
        view.refreshListView(model.buildPantryQuery());
    }
}
