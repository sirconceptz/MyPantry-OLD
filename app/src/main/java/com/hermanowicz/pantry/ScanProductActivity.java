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

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.hermanowicz.pantry.presenters.ScanProductActivityPresenter;
import com.hermanowicz.pantry.views.ScanProductActivityView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
    private ScanProductActivityPresenter presenter;

    static final int VIBRATE_DURATION = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_product);

        presenter = new ScanProductActivityPresenter(this, null);

        context = ScanProductActivity.this;
        resources = context.getResources();
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
        if (result != null){
            if (result.getContents() == null){
                Toast.makeText(context, resources.getString(R.string.ScanProductActivity_product_not_found), Toast.LENGTH_LONG).show();
                Intent mainActivityIntent = new Intent(context, MainActivity.class);
                startActivity(mainActivityIntent);
                finish();
            }
            else {
                startActivity(createProductDetailsIntent(result.getContents()));
            }
        }
        else
            super.onActivityResult(requestCode, resultCode, data);
    }

    private Intent createProductDetailsIntent(String scanResult){
        Intent productDetailsIntent = new Intent(context, ProductDetailsActivity.class);
        List <Integer> decodedQRCodeAsList = decodedQRCode(scanResult);
        productDetailsIntent.putExtra("product_id", decodedQRCodeAsList.get(0));
        productDetailsIntent.putExtra("hash_code", decodedQRCodeAsList.get(1));
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(VIBRATE_DURATION);
        }
        return productDetailsIntent;
    }

    private List<Integer> decodedQRCode (String scanResult){
        List <Integer> decodedQRCodeAsList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(scanResult);
            decodedQRCodeAsList.add(jsonObject.getInt("product_id"));
            decodedQRCodeAsList.add(jsonObject.getInt("hash_code"));
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, scanResult, Toast.LENGTH_LONG).show();
        }
        return decodedQRCodeAsList;
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent mainActivityIntent = new Intent(context, MainActivity.class);
            startActivity(mainActivityIntent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}