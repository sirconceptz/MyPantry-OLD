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

package com.hermanowicz.pantry.model;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfDocument;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.db.product.Product;
import com.hermanowicz.pantry.util.FileManager;
import com.hermanowicz.pantry.util.PermissionsHandler;
import com.hermanowicz.pantry.util.PrintQRData;

import java.util.ArrayList;
import java.util.List;

public class PrintQRCodesModel {

    private AppCompatActivity activity;
    private ArrayList<String> textToQRCodeArray;
    private ArrayList<String> namesOfProductsArray;
    private ArrayList<String> expirationDatesArray;
    private ArrayList<String> productionDatesArray;
    private List<Product> allProductList;
    private String pdfFileName;

    public PrintQRCodesModel (@NonNull AppCompatActivity activity) {
        this.activity = activity;
    }

    public void setActivity(@NonNull AppCompatActivity activity) {
        this.activity = activity;
    }

    public int getIdLastProductInDb() {
        int lastId = 0;
        if(allProductList != null) {
            int allProductListSize = allProductList.size();
            lastId = allProductList.get(allProductListSize - 1).getId();
        }
        return lastId;
    }

    public void setAllProductList(@NonNull List<Product> productList) {
        this.allProductList = productList;
    }

    public void setProductList(@Nullable List<Product> productList) {
        textToQRCodeArray = PrintQRData.getTextToQRCodeList(productList, getIdLastProductInDb());
        namesOfProductsArray = PrintQRData.getNamesOfProductsList(productList);
        expirationDatesArray = PrintQRData.getExpirationDatesList(productList);
        productionDatesArray = PrintQRData.getProductionDatesList(productList);
    }

    public String getPdfFileName(){
        return pdfFileName;
    }

    public Bitmap getThumb() {
        return PrintQRData.getBitmapQRCode(textToQRCodeArray.get(0), false);
    }

    public void createAndSavePDF() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        boolean isBigQrCodePrint = sharedPreferences.getBoolean(activity.getString(R.string.PreferencesKey_qr_code_size), true);
        pdfFileName = FileManager.generatePdfFileName();
        ArrayList<Bitmap> qrCodeBitmapArray = PrintQRData.getQrCodeBitmapArray(textToQRCodeArray, isBigQrCodePrint);
        PdfDocument pdfDocument;
        if(isBigQrCodePrint)
            pdfDocument = FileManager.createPdfDocumentBigQrCodes(qrCodeBitmapArray, namesOfProductsArray, expirationDatesArray, productionDatesArray);
        else
            pdfDocument = FileManager.createPdfDocumentSmallQrCodes(qrCodeBitmapArray, namesOfProductsArray, expirationDatesArray);
        FileManager.savePdf(activity, pdfDocument, pdfFileName);
        pdfDocument.close();
    }

    public boolean isWritePermission() {
        return PermissionsHandler.isWritePermission(activity);
    }

    public void requestWritePermission(){
        PermissionsHandler.requestWritePermission(activity);
    }
}