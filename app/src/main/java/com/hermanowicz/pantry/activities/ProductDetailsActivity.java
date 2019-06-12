/*
 * Copyright (c) 2019
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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
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
import com.hermanowicz.pantry.interfaces.ProductDetailsView;
import com.hermanowicz.pantry.presenters.ProductDetailsPresenter;
import com.hermanowicz.pantry.utils.DateHelper;
import com.hermanowicz.pantry.utils.Notification;

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
public class ProductDetailsActivity extends AppCompatActivity implements ProductDetailsView {

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
    @BindView(R.id.adBanner)
    AdView adView;

    private Context context;
    private ProductDb productDb;
    private Product selectedProduct;
    private int productID;
    private String hashCode;

    private ProductDetailsPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        ButterKnife.bind(this);

        MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.admob_ad_id));

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        presenter = new ProductDetailsPresenter(this);

        getDataFromIntent();

        context = ProductDetailsActivity.this;

        productDb = ProductDb.getInstance(context);
        selectedProduct = productDb.productsDao().getProductById(productID);

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

    @OnClick(R.id.button_deleteProduct)
    void onClickDeleteProductButton() {
        presenter.deleteProduct(selectedProduct.getId());
    }

    @OnClick(R.id.button_printQRCode)
    void onClickPrintQRCodeButton() {
        presenter.printQRCode();
    }

    @Override
    public void showProductDetails(Product product) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(product.getName());
        typeOfProduct.setText(product.getTypeOfProduct());
        productFeatures.setText(product.getProductFeatures());
        DateHelper dateHelper = new DateHelper(product.getExpirationDate());
        expirationDate.setText(dateHelper.getDateInLocalFormat());
        productionDate.setText(dateHelper.getDateInLocalFormat());
        composition.setText(product.getComposition());
        healingProperties.setText(product.getHealingProperties());
        dosage.setText(product.getDosage());
        volume.setText(String.format("%s%s", product.getVolume(), getString(R.string.ProductDetailsActivity_volume_unit)));
        weight.setText(String.format("%s%s", product.getWeight(), getString(R.string.ProductDetailsActivity_weight_unit)));
        taste.setText(product.getTaste());
        if (product.getHasSugar()) hasSugar.setText(getString(R.string.ProductDetailsActivity_yes));
        if (product.getHasSalt()) hasSalt.setText(getString(R.string.ProductDetailsActivity_yes));
    }

    @Override
    public void showErrorWrongData() {
        Toast.makeText(context, getString(R.string.Errors_wrong_data), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDeletedProduct(int productID) {
        productDb.productsDao().deleteProductById(productID);
        Notification.cancelNotification(context, selectedProduct);
        Toast.makeText(context, getString(R.string.ProductDetailsActivity_product_has_been_removed), Toast.LENGTH_LONG).show();
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
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            presenter.navigateToMyPantryActivity();
        }
        return super.onKeyDown(keyCode, event);
    }
}