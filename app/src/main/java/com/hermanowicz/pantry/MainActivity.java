/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.hermanowicz.pantry.presenters.MainActivityPresenter;
import com.hermanowicz.pantry.views.MainActivityView;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <h1>MainActivity</h1>
 * Main activity - main window of the application. First window after launching this application.
 * In main activity is only main menu with buttons to the other activities.
 *
 * @author  Mateusz Hermanowicz
 * @version 1.0
 * @since   1.0
 */
public class MainActivity extends AppCompatActivity implements MainActivityView {

    private MainActivityPresenter presenter;

    @BindView(R.id.button_myPantry)
    Button button_myPantry;
    @BindView(R.id.button_scanProduct)
    Button button_scanProduct;
    @BindView(R.id.button_newProduct)
    Button button_newProduct;
    @BindView(R.id.button_appSettings)
    Button button_appSettings;
    @BindView(R.id.adBanner)
    AdView adView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-4025776034769422~3797748160");

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        presenter = new MainActivityPresenter(this, null);

        button_myPantry.setOnClickListener(view -> {
            Intent myPantryActivityIntent = new Intent(MainActivity.this, MyPantryActivity.class);
            startActivity(myPantryActivityIntent);
            finish();
        });

        button_scanProduct.setOnClickListener(view -> {
            Intent scanProductActivityIntent = new Intent(MainActivity.this, ScanProductActivity.class);
            startActivity(scanProductActivityIntent);
            finish();
        });

        button_newProduct.setOnClickListener(view -> {
            Intent newProductActivityIntent = new Intent(MainActivity.this, NewProductActivity.class);
            startActivity(newProductActivityIntent);
            finish();
        });

        button_appSettings.setOnClickListener(view -> {
            Intent appSettingsActivityIntent = new Intent(MainActivity.this, AppSettingsActivity.class);
            startActivity(appSettingsActivityIntent);
            finish();
        });
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