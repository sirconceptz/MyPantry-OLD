/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry.presenters;

import com.hermanowicz.pantry.repositories.ProductDetailsActivityRepository;
import com.hermanowicz.pantry.views.ProductDetailsActivityView;

public class ProductDetailsActivityPresenter {

    private ProductDetailsActivityView view;
    private ProductDetailsActivityRepository repository;

    public ProductDetailsActivityPresenter(ProductDetailsActivityView view, ProductDetailsActivityRepository repository) {
        this.view = view;
        this.repository = repository;
    }
}