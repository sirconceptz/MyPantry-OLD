/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry.presenters;

import com.hermanowicz.pantry.interfaces.IMainActivityView;

public class MainActivityPresenter implements com.hermanowicz.pantry.interfaces.IMainActivityPresenter {

    private IMainActivityView view;

    public MainActivityPresenter(IMainActivityView view) {
        this.view = view;
    }

    public void onDestroy() {
        view = null;
    }

    @Override
    public void navigateToMyPantryActivity() {
        view.onNavigationToMyPantryActivity();
    }

    @Override
    public void navigateToScanProductActivity() {
        view.onNavigationToScanProductActivity();
    }

    @Override
    public void navigateToNewProductActivity() {
        view.onNavigationToNewProductActivity();
    }

    @Override
    public void navigateToAppSettingsActivity() {
        view.onNavigationToAppSettingsActivity();
    }
}