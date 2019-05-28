/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry.presenters;

import com.hermanowicz.pantry.interfaces.IScanProductActivityPresenter;
import com.hermanowicz.pantry.interfaces.IScanProductActivityView;
import com.hermanowicz.pantry.models.ScanProductActivityModel;

import java.util.List;

public class ScanProductActivityPresenter implements IScanProductActivityPresenter {

    private IScanProductActivityView view;
    private ScanProductActivityModel model;

    public ScanProductActivityPresenter(IScanProductActivityView view, ScanProductActivityModel model) {
        this.view = view;
        this.model = model;
    }

    public void onDestroy() {
        view = null;
        model = null;
    }

    @Override
    public void onScanResult(String scanResult) {
        List<Integer> decodedQRCodeAsList = model.decodeScanResult(scanResult);
        if (decodedQRCodeAsList == null)
            view.navigateToProductDetailsActivity(decodedQRCodeAsList);
        else {
            view.showErrorProductNotFound();
            view.navigateToMainActivity();
        }
    }

    @Override
    public void showErrorProductNotFound() {
        view.showErrorProductNotFound();
    }


    @Override
    public void navigateToMainActivity() {
        view.navigateToMainActivity();
    }
}