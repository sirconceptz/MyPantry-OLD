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
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import com.hermanowicz.pantry.interfaces.ProductDetailsActivityView;
import com.hermanowicz.pantry.models.ProductDetailsActivityModel;
import com.hermanowicz.pantry.models.ProductEntity;
import com.hermanowicz.pantry.presenters.ProductDetailsActivityPresenter;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <h1>ProductDetailsActivity</h1>
 * Activity with details of product. Is opened after choose a product in MyPantryActivity or after
 * scanning the QR code of product.
 *
 * @author  Mateusz Hermanowicz
 * @version 1.0
 * @since   1.0
 */
public class ProductDetailsActivity extends AppCompatActivity implements ProductDetailsActivityView {

    private Context context;
    private Resources resources;
    private ProductDb productDB;
    private ProductEntity selectedProduct;
    private int productID;
    private String hashCode;

    private ProductDetailsActivityPresenter presenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.text_productTypeValue)
    TextView typeOfProduct;
    @BindView(R.id.text_productFeaturesValue)
    TextView productFeatures;
    @BindView(R.id.text_productExpirationDateValue)
    TextView expirationDate;
    @BindView(R.id.text_productProductionDateValue)
    TextView productionDate;
    @BindView(R.id.text_productCompositionValue)
    TextView composition;
    @BindView(R.id.text_productHealingPropertiesValue)
    TextView healingProperties;
    @BindView(R.id.text_productDosageValue)
    TextView dosage;
    @BindView(R.id.text_productVolumeValue)
    TextView volume;
    @BindView(R.id.text_productWeightValue)
    TextView weight;
    @BindView(R.id.text_productHasSugarValue)
    TextView hasSugar;
    @BindView(R.id.text_productHasSaltValue)
    TextView hasSalt;
    @BindView(R.id.text_productTasteValue)
    TextView taste;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        ButterKnife.bind(this);

        ProductDetailsActivityModel model = new ProductDetailsActivityModel();
        presenter = new ProductDetailsActivityPresenter(this, model);

        getDataFromIntent();

        context = ProductDetailsActivity.this;
        resources = context.getResources();

        productDB = Room.databaseBuilder(context, ProductDb.class, "Products").allowMainThreadQueries().build();
        selectedProduct = productDB.productsDao().getProductById(productID);

        setSupportActionBar(toolbar);

        presenter.setProduct(selectedProduct);
        presenter.setHashCode(String.valueOf(hashCode));
        presenter.showProductDetails();
    }

    private void getDataFromIntent() {
        Intent myPantryActivityIntent = getIntent();
        productID = myPantryActivityIntent.getIntExtra("product_id", 0);
        hashCode = myPantryActivityIntent.getStringExtra("hash_code");
    }

    private String convertDateFormat(String dateToConvert) {
        String[] dateArray = dateToConvert.split("-");
        String convertedDate = "";
        if (dateArray.length > 1)
            convertedDate = dateArray[2] + "." + dateArray[1] + "." + dateArray[0];
        return convertedDate;
    }

    @OnClick(R.id.button_deleteProduct)
    void onClickDeleteProductButton() {
        presenter.deleteProduct(selectedProduct.getId());
    }

    @OnClick(R.id.button_printQRCode)
    void onClickPrintQRCodeButton() {
        presenter.printQRCode();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void showProductDetails(ProductEntity product) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(product.getName());
        typeOfProduct.setText(product.getTypeOfProduct());
        productFeatures.setText(product.getProductFeatures());
        expirationDate.setText(convertDateFormat(product.getExpirationDate()));
        productionDate.setText(convertDateFormat(product.getProductionDate()));
        composition.setText(product.getComposition());
        healingProperties.setText(product.getHealingProperties());
        dosage.setText(product.getDosage());
        volume.setText(product.getVolume() + resources.getString(R.string.ProductDetailsActivity_volume_unit));
        weight.setText(product.getWeight() + resources.getString(R.string.ProductDetailsActivity_weight_unit));
        taste.setText(product.getTaste());
        if (product.getHasSugar())
            hasSugar.setText(resources.getString(R.string.ProductDetailsActivity_yes));
        else
            hasSugar.setText(resources.getString(R.string.ProductDetailsActivity_no));
        if (product.getHasSalt())
            hasSalt.setText(resources.getString(R.string.ProductDetailsActivity_yes));
        else
            hasSalt.setText(resources.getString(R.string.ProductDetailsActivity_no));
    }

    @Override
    public void showErrorWrongData() {
        Toast.makeText(context, resources.getString(R.string.Errors_wrong_data), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDeletedProduct(int productID) {
        productDB.productsDao().deleteProductById(productID);
        Notification.cancelNotification(context, selectedProduct);
        Toast.makeText(context, resources.getString(R.string.ProductDetailsActivity_product_has_been_removed), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPrintQRCode(ArrayList<String> textToQRCodeList, ArrayList<String> namesOfProductsList, ArrayList<String> expirationDatesList) {
        Intent printQRCodesActivityIntent = new Intent(context, PrintQRCodesActivity.class);

        printQRCodesActivityIntent.putStringArrayListExtra("text_to_qr_code", textToQRCodeList);
        printQRCodesActivityIntent.putStringArrayListExtra("expiration_dates", expirationDatesList);
        printQRCodesActivityIntent.putStringArrayListExtra("names_of_products", namesOfProductsList);

        startActivity(printQRCodesActivityIntent);
        finish();
    }

    @Override
    public void navigateToMyPantryActivity() {
        Intent myPantryActivityIntent = new Intent(context, MyPantryActivity.class);
        startActivity(myPantryActivityIntent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            presenter.navigateToMyPantryActivity();
        }
        return super.onKeyDown(keyCode, event);
    }
}