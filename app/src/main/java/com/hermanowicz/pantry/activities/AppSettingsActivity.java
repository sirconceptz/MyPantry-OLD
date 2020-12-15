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
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.databinding.ActivityAppSettingsBinding;
import com.hermanowicz.pantry.db.ProductDb;
import com.hermanowicz.pantry.dialog.AuthorDialog;
import com.hermanowicz.pantry.interfaces.AppSettingsView;
import com.hermanowicz.pantry.presenters.AppSettingsPresenter;
import com.hermanowicz.pantry.utils.Notification;
import com.hermanowicz.pantry.utils.Orientation;
import com.hermanowicz.pantry.utils.ThemeMode;

/**
 * <h1>AppSettingsActivity</h1>
 * Activity for application settings.
 *
 * @author  Mateusz Hermanowicz
 * @version 1.0
 * @since   1.0
 */

public class AppSettingsActivity extends AppCompatActivity implements AppSettingsView {

    private ActivityAppSettingsBinding binding;
    private AppSettingsPresenter presenter;
    private Context context;

    private TextView appVersion, appAuthor;
    private Spinner themeSelector, cameraSelector;
    private CheckBox vibrationMode, soundMode, pushNotifications, emailNotifications;
    private EditText daysToNofitication, emailAddress;
    private Button saveSettings, clearProductDatabase;
    private NumberPicker hourOfNotification;

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
        binding = ActivityAppSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = getApplicationContext();

        Toolbar toolbar = binding.toolbar;
        appVersion = binding.appVersion;
        appAuthor = binding.appAuthor;
        themeSelector = binding.spinnerAppThemeSelector;
        cameraSelector = binding.spinnerCameraSelector;
        vibrationMode = binding.checkboxScannerVibrationMode;
        soundMode = binding.checkboxScannerSoundMode;
        daysToNofitication = binding.edittextDaysToNotification;
        pushNotifications = binding.checkboxPushNotifications;
        emailNotifications = binding.checkboxEmailNotifications;
        emailAddress = binding.edittextEmailAddress;
        saveSettings = binding.buttonSaveSettings;
        clearProductDatabase = binding.buttonClearProductDatabase;
        hourOfNotification = binding.numberpickerHourOfNotification;

        appAuthor.setText(String.format("%s: %s", getString(R.string.General_author_label), getString(R.string.Author_name)));

        setSupportActionBar(toolbar);
        setNumberpickerSettings();
        setSpinnerAdapters();

        presenter = new AppSettingsPresenter(this, PreferenceManager.getDefaultSharedPreferences(context));
        presenter.loadSettings();
    }

    private void setListeners() {
        saveSettings.setOnClickListener(view -> onClickSaveSettingsButton());
        clearProductDatabase.setOnClickListener(view -> onClickClearDatabaseButton());
        appAuthor.setOnClickListener(view -> onClickAuthorLabel());

        emailAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String emailAddress = binding.edittextEmailAddress.getText().toString();
                presenter.enableEmailCheckbox(emailAddress);
            }
        });
    }

    private void setNumberpickerSettings() {
        hourOfNotification.setMinValue(1);
        hourOfNotification.setMaxValue(24);
        hourOfNotification.setWrapSelectorWheel(false);
    }

    private void setSpinnerAdapters() {
        ArrayAdapter<CharSequence> appThemeSelectorAdapter = ArrayAdapter.createFromResource(context, R.array.AppSettingsActivity_darkmodeSelector, R.layout.custom_spinner);
        binding.spinnerAppThemeSelector.setAdapter(appThemeSelectorAdapter);
        ArrayAdapter<CharSequence> cameraSelectorAdapter = ArrayAdapter.createFromResource(context, R.array.AppSettingsActivity_camera_to_scan, R.layout.custom_spinner);
        binding.spinnerCameraSelector.setAdapter(cameraSelectorAdapter);
    }

    private void onClickClearDatabaseButton() {
        new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppThemeDialog))
                .setMessage(R.string.AppSettingsActivity_clear_database)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> presenter.clearDatabase())
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void onClickAuthorLabel() {
        AuthorDialog authorDialog = new AuthorDialog();
        authorDialog.show(getSupportFragmentManager(), "");
    }

    void onClickSaveSettingsButton() {
        presenter.setSelectedTheme(themeSelector.getSelectedItemPosition());
        presenter.setSelectedScanCamera(cameraSelector.getSelectedItemPosition());
        presenter.setScannerVibrationMode(vibrationMode.isChecked());
        presenter.setScannerSoundMode(soundMode.isChecked());
        presenter.setDaysBeforeExpirationDate(Integer.parseInt(daysToNofitication.getText().toString()));
        presenter.setIsEmailNotificationsAllowed(emailNotifications.isChecked());
        presenter.setIsPushNotificationsAllowed(pushNotifications.isChecked());
        presenter.setHourOfNotifications(hourOfNotification.getValue());
        presenter.setEmailAddress(emailAddress.getText().toString());

        presenter.saveSettings();
    }

    @Override
    public void setSelectedTheme(int selectedTheme) {
        themeSelector.setSelection(selectedTheme);
    }

    @Override
    public void setScanCamera(int selectedCamera) {
        cameraSelector.setSelection(selectedCamera);
    }

    @Override
    public void setCheckboxScannerVibrationMode(boolean mode) {
        vibrationMode.setChecked(mode);
    }

    @Override
    public void setCheckboxScannerSoundMode(boolean mode) {
        soundMode.setChecked(mode);
    }

    @Override
    public void setDaysBeforeExpirationDate(int days) {
        daysToNofitication.setText(String.valueOf(days));
    }

    @Override
    public void setCheckboxPushNotification(boolean mode) {
        pushNotifications.setChecked(mode);
    }

    @Override
    public void setCheckboxEmailNotification(boolean mode) {
        emailNotifications.setChecked(mode);
    }

    @Override
    public void setEmailAddress(String address) {
        emailAddress.setText(address);
    }

    @Override
    public void setHourOfNotifications(int hour) {
        hourOfNotification.setValue(hour);
    }

    @Override
    public void recreateNotifications() {
        Notification.cancelAllNotifications(context);
        Notification.createNotificationsForAllProducts(context);
    }

    @Override
    public void enableEmailCheckbox(boolean isValidEmail) {
        emailNotifications.setEnabled(isValidEmail);
    }

    @Override
    public void onSettingsSaved() {
        Toast.makeText(context, getString(R.string.AppSettingsActivity_settings_saved_successful), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDatabaseClear() {
        ProductDb productDb = ProductDb.getInstance(context);
        productDb.productsDao().clearDb();
        Notification.cancelAllNotifications(context);
        Toast.makeText(context, getString(R.string.AppSettingsActivity_database_is_clear), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showCodeVersion(String version) {
        appVersion.setText(String.format("%s: %s", getString(R.string.AppSettingsActivity_version), version));
    }

    @Override
    public void navigateToMainActivity() {
        Intent mainActivityIntent = new Intent(context, MainActivity.class);
        startActivity(mainActivityIntent);
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            presenter.navigateToMainActivity();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}