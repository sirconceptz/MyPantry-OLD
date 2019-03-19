/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

/**
 * <h1>ProductDetailsActivity</h1>
 * Activity with details of product. Is opened after choose a product in MyPantryActivity or after
 * scanning the QR code of product.
 *
 * @author  Mateusz Hermanowicz
 * @version 1.0
 * @since   1.0
 */
public class ProductDetailsActivity extends AppCompatActivity {

    private Context         context;
    private DatabaseManager db;
    private Product         selectedProduct;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Intent myPantryActivityIntent = getIntent();
        int productID = myPantryActivityIntent.getIntExtra("product_id", 1) - 1;

        int hashCode;
        try {
            hashCode = myPantryActivityIntent.getIntExtra("hash_code", 0);
        }
        catch (NumberFormatException n){
            hashCode = 0;
        }

                 context           = getApplicationContext();
                 db                = new DatabaseManager(context);
                 selectedProduct   = db.getProductsFromDB("SELECT * FROM 'products' DESC").get(productID);
        Toolbar  toolbar           = findViewById(R.id.Toolbar);
        TextView typeOfProduct     = findViewById(R.id.ProductTypeValue);
        TextView productFeatures   = findViewById(R.id.ProductFeaturesValue);
        TextView expirationDate    = findViewById(R.id.ProductExpirationDateValue);
        TextView productionDate    = findViewById(R.id.ProductProductionDateValue);
        TextView composition       = findViewById(R.id.ProductCompositionValue);
        TextView healingProperties = findViewById(R.id.ProductHealingPropertiesValue);
        TextView dosage            = findViewById(R.id.ProductDosageValue);
        TextView volume            = findViewById(R.id.ProductVolumeValue);
        TextView weight            = findViewById(R.id.ProductWeightValue);
        TextView hasSugar          = findViewById(R.id.ProductHasSugarValue);
        TextView hasSalt           = findViewById(R.id.ProductHasSaltValue);
        TextView taste             = findViewById(R.id.ProductTasteValue);
        Button   printQRCode       = findViewById(R.id.PrintQRCode);
        Button   deleteButton      = findViewById(R.id.DeleteProductButton);

        setSupportActionBar(toolbar);

        if (hashCode == Integer.parseInt(selectedProduct.getHashCode())) {
            Objects.requireNonNull(getSupportActionBar()).setTitle(selectedProduct.getName());
            typeOfProduct.setText(selectedProduct.getTypeOfProduct());
            productFeatures.setText(selectedProduct.getProductFeatures());
            expirationDate.setText(MyPantryActivity.convertDate(selectedProduct.getExpirationDate()));
            productionDate.setText(MyPantryActivity.convertDate(selectedProduct.getProductionDate()));
            composition.setText(selectedProduct.getComposition());
            healingProperties.setText(selectedProduct.getHealingProperties());
            dosage.setText(selectedProduct.getDosage());
            volume.setText(selectedProduct.getVolume() + getResources().getString(R.string.ProductDetailsActivity_volume_unit));
            weight.setText(selectedProduct.getWeight() + getResources().getString(R.string.ProductDetailsActivity_weight_unit));
            taste.setText(selectedProduct.getTaste());
            if (selectedProduct.getHasSugar() == 1)
                hasSugar.setText(getResources().getString(R.string.ProductDetailsActivity_yes));
            else
                hasSugar.setText(getResources().getString(R.string.ProductDetailsActivity_no));
            if (selectedProduct.getHasSalt() == 1)
                hasSalt.setText(getResources().getString(R.string.ProductDetailsActivity_yes));
            else
                hasSalt.setText(getResources().getString(R.string.ProductDetailsActivity_no));
        } else {
            Toast.makeText(context, getResources().getString(R.string.Errors_wrong_data), Toast.LENGTH_LONG).show();
        }

        printQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ArrayList<Product> productArrayList = new ArrayList<>();
                    productArrayList.add(selectedProduct);
                    startActivity(PrintQRCodesActivity.createPrintQRCodesActivityIntent(context, productArrayList));
                    finish();
                }
                catch (Exception e){
                    Toast.makeText(context, getResources().getString(R.string.Errors_wrong_data), Toast.LENGTH_LONG).show();
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(db.deleteProductFromDB(selectedProduct.getID())){
                    Notification.cancelNotification(context, selectedProduct);
                    Toast.makeText(context, getResources().getString(R.string.ProductDetailsActivity_product_has_been_removed), Toast.LENGTH_LONG).show();
                    Intent myPantryActivityIntent = new Intent(context, MyPantryActivity.class);
                    startActivity(myPantryActivityIntent);
                    finish();
                    }
                }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent myPantryActivityIntent = new Intent(context, MyPantryActivity.class);
            startActivity(myPantryActivityIntent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}