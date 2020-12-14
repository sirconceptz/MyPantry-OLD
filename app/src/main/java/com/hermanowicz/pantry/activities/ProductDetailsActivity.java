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

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.databinding.ActivityProductDetailsBinding;
import com.hermanowicz.pantry.db.CategoryDb;
import com.hermanowicz.pantry.db.ProductDb;
import com.hermanowicz.pantry.interfaces.ProductDetailsView;
import com.hermanowicz.pantry.models.GroupProducts;
import com.hermanowicz.pantry.models.ProductDataModel;
import com.hermanowicz.pantry.presenters.ProductDetailsPresenter;
import com.hermanowicz.pantry.utils.DateHelper;
import com.hermanowicz.pantry.utils.Orientation;
import com.hermanowicz.pantry.utils.ThemeMode;

import org.jetbrains.annotations.NotNull;

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
public class ProductDetailsActivity extends AppCompatActivity implements ProductDetailsView {

    private ActivityProductDetailsBinding binding;

    private Context context;
    private int productId;
    private String hashCode;
    private AdView adView;

    private ProductDetailsPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(ThemeMode.getThemeMode(this));
        if(Orientation.isTablet(this))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        super.onCreate(savedInstanceState);
        initView();
        setListeners();

        presenter = new ProductDetailsPresenter(this, new ProductDataModel(ProductDb.getInstance(context), CategoryDb.getInstance(context), getResources()));

        getDataFromIntent();

        presenter.setProductList(productId);
        presenter.showProductDetails(hashCode);

    }

    private void initView() {
        binding = ActivityProductDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adView = binding.adview;

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        context = getApplicationContext();

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
    }

    private void setListeners() {
        binding.buttonDeleteProduct.setOnClickListener(view -> presenter.deleteProduct(productId));
        binding.buttonPrintQRCode.setOnClickListener(view -> presenter.printQRCode());
        binding.buttonEditProduct.setOnClickListener(view -> presenter.editProduct(productId));
    }

    private void getDataFromIntent() {
        Intent myPantryActivityIntent = getIntent();
        productId = myPantryActivityIntent.getIntExtra("product_id", 1);
        hashCode = myPantryActivityIntent.getStringExtra("hash_code");
    }

    @Override
    public void showProductDetails(@NotNull GroupProducts groupProducts) {
        DateHelper dateHelper = new DateHelper(groupProducts.getProduct().getExpirationDate());
        Objects.requireNonNull(getSupportActionBar()).setTitle(groupProducts.getProduct().getName());
        binding.textProductTypeValue.setText(groupProducts.getProduct().getTypeOfProduct());
        binding.textQuantityValue.setText(String.valueOf(groupProducts.getQuantity()));
        binding.textProductFeaturesValue.setText(groupProducts.getProduct().getProductFeatures());
        binding.textProductExpirationDateValue.setText(dateHelper.getDateInLocalFormat());
        dateHelper = new DateHelper(groupProducts.getProduct().getProductionDate());
        binding.textProductProductionDateValue.setText(dateHelper.getDateInLocalFormat());
        binding.textProductCompositionValue.setText(groupProducts.getProduct().getComposition());
        binding.textProductHealingPropertiesValue.setText(groupProducts.getProduct().getHealingProperties());
        binding.textProductDosageValue.setText(groupProducts.getProduct().getDosage());
        binding.textProductVolumeValue.setText(String.format("%s%s", groupProducts.getProduct().getVolume(), getString(R.string.Product_volume_unit)));
        binding.textProductWeightValue.setText(String.format("%s%s", groupProducts.getProduct().getWeight(), getString(R.string.Product_weight_unit)));
        binding.textProductTasteValue.setText(groupProducts.getProduct().getTaste());
        if (groupProducts.getProduct().getHasSugar()) binding.textProductHasSugarValue.setText(getString(R.string.ProductDetailsActivity_yes));
        if (groupProducts.getProduct().getHasSalt()) binding.textProductHasSaltValue.setText(getString(R.string.ProductDetailsActivity_yes));
    }

    @Override
    public void showErrorWrongData() {
        Toast.makeText(context, getString(R.string.Error_wrong_data), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDeletedProduct() {
        Toast.makeText(context, getString(R.string.ProductDetailsActivity_product_has_been_removed), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPrintQRCode(ArrayList<String> textToQRCodeList, ArrayList<String> namesOfProductsList, ArrayList<String> expirationDatesList) {
        Intent printQRCodesActivityIntent = new Intent(context, PrintQRCodesActivity.class);

        printQRCodesActivityIntent.putStringArrayListExtra("text_to_qr_code", textToQRCodeList);
        printQRCodesActivityIntent.putStringArrayListExtra("expiration_dates", expirationDatesList);
        printQRCodesActivityIntent.putStringArrayListExtra("names_of_products", namesOfProductsList);

        startActivity(printQRCodesActivityIntent);
    }

    @Override
    public void navigateToEditProductActivity(int productId) {
        Intent editProductActivityIntent = new Intent(context, EditProductActivity.class)
                .putExtra("product_id", productId);
        startActivity(editProductActivityIntent);
    }

    @Override
    public void navigateToMyPantryActivity() {
        Intent myPantryActivityIntent = new Intent(context, MyPantryActivity.class);
        startActivity(myPantryActivityIntent);
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
}