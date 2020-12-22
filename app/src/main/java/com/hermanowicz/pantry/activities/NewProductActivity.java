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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.databinding.ActivityNewProductBinding;
import com.hermanowicz.pantry.db.Product;
import com.hermanowicz.pantry.interfaces.NewProductView;
import com.hermanowicz.pantry.models.DatabaseOperations;
import com.hermanowicz.pantry.models.NewProductModel;
import com.hermanowicz.pantry.presenters.NewProductPresenter;
import com.hermanowicz.pantry.utils.DateHelper;
import com.hermanowicz.pantry.utils.Notification;
import com.hermanowicz.pantry.utils.Orientation;
import com.hermanowicz.pantry.utils.ThemeMode;

import java.util.ArrayList;
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

public class NewProductActivity extends AppCompatActivity implements OnItemSelectedListener, DatePickerDialog.OnDateSetListener, NewProductView {

    private ActivityNewProductBinding binding;
    private NewProductPresenter presenter;
    private Context context;
    private Resources resources;
    private int day, month, year;
    private boolean isTypeOfProductTouched;
    private DatePickerDialog.OnDateSetListener productionDateListener, expirationDateListener;
    private ArrayAdapter<CharSequence> productTypeAdapter, productCategoryAdapter,
            productStorageLocationAdapter;

