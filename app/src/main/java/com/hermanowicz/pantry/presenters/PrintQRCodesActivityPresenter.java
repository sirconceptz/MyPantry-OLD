/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry.presenters;

import com.hermanowicz.pantry.repositories.PrintQRCodesRepository;
import com.hermanowicz.pantry.views.PrintQRCodesActivityView;

public class PrintQRCodesActivityPresenter {

    private PrintQRCodesActivityView view;
    private PrintQRCodesRepository repository;

    public PrintQRCodesActivityPresenter(PrintQRCodesActivityView view, PrintQRCodesRepository repository) {
        this.view = view;
        this.repository = repository;
    }
}
