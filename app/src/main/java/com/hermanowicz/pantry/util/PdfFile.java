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

import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class PdfFile {

    public static final String PDF_PATH = "Download/MyPantry";

    public static void savePdf(@NonNull AppCompatActivity activity, @NonNull PdfDocument pdfDocument,
                               @NonNull String fileName) {
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                OutputStream fos;
                ContentResolver resolver = activity.getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.Files.FileColumns.DISPLAY_NAME, fileName);
                contentValues.put(MediaStore.Files.FileColumns.MIME_TYPE, "application/pdf");
                contentValues.put(MediaStore.Files.FileColumns.RELATIVE_PATH, PDF_PATH);
                Uri pdfUri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues);
                fos = resolver.openOutputStream(pdfUri);
                pdfDocument.writeTo(fos);
                fos.close();
            } else {
                FileOutputStream pdfOutputStream = new FileOutputStream(Environment.getExternalStorageDirectory()
                        + File.separator + fileName, false);
                pdfDocument.writeTo(pdfOutputStream);
                pdfOutputStream.close();
            }

        } catch (IOException e) {
            Log.e("Can't save the PDF file", e.toString());
        }
    }

    public static String generatePdfFileName() {
        return DateHelper.getTimeStamp() + ".pdf";
    }

    public static File getPdfFile(@NonNull String fileName) {
        File pdfFile;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            pdfFile = new File(Environment.getExternalStorageDirectory() + File.separator
                    + "Download/MyPantry", fileName);
        } else {
            pdfFile = new File(Environment.getExternalStorageDirectory() + File.separator, fileName);
        }
        return pdfFile;
    }
}