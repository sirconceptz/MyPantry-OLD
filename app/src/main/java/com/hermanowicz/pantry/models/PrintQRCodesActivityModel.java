/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry.models;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.hermanowicz.pantry.PermissionsHandlerAndroid;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class PrintQRCodesActivityModel {

    private static final String PDF_FILENAME = "qrcodes-mypantry.pdf";
    private static final int QR_CODE_WIDTH = 100;
    private static final int QR_CODE_HEIGHT = 100;
    private ArrayList<String> textToQRCodeArray;
    private ArrayList<String> namesOfProductsArray;
    private ArrayList<String> expirationDatesArray;
    private AppCompatActivity activity;

    public void setTextToQRCodeArray(ArrayList<String> textToQRCodeArray) {
        this.textToQRCodeArray = textToQRCodeArray;
    }

    public void setNamesOfProductsArray(ArrayList<String> namesOfProductsArray) {
        this.namesOfProductsArray = namesOfProductsArray;
    }

    public void setExpirationDatesArray(ArrayList<String> expirationDatesArray) {
        this.expirationDatesArray = expirationDatesArray;
    }

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public boolean checkWritePermission() {
        PermissionsHandlerAndroid permissionHandler = new PermissionsHandlerAndroid();
        return permissionHandler.checkHasPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public void requestWritePermission() {
        PermissionsHandlerAndroid permissionHandler = new PermissionsHandlerAndroid();
        permissionHandler.requestPermission(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                23);
    }

    public Bitmap generateQRCode(String textToQrCode) {

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = qrCodeWriter.encode(textToQrCode, BarcodeFormat.QR_CODE, QR_CODE_WIDTH, QR_CODE_HEIGHT);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        return barcodeEncoder.createBitmap(bitMatrix);
    }

    public void createAndSavePDF() {
        int pageNumber = 1;
        int widthCounter = 0;
        int topCounter = 0;
        int codesOnPageCounter = 0;
        ArrayList<Bitmap> qrCodesArray = createQrCodeBitmapArray();

        PdfDocument pdfDocument = new PdfDocument();
        Paint canvasPaint = new Paint();
        Paint textPaint = new Paint();

        canvasPaint.setColor(Color.WHITE);
        canvasPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(8);

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, pageNumber).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        canvas.drawPaint(canvasPaint);
        for (int counter = 0; counter < qrCodesArray.size(); counter++) {
            if (codesOnPageCounter == 49) {
                pageNumber++;
                pageInfo = new PdfDocument.PageInfo.Builder(595, 842, pageNumber).create();
                pdfDocument.finishPage(page);
                page = pdfDocument.startPage(pageInfo);
                canvas = page.getCanvas();
                canvas.drawPaint(canvasPaint);
                codesOnPageCounter = 0;
                widthCounter = 0;
                topCounter = 0;
            }
            if (widthCounter == 7) {
                topCounter++;
                widthCounter = 0;
            }
            canvas.drawBitmap(qrCodesArray.get(counter), widthCounter * 80, (topCounter * 120), null);
            canvas.drawText(namesOfProductsArray.get(counter), (widthCounter * 80) + 20, (topCounter * 120) + 90, textPaint);
            canvas.drawText(expirationDatesArray.get(counter), (widthCounter * 80) + 20, (topCounter * 120) + 100, textPaint);
            widthCounter++;
            codesOnPageCounter++;
        }
        pdfDocument.finishPage(page);
        try {
            FileOutputStream pdfOutputStream = new FileOutputStream(Environment.getExternalStorageDirectory() + File.separator + PDF_FILENAME, false);
            pdfDocument.writeTo(pdfOutputStream);
            pdfOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        pdfDocument.close();
    }

    private ArrayList<Bitmap> createQrCodeBitmapArray() {
        ArrayList<Bitmap> qrCodeBitmapArray = new ArrayList<>();
        for (int i = 0; i < textToQRCodeArray.size(); i++) {
            qrCodeBitmapArray.add(generateQRCode(textToQRCodeArray.get(i)));
        }
        return qrCodeBitmapArray;
    }
}