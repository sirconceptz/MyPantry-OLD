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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.databinding.ActivityEditProductBinding;
import com.hermanowicz.pantry.db.product.Product;
import com.hermanowicz.pantry.interfaces.EditProductView;
import com.hermanowicz.pantry.interfaces.ProductDataView;
import com.hermanowicz.pantry.model.GroupProducts;
import com.hermanowicz.pantry.model.ProductDataModel;
import com.hermanowicz.pantry.presenter.EditProductPresenter;
import com.hermanowicz.pantry.util.DateHelper;
import com.hermanowicz.pantry.util.Orientation;
import com.hermanowicz.pantry.util.ThemeMode;

import java.util.Date;
import java.util.Objects;

import maes.tech.intentanim.CustomIntent;

/**
 * <h1>EditProductActivity</h1>
 * Activity for edit product details.
 *
 * @author  Mateusz Hermanowicz
 * @version 1.0
 * @since   1.3
 */

public class EditProductActivity extends AppCompatActivity implements EditProductView, ProductDataView {

    private ActivityEditProductBinding binding;
    private EditProductPresenter presenter;
    private Context context;
    private Resources resources;
    private DatePickerDialog.OnDateSetListener productionDateListener, expirationDateListener;
    private ArrayAdapter<CharSequence> productCategoryAdapter, productStorageLocationAdapter;
    private int day, month, year;
    private boolean isTypeOfProductTouched;

