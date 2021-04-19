/*
 * Copyright (c) 2019-2021
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

package com.hermanowicz.pantry.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.hermanowicz.pantry.BuildConfig;
import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.databinding.ActivityPrintQrcodesBinding;
import com.hermanowicz.pantry.db.product.Product;
import com.hermanowicz.pantry.interfaces.PrintQRCodesView;
import com.hermanowicz.pantry.presenter.PrintQRCodesPresenter;
import com.hermanowicz.pantry.util.FileManager;
import com.hermanowicz.pantry.util.Orientation;
import com.hermanowicz.pantry.util.ThemeMode;

import java.util.List;
import java.util.Objects;

import maes.tech.intentanim.CustomIntent;

import static androidx.core.content.FileProvider.getUriForFile;

/**
 * <h1>PrintQRCodesActivity</h1>
 * Activity to print QR codes. Uses zxing library. User will be asked. The user gets a query if
 * he wants to print QR codes for added products.
 *
 * @author  Mateusz Hermanowicz
 * @version 1.0
 * @since   1.0
 */

public class PrintQRCodesActivity extends AppCompatActivity implements PrintQRCodesView {

    private ActivityPrintQrcodesBinding binding;
    private PrintQRCodesPresenter presenter;
    private Context context;

    private AdView adView;
    private ImageView qrCodeImage;
    private Button printQrCodes, sendPdfByEmail, skip;
    private List<Product> productList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(ThemeMode.getThemeMode(this));
        if(Orientation.isTablet(this))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        MobileAds.initialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        initView();
        setListeners();
    }

    private void initView() {
        binding = ActivityPrintQrcodesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = getApplicationContext();

        adView = binding.adview;
        qrCodeImage = binding.imageQrCode;
        printQrCodes = binding.buttonPrintQRCodes;
        sendPdfByEmail = binding.buttonSendPdfByEmail;
        skip = binding.buttonSkip;

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        productList = (List<Product>) getIntent().getSerializableExtra("PRODUCT_LIST");

        presenter = new PrintQRCodesPresenter(this);

        presenter.setActivity(this);
        presenter.setProductList(productList);
        presenter.showQRCodeImage();

        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.PrintQRCodesActivity_print_qr_codes));
    }

    private void setListeners() {
        printQrCodes.setOnClickListener(view -> presenter.onClickPrintQRCodes());
        sendPdfByEmail.setOnClickListener(view -> presenter.onClickSendPdfByEmail());
        skip.setOnClickListener(view -> presenter.onClickSkipButton());
    }

    @Override
    public void showQRCodeImage(Bitmap qrCode) {
        qrCodeImage.setImageBitmap(qrCode);
    }

    @Override
    public void openPDF(String fileName) {
        Uri pdfUri = getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", FileManager.getPdfFile(fileName));
        Intent pdfDocumentIntent = new Intent(Intent.ACTION_VIEW);
        pdfDocumentIntent.setDataAndType(pdfUri, "application/pdf");
        pdfDocumentIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        pdfDocumentIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(pdfDocumentIntent);
    }

    @Override
    public void sendPDFByEmail(String fileName) {
        Uri pdfUri = getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", FileManager.getPdfFile(fileName));
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        if (pdfUri != null) {
            emailIntent.putExtra(Intent.EXTRA_STREAM, pdfUri);
        }
        startActivity(Intent.createChooser(emailIntent, ""));
    }

    @Override
    public void navigateToMainActivity() {
        Intent mainActivityIntent = new Intent(context, MainActivity.class);
        startActivity(mainActivityIntent);
        CustomIntent.customType(this, "fadein-to-fadeout");
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.print_qr_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_print_codes:
                presenter.onClickPrintQRCodes();
                return true;
            case R.id.action_send_email:
                presenter.onClickSendPdfByEmail();
                return true;
            case R.id.action_skip:
                presenter.onClickSkipButton();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            presenter.onClickPrintQRCodes();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            presenter.navigateToMainActivity();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this, "fadein-to-fadeout");
    }
}