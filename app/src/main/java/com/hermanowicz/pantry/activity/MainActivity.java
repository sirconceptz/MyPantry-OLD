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
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.preference.PreferenceManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.databinding.ActivityMainBinding;
import com.hermanowicz.pantry.db.product.Product;
import com.hermanowicz.pantry.db.product.ProductDb;
import com.hermanowicz.pantry.dialog.AuthorDialog;
import com.hermanowicz.pantry.interfaces.AccountView;
import com.hermanowicz.pantry.interfaces.MainView;
import com.hermanowicz.pantry.interfaces.ProductDbResponse;
import com.hermanowicz.pantry.presenter.MainPresenter;
import com.hermanowicz.pantry.util.Orientation;
import com.hermanowicz.pantry.util.PremiumAccess;
import com.hermanowicz.pantry.util.ThemeMode;

import java.util.ArrayList;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

/**
 * <h1>MainActivity</h1>
 * Main activity - main window of the application.
 *
 * @author  Mateusz Hermanowicz
 */

public class MainActivity extends AppCompatActivity implements MainView, AccountView, ProductDbResponse {


    private MainPresenter presenter;
    private Context context;
    private long pressedTime;

    private CardView myPantry, scanProduct, newProduct, ownCategories, storageLocations, appSettings;
    private TextView loggedUser;
    private ImageView authorInfo;
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
        com.hermanowicz.pantry.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = getApplicationContext();

        MobileAds.initialize(context);

        adView = binding.adview;
        authorInfo = binding.authorInfo;
        myPantry = binding.myPantryCV;
        scanProduct = binding.scanProductCV;
        newProduct = binding.newProductCV;
        ownCategories = binding.ownCategoriesCV;
        storageLocations = binding.storageLocationsCV;
        appSettings = binding.appSettingsCV;
        loggedUser = binding.loggedUser;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        presenter = new MainPresenter(this, this, sharedPreferences);
        presenter.setPremiumAccess(new PremiumAccess(context));
        presenter.updateUserData();

        if (!presenter.isPremium()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }

        setOnlineDbProductList(this);

        if (presenter.isOfflineDb()) {
            ProductDb db = ProductDb.getInstance(context);
            List<Product> productList = db.productsDao().getAllProductsList();
            presenter.restoreNotifications(context, productList);
        }
    }

    private void setOnlineDbProductList(ProductDbResponse response) {
        if(!presenter.isOfflineDb()) {
            List<Product> onlineProductList = new ArrayList<>();
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference ref = db.getReference().child("products/" +
                    FirebaseAuth.getInstance().getUid());
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists())
                        onlineProductList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Product product = dataSnapshot.getValue(Product.class);
                        onlineProductList.add(product);
                    }
                    response.onProductResponse(onlineProductList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("FirebaseDB", error.getMessage());
                }
            });
        }
    }

    private void setListeners() {
        myPantry.setOnClickListener(view -> presenter.navigateToMyPantryActivity());
        scanProduct.setOnClickListener(view -> presenter.navigateToScanProductActivity());
        newProduct.setOnClickListener(view -> presenter.navigateToNewProductActivity());
        ownCategories.setOnClickListener(view -> presenter.navigateToCategoriesActivity());
        storageLocations.setOnClickListener(view -> presenter.navigateToStorageLocationsActivity());
        appSettings.setOnClickListener(view -> presenter.navigateToAppSettingsActivity());
        authorInfo.setOnClickListener(view -> presenter.showAuthorInfoDialog());
    }

    @Override
    public void onNavigationToMyPantryActivity() {
        Intent myPantryActivityIntent = new Intent(MainActivity.this, MyPantryActivity.class);
        startActivity(myPantryActivityIntent);
        CustomIntent.customType(this, "fadein-to-fadeout");
    }

    @Override
    public void onNavigationToScanProductActivity() {
        Intent scanProductActivityIntent = new Intent(MainActivity.this, ScanProductActivity.class);
        startActivity(scanProductActivityIntent);
        CustomIntent.customType(this, "fadein-to-fadeout");
    }

    @Override
    public void onNavigationToNewProductActivity() {
        Intent newProductActivityIntent = new Intent(MainActivity.this, NewProductActivity.class);
        startActivity(newProductActivityIntent);
        CustomIntent.customType(this, "fadein-to-fadeout");
    }

    @Override
    public void onNavigationToCategoriesActivity() {
        Intent categoriesActivity = new Intent(MainActivity.this, CategoriesActivity.class);
        startActivity(categoriesActivity);
        CustomIntent.customType(this, "fadein-to-fadeout");
    }

    @Override
    public void onNavigationToStorageLocationsActivity() {
        Intent storageLocationsActivity = new Intent(MainActivity.this, StorageLocationsActivity.class);
        startActivity(storageLocationsActivity);
        CustomIntent.customType(this, "fadein-to-fadeout");
    }

    @Override
    public void onNavigationToAppSettingsActivity() {
        Intent appSettingsActivityIntent = new Intent(MainActivity.this, AppSettingsActivity.class);
        startActivity(appSettingsActivityIntent);
        CustomIntent.customType(this, "fadein-to-fadeout");
    }

    @Override
    public void showAuthorInfoDialog() {
        AuthorDialog authorDialog = new AuthorDialog();
        authorDialog.show(getSupportFragmentManager(), "");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.General_press_back_agait_to_exit), Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int RC_SIGN_IN = 10;
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                presenter.updateUserData();
            }
        }
    }

    @Override
    public void signIn() {
    }

    @Override
    public void signOut() {
    }

    @Override
    public void updateUserData(String userEmail) {
        if(userEmail == null)
            loggedUser.setText(String.format("%s: %s", getString(R.string.General_user), getString(R.string.General_loggedOut)));
        else
            loggedUser.setText(String.format("%s: %s", getString(R.string.General_user), userEmail));
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!presenter.isPremium())
            adView.resume();
    }

    @Override
    public void onPause() {
        super.onResume();
        if(!presenter.isPremium())
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

    @Override
    public void onProductResponse(List<Product> productList) {
        presenter.restoreNotifications(context, productList);
    }
}