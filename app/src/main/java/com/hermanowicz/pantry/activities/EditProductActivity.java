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

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.db.Product;
import com.hermanowicz.pantry.db.ProductDb;
import com.hermanowicz.pantry.interfaces.EditProductView;
import com.hermanowicz.pantry.interfaces.ProductDataView;
import com.hermanowicz.pantry.presenters.EditProductPresenter;
import com.hermanowicz.pantry.utils.DateHelper;
import com.hermanowicz.pantry.utils.Orientation;

import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditProductActivity extends AppCompatActivity implements EditProductView, ProductDataView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.edittext_name)
    EditText name;
    @BindView(R.id.spinner_productType)
    Spinner productTypeSpinner;
    @BindView(R.id.spinner_productFeatures)
    Spinner productFeaturesSpinner;
    @BindView(R.id.edittext_expirationDate)
    EditText expirationDate;
    @BindView(R.id.edittext_productionDate)
    EditText productionDate;
    @BindView(R.id.edittext_composition)
    EditText composition;
    @BindView(R.id.edittext_healingProperties)
    EditText healingProperties;
    @BindView(R.id.edittext_dosage)
    EditText dosage;
    @BindView(R.id.edittext_volume)
    EditText volume;
    @BindView(R.id.edittext_weight)
    EditText weight;
    @BindView(R.id.checkbox_hasSugar)
    CheckBox hasSugar;
    @BindView(R.id.checkbox_hasSalt)
    CheckBox hasSalt;
    @BindView(R.id.radiogroup_taste)
    RadioGroup tasteGroup;
    @BindView(R.id.radiobtn_isSweet)
    RadioButton isSweet;
    @BindView(R.id.radiobtn_isSour)
    RadioButton isSour;
    @BindView(R.id.radiobtn_isSweetAndSour)
    RadioButton isSweetAndSour;
    @BindView(R.id.radiobtn_isSalty)
    RadioButton isSalty;
    @BindView(R.id.radiobtn_isBitter)
    RadioButton isBitter;
    @BindView(R.id.text_volumeLabel)
    TextView volumeLabel;
    @BindView(R.id.text_weightLabel)
    TextView weightLabel;
    @BindView(R.id.adBanner)
    AdView adView;

    private Context context;
    private Resources resources;
    private int productId;
    private DatePickerDialog.OnDateSetListener productionDateListener, expirationDateListener;
    private ArrayAdapter<CharSequence> typeOfProductAdapter, productFeaturesAdapter;
    private int day, month, year;
    private boolean isTypeOfProductTouched;

    private EditProductPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstantState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        if(Orientation.isTablet(this))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        super.onCreate(savedInstantState);
        setContentView(R.layout.activity_edit_product);
        ButterKnife.bind(this);

        getDataFromIntent();
        init();

        typeOfProductAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_type_of_product_array, android.R.layout.simple_spinner_item);
        typeOfProductAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productTypeSpinner.setAdapter(typeOfProductAdapter);

        productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_choose_array, android.R.layout.simple_spinner_item);
        productFeaturesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productFeaturesSpinner.setAdapter(productFeaturesAdapter);

        presenter.setProduct(productId);

        expirationDate.setOnClickListener(v -> {
            if (expirationDate.length() <= 1) {
                year = DateHelper.getActualYear();
                month = DateHelper.getActualMonth();
                day = DateHelper.getActualDay(1);
            } else {
                int[] expirationDateArray = presenter.getExpirationDateArray();
                year = expirationDateArray[0];
                month = expirationDateArray[1];
                day = expirationDateArray[2];
            }
            DatePickerDialog dialog = new DatePickerDialog(
                    context,
                    R.style.AppThemeDatePicker,
                    expirationDateListener,
                    year, month, day);
            dialog.getDatePicker().setMinDate(new Date().getTime());
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });

        expirationDateListener = (datePicker, year, month, day) -> {
            presenter.showExpirationDate(day, month, year);
            presenter.setExpirationDate(year, month, day);
        };

        productionDate.setOnClickListener(v -> {
            if (productionDate.length() <= 1) {
                year = DateHelper.getActualYear();
                month = DateHelper.getActualMonth();
                day = DateHelper.getActualDay(0);
            } else {
                int[] productionDateArray = presenter.getProductionDateArray();
                year = productionDateArray[0];
                month = productionDateArray[1];
                day = productionDateArray[2];
            }
            DatePickerDialog dialog = new DatePickerDialog(
                    context,
                    R.style.AppThemeDatePicker,
                    productionDateListener,
                    year, month, day);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });

        productionDateListener = (datePicker, year, month, day) -> {
            presenter.showProductionDate(day, month, year);
            presenter.setProductionDate(year, month, day);
        };

        productTypeSpinner.setOnTouchListener((v, event) -> {
            isTypeOfProductTouched = true;
            return false;
        });

        productTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(isTypeOfProductTouched) {
                    String typeOfProductValue = String.valueOf(productTypeSpinner.getSelectedItem());
                    presenter.updateProductFeaturesAdapter(typeOfProductValue);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void init(){
        context = EditProductActivity.this;
        resources = context.getResources();
        presenter = new EditProductPresenter(this, this, ProductDb.getInstance(context),resources);

        MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.admob_ad_id));

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        setSupportActionBar(toolbar);

        volumeLabel.setText(String.format("%s (%s)", getString(R.string.ProductDetailsActivity_volume), getString(R.string.ProductDetailsActivity_volume_unit)));

        weightLabel.setText(String.format("%s (%s)", getString(R.string.ProductDetailsActivity_weight), getString(R.string.ProductDetailsActivity_weight_unit)));

        name.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        composition.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        healingProperties.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        dosage.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
    }

    private void getDataFromIntent() {
        Intent myPantryActivityIntent = getIntent();
        productId = myPantryActivityIntent.getIntExtra("product_id", 1);
    }

    @OnClick(R.id.button_saveProduct)
    void onClickSaveProductButton(){
        int selectedTasteId = tasteGroup.getCheckedRadioButtonId();
        RadioButton taste = findViewById(selectedTasteId);

        Product product = presenter.getProduct();
        product.setName(name.getText().toString());
        product.setTypeOfProduct(String.valueOf(productTypeSpinner.getSelectedItem()));
        product.setProductFeatures(String.valueOf(productFeaturesSpinner.getSelectedItem()));
        product.setComposition(composition.getText().toString());
        product.setHealingProperties(healingProperties.getText().toString());
        product.setDosage(dosage.getText().toString());
        product.setVolume(Integer.parseInt(volume.getText().toString()));
        product.setWeight(Integer.parseInt(weight.getText().toString()));
        product.setHasSugar(hasSugar.isChecked());
        product.setHasSalt(hasSalt.isChecked());
        presenter.setTaste(taste);
        presenter.saveProduct(product);
    }

    @OnClick(R.id.button_cancel)
    void onClickCancelButton(){
        presenter.navigateToMyPantryActivity();
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            presenter.navigateToMyPantryActivity();
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

    @Override
    public void setSpinnerSelections(int typeOfProductPosition, int productFeaturesPosition) {
        productTypeSpinner.setSelection(typeOfProductPosition);
        updateProductFeaturesAdapter(String.valueOf(productTypeSpinner.getSelectedItem()));
        productFeaturesSpinner.setSelection(productFeaturesPosition);
    }

    @Override
    public void showProductData(Product product) {
        DateHelper expirationDateString = new DateHelper(product.getExpirationDate());
        DateHelper productionDateString = new DateHelper(product.getProductionDate());
        String[] tasteArray = resources.getStringArray(R.array.ProductDetailsActivity_taste_array);

        name.setText(product.getName());
        expirationDate.setText(expirationDateString.getDateInLocalFormat());
        productionDate.setText(productionDateString.getDateInLocalFormat());
        composition.setText(product.getComposition());
        healingProperties.setText(product.getHealingProperties());
        dosage.setText(product.getDosage());
        volume.setText(String.valueOf(product.getVolume()));
        weight.setText(String.valueOf(product.getWeight()));
        hasSugar.setChecked(product.getHasSugar());
        hasSalt.setChecked(product.getHasSalt());

        if(product.getTaste().equals(tasteArray[0]))
            isSweet.setChecked(true);
        else if(product.getTaste().equals(tasteArray[1]))
            isSour.setChecked(true);
        else if(product.getTaste().equals(tasteArray[2]))
            isSweetAndSour.setChecked(true);
        else if(product.getTaste().equals(tasteArray[3]))
            isSalty.setChecked(true);
        else if(product.getTaste().equals(tasteArray[4]))
            isBitter.setChecked(true);
    }

    @Override
    public void onSavedProduct() {
        Toast.makeText(context, getString(R.string.EditProductActivity_product_was_updated), Toast.LENGTH_LONG).show();
    }

    @Override
    public void navigateToMyPantryActivity() {
        Intent myPantryActivityIntent = new Intent(context, MyPantryActivity.class);
        startActivity(myPantryActivityIntent);
    }

    @Override
    public void updateProductFeaturesAdapter(String typeOfProductSpinnerValue) {
        String[] productTypesArray = resources.getStringArray(R.array.ProductDetailsActivity_type_of_product_array);
        if (typeOfProductSpinnerValue.equals(productTypesArray[1]))
            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_store_products_array, android.R.layout.simple_spinner_item);
        else if (typeOfProductSpinnerValue.equals(productTypesArray[2]))
            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_ready_meals_array, android.R.layout.simple_spinner_item);
        else if (typeOfProductSpinnerValue.equals(productTypesArray[3]))
            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_vegetables_array, android.R.layout.simple_spinner_item);
        else if (typeOfProductSpinnerValue.equals(productTypesArray[4]))
            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_fruits_array, android.R.layout.simple_spinner_item);
        else if (typeOfProductSpinnerValue.equals(productTypesArray[5]))
            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_herbs_array, android.R.layout.simple_spinner_item);
        else if (typeOfProductSpinnerValue.equals(productTypesArray[6]))
            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_liqueurs_array, android.R.layout.simple_spinner_item);
        else if (typeOfProductSpinnerValue.equals(productTypesArray[7]))
            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_wines_type_array, android.R.layout.simple_spinner_item);
        else if (typeOfProductSpinnerValue.equals(productTypesArray[8]))
            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_mushrooms_array, android.R.layout.simple_spinner_item);
        else if (typeOfProductSpinnerValue.equals(productTypesArray[9]))
            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_vinegars_array, android.R.layout.simple_spinner_item);
        else if (typeOfProductSpinnerValue.equals(productTypesArray[10]))
            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_chemical_products_array, android.R.layout.simple_spinner_item);
        else if (typeOfProductSpinnerValue.equals(productTypesArray[11]))
            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_other_products_array, android.R.layout.simple_spinner_item);

        productFeaturesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productFeaturesAdapter.notifyDataSetChanged();
        productFeaturesSpinner.setAdapter(productFeaturesAdapter);
    }

    @Override
    public void showExpirationDate(String date) {
        expirationDate.setText(date);
    }

    @Override
    public void showProductionDate(String date) {
        productionDate.setText(date);
    }

    @Override
    public void showErrorNameNotSet() {
        name.setError(getString(R.string.Errors_product_name_is_required));
        Toast.makeText(context, getString(R.string.Errors_product_name_is_required), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showErrorCategoryNotSelected() {
        Toast.makeText(context, getString(R.string.Errors_category_not_selected), Toast.LENGTH_LONG).show();
    }
}