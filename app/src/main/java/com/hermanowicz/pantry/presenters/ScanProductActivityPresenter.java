/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry.presenters;

import com.hermanowicz.pantry.repositories.ScanProductActivityRepository;
import com.hermanowicz.pantry.views.ScanProductActivityView;

public class ScanProductActivityPresenter {

    private ScanProductActivityView view;
    private ScanProductActivityRepository repository;

    public ScanProductActivityPresenter(ScanProductActivityView view, ScanProductActivityRepository repository) {
        this.view = view;
        this.repository = repository;
    }
}