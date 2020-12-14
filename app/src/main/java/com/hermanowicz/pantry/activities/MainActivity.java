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

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.databinding.ActivityMainBinding;
import com.hermanowicz.pantry.interfaces.MainView;
import com.hermanowicz.pantry.presenters.MainPresenter;
import com.hermanowicz.pantry.utils.Orientation;
import com.hermanowicz.pantry.utils.ThemeMode;

import java.util.Objects;

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

    private long pressedTime;
    private AdView adView;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(ThemeMode.getThemeMode(this));
        if(Orientation.isTablet(this))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        MobileAds.initialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        adView = binding.adview;

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        presenter = new MainPresenter(this);

        binding.buttonMyPantry.setOnClickListener(view -> presenter.navigateToMyPantryActivity());
        binding.buttonScanProduct.setOnClickListener(view -> presenter.navigateToScanProductActivity());
        binding.buttonNewProduct.setOnClickListener(view -> presenter.navigateToNewProductActivity());
        binding.buttonOwnCategories.setOnClickListener(view -> presenter.navigateToCategoriesActivity());
        binding.buttonAppSettings.setOnClickListener(view -> presenter.navigateToAppSettingsActivity());
    }

    @Override
    public void onNavigationToMyPantryActivity() {
        Intent myPantryActivityIntent = new Intent(MainActivity.this, MyPantryActivity.class);
        startActivity(myPantryActivityIntent);
    }

    @Override
    public void onNavigationToScanProductActivity() {
        Intent scanProductActivityIntent = new Intent(MainActivity.this, ScanProductActivity.class);
        startActivity(scanProductActivityIntent);
    }

    @Override
    public void onNavigationToNewProductActivity() {
        Intent newProductActivityIntent = new Intent(MainActivity.this, NewProductActivity.class);
        startActivity(newProductActivityIntent);
    }

    @Override
    public void onNavigationToCategoriesActivity() {
        Intent categoriesActivity = new Intent(MainActivity.this, CategoriesActivity.class);
        startActivity(categoriesActivity);
    }

    @Override
    public void onNavigationToAppSettingsActivity() {
        Intent appSettingsActivityIntent = new Intent(MainActivity.this, AppSettingsActivity.class);
        startActivity(appSettingsActivityIntent);
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
}