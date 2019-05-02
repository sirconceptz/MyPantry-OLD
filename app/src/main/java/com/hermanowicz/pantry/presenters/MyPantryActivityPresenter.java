/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry.presenters;

import com.hermanowicz.pantry.interfaces.IMyPantryActivityPresenter;
import com.hermanowicz.pantry.interfaces.MyPantryActivityView;
import com.hermanowicz.pantry.models.MyPantryActivityModel;

public class MyPantryActivityPresenter implements IMyPantryActivityPresenter {

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
    public void clearFilters() {
        view.clearFilterIcons();
    }

    @Override
    public void openDialog(String typeOfDialog) {

    }

    @Override
    public void navigateToMainActivity() {
        view.navigateToMainActivity();
    }
}
