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
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.databinding.ActivityMainBinding;
import com.hermanowicz.pantry.dialog.AuthorDialog;
import com.hermanowicz.pantry.interfaces.MainView;
import com.hermanowicz.pantry.presenter.MainPresenter;
import com.hermanowicz.pantry.util.Orientation;
import com.hermanowicz.pantry.util.ThemeMode;

import maes.tech.intentanim.CustomIntent;

/**
 * <h1>MainActivity</h1>
 * Main activity - main window of the application. First window after launching this application.
 *
 * @author  Mateusz Hermanowicz
 * @version 1.0
 * @since   1.0
 */

public class MainActivity extends AppCompatActivity implements MainView {

    private ActivityMainBinding binding;
    private MainPresenter presenter;
    private Context context;
    private long pressedTime;

    private CardView myPantry, scanProduct, newProduct, ownCategories, storageLocations, appSettings;
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
        binding = ActivityMainBinding.inflate(getLayoutInflater());
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

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        presenter = new MainPresenter(this);
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
        CustomIntent.customType(this, "up-to-bottom");
    }

    @Override
    public void onNavigationToScanProductActivity() {
        Intent scanProductActivityIntent = new Intent(MainActivity.this, ScanProductActivity.class);
        startActivity(scanProductActivityIntent);
        CustomIntent.customType(this, "up-to-bottom");
    }

    @Override
    public void onNavigationToNewProductActivity() {
        Intent newProductActivityIntent = new Intent(MainActivity.this, NewProductActivity.class);
        startActivity(newProductActivityIntent);
        CustomIntent.customType(this, "up-to-bottom");
    }

    @Override
    public void onNavigationToCategoriesActivity() {
        Intent categoriesActivity = new Intent(MainActivity.this, CategoriesActivity.class);
        startActivity(categoriesActivity);
        CustomIntent.customType(this, "up-to-bottom");
    }

    @Override
    public void onNavigationToStorageLocationsActivity() {
        Intent storageLocationsActivity = new Intent(MainActivity.this, StorageLocationsActivity.class);
        startActivity(storageLocationsActivity);
        CustomIntent.customType(this, "up-to-bottom");
    }

    @Override
    public void onNavigationToAppSettingsActivity() {
        Intent appSettingsActivityIntent = new Intent(MainActivity.this, AppSettingsActivity.class);
        startActivity(appSettingsActivityIntent);
        CustomIntent.customType(this, "up-to-bottom");
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