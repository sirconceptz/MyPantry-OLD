/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry.presenters;

import com.hermanowicz.pantry.interfaces.INewProductActivityPresenter;
import com.hermanowicz.pantry.interfaces.NewProductActivityView;
import com.hermanowicz.pantry.models.NewProductActivityModel;

public class NewProductActivityPresenter implements INewProductActivityPresenter {

    private NewProductActivityView view;
    private NewProductActivityModel model;

    public NewProductActivityPresenter(NewProductActivityView view, NewProductActivityModel model) {
        this.view = view;
        this.model = model;
    }

    public void onDestroy() {
        view = null;
        model = null;
    }

    @Override
    public void navigateToMainActivity() {
        view.navigateToMainActivity();
    }
}