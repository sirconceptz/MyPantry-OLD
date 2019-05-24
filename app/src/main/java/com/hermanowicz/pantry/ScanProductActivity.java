/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.hermanowicz.pantry.interfaces.ScanProductActivityView;
import com.hermanowicz.pantry.models.ScanProductActivityModel;
import com.hermanowicz.pantry.presenters.ScanProductActivityPresenter;

import java.util.List;

/**
 * <h1>ScanProductActivity/h1>
 * Activity to scan QR code. Uses camera and zxing library.
 *
 * @author  Mateusz Hermanowicz
 * @version 1.0
 * @since   1.0
 */
public class ScanProductActivity extends AppCompatActivity implements ScanProductActivityView {

    private Context context;
    private Resources resources;

    private ScanProductActivityModel model;
    private ScanProductActivityPresenter presenter;

    static final int VIBRATE_DURATION = 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_product);

        model = new ScanProductActivityModel();
        presenter = new ScanProductActivityPresenter(this, model);

        context = ScanProductActivity.this;
        resources = context.getResources();

        setQRCodeScanner();
    }

    void setQRCodeScanner() {
        IntentIntegrator qrCodeScanner = new IntentIntegrator(this);
        qrCodeScanner.setPrompt(resources.getString(R.string.ScanProductActivity_scan_qr_code));
        qrCodeScanner.setOrientationLocked(true);
        qrCodeScanner.setBeepEnabled(true);
        qrCodeScanner.setCameraId(0);
        qrCodeScanner.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null)
            if (result.getContents() == null) {
                presenter.showErrorProductNotFound();
                presenter.navigateToMainActivity();
            } else {
                presenter.onScanResult(result.getContents());
            }
        else
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            presenter.navigateToMainActivity();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void showErrorProductNotFound() {
        Toast.makeText(context, resources.getString(R.string.ScanProductActivity_product_not_found), Toast.LENGTH_LONG).show();
    }

    @Override
    public void navigateToProductDetailsActivity(List<Integer> decodedScanResultAsList) {
        Intent productDetailsIntent = new Intent(context, ProductDetailsActivity.class);
        productDetailsIntent.putExtra("product_id", decodedScanResultAsList.get(0));
        productDetailsIntent.putExtra("hash_code", decodedScanResultAsList.get(1));
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(VIBRATE_DURATION);
        }
        startActivity(productDetailsIntent);
        finish();
    }

    @Override
    public void navigateToMainActivity() {
        Intent mainActivityIntent = new Intent(context, MainActivity.class);
        startActivity(mainActivityIntent);
        finish();
    }
}