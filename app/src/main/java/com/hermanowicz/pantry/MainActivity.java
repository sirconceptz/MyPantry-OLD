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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.hermanowicz.pantry.interfaces.MainActivityView;
import com.hermanowicz.pantry.presenters.MainActivityPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @BindView(R.id.adBanner)
    AdView adView;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-4025776034769422~3797748160");

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        presenter = new MainActivityPresenter(this);
    }

    @OnClick(R.id.button_myPantry)
    void onClickMyPantryButton() {
        presenter.navigateToMyPantryActivity();
    }

    @OnClick(R.id.button_scanProduct)
    void onClickScanProductButton() {
        presenter.navigateToScanProductActivity();
    }

    @OnClick(R.id.button_newProduct)
    void onClickNewProductButton() {
        presenter.navigateToNewProductActivity();
    }

    @OnClick(R.id.button_appSettings)
    void onClickAppSettingsButton() {
        presenter.navigateToAppSettingsActivity();
    }

    @Override
    public void onNavigationToMyPantryActivity() {
        Intent scanProductActivityIntent = new Intent(MainActivity.this, MyPantryActivity.class);
        startActivity(scanProductActivityIntent);
        finish();
    }

    @Override
    public void onNavigationToScanProductActivity() {
        Intent scanProductActivityIntent = new Intent(MainActivity.this, ScanProductActivity.class);
        startActivity(scanProductActivityIntent);
        finish();
    }

    @Override
    public void onNavigationToNewProductActivity() {
        Intent newProductActivityIntent = new Intent(MainActivity.this, NewProductActivity.class);
        startActivity(newProductActivityIntent);
        finish();
    }

    @Override
    public void onNavigationToAppSettingsActivity() {
        Intent appSettingsActivityIntent = new Intent(MainActivity.this, AppSettingsActivity.class);
        startActivity(appSettingsActivityIntent);
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter = new MainActivityPresenter(this);
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
        presenter.onDestroy();
        super.onDestroy();
    }
}