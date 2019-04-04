/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry.presenters;

import com.hermanowicz.pantry.repositories.MainActivityRepository;
import com.hermanowicz.pantry.views.MainActivityView;

public class MainActivityPresenter {

    private MainActivityView view;
    private MainActivityRepository repository;


    public MainActivityPresenter(MainActivityView view, MainActivityRepository repository) {
        this.view = view;
        this.repository = repository;
    }
}