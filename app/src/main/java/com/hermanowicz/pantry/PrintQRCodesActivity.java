/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.hermanowicz.pantry.presenters.PrintQRCodesActivityPresenter;
import com.hermanowicz.pantry.views.PrintQRCodesActivityView;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;

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
public class PrintQRCodesActivity extends AppCompatActivity implements PrintQRCodesActivityView {

    private Context context;
    private Resources resources;
    private ArrayList<String> textToQRCode, namesOfProducts, expirationDates;
    private ArrayList<Bitmap> qrCodesBitmapArrayList;
    private PrintQRCodesActivityPresenter presenter;

    static final String PDF_FILENAME = "qrcodes-mypantry.pdf";
    static final int QR_CODE_WIDTH = 100;
    static final int QR_CODE_HEIGHT = 100;
    static final int APP_PERMISSIONS_EXTERNAL_STORAGE = 23;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.image_qrCode)
    ImageView image_qrCode;
    @BindView(R.id.button_printQRCodes)
    Button button_printQRCodes;
    @BindView(R.id.button_sendPdfByEmail)
    Button button_sendPdfByEmail;
    @BindView(R.id.button_skip)
    Button button_skipPrinting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_qrcodes);

        ButterKnife.bind(this);

        context = PrintQRCodesActivity.this;
        resources = context.getResources();
        textToQRCode = getIntent().getStringArrayListExtra("text_to_qr_code");
        namesOfProducts = getIntent().getStringArrayListExtra("names_of_products");
        expirationDates = getIntent().getStringArrayListExtra("expiration_dates");
        qrCodesBitmapArrayList = new ArrayList<>();

        presenter = new PrintQRCodesActivityPresenter(this, null);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(resources.getString(R.string.PrintQRCodesActivity_print_qr_codes));

        try {
            image_qrCode.setImageBitmap(generateQRCode(textToQRCode.get(0)));
        } catch (WriterException e) {
            Toast.makeText(context, resources.getString(R.string.Errors_error) + "WriterException" + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        button_printQRCodes.setOnClickListener(v -> checkPermissionsAndCreatePDF());

        button_sendPdfByEmail.setOnClickListener(v -> checkPermissionsAndCreatePDF());

        button_skipPrinting.setOnClickListener(v -> {
            Intent newProductActivityIntent = new Intent(context, MainActivity.class);
            startActivity(newProductActivityIntent);
            finish();
        });

    }

    private void checkPermissionsAndCreatePDF(){
        try {
            for (int counter =  0; counter < textToQRCode.size(); counter++){
                qrCodesBitmapArrayList.add(generateQRCode(textToQRCode.get(counter)));
            }
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(PrintQRCodesActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    Toast.makeText(context, resources.getString(R.string.Errors_permission_is_needed_to_save_the_file), Toast.LENGTH_LONG).show();

                    ActivityCompat.requestPermissions(PrintQRCodesActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            APP_PERMISSIONS_EXTERNAL_STORAGE);
                } else {
                    ActivityCompat.requestPermissions(PrintQRCodesActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            APP_PERMISSIONS_EXTERNAL_STORAGE);
                }
            } else {
                createPDF(qrCodesBitmapArrayList, namesOfProducts, expirationDates);
            }

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private Bitmap generateQRCode(@NonNull String textToQRCode)
            throws WriterException {

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(textToQRCode, BarcodeFormat.QR_CODE, QR_CODE_WIDTH, QR_CODE_HEIGHT);
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        return barcodeEncoder.createBitmap(bitMatrix);
    }

    /**
     * The method to create pdf file with stickers for the products.
     *
     * @param qrCodes image with QR code.
     */
    private void createPDF(@NonNull ArrayList<Bitmap> qrCodes, @NonNull List<String> namesOfProducts, @NonNull List<String> expirationDates){

        int pageNumber         = 1;
        int widthCounter       = 0;
        int topCounter         = 0;
        int codesOnPageCounter = 0;

        PdfDocument pdfDocument = new PdfDocument();
        Paint       canvasPaint = new Paint();
        Paint       textPaint   = new Paint();

        canvasPaint.setColor(Color.WHITE);
        canvasPaint.setStyle(Paint.Style.FILL);
        textPaint  .setColor(Color.BLACK);
        textPaint  .setTextSize(8);

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, pageNumber).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        canvas.drawPaint(canvasPaint);
        for (int counter = 0; counter < qrCodes.size(); counter++){
            if(codesOnPageCounter == 49) {
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
            canvas.drawBitmap(qrCodes.get(counter),widthCounter*80, (topCounter*120), null);
            canvas.drawText(namesOfProducts.get(counter), (widthCounter*80)+20, (topCounter*120)+90, textPaint);
            canvas.drawText(expirationDates.get(counter), (widthCounter*80)+20, (topCounter*120)+100, textPaint);
            widthCounter++;
            codesOnPageCounter++;
        }
        pdfDocument.finishPage(page);
        try {
            FileOutputStream pdfOutputStream = new FileOutputStream(Environment.getExternalStorageDirectory() + File.separator + PDF_FILENAME, false);
            pdfDocument.writeTo(pdfOutputStream);
            pdfOutputStream.close();
            } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context,resources.getString(R.string.Errors_error) + "FileNotFoundException", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context,resources.getString(R.string.Errors_error) + "IOException", Toast.LENGTH_LONG).show();
        }
        pdfDocument.close();
        openPDF();
    }

    private void openPDF(){
        File pdfFile = new File(Environment.getExternalStorageDirectory(), PDF_FILENAME);
        Uri pdfUri = getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", pdfFile);
        Intent pdfDocumentIntent = new Intent(Intent.ACTION_SENDTO);
        pdfDocumentIntent.setDataAndType(pdfUri, "application/pdf");
        pdfDocumentIntent.putExtra(Intent.EXTRA_STREAM, pdfUri);
        pdfDocumentIntent.setData(Uri.parse("mailto:" + AppSettingsActivity.getEmailForNotifications(context)));
        pdfDocumentIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(pdfDocumentIntent);
    }

    private static ArrayList<String> createTextToQRCodeList(List<Product> productsList){
        ArrayList<String> textToQRCodeList = new ArrayList<>();

        JSONObject jsonObject = new JSONObject();
        for(int counter = 0; counter < productsList.size(); counter++) {
            try {
                jsonObject.put("product_id", productsList.get(counter).getID());
                jsonObject.put("hash_code", productsList.get(counter).hashCode());
                textToQRCodeList.add(jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return textToQRCodeList;
    }

    private static ArrayList<String> createNamesOfProductsList(List<Product> productsList){

        ArrayList<String> namesOfProductsList = new ArrayList<>();
        String productName;

        for(int counter = 0; counter < productsList.size(); counter++) {
            productName = productsList.get(counter).getName();
            if(productName.length() > 15) {
                namesOfProductsList.add(productName.substring(0, 14) + ".");
            }
            else{
                namesOfProductsList.add(productName);
            }
        }
        return namesOfProductsList;
    }

    private static ArrayList<String> createExpirationDatesList(List<Product> productsList){
        ArrayList<String> expirationDatesList = new ArrayList<>();
        for(int counter = 0; counter < productsList.size(); counter++) {
            expirationDatesList.add(productsList.get(counter).getExpirationDate());
        }
        return expirationDatesList;
    }

    public static Intent createPrintQRCodesActivityIntent(@NonNull Context context, @NonNull List<Product> productsList){

        ArrayList<String> textToQRCodeList    = createTextToQRCodeList(productsList);
        ArrayList<String> namesOfProductsList = createNamesOfProductsList(productsList);
        ArrayList<String> expirationDatesList = createExpirationDatesList(productsList);

        Intent printQRCodesActivityIntent = new Intent(context, PrintQRCodesActivity.class);

        printQRCodesActivityIntent.putStringArrayListExtra("text_to_qr_code", textToQRCodeList);
        printQRCodesActivityIntent.putStringArrayListExtra("expiration_dates", expirationDatesList);
        printQRCodesActivityIntent.putStringArrayListExtra("names_of_products", namesOfProductsList);

        return printQRCodesActivityIntent;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case APP_PERMISSIONS_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createPDF(qrCodesBitmapArrayList, namesOfProducts, expirationDates);
                } else {
                    Toast.makeText(context, resources.getString(R.string.Errors_permission_is_needed_to_save_the_file), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent newProductActivityIntent = new Intent(context, MainActivity.class);
            startActivity(newProductActivityIntent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}