    private Spinner productType, productCategory, productStorageLocation;
    private EditText productName, productExpirationDate, productProductionDate, productComposition,
            productHealingProperties, productDosage, productVolume, productWeight, productQuantity;
    private CheckBox productHasSugar, productHasSalt, productIsBio, productIsVege;
    private RadioButton productIsSweet, productIsSour, productIsSweetAndSour, productIsBitter,
            productIsSalty;
    private Button saveProduct, cancelButton;
    private AdView adView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstantState) {
        AppCompatDelegate.setDefaultNightMode(ThemeMode.getThemeMode(this));
        if(Orientation.isTablet(this))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        super.onCreate(savedInstantState);
        initView();
        setListeners();
    }

    private void initView(){
        binding = ActivityEditProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = EditProductActivity.this;
        resources = context.getResources();
        ProductDataModel model = new ProductDataModel(context, resources);
        presenter = new EditProductPresenter(this, this, model);

        adView = binding.adview;

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        setSupportActionBar(binding.toolbar);

        productName = binding.productEdit.edittextName;
        productType = binding.productEdit.spinnerProductType;
        productCategory = binding.productEdit.spinnerProductCategory;
        productStorageLocation = binding.productEdit.spinnerProductStorageLocation;
        productExpirationDate = binding.productEdit.edittextExpirationDate;
        productProductionDate = binding.productEdit.edittextProductionDate;
        productComposition = binding.productEdit.edittextComposition;
        productHealingProperties = binding.productEdit.edittextHealingProperties;
        productDosage = binding.productEdit.edittextDosage;
        productVolume = binding.productEdit.edittextVolume;
        productWeight = binding.productEdit.edittextWeight;
        productHasSugar = binding.productEdit.checkboxHasSugar;
        productHasSalt = binding.productEdit.checkboxHasSalt;
        productIsBio = binding.productEdit.checkboxIsBio;
        productIsVege = binding.productEdit.checkboxIsVege;
        productQuantity = binding.productEdit.edittextQuantity;
        productIsSweet = binding.productEdit.radiobtnIsSweet;
        productIsSour = binding.productEdit.radiobtnIsSour;
        productIsSweetAndSour = binding.productEdit.radiobtnIsSweetAndSour;
        productIsBitter = binding.productEdit.radiobtnIsBitter;
        productIsSalty = binding.productEdit.radiobtnIsSalty;
        saveProduct = binding.buttonSaveProduct;
        cancelButton = binding.buttonCancel;

        binding.productEdit.edittextVolume.setText(String.format("%s (%s)", getString(R.string.Product_volume), getString(R.string.Product_volume_unit)));
        binding.productEdit.edittextWeight.setText(String.format("%s (%s)", getString(R.string.Product_weight), getString(R.string.Product_weight_unit)));

        productName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        productComposition.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        productHealingProperties.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        productDosage.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        Intent myPantryActivityIntent = getIntent();
        int productId = myPantryActivityIntent.getIntExtra("PRODUCT_ID", 1);

        ArrayAdapter<CharSequence> productTypeAdapter = ArrayAdapter.createFromResource(context, R.array.Product_type_of_product_array, R.layout.custom_spinner);
        binding.productEdit.spinnerProductType.setAdapter(productTypeAdapter);

        productStorageLocationAdapter = new ArrayAdapter<>(context, R.layout.custom_spinner, presenter.getStorageLocationsArray());
        productStorageLocationAdapter.notifyDataSetChanged();
        productStorageLocation.setAdapter(productStorageLocationAdapter);

        productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_choose_array, R.layout.custom_spinner);
        binding.productEdit.spinnerProductCategory.setAdapter(productCategoryAdapter);
        presenter.setProduct(productId);
    }

    private void setListeners() {
        saveProduct.setOnClickListener(view -> presenter.onClickSaveProductButton());
        cancelButton.setOnClickListener(view -> presenter.onClickCancelButton());

        productExpirationDate.setOnClickListener(v -> {
            if (productExpirationDate.length() <= 1) {
                year = DateHelper.getActualYear();
                month = DateHelper.getActualMonth();
                day = DateHelper.getActualDay(1);
            } else {
                int[] expirationDateArray = presenter.getExpirationDateArray();
                year = expirationDateArray[0];
                month = expirationDateArray[1]-1;
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

        productProductionDate.setOnClickListener(v -> {
            if (productProductionDate.length() <= 1) {
                year = DateHelper.getActualYear();
                month = DateHelper.getActualMonth();
                day = DateHelper.getActualDay(0);
            } else {
                int[] productionDateArray = presenter.getProductionDateArray();
                year = productionDateArray[0];
                month = productionDateArray[1]-1;
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

        productType.setOnTouchListener((v, event) -> {
            isTypeOfProductTouched = true;
            return false;
        });

        productType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(isTypeOfProductTouched) {
                    String typeOfProductValue = String.valueOf(productType.getSelectedItem());
                    presenter.updateProductFeaturesAdapter(typeOfProductValue);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onClickSaveProductButton(){
        int selectedTasteId = binding.productEdit.radiogroupTaste.getCheckedRadioButtonId();
        RadioButton taste = findViewById(selectedTasteId);

        Product product = presenter.getGroupProducts().getProduct();
        product.setName(productName.getText().toString());
        product.setTypeOfProduct(String.valueOf(productType.getSelectedItem()));
        product.setProductFeatures(String.valueOf(productCategory.getSelectedItem()));
        product.setComposition(productComposition.getText().toString());
        product.setHealingProperties(productHealingProperties.getText().toString());
        product.setDosage(productDosage.getText().toString());
        product.setVolume(Integer.parseInt(productVolume.getText().toString()));
        product.setWeight(Integer.parseInt(productWeight.getText().toString()));
        product.setHasSugar(productHasSugar.isChecked());
        product.setHasSalt(productHasSalt.isChecked());
        product.setIsBio(productIsBio.isChecked());
        product.setIsVege(productIsVege.isChecked());
        product.setStorageLocation(String.valueOf(productStorageLocation.getSelectedItem()));
        GroupProducts groupProducts = new GroupProducts(product, Integer.parseInt(productQuantity.getText().toString()));
        presenter.setTaste(taste);
        presenter.saveProduct(groupProducts);
    }

    @Override
    public void setSpinnerSelections(int typeOfProductPosition, int productFeaturesPosition, int productStorageLocationSpinnerPosition) {
        productType.setSelection(typeOfProductPosition);
        updateProductFeaturesAdapter(String.valueOf(productType.getSelectedItem()));
        productCategory.setSelection(productFeaturesPosition);
        productStorageLocation.setSelection(productStorageLocationSpinnerPosition);
    }

    @Override
    public void showProductData(GroupProducts groupProducts) {
        DateHelper expirationDateString = new DateHelper(groupProducts.getProduct().getExpirationDate());
        DateHelper productionDateString = new DateHelper(groupProducts.getProduct().getProductionDate());
        String[] tasteArray = resources.getStringArray(R.array.Product_taste_array);

        productName.setText(groupProducts.getProduct().getName());
        productQuantity.setText(String.valueOf(groupProducts.getQuantity()));
        productExpirationDate.setText(expirationDateString.getDateInLocalFormat());
        productProductionDate.setText(productionDateString.getDateInLocalFormat());
        productComposition.setText(groupProducts.getProduct().getComposition());
        productHealingProperties.setText(groupProducts.getProduct().getHealingProperties());
        productDosage.setText(groupProducts.getProduct().getDosage());
        productVolume.setText(String.valueOf(groupProducts.getProduct().getVolume()));
        productWeight.setText(String.valueOf(groupProducts.getProduct().getWeight()));
        productHasSugar.setChecked(groupProducts.getProduct().getHasSugar());
        productHasSalt.setChecked(groupProducts.getProduct().getHasSalt());
        productIsBio.setChecked(groupProducts.getProduct().getIsBio());
        productIsVege.setChecked(groupProducts.getProduct().getIsVege());

        if(groupProducts.getProduct().getTaste().equals(tasteArray[0]))
            productIsSweet.setChecked(true);
        else if(groupProducts.getProduct().getTaste().equals(tasteArray[1]))
            productIsSour.setChecked(true);
        else if(groupProducts.getProduct().getTaste().equals(tasteArray[2]))
            productIsSweetAndSour.setChecked(true);
        else if(groupProducts.getProduct().getTaste().equals(tasteArray[3]))
            productIsSalty.setChecked(true);
        else if(groupProducts.getProduct().getTaste().equals(tasteArray[4]))
            productIsBitter.setChecked(true);
    }

    @Override
    public void onSavedProduct() {
        Toast.makeText(context, getString(R.string.EditProductActivity_product_was_updated), Toast.LENGTH_LONG).show();
    }

    @Override
    public void navigateToMyPantryActivity() {
        Intent myPantryActivityIntent = new Intent(context, MyPantryActivity.class);
        startActivity(myPantryActivityIntent);
        CustomIntent.customType(this, "fadein-to-fadeout");
    }

    @Override
    public void updateProductFeaturesAdapter(String typeOfProductSpinnerValue) {
        String[] productTypesArray = resources.getStringArray(R.array.Product_type_of_product_array);
        if (typeOfProductSpinnerValue.equals(productTypesArray[1]))
            productCategoryAdapter = new ArrayAdapter<>(context, R.layout.custom_spinner, presenter.getOwnCategoryArray());
        else if (typeOfProductSpinnerValue.equals(productTypesArray[2]))
            productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_store_products_array, R.layout.custom_spinner);
        else if (typeOfProductSpinnerValue.equals(productTypesArray[3]))
            productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_ready_meals_array, R.layout.custom_spinner);
        else if (typeOfProductSpinnerValue.equals(productTypesArray[4]))
            productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_vegetables_array, R.layout.custom_spinner);
        else if (typeOfProductSpinnerValue.equals(productTypesArray[5]))
            productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_fruits_array, R.layout.custom_spinner);
        else if (typeOfProductSpinnerValue.equals(productTypesArray[6]))
            productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_herbs_array, R.layout.custom_spinner);
        else if (typeOfProductSpinnerValue.equals(productTypesArray[7]))
            productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_liqueurs_array, R.layout.custom_spinner);
        else if (typeOfProductSpinnerValue.equals(productTypesArray[8]))
            productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_wines_type_array, R.layout.custom_spinner);
        else if (typeOfProductSpinnerValue.equals(productTypesArray[9]))
            productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_mushrooms_array, R.layout.custom_spinner);
        else if (typeOfProductSpinnerValue.equals(productTypesArray[10]))
            productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_vinegars_array, R.layout.custom_spinner);
        else if (typeOfProductSpinnerValue.equals(productTypesArray[11]))
            productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_chemical_products_array, R.layout.custom_spinner);
        else if (typeOfProductSpinnerValue.equals(productTypesArray[12]))
            productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_other_products_array, R.layout.custom_spinner);

        productCategoryAdapter.notifyDataSetChanged();
        binding.productEdit.spinnerProductCategory.setAdapter(productCategoryAdapter);
    }

    @Override
    public void showExpirationDate(String date) {
        productExpirationDate.setText(date);
    }

    @Override
    public void showProductionDate(String date) {
        productProductionDate.setText(date);
    }

    @Override
    public void showErrorNameNotSet() {
        productName.setError(getString(R.string.Error_product_name_is_required));
        Toast.makeText(context, getString(R.string.Error_product_name_is_required), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorCategoryNotSelected() {
        Toast.makeText(context, getString(R.string.Error_category_not_selected), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            presenter.onClickCancelButton();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.edit_product_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_save_product:
                presenter.onClickSaveProductButton();
                return true;
            case R.id.action_cancel:
                presenter.onClickCancelButton();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
    public void finish() {
        super.finish();
        CustomIntent.customType(this, "fadein-to-fadeout");
    }
}