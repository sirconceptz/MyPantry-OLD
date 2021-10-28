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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.databinding.ActivityStorageLocationDetailsBinding;
import com.hermanowicz.pantry.db.storagelocation.StorageLocation;
import com.hermanowicz.pantry.interfaces.StorageLocationDetailsView;
import com.hermanowicz.pantry.presenter.StorageLocationDetailsPresenter;
import com.hermanowicz.pantry.util.Orientation;
import com.hermanowicz.pantry.util.ThemeMode;

import maes.tech.intentanim.CustomIntent;

/**
 * <h1>StorageLocationActivity</h1>
 * Activity for add new storage location.
 *
 * @author Mateusz Hermanowicz
 */

public class StorageLocationDetailsActivity extends AppCompatActivity implements StorageLocationDetailsView {

    private StorageLocationDetailsPresenter presenter;
    private Context context;

    private TextView storageLocationName;
    private TextView storageLocationDescription;
    private TextView nameCharCounter;
    private TextView descriptionCharCounter;
    private Button updateStorageLocation;
    private Button deleteStorageLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(ThemeMode.getThemeMode(this));
        if (Orientation.isTablet(this))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        super.onCreate(savedInstanceState);
        initView();
        setListeners();
    }

    private void initView() {
        context = getApplicationContext();
        com.hermanowicz.pantry.databinding.ActivityStorageLocationDetailsBinding binding = ActivityStorageLocationDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        storageLocationName = binding.storageLocationName;
        storageLocationDescription = binding.storageLocationDescription;
        nameCharCounter = binding.nameCharCounter;
        descriptionCharCounter = binding.descriptionCharCounter;
        updateStorageLocation = binding.buttonUpdateStorageLocation;
        deleteStorageLocation = binding.buttonDeleteStorageLocation;

        Intent storageLocationIntent = getIntent();
        StorageLocation storageLocation = (StorageLocation) storageLocationIntent.getSerializableExtra("storage_location");
        presenter = new StorageLocationDetailsPresenter(this, context);
        presenter.setStorageLocation(storageLocation);
    }

    private void setListeners(){
        updateStorageLocation.setOnClickListener(view -> onClickUpdateStorageLocation());
        deleteStorageLocation.setOnClickListener(view -> onClickDeleteStorageLocation());

        storageLocationName.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                presenter.isStorageLocationNameCorrect(storageLocationName.getText().toString());
            }
        });

        storageLocationDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                presenter.isStorageLocationDescriptionCorrect(storageLocationDescription.getText().toString());
            }
        });
    }

    private void onClickUpdateStorageLocation(){
        StorageLocation storageLocation = presenter.getStorageLocation();
        storageLocation.setName(storageLocationName.getText().toString());
        storageLocation.setDescription(storageLocationDescription.getText().toString());
        presenter.updateStorageLocation(storageLocation);
    }

    private void onClickDeleteStorageLocation() {
        new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppThemeDialog))
                .setMessage(R.string.StorageLocationDetailsActivity_storage_location_delete_warning)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> presenter.deleteStorageLocation())
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void showErrorOnUpdateStorageLocation() {
        Toast.makeText(context, getString(R.string.Error_wrong_data), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showStorageLocationUpdated() {
        Toast.makeText(context, getString(R.string.StorageLocationDetailsActivity_storage_location_was_saved), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showStorageLocationDetails(@NonNull StorageLocation storageLocation) {
        storageLocationName.setText(storageLocation.getName());
        storageLocationDescription.setText(storageLocation.getDescription());
    }

    @Override
    public void showStorageLocationNameError() {
        storageLocationName.setError(getText(R.string.Error_char_counter));
    }

    @Override
    public void showStorageLocationDescriptionError() {
        storageLocationDescription.setError(getText(R.string.Error_char_counter));
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void updateNameCharCounter(int charCounter, int maxChar) {
        nameCharCounter.setText(String.format("%s: %d/%d",
                getText(R.string.General_char_counter).toString(), charCounter, maxChar));
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void updateDescriptionCharCounter(int charCounter, int maxChar) {
        descriptionCharCounter.setText(String.format("%s: %d/%d",
                getText(R.string.General_char_counter).toString(), charCounter, maxChar));
    }

    @Override
    public void navigateToStorageLocationActivity() {
        Intent intent = new Intent (getApplicationContext(), StorageLocationsActivity.class);
        startActivity(intent);
        CustomIntent.customType(this, "fadein-to-fadeout");
    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this, "fadein-to-fadeout");
    }
}