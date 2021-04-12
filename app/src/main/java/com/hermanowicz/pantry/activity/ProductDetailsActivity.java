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

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
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
import com.hermanowicz.pantry.databinding.ActivityProductDetailsBinding;
import com.hermanowicz.pantry.db.product.Product;
import com.hermanowicz.pantry.interfaces.ProductDetailsView;
import com.hermanowicz.pantry.model.DatabaseOperations;
import com.hermanowicz.pantry.model.GroupProducts;
import com.hermanowicz.pantry.model.ProductDataModel;
import com.hermanowicz.pantry.presenter.ProductDetailsPresenter;
import com.hermanowicz.pantry.util.DateHelper;
import com.hermanowicz.pantry.util.Orientation;
import com.hermanowicz.pantry.util.ThemeMode;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import maes.tech.intentanim.CustomIntent;

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
    private ProductDetailsPresenter presenter;
    private Context context;
    private int productId;

    private TextView productType, productCategory, productStorageLocation, productExpirationDate,
            productProductionDate, productComposition, productHealingProperties, productDosage,
            productVolume, productWeight, productQuantity, productHasSugar, productHasSalt,
            productIsVege, productIsBio, productTaste;
    private ImageView photoIv;
    private Button deleteProduct, printQrCode, addPhoto, editProduct;
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

    private void initView() {
        binding = ActivityProductDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = getApplicationContext();

        Toolbar toolbar = binding.toolbar;
        photoIv = binding.photoIv;
        productType = binding.textProductTypeValue;
        productCategory = binding.textProductCategoryValue;
        productStorageLocation = binding.textProductStorageLocationValue;
        productExpirationDate = binding.textProductExpirationDateValue;
        productProductionDate = binding.textProductProductionDateValue;
        productComposition = binding.textProductCompositionValue;
        productHealingProperties = binding.textProductHealingPropertiesValue;
        productDosage = binding.textProductDosageValue;
        productVolume = binding.textProductVolumeValue;
        productWeight = binding.textProductWeightValue;
        productQuantity = binding.textQuantityValue;
        productHasSugar = binding.textProductHasSugarValue;
        productHasSalt = binding.textProductHasSaltValue;
        productIsBio = binding.textProductIsBioValue;
        productIsVege = binding.textProductIsVegeValue;
        productTaste = binding.textProductTasteValue;
        deleteProduct = binding.buttonDeleteProduct;
        printQrCode = binding.buttonPrintQRCode;
        editProduct = binding.buttonEditProduct;
        addPhoto = binding.buttonAddPhoto;
        adView = binding.adview;

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        setSupportActionBar(toolbar);

        presenter = new ProductDetailsPresenter(this, new ProductDataModel(context,
                getResources()), new DatabaseOperations(context));

        Intent myPantryActivityIntent = getIntent();
        productId = myPantryActivityIntent.getIntExtra("PRODUCT_ID", 1);
        String hashCode = myPantryActivityIntent.getStringExtra("HASH_CODE");

        presenter.setProductId(productId);
        presenter.showProductDetails(hashCode);
    }

    private void setListeners() {
        deleteProduct.setOnClickListener(view -> presenter.onClickDeleteProduct(productId));
        printQrCode.setOnClickListener(view -> presenter.onClickPrintQRCodes());
        editProduct.setOnClickListener(view -> presenter.onClickEditProduct(productId));
        addPhoto.setOnClickListener(view -> presenter.onClickTakePhoto());
    }

    @Override
    public void showProductDetails(@NotNull GroupProducts groupProducts) {
        DateHelper dateHelper = new DateHelper(groupProducts.getProduct().getExpirationDate());
        Objects.requireNonNull(getSupportActionBar()).setTitle(groupProducts.getProduct().getName());
        productType.setText(groupProducts.getProduct().getTypeOfProduct());
        productStorageLocation.setText(groupProducts.getProduct().getStorageLocation());
        productQuantity.setText(String.valueOf(groupProducts.getQuantity()));
        productCategory.setText(groupProducts.getProduct().getProductFeatures());
        productExpirationDate.setText(dateHelper.getDateInLocalFormat());
        dateHelper = new DateHelper(groupProducts.getProduct().getProductionDate());
        productProductionDate.setText(dateHelper.getDateInLocalFormat());
        productComposition.setText(groupProducts.getProduct().getComposition());
        productHealingProperties.setText(groupProducts.getProduct().getHealingProperties());
        productDosage.setText(groupProducts.getProduct().getDosage());
        productVolume.setText(String.format("%s%s", groupProducts.getProduct().getVolume(), getString(R.string.Product_volume_unit)));
        productWeight.setText(String.format("%s%s", groupProducts.getProduct().getWeight(), getString(R.string.Product_weight_unit)));
        productTaste.setText(groupProducts.getProduct().getTaste());
        if (groupProducts.getProduct().getHasSugar()) productHasSugar.setText(getString(R.string.ProductDetailsActivity_yes));
        if (groupProducts.getProduct().getHasSalt()) productHasSalt.setText(getString(R.string.ProductDetailsActivity_yes));
        if (groupProducts.getProduct().getIsBio()) productIsBio.setText(getString(R.string.ProductDetailsActivity_yes));
        if (groupProducts.getProduct().getIsVege()) productIsVege.setText(getString(R.string.ProductDetailsActivity_yes));
    }

    @Override
    public void showPhoto(Bitmap photo) {
        photoIv.setImageBitmap(photo);
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
    public void navigateToPrintQRCodeActivity(@NonNull List<Product> productList) {
        Intent printQRCodesActivityIntent = new Intent(context, PrintQRCodesActivity.class)
                .putExtra("PRODUCT_LIST", (Serializable) productList);
        startActivity(printQRCodesActivityIntent);
        CustomIntent.customType(this, "up-to-bottom");
    }

    @Override
    public void navigateToEditProductActivity(int productId) {
        Intent editProductActivityIntent = new Intent(context, EditProductActivity.class)
                .putExtra("PRODUCT_ID", productId);
        startActivity(editProductActivityIntent);
        CustomIntent.customType(this, "up-to-bottom");
    }

    @Override
    public void navigateToMyPantryActivity() {
        Intent myPantryActivityIntent = new Intent(context, MyPantryActivity.class);
        startActivity(myPantryActivityIntent);
        CustomIntent.customType(this, "bottom-to-up");
    }

    @Override
    public void navigateToAddPhotoActivity(List<Product> productList) {
        Intent addPhotoActivityIntent = new Intent(context, AddPhotoActivity.class)
                .putExtra("PRODUCT_LIST", (Serializable) productList);
        startActivity(addPhotoActivityIntent);
        CustomIntent.customType(this, "up-to-bottom");
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.product_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_take_photo:
                presenter.onClickTakePhoto();
                return true;
            case R.id.action_print_codes:
                presenter.onClickPrintQRCodes();
                return true;
            case R.id.action_edit_product:
                presenter.onClickEditProduct(productId);
                return true;
            case R.id.action_delete_product:
                presenter.onClickDeleteProduct(productId);
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
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            presenter.navigateToMyPantryActivity();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this, "up-to-bottom");
    }
}