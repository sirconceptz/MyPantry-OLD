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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
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
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
    private DatabaseManager                    db;
    private Spinner                            productTypeSpinner, productFeaturesSpinner;
    private EditText                           name, expirationDate, productionDate, quantity, composition,
                                               healingProperties, dosage, volume, weight;
    private CheckBox                           hasSugar, hasSalt;
    private RadioButton                        isSweet, isSour, isSweetAndSour, isBitter, isSalty;
    private String                             selectedProductType, taste, productionDateValue,
                                               expirationDateValue, productFeatures;
    private String[]                           productTypesArray;
    private int                                howManyProductsToAdd = 0, day, month, year,
                                               volumeValue, weightValue;
    private boolean                            isTypeOfProductTouched;
    private Calendar                           calendar;
    private DatePickerDialog.OnDateSetListener productionDateListener, expirationDateListener;
    private ArrayAdapter<CharSequence>         productFeaturesAdapter;
    private AdView                             adView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        context                = NewProductActivity.this;
        db                     = new DatabaseManager(context);
        Toolbar toolbar        = findViewById(R.id.toolbar);
        name                   = findViewById(R.id.edittext_name);
        productTypeSpinner     = findViewById(R.id.spinner_productType);
        productFeaturesSpinner = findViewById(R.id.spinner_productFeatures);
        expirationDate         = findViewById(R.id.edittext_expirationDate);
        productionDate         = findViewById(R.id.edittext_productionDate);
        quantity               = findViewById(R.id.edittext_quantity);
        composition            = findViewById(R.id.edittext_composition);
        healingProperties      = findViewById(R.id.edittext_healingProperties);
        dosage                 = findViewById(R.id.edittext_dosage);
        volume                 = findViewById(R.id.edittext_volume);
        weight                 = findViewById(R.id.edittext_weight);
        hasSugar               = findViewById(R.id.checkbox_hasSugar);
        hasSalt                = findViewById(R.id.checkbox_hasSugar);
        isSweet                = findViewById(R.id.radiobtn_isSweet);
        isSour                 = findViewById(R.id.radiobtn_isSour);
        isSweetAndSour         = findViewById(R.id.radiobtn_isSweetAndSour);
        isBitter               = findViewById(R.id.radiobtn_isBitter);
        isSalty                = findViewById(R.id.radiobtn_isSalty);
        TextView volumeLabel   = findViewById(R.id.text_volume);
        TextView weightLabel   = findViewById(R.id.text_weight);
        Button  addProduct     = findViewById(R.id.button_addProduct);
        adView                 = findViewById(R.id.adBanner);

        setSupportActionBar(toolbar);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-4025776034769422~3797748160");

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        quantity.setText("1");
        volume.setText("0");
        weight.setText("0");

        volumeLabel.setText(getResources().getString(R.string.ProductDetailsActivity_volume) + " (" + getResources().getString(R.string.ProductDetailsActivity_volume_unit) + ")");

        weightLabel.setText(getResources().getString(R.string.ProductDetailsActivity_weight) + " (" + getResources().getString(R.string.ProductDetailsActivity_weight_unit) + ")");

        name             .setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        composition      .setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        healingProperties.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        dosage           .setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_choose_array, android.R.layout.simple_spinner_item);
        productFeaturesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productFeaturesSpinner.setAdapter(productFeaturesAdapter);

        expirationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expirationDate.length() < 1) {
                    calendar = Calendar.getInstance();
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    year  = calendar.get(Calendar.YEAR);
                    month = calendar.get(Calendar.MONTH);
                    day   = calendar.get(Calendar.DAY_OF_MONTH);
                }
                else{
                    String[] splitedDate = MyPantryActivity.splitDate(expirationDate.getText().toString());
                    year  = Integer.valueOf(splitedDate[2]);
                    month = Integer.valueOf(splitedDate[1]);
                    day   = Integer.valueOf(splitedDate[0]);
                }

                DatePickerDialog dialog = new DatePickerDialog(
                        context,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        expirationDateListener,
                        year,month,day);
                dialog.getDatePicker().setMinDate(new Date().getTime());
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        expirationDateListener = new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                expirationDate.setText(day + "." + month + "." + year);
                expirationDateValue = year + "-" + month + "-" + day;
            }
        };

        productionDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productionDate.length() < 1) {
                    calendar = Calendar.getInstance();
                    year  = calendar.get(Calendar.YEAR);
                    month = calendar.get(Calendar.MONTH);
                    day   = calendar.get(Calendar.DAY_OF_MONTH);
                }
                else{
                    String[] splitedDate = MyPantryActivity.splitDate(productionDate.getText().toString());
                    year  = Integer.valueOf(splitedDate[2]);
                    month = Integer.valueOf(splitedDate[1]);
                    day   = Integer.valueOf(splitedDate[0]);
                }

                DatePickerDialog dialog = new DatePickerDialog(
                        context,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        productionDateListener,
                        year,month,day);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        productionDateListener = new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                productionDate.setText(day + "." + month + "." + year);
                productionDateValue = year + "-" + month + "-" + day;
            }
        };

        productTypeSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isTypeOfProductTouched = true;
                return false;
            }
        });

        productTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    if(isTypeOfProductTouched) {
                        selectedProductType = String.valueOf(productTypeSpinner.getSelectedItem());
                        productTypesArray = getResources().getStringArray(R.array.ProductDetailsActivity_type_of_product_array);
                        if (selectedProductType.equals(productTypesArray[0]))
                            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_choose_array, android.R.layout.simple_spinner_item);
                        else if (selectedProductType.equals(productTypesArray[1]))
                            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_store_products_array, android.R.layout.simple_spinner_item);
                        else if (selectedProductType.equals(productTypesArray[2]))
                            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_ready_meals_array, android.R.layout.simple_spinner_item);
                        else if (selectedProductType.equals(productTypesArray[3]))
                            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_vegetables_array, android.R.layout.simple_spinner_item);
                        else if (selectedProductType.equals(productTypesArray[4]))
                            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_fruits_array, android.R.layout.simple_spinner_item);
                        else if (selectedProductType.equals(productTypesArray[5]))
                            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_herbs_array, android.R.layout.simple_spinner_item);
                        else if (selectedProductType.equals(productTypesArray[6]))
                            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_liqueurs_array, android.R.layout.simple_spinner_item);
                        else if (selectedProductType.equals(productTypesArray[7]))
                            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_wines_type_array, android.R.layout.simple_spinner_item);
                        else if (selectedProductType.equals(productTypesArray[8]))
                            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_mushrooms_array, android.R.layout.simple_spinner_item);
                        else if (selectedProductType.equals(productTypesArray[9]))
                            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_vinegars_array, android.R.layout.simple_spinner_item);
                        else if (selectedProductType.equals(productTypesArray[10]))
                            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_other_products_array, android.R.layout.simple_spinner_item);

                        productFeaturesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        productFeaturesAdapter.notifyDataSetChanged();
                        productFeaturesSpinner.setAdapter(productFeaturesAdapter);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
        });

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parseQuantityProducts();
                parseVolumeProduct();
                parseWeightProduct();

                if (howManyProductsToAdd < 1){
                    quantity.setError(getResources().getString(R.string.Errors_set_correct_quantity));
                    Toast.makeText(context, getResources().getString(R.string.Errors_set_correct_quantity), Toast.LENGTH_LONG).show();
                }
                else{
                    if(TextUtils.isEmpty(name.getText())) {
                        name.setError(getResources().getString(R.string.Errors_product_name_is_required));
                        Toast.makeText(context, getResources().getString(R.string.Errors_product_name_is_required), Toast.LENGTH_LONG);
                    }
                    else {
                        if(productTypeSpinner.getSelectedItemId() > 0) {
                            if(TextUtils.isEmpty(expirationDate.getText())) {
                                expirationDate.setError(getResources().getString(R.string.Errors_expiration_date_is_required));
                                Toast.makeText(context, getResources().getString(R.string.Errors_expiration_date_is_required), Toast.LENGTH_LONG);
                            }
                            else {
                                setTaste();
                                if (productFeaturesSpinner.getSelectedItemId() > 0)
                                    productFeatures = String.valueOf(productFeaturesSpinner.getSelectedItem());
                                else
                                    productFeatures = getResources().getString(R.string.ProductDetailsActivity_not_selected);

                                List <Product> productList = new ArrayList<Product>() {};
                                for (int counter = 1; counter <= howManyProductsToAdd; counter++) {
                                    Product product = new Product.Builder()
                                            .setID(db.idOfLastProductInDB() + counter)
                                            .setName(name.getText().toString())
                                            .setHashCode("")
                                            .setTypeOfProduct(String.valueOf(productTypeSpinner.getSelectedItem()))
                                            .setProductFeatures(productFeatures)
                                            .setExpirationDate(expirationDateValue)
                                            .setProductionDate(productionDateValue)
                                            .setComposition(composition.getText().toString())
                                            .setHealingProperties(healingProperties.getText().toString())
                                            .setDosage(dosage.getText().toString())
                                            .setVolume(volumeValue)
                                            .setWeight(weightValue)
                                            .setHasSugar(Boolean.compare(hasSugar.isChecked(), false))
                                            .setHasSalt(Boolean.compare(hasSalt.isChecked(), false))
                                            .setTaste(taste)
                                            .createProduct();
                                    productList.add(product);
                                }
                                if(addProducts(productList)){
                                    startActivity(PrintQRCodesActivity.createPrintQRCodesActivityIntent(context, productList));
                                    finish();
                                }
                                else{
                                    Toast.makeText(context, getResources().getString(R.string.Errors_something_wrong), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                        else{
                            Toast.makeText(context, getResources().getString(R.string.Errors_category_not_selected), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
    }

    private void parseQuantityProducts(){
        try {
            howManyProductsToAdd = Integer.parseInt(quantity.getText().toString());
        }
        catch (NumberFormatException n){
            howManyProductsToAdd = 1;
        }
    }

    private void parseVolumeProduct(){
        try {
            volumeValue = Integer.parseInt(volume.getText().toString());
        }
        catch (NumberFormatException n){
            volumeValue = 0;
        }
    }

    private void parseWeightProduct(){
        try {
            weightValue = Integer.parseInt(weight.getText().toString());
        }
        catch (NumberFormatException n){
            weightValue = 0;
        }
    }

    private boolean addProducts(List<Product> productsList) {
        boolean isProductsAdded = false;
        Product product;
        for(int counter = 0; counter < productsList.size(); counter++) {
            product = productsList.get(counter);
            isProductsAdded = db.insertProductToDB(product);
            Notification.createNotification(context, product);
        }
        if (isProductsAdded) {
            if (howManyProductsToAdd > 1) {
                Toast.makeText(context, getResources().getString(R.string.NewProductActivity_products_added_successful), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, getResources().getString(R.string.NewProductActivity_product_added_successful), Toast.LENGTH_LONG).show();
            }
        }
        return isProductsAdded;
    }

    private void setTaste(){
        String[] filterTasteArray = getResources().getStringArray(R.array.ProductDetailsActivity_taste_array);
        if(!isSweet.isChecked() && !isSour.isChecked() && !isSweetAndSour.isChecked() && !isBitter.isChecked() && !isSalty.isChecked()) {
            taste = getResources().getString(R.string.ProductDetailsActivity_not_selected);
        }
        else if(isSweet.isChecked()){
            taste = filterTasteArray[1];
        }
        else if(isSour.isChecked()){
            taste = filterTasteArray[2];
        }
        else if(isSweetAndSour.isChecked()){
            taste = filterTasteArray[3];
        }
        else if(isBitter.isChecked()){
            taste = filterTasteArray[4];
        }
        else if(isSalty.isChecked()){
            taste = filterTasteArray[5];
        }
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

    @Override
    public void onResume() {
        super.onResume();

        adView.resume();
    }

    @Override
    public void onPause() {
        adView.pause();

        super.onPause();
    }

    @Override
    public void onDestroy() {
        adView.destroy();

        super.onDestroy();
    }
}