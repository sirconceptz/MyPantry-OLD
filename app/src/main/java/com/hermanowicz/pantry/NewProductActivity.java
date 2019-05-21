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
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.hermanowicz.pantry.interfaces.NewProductActivityView;
import com.hermanowicz.pantry.models.NewProductActivityModel;
import com.hermanowicz.pantry.models.Product;
import com.hermanowicz.pantry.presenters.NewProductActivityPresenter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
public class NewProductActivity extends AppCompatActivity implements OnItemSelectedListener, DatePickerDialog.OnDateSetListener, NewProductActivityView {

    private Context context;
    private Resources resources;
    private DatabaseManager db;
    private int day, month, year;
    private boolean isTypeOfProductTouched;
    private Calendar calendar;
    private DatePickerDialog.OnDateSetListener productionDateListener, expirationDateListener;
    private ArrayAdapter<CharSequence> productFeaturesAdapter;

    private NewProductActivityModel model;
    private NewProductActivityPresenter presenter;

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
    @BindView(R.id.edittext_quantity)
    EditText quantity;
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
    @BindView(R.id.radiobtn_isSweet)
    RadioButton isSweet;
    @BindView(R.id.radiobtn_isSour)
    RadioButton isSour;
    @BindView(R.id.radiobtn_isSweetAndSour)
    RadioButton isSweetAndSour;
    @BindView(R.id.radiobtn_isBitter)
    RadioButton isBitter;
    @BindView(R.id.radiobtn_isSalty)
    RadioButton isSalty;
    @BindView(R.id.text_volume)
    TextView volumeLabel;
    @BindView(R.id.text_weight)
    TextView weightLabel;
    @BindView(R.id.adBanner)
    AdView adView;

    @SuppressLint({"SetTextI18n", "CutPasteId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        ButterKnife.bind(this);

        init();

        model = new NewProductActivityModel(resources);
        presenter = new NewProductActivityPresenter(this, model);

        productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_choose_array, android.R.layout.simple_spinner_item);
        productFeaturesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productFeaturesSpinner.setAdapter(productFeaturesAdapter);

        expirationDate.setOnClickListener(v -> {
            if (expirationDate.length() < 1) {
                calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
            } else {
                String[] expirationDateArray = presenter.getExpirationDateArray();
                year = Integer.valueOf(expirationDateArray[2]);
                month = Integer.valueOf(expirationDateArray[1]);
                day = Integer.valueOf(expirationDateArray[0]);
            }

            DatePickerDialog dialog = new DatePickerDialog(
                    context,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    expirationDateListener,
                    year, month, day);
            dialog.getDatePicker().setMinDate(new Date().getTime());
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });

        expirationDateListener = (datePicker, year, month, day) -> {
            month = month + 1;
            presenter.showExpirationDate(day, month, year);
        };

        productionDate.setOnClickListener(v -> {
            if (productionDate.length() < 1) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
            } else {
                String[] productionDateArray = presenter.getProductionDateArray();
                year = Integer.valueOf(productionDateArray[2]);
                month = Integer.valueOf(productionDateArray[1]);
                day = Integer.valueOf(productionDateArray[0]);

            }
            DatePickerDialog dialog = new DatePickerDialog(
                    context,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    productionDateListener,
                    year, month, day);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });

        productionDateListener = (datePicker, year, month, day) -> {
            month = month + 1;
            presenter.showProductionDate(day, month, year);
        };

        productTypeSpinner.setOnTouchListener((v, event) -> {
            isTypeOfProductTouched = true;
            return false;
        });

        productTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
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

