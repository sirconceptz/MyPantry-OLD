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
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

/**
 * <h1>MainActivity</h1>
 * Main activity - main window of the application. First window after launching this application.
 * In main activity is only main menu with buttons to the other activities.
 *
 * @author  Mateusz Hermanowicz
 * @version 1.0
 * @since   1.0
 */
public class MainActivity extends AppCompatActivity {

    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button myPantryButton    = findViewById(R.id.button_myPantry);
        Button scanProductButton = findViewById(R.id.button_scanProduct);
        Button newProductButton  = findViewById(R.id.button_newProduct);
        Button appSettingsButton = findViewById(R.id.button_appSettings);
        adView                   = findViewById(R.id.adBanner);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-4025776034769422~3797748160");

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        myPantryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myPantryActivityIntent = new Intent(MainActivity.this, MyPantryActivity.class);
                startActivity(myPantryActivityIntent);
                finish();
            }
        });

        scanProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent scanProductActivityIntent = new Intent(MainActivity.this, ScanProductActivity.class);
                startActivity(scanProductActivityIntent);
                finish();
            }
        });

        newProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newProductActivityIntent = new Intent(MainActivity.this, NewProductActivity.class);
                startActivity(newProductActivityIntent);
                finish();
            }
        });

        appSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent appSettingsActivityIntent = new Intent(MainActivity.this, AppSettingsActivity.class);
                startActivity(appSettingsActivityIntent);
                finish();
            }
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