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

package com.hermanowicz.pantry.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;

import androidx.annotation.NonNull;

import java.util.ArrayList;

/**
 * <h1>FileManager</h1>
 * Class used to create and manage PDF data with printable QR codes
 *
 * @author Mateusz Hermanowicz
 */

public class PdfData {

    PdfDocument pdfDocument = new PdfDocument();
    Paint canvasPaint = new Paint();
    Paint textPaint = new Paint();
    private int pageNumber = 1;
    private int widthCounter = 0;
    private int topCounter = 0;
    private int codesOnPageCounter = 0;
    private android.graphics.pdf.PdfDocument.PageInfo pageInfo;
    private android.graphics.pdf.PdfDocument.Page page;
    private Canvas canvas;

    public PdfData() {
        canvasPaint.setColor(Color.WHITE);
        canvasPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(8);
        pageInfo = new PdfDocument.PageInfo.Builder(595, 842, pageNumber).create();
        page = pdfDocument.startPage(pageInfo);
        canvas = page.getCanvas();
        canvas.drawPaint(canvasPaint);
    }

    private void createNewPage() {
        pageNumber++;
        pageInfo = new android.graphics.pdf.PdfDocument.PageInfo.Builder(595, 842, pageNumber).create();
        pdfDocument.finishPage(page);
        page = pdfDocument.startPage(pageInfo);
        canvas = page.getCanvas();
        canvas.drawPaint(canvasPaint);
        codesOnPageCounter = 0;
        widthCounter = 0;
        topCounter = 0;
    }

    public PdfDocument createPdfDocumentBigQrCodes(@NonNull ArrayList<Bitmap> qrCodesArray,
                                                   @NonNull ArrayList<String> namesOfProductsArray,
                                                   @NonNull ArrayList<String> expirationDatesArray,
                                                   @NonNull ArrayList<String> productionDatesArray) {

        for (int counter = 0; counter < qrCodesArray.size(); counter++) {
            if (codesOnPageCounter == 20) {
                createNewPage();
            }
            if (widthCounter == 4) {
                topCounter++;
                widthCounter = 0;
            }
            canvas.drawBitmap(qrCodesArray.get(counter), 25 + (widthCounter * 141), (64 + topCounter * 142), null);
            canvas.drawText(namesOfProductsArray.get(counter), (widthCounter * 141) + 40, (64 + topCounter * 142) + 112, textPaint);
            canvas.drawText("EXP: " + expirationDatesArray.get(counter), (widthCounter * 141) + 40, (64 + topCounter * 142) + 122, textPaint);
            canvas.drawText("PRO: " + productionDatesArray.get(counter), (widthCounter * 141) + 40, (64 + topCounter * 142) + 132, textPaint);
            widthCounter++;
            codesOnPageCounter++;
        }
        pdfDocument.finishPage(page);
        return pdfDocument;
    }

    public PdfDocument createPdfDocumentSmallQrCodes(@NonNull ArrayList<Bitmap> qrCodesArray,
                                                     @NonNull ArrayList<String> namesOfProductsArray,
                                                     @NonNull ArrayList<String> expirationDatesArray) {

        for (int counter = 0; counter < qrCodesArray.size(); counter++) {
            if (codesOnPageCounter == 49) {
                createNewPage();
            }
            if (widthCounter == 7) {
                topCounter++;
                widthCounter = 0;
            }
            canvas.drawBitmap(qrCodesArray.get(counter), widthCounter * 80, (topCounter * 120), null);
            canvas.drawText(namesOfProductsArray.get(counter), (widthCounter * 80) + 20, (topCounter * 120) + 90, textPaint);
            canvas.drawText("EXP: " + expirationDatesArray.get(counter), (widthCounter * 80) + 20, (topCounter * 120) + 100, textPaint);
            widthCounter++;
            codesOnPageCounter++;
        }
        pdfDocument.finishPage(page);
        return pdfDocument;
    }
}