    private Spinner productType, productCategory, productStorageLocation;
    private EditText productName, productExpirationDate, productProductionDate, productComposition,
            productHealingProperties, productDosage, productVolume, productWeight, productQuantity;
    private CheckBox productHasSugar, productHasSalt, productIsBio, productIsVege;
    private Button addProduct;
    private AdView adView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(ThemeMode.getThemeMode(this));
        if(Orientation.isTablet(this))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        super.onCreate(savedInstanceState);
        initView();
        setListeners();
    }

    private void initView(){
        binding = ActivityNewProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = NewProductActivity.this;
        resources = context.getResources();

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        adView = binding.adview;
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
        TextView volumeLabel = binding.productEdit.textVolumeLabel;
        TextView weightLabel = binding.productEdit.textWeightLabel;
        addProduct = binding.buttonAddProduct;

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        volumeLabel.setText(String.format("%s (%s)", getString(R.string.Product_volume), getString(R.string.Product_volume_unit)));
        weightLabel.setText(String.format("%s (%s)", getString(R.string.Product_weight), getString(R.string.Product_weight_unit)));

        productName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        productComposition.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        productHealingProperties.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        productDosage.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        presenter = new NewProductPresenter(this, new NewProductModel(resources, new DatabaseOperations(context)));

        productTypeAdapter = ArrayAdapter.createFromResource(context, R.array.Product_type_of_product_array, R.layout.custom_spinner);
        productType.setAdapter(productTypeAdapter);
        productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_choose_array, R.layout.custom_spinner);
        productCategory.setAdapter(productCategoryAdapter);
        productStorageLocationAdapter = new ArrayAdapter<>(context, R.layout.custom_spinner, presenter.getStorageLocationsArray());
        productStorageLocation.setAdapter(productStorageLocationAdapter);

    }

    private void setListeners() {
        addProduct.setOnClickListener(view -> onClickAddProduct());

        productExpirationDate.setOnClickListener(v -> {
            if (productExpirationDate.length() < 1) {
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
            if (productProductionDate.length() < 1) {
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

        productType.setOnItemSelectedListener(new OnItemSelectedListener() {
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

    private void onClickAddProduct() {
        int selectedTasteId = binding.productEdit.radiogroupTaste.getCheckedRadioButtonId();
        RadioButton taste = findViewById(selectedTasteId);

        Product product = new Product();
        product.setName(productName.getText().toString());
        product.setTypeOfProduct(String.valueOf(productType.getSelectedItem()));
        product.setProductFeatures(String.valueOf(productCategory.getSelectedItem()));
        product.setStorageLocation(String.valueOf(productStorageLocation.getSelectedItem()));
        product.setComposition(productComposition.getText().toString());
        product.setHealingProperties(productHealingProperties.getText().toString());
        product.setDosage(productDosage.getText().toString());
        product.setVolume(Integer.parseInt(productVolume.getText().toString()));
        product.setWeight(Integer.parseInt(productWeight.getText().toString()));
        product.setHasSugar(productHasSugar.isChecked());
        product.setHasSalt(productHasSalt.isChecked());
        product.setIsBio(productIsBio.isChecked());
        product.setIsVege(productIsVege.isChecked());
        presenter.setTaste(taste);
        presenter.setQuantity(productQuantity.getText().toString());
        presenter.addProducts(product);
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
    public void navigateToPrintQRCodesActivity(ArrayList<String> textToQRCodeList, ArrayList<String> namesOfProductsList, ArrayList<String> expirationDatesList) {
        Intent printQRCodesActivityIntent = new Intent(context, PrintQRCodesActivity.class)
                .putStringArrayListExtra("text_to_qr_code", textToQRCodeList)
                .putStringArrayListExtra("expiration_dates", expirationDatesList)
                .putStringArrayListExtra("names_of_products", namesOfProductsList);

        startActivity(printQRCodesActivityIntent);
    }

    @Override
    public void onProductsAdd(List<Product> products) {
        for (Product product : products) {
            Notification.createNotification(context, product);
        }
    }

    @Override
    public void updateProductFeaturesAdapter(String productTypeSpinnerValue) {
        String[] productTypesArray = resources.getStringArray(R.array.Product_type_of_product_array);
        if (productTypeSpinnerValue.equals(productTypesArray[0]))
            productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_choose_array, R.layout.custom_spinner);
        else if (productTypeSpinnerValue.equals(productTypesArray[1]))
            productCategoryAdapter = new ArrayAdapter<>(context, R.layout.custom_spinner, presenter.getOwnCategoryArray());
        else if (productTypeSpinnerValue.equals(productTypesArray[2]))
            productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_store_products_array, R.layout.custom_spinner);
        else if (productTypeSpinnerValue.equals(productTypesArray[3]))
            productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_ready_meals_array, R.layout.custom_spinner);
        else if (productTypeSpinnerValue.equals(productTypesArray[4]))
            productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_vegetables_array, R.layout.custom_spinner);
        else if (productTypeSpinnerValue.equals(productTypesArray[5]))
            productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_fruits_array, R.layout.custom_spinner);
        else if (productTypeSpinnerValue.equals(productTypesArray[6]))
            productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_herbs_array, R.layout.custom_spinner);
        else if (productTypeSpinnerValue.equals(productTypesArray[7]))
            productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_liqueurs_array, R.layout.custom_spinner);
        else if (productTypeSpinnerValue.equals(productTypesArray[8]))
            productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_wines_type_array, R.layout.custom_spinner);
        else if (productTypeSpinnerValue.equals(productTypesArray[9]))
            productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_mushrooms_array, R.layout.custom_spinner);
        else if (productTypeSpinnerValue.equals(productTypesArray[10]))
            productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_vinegars_array, R.layout.custom_spinner);
        else if (productTypeSpinnerValue.equals(productTypesArray[11]))
            productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_chemical_products_array, R.layout.custom_spinner);
        else if (productTypeSpinnerValue.equals(productTypesArray[12]))
            productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_other_products_array, R.layout.custom_spinner);

        productCategoryAdapter.notifyDataSetChanged();
        productCategory.setAdapter(productCategoryAdapter);
    }

    @Override
    public void showStatementOnAreProductsAdded(String statementToShow) {
        Toast.makeText(context, statementToShow, Toast.LENGTH_LONG).show();
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
        Toast.makeText(context, getString(R.string.Error_product_name_is_required), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showErrorCategoryNotSelected() {
        Toast.makeText(context, getString(R.string.Error_category_not_selected), Toast.LENGTH_LONG).show();
    }

    @Override
    public void navigateToMainActivity() {
        Intent mainActivityIntent = new Intent(context, MainActivity.class);
        startActivity(mainActivityIntent);
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            presenter.navigateToMainActivity();
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