    @OnClick(R.id.button_addProduct)
    void onClickAddProduct() {
        presenter.setIdOfLastProductInDb(db.idOfLastProductInDB());
        presenter.setName(name.getText().toString());
        presenter.setTypeOfProduct(String.valueOf(productTypeSpinner.getSelectedItem()));
        presenter.setProductFeatures(String.valueOf(productFeaturesSpinner.getSelectedItem()));
        presenter.setExpirationDate(expirationDate.getText().toString());
        presenter.setProductionDate(productionDate.getText().toString());
        presenter.setComposition(composition.getText().toString());
        presenter.setHealingProperties(healingProperties.getText().toString());
        presenter.setDosage(dosage.getText().toString());
        presenter.parseQuantity(quantity.getText().toString());
        presenter.parseVolume(volume.getText().toString());
        presenter.parseWeight(weight.getText().toString());
        presenter.setHasSugar(hasSugar.isChecked());
        presenter.setHasSalt(hasSalt.isChecked());
        presenter.setIsSweet(isSweet.isChecked());
        presenter.setIsSour(isSour.isChecked());
        presenter.setIsSweetAndSour(isSweetAndSour.isChecked());
        presenter.setIsBitter(isBitter.isChecked());
        presenter.setIsSalty(isSalty.isChecked());
        presenter.addProducts();
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

    @SuppressLint({"CutPasteId", "SetTextI18n"})
    private void init(){
        context = NewProductActivity.this;
        resources = context.getResources();
        db = new DatabaseManager(context);

        setSupportActionBar(toolbar);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-4025776034769422~3797748160");

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        quantity.setText("1");
        volume.setText("0");
        weight.setText("0");

        volumeLabel.setText(resources.getString(R.string.ProductDetailsActivity_volume) + " (" + resources.getString(R.string.ProductDetailsActivity_volume_unit) + ")");

        weightLabel.setText(resources.getString(R.string.ProductDetailsActivity_weight) + " (" + resources.getString(R.string.ProductDetailsActivity_weight_unit) + ")");

        name.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        composition.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        healingProperties.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        dosage.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            presenter.navigateToMainActivity();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter = new NewProductActivityPresenter(this, model);
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
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void navigateToPrintQRCodesActivity(ArrayList<String> textToQRCodeList, ArrayList<String> namesOfProductsList, ArrayList<String> expirationDatesList) {
        Intent printQRCodesActivityIntent = new Intent(context, PrintQRCodesActivity.class);

        printQRCodesActivityIntent.putStringArrayListExtra("text_to_qr_code", textToQRCodeList);
        printQRCodesActivityIntent.putStringArrayListExtra("expiration_dates", expirationDatesList);
        printQRCodesActivityIntent.putStringArrayListExtra("names_of_products", namesOfProductsList);

        startActivity(printQRCodesActivityIntent);
        finish();
    }

    @Override
    public boolean isAddProductsSuccess(ArrayList<Product> productsArrayList) {
        boolean isProductsAdded = false;
        Product product;
        for (int counter = 0; counter < productsArrayList.size(); counter++) {
            product = productsArrayList.get(counter);
            isProductsAdded = db.insertProductToDB(product);
            Notification.createNotification(context, product);
        }
        return isProductsAdded;
    }

    @Override
    public void updateProductFeaturesAdapter(String typeOfProductSpinnerValue) {
        String[] productTypesArray = resources.getStringArray(R.array.ProductDetailsActivity_type_of_product_array);
        if (typeOfProductSpinnerValue.equals(productTypesArray[0]))
            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_choose_array, android.R.layout.simple_spinner_item);
        else if (typeOfProductSpinnerValue.equals(productTypesArray[1]))
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
    public void showStatementOnAreProductsAdded(String statementToShow) {
        Toast.makeText(context, statementToShow, Toast.LENGTH_LONG).show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void showExpirationDate(int day, int month, int year) {
        expirationDate.setText(day + "." + month + "." + year);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void showProductionDate(int day, int month, int year) {
        productionDate.setText(day + "." + month + "." + year);
    }

    @Override
    public void showErrorNameNotSet() {
        name.setError(resources.getString(R.string.Errors_product_name_is_required));
        Toast.makeText(context, resources.getString(R.string.Errors_product_name_is_required), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showErrorCategoryNotSelected() {
        Toast.makeText(context, resources.getString(R.string.Errors_category_not_selected), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showErrorExpirationDateNotSet() {
        expirationDate.setError(resources.getString(R.string.Errors_expiration_date_is_required));
        Toast.makeText(context, resources.getString(R.string.Errors_expiration_date_is_required), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showErrorSomethingIsWrong() {
        Toast.makeText(context, resources.getString(R.string.Errors_something_wrong), Toast.LENGTH_LONG).show();
    }

    @Override
    public void navigateToMainActivity() {
        Intent mainActivityIntent = new Intent(context, MainActivity.class);
        startActivity(mainActivityIntent);
        finish();
    }
}