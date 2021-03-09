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
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.hermanowicz.pantry.BuildConfig;
import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.databinding.ActivityPrintQrcodesBinding;
import com.hermanowicz.pantry.db.product.Product;
import com.hermanowicz.pantry.interfaces.PrintQRCodesView;
import com.hermanowicz.pantry.presenter.PrintQRCodesPresenter;
import com.hermanowicz.pantry.util.Orientation;
import com.hermanowicz.pantry.util.ThemeMode;

import java.io.File;
import java.io.Serializable;
import java.util.List;
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

    private ActivityPrintQrcodesBinding binding;
    private PrintQRCodesPresenter presenter;
    private Context context;

    private ImageView qrCodeImage;
    private Button printQrCodes, sendPdfByEmail, addPhoto, skip;
    private List<Product> productList;

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
        addPhoto = binding.buttonAddPhoto;
        sendPdfByEmail = binding.buttonSendPdfByEmail;
        skip = binding.buttonSkip;

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
        addPhoto.setOnClickListener(view -> presenter.onClickAddPhoto(productList));
        skip.setOnClickListener(view -> presenter.onClickSkipButton());
    }

    @Override
    public void showQRCodeImage(Bitmap qrCode) {
        qrCodeImage.setImageBitmap(qrCode);
    }

    @Override
    public void openPDF(String fileName) {
        File pdfFile = new File(Environment.getExternalStorageDirectory() + File.separator
                + "Download/MyPantry", fileName);
        Uri pdfUri = getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", pdfFile);
        Intent pdfDocumentIntent = new Intent(Intent.ACTION_VIEW);
        pdfDocumentIntent.setDataAndType(pdfUri, "application/pdf");
        pdfDocumentIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        pdfDocumentIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(pdfDocumentIntent);
    }

    @Override
    public void sendPDFByEmail(String fileName) {
        File pdfFile = new File(Environment.getExternalStorageDirectory() + File.separator
                + "Download/MyPantry", fileName);
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
    public void navigateToAddPhoto(List<Product> productList) {
        Intent addPhotoActivityIntent = new Intent(context, AddPhotoActivity.class)
                .putExtra("PRODUCT_LIST", (Serializable) productList);
        startActivity(addPhotoActivityIntent);
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            presenter.navigateToMainActivity();
        }
        return super.onKeyDown(keyCode, event);
    }
}