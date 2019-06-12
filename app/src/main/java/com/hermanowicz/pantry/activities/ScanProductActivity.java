/*
 * Copyright (c) 2019
 * Mateusz Hermanowicz - All rights reserved.
 * My Pantry
 * https://www.mypantry.eu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hermanowicz.pantry.activities;

import android.content.Context;
import android.content.Intent;
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
import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.interfaces.ScanProductView;
import com.hermanowicz.pantry.presenters.ScanProductPresenter;

import java.util.List;

/**
 * <h1>ScanProductActivity/h1>
 * Activity to scan QR code. Uses camera and zxing library.
 *
 * @author  Mateusz Hermanowicz
 * @version 1.0
 * @since   1.0
 */
public class ScanProductActivity extends AppCompatActivity implements ScanProductView {

    private Context context;

    private ScanProductPresenter presenter;

    static final int VIBRATE_DURATION = 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_product);

        presenter = new ScanProductPresenter(this);

        context = getApplicationContext();

        setQRCodeScanner();
    }

    void setQRCodeScanner() {
        IntentIntegrator qrCodeScanner = new IntentIntegrator(this);
        qrCodeScanner.setPrompt(getString(R.string.ScanProductActivity_scan_qr_code));
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
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            presenter.navigateToMainActivity();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void showErrorProductNotFound() {
        Toast.makeText(context, getString(R.string.ScanProductActivity_product_not_found), Toast.LENGTH_LONG).show();
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