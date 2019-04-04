/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry.presenters;

import com.hermanowicz.pantry.repositories.MyPantryActivityRepository;
import com.hermanowicz.pantry.views.MyPantryActivityView;

public class MyPantryActivityPresenter {

    private MyPantryActivityView view;
    private MyPantryActivityRepository repository;

    public MyPantryActivityPresenter(MyPantryActivityView view, MyPantryActivityRepository repository) {
        this.view = view;
        this.repository = repository;
    }

}
