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
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.databinding.ActivityProductDetailsBinding;
import com.hermanowicz.pantry.db.photo.Photo;
import com.hermanowicz.pantry.db.product.Product;
import com.hermanowicz.pantry.interfaces.PhotoDbResponse;
import com.hermanowicz.pantry.interfaces.ProductDetailsView;
import com.hermanowicz.pantry.model.GroupProducts;
import com.hermanowicz.pantry.presenter.ProductDetailsPresenter;
import com.hermanowicz.pantry.util.DateHelper;
import com.hermanowicz.pantry.util.Orientation;
import com.hermanowicz.pantry.util.PremiumAccess;
import com.hermanowicz.pantry.util.ThemeMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import maes.tech.intentanim.CustomIntent;

/**
 * <h1>ProductDetailsActivity</h1>
 * Activity with details of product. Is opened after choose a product in MyPantryActivity or after
 * scanning the QR code of product.
 *
 * @author Mateusz Hermanowicz
 */

public class ProductDetailsActivity extends AppCompatActivity implements ProductDetailsView,
        PhotoDbResponse {

    private ProductDetailsPresenter presenter;
    private Context context;

    private ImageView photoIv;
    private TextView productType;
    private TextView productCategory;
    private TextView productStorageLocation;
    private TextView productExpirationDate;
    private TextView productProductionDate;
    private TextView productComposition;
    private TextView productHealingProperties;
    private TextView productDosage;
    private TextView productVolume;
    private TextView productWeight;
    private TextView productQuantity;
    private TextView productHasSugar;
    private TextView productHasSalt;
    private TextView productIsVege;
    private TextView productIsBio;
    private TextView productTaste;
    private Button deleteProduct;
    private Button printQrCode;
    private Button addBarcode;
    private Button addPhoto;
    private Button editProduct;
    private AdView adView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(ThemeMode.getThemeMode(this));
        if (Orientation.isTablet(this))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        super.onCreate(savedInstanceState);
        initView();
        setListeners();
    }

    private void initView() {
        ActivityProductDetailsBinding binding = ActivityProductDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = getApplicationContext();

        Toolbar toolbar = binding.toolbar;
        photoIv = binding.imageviewPhoto;
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
        addBarcode = binding.buttonAddBarcode;
        addPhoto = binding.buttonAddPhoto;
        adView = binding.adview;

        setSupportActionBar(toolbar);

        presenter = new ProductDetailsPresenter(this, this);
        presenter.setPremiumAccess(new PremiumAccess(context));

        setOnlineDbPhotoList(this);

        if (!presenter.isPremium()) {
            MobileAds.initialize(context);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }

        Intent intent = getIntent();
        List<Product> productList = (List<Product>) intent.getSerializableExtra("product_list");
        List<Product> allProductList = (List<Product>) intent.getSerializableExtra("all_product_list");

        presenter.setAllProductList(allProductList);
        presenter.setProductList(productList);
        int productId = intent.getIntExtra("product_id", 1);
        String hashCode = intent.getStringExtra("hash_code");
        presenter.setProductId(productId);
        presenter.setHashCode(hashCode);

        if (presenter.isOfflineDb())
            presenter.showProductDetails();
    }

    private void setListeners() {
        addBarcode.setOnClickListener(view -> presenter.onClickAddBarcode());
        deleteProduct.setOnClickListener(view -> presenter.onClickDeleteProduct());
        printQrCode.setOnClickListener(view -> presenter.onClickPrintQRCodes());
        editProduct.setOnClickListener(view -> presenter.onClickEditProduct());
        addPhoto.setOnClickListener(view -> presenter.onClickTakePhoto());
    }

    private void setOnlineDbPhotoList(PhotoDbResponse response) {
        if(!presenter.isOfflineDb()) {
            List<Photo> onlinePhotoList = new ArrayList<>();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("photos/" + FirebaseAuth.getInstance().getUid());
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists())
                        onlinePhotoList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Photo photo = dataSnapshot.getValue(Photo.class);
                        onlinePhotoList.add(photo);
                    }
                    response.onPhotoResponse(onlinePhotoList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("FirebaseDB - Photos", error.getMessage());
                }
            });
        }
    }

    @Override
    public void showProductDetails(@NotNull GroupProducts groupProducts) {
        DateHelper dateHelper = new DateHelper(groupProducts.getProduct().getExpirationDate());
        Objects.requireNonNull(getSupportActionBar()).setTitle(groupProducts.getProduct().getName());
        productType.setText(groupProducts.getProduct().getTypeOfProduct());
        if(groupProducts.getProduct().getStorageLocation().equals("null"))
            productStorageLocation.setText(R.string.ProductDetailsActivity_not_set);
        else
            productStorageLocation.setText(groupProducts.getProduct().getStorageLocation());
        productQuantity.setText(String.valueOf(groupProducts.getQuantity()));
        if(!groupProducts.getProduct().getProductFeatures().equals("null"))
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
    public void showDialogOnDeleteProduct() {
        new AlertDialog.Builder(new ContextThemeWrapper(ProductDetailsActivity.this, R.style.AppThemeDialog))
                .setMessage(R.string.General_are_you_sure_delete_product)
                .setPositiveButton(android.R.string.yes, (dialog, which) ->
                        presenter.onConfirmDeleteProduct())
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void navigateToPrintQRCodeActivity(@NonNull List<Product> productList) {
        Intent intent = new Intent(context, PrintQRCodesActivity.class)
                .putExtra("product_list", (Serializable) productList);
        startActivity(intent);
        CustomIntent.customType(this, "fadein-to-fadeout");
    }

    @Override
    public void navigateToEditProductActivity(int productId, List<Product> productList, List<Product> allProductList) {
        Intent intent = new Intent(context, EditProductActivity.class)
                .putExtra("product_id", productId)
                .putExtra("product_list", (Serializable) productList)
                .putExtra("all_product_list", (Serializable) allProductList);
        startActivity(intent);
        CustomIntent.customType(this, "fadein-to-fadeout");
    }

    @Override
    public void navigateToMyPantryActivity() {
        Intent intent = new Intent(context, MyPantryActivity.class);
        startActivity(intent);
        CustomIntent.customType(this, "fadein-to-fadeout");
    }

    @Override
    public void navigateToAddPhotoActivity(List<Product> productList, List<Photo> photoList) {
        Intent intent = new Intent(context, AddPhotoActivity.class)
                .putExtra("product_list", (Serializable) productList);
        startActivity(intent);
        CustomIntent.customType(this, "fadein-to-fadeout");
    }

    @Override
    public void navigateToScanProductActivity(List<Product> productList) {
        Intent intent = new Intent(context, ScanProductActivity.class)
                .putExtra("product_list_to_add_barcode", (Serializable) productList);
        startActivity(intent);
        CustomIntent.customType(this, "fadein-to-fadeout");
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.product_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_take_photo) {
            presenter.onClickTakePhoto();
            return true;
        } else if (id == R.id.action_print_codes) {
            presenter.onClickPrintQRCodes();
            return true;
        } else if (id == R.id.action_edit_product) {
            presenter.onClickEditProduct();
            return true;
        } else if (id == R.id.action_add_barcode) {
            presenter.onClickAddBarcode();
            return true;
        } else if (id == R.id.action_delete_product) {
            presenter.onClickDeleteProduct();
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
    public void onPhotoResponse(List<Photo> photoList) {
        presenter.setPhotoList(photoList);
        presenter.showProductDetails();
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
        CustomIntent.customType(this, "fadein-to-fadeout");
    }
}