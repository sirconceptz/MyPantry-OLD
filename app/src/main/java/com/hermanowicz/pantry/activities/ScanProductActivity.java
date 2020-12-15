/*
 * Copyright (c) 2020
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
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.databinding.ActivityScanProductBinding;
import com.hermanowicz.pantry.interfaces.ScanProductView;
import com.hermanowicz.pantry.presenters.ScanProductPresenter;
import com.hermanowicz.pantry.utils.Orientation;
import com.hermanowicz.pantry.utils.ThemeMode;

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

    private ActivityScanProductBinding binding;
    private Context context;
    private ScanProductPresenter presenter;

    static final int VIBRATE_DURATION = 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(ThemeMode.getThemeMode(this));
        if(Orientation.isTablet(this))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        binding = ActivityScanProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = getApplicationContext();

        presenter = new ScanProductPresenter(this, PreferenceManager.getDefaultSharedPreferences(context));
        presenter.initQrScanner();
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
    public void onVibration() {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && vibrator != null)
            vibrator.vibrate(VibrationEffect.createOneShot(VIBRATE_DURATION, VibrationEffect.DEFAULT_AMPLITUDE));
    }

    @Override
    public void setQrScanner(boolean scannerSoundMode) {
        int cameraId = presenter.getSelectedCamera(); //0 - Rear camera, 1 - Front camera
        IntentIntegrator qrCodeScanner = new IntentIntegrator(this);
        qrCodeScanner.setPrompt(getString(R.string.ScanProductActivity_scan_qr_code));
        qrCodeScanner.setOrientationLocked(true);
        qrCodeScanner.setBeepEnabled(scannerSoundMode);
        qrCodeScanner.setCameraId(cameraId);
        qrCodeScanner.initiateScan();
    }

    @Override
    public void navigateToProductDetailsActivity(List<Integer> decodedScanResultAsList) {
        Intent productDetailsIntent = new Intent(context, ProductDetailsActivity.class);
        productDetailsIntent.putExtra("product_id", decodedScanResultAsList.get(0));
        productDetailsIntent.putExtra("hash_code", String.valueOf(decodedScanResultAsList.get(1)));
        startActivity(productDetailsIntent);
    }

    @Override
    public void navigateToMainActivity() {
        Intent mainActivityIntent = new Intent(context, MainActivity.class);
        startActivity(mainActivityIntent);
    }
}