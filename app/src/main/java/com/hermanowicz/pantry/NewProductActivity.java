/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;


/**
 * <h1>NewProductActivity</h1>
 * Activity to add a new product. User can add a new product to the database.
 * In new product user can choose a type of product, taste, name, production and expiration dates,
 * composition, volume, weight and for specific products attributes like healing properties
 * or dosage. Product can have a sugar and a salt (checkbox). User can add more like 1 item after
 * giving quantity. After inserting a new product to database user will be asked (in different
 * activity) to print a QR code to scan in the future.
 *
 * @author  Mateusz Hermanowicz
 * @version 1.0
 * @since   1.0
 */
public class NewProductActivity extends AppCompatActivity implements OnItemSelectedListener, DatePickerDialog.OnDateSetListener {

    private Context                            context;
    private Spinner                            productTypeSpinner, productFeaturesSpinner;
    private EditText                           name, expirationDate, productionDate, quantity, composition,
                                               healingProperties, dosage, volume, weight;
    private TextView                           volumeLabel, weightLabel;
    private CheckBox                           hasSugar, hasSalt;
    private RadioButton                        isSweet, isSour, isSweetAndSour, isBitter, isSalty;
    private String                             selectedProductType, taste, productionDateValue,
                                               expirationDateValue, productFeatures;
    private String[]                           productTypesArray;
    private int                                howManyProductsToAdd = 0, day, month, year;
    private boolean                            isTypeOfProductTouched;
    private Calendar                           calendar;
    private DatePickerDialog.OnDateSetListener productionDateListener, expirationDateListener;
    private ArrayAdapter<CharSequence>         productFeaturesAdapter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        context                 = NewProductActivity.this;
        name                    = findViewById(R.id.NameValue);
        productTypeSpinner      = findViewById(R.id.ProductTypeSpinner);
        productFeaturesSpinner  = findViewById(R.id.ProductFeaturesSpinner);
        expirationDate          = findViewById(R.id.ExpirationDate);
        productionDate          = findViewById(R.id.ProductionDate);
        quantity                = findViewById(R.id.Quantity);
        composition             = findViewById(R.id.Composition);
        healingProperties       = findViewById(R.id.HealingProperties);
        dosage                  = findViewById(R.id.Dosage);
        volume                  = findViewById(R.id.Volume);
        weight                  = findViewById(R.id.Weight);
        hasSugar                = findViewById(R.id.HasSugar);
        hasSalt                 = findViewById(R.id.HasSalt);
        isSweet                 = findViewById(R.id.IsSweet);
        isSour                  = findViewById(R.id.IsSour);
        isSweetAndSour          = findViewById(R.id.IsSweetAndSour);
        isBitter                = findViewById(R.id.IsBitter);
        isSalty                 = findViewById(R.id.IsSalty);
        volumeLabel             = findViewById(R.id.VolumeLabel);
        weightLabel             = findViewById(R.id.WeightLabel);
        Toolbar toolbar         = findViewById(R.id.Toolbar);
        Button  addProduct      = findViewById(R.id.AddProduct);

        setSupportActionBar(toolbar);

    }


    @Override
    public void onItemSelected(@NonNull AdapterView<?> parent, @NonNull View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(@NonNull AdapterView<?> parent) {
    }

    @Override
    public void onDateSet(@NonNull DatePicker view, int year, int month, int dayOfMonth) {
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