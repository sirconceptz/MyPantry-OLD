/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry.presenters;

import com.hermanowicz.pantry.repositories.NewProductActivityRepository;
import com.hermanowicz.pantry.views.NewProductActivityView;

public class NewProductActivityPresenter {

    private NewProductActivityView view;
    private NewProductActivityRepository repository;

    public NewProductActivityPresenter(NewProductActivityView view, NewProductActivityRepository repository) {
        this.view = view;
        this.repository = repository;
    }
}