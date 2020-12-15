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
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.hermanowicz.pantry.BuildConfig;
import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.databinding.ActivityPrintQrcodesBinding;
import com.hermanowicz.pantry.interfaces.PrintQRCodesView;
import com.hermanowicz.pantry.presenters.PrintQRCodesPresenter;
import com.hermanowicz.pantry.utils.Orientation;
import com.hermanowicz.pantry.utils.ThemeMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

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

    static final String PDF_FILENAME = "qrcodes-mypantry.pdf";

    private ActivityPrintQrcodesBinding binding;
    private PrintQRCodesPresenter presenter;
    private Context context;

    private ImageView qrCodeImage;
    private Button printQrCodes, sendPdfByEmail, skip;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(ThemeMode.getThemeMode(this));
        if(Orientation.isTablet(this))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        super.onCreate(savedInstanceState);
        initView();
        setListeners();
    }

    private void initView() {
        binding = ActivityPrintQrcodesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = getApplicationContext();

        qrCodeImage = binding.imageQrCode;
        printQrCodes = binding.buttonPrintQRCodes;
        sendPdfByEmail = binding.buttonSendPdfByEmail;
        skip = binding.buttonSkip;

        ArrayList<String> textToQRCodeArray = getIntent().getStringArrayListExtra("text_to_qr_code");
        ArrayList<String> namesOfProductsArray = getIntent().getStringArrayListExtra("names_of_products");
        ArrayList<String> expirationDatesArray = getIntent().getStringArrayListExtra("expiration_dates");

        presenter = new PrintQRCodesPresenter(this);

        presenter.setTextToQRCodeArray(textToQRCodeArray);
        presenter.setNamesOfProductsArray(namesOfProductsArray);
        presenter.setExpirationDatesArray(expirationDatesArray);
        presenter.setActivity(this);
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
    public void showPermissionsError() {
        Toast.makeText(context, getString(R.string.Error_permission_is_needed_to_save_the_file), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showQRCodeImage(Bitmap qrCode) {
        qrCodeImage.setImageBitmap(qrCode);
    }

    @Override
    public void openPDF() {
        File pdfFile = new File(Environment.getExternalStorageDirectory(), PDF_FILENAME);
        Uri pdfUri = getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", pdfFile);
        Intent pdfDocumentIntent = new Intent(Intent.ACTION_VIEW);
        pdfDocumentIntent.setDataAndType(pdfUri, "application/pdf");
        pdfDocumentIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        pdfDocumentIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(pdfDocumentIntent);
    }

    @Override
    public void sendPDFByEmail() {
        File pdfFile = new File(Environment.getExternalStorageDirectory(), PDF_FILENAME);
        Uri pdfUri = getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", pdfFile);
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
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            presenter.navigateToMainActivity();
        }
        return super.onKeyDown(keyCode, event);
    }
}