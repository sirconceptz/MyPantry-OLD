/*
 * Copyright (c) 2019
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
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.db.ProductDb;
import com.hermanowicz.pantry.dialog.ClearDbDialog;
import com.hermanowicz.pantry.interfaces.AppSettingsView;
import com.hermanowicz.pantry.interfaces.DialogListener;
import com.hermanowicz.pantry.presenters.AppSettingsPresenter;
import com.hermanowicz.pantry.utils.Notification;
import com.hermanowicz.pantry.utils.Orientation;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <h1>AppSettingsActivity</h1>
 * Activity for application settings.
 *
 * @author  Mateusz Hermanowicz
 * @version 1.0
 * @since   1.0
 */

public class AppSettingsActivity extends AppCompatActivity implements AppSettingsView, DialogListener {

    private Context context;
    private SharedPreferences preferences;

    private AppSettingsPresenter presenter;

    @BindView(R.id.edittext_daysToNotification)
    EditText daysBeforeExpirationDate;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.checkbox_emailNotifications)
    CheckBox notificationsByEmail;
    @BindView(R.id.checkbox_pushNotifications)
    CheckBox notificationsByPush;
    @BindView(R.id.numberpicker_hourOfNotification)
    NumberPicker hourOfNotifications;
    @BindView(R.id.edittext_emailAddress)
    EditText emailAddress;
    @BindView(R.id.textView_version)
    TextView appVersion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        if(Orientation.isTablet(this))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);

        ButterKnife.bind(this);

        context = getApplicationContext();
        preferences = PreferenceManager.getDefaultSharedPreferences(context);

        setSupportActionBar(toolbar);

        presenter = new AppSettingsPresenter(this, preferences);

        setNumberpickerSettings();
        presenter.loadSettings();

        emailAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String emailAddress = AppSettingsActivity.this.emailAddress.getText().toString();
                presenter.enableEmailCheckbox(emailAddress);
            }
        });
    }

    void setNumberpickerSettings() {
        hourOfNotifications.setMinValue(1);
        hourOfNotifications.setMaxValue(24);
        hourOfNotifications.setWrapSelectorWheel(false);
    }

    @OnClick(R.id.button_clearDatabase)
    void onClickClearDatabaseButton() {
        ClearDbDialog clearDbDialog = new ClearDbDialog();
        clearDbDialog.show(getSupportFragmentManager(), "");
    }

    @OnClick(R.id.button_saveSettings)
    void onClickSaveSettingsButton() {
        int daysToNotification = Integer.parseInt(daysBeforeExpirationDate.getText().toString());
        boolean isEmailNotificationsAllowed = notificationsByEmail.isChecked();
        boolean isPushNotificationsAllowed = notificationsByPush.isChecked();
        int hourOfNotifications = this.hourOfNotifications.getValue();
        String emailAddress = this.emailAddress.getText().toString();

        presenter.setDaysBeforeExpirationDate(daysToNotification);
        presenter.setIsEmailNotificationsAllowed(isEmailNotificationsAllowed);
        presenter.setIsPushNotificationsAllowed(isPushNotificationsAllowed);
        presenter.setHourOfNotifications(hourOfNotifications);
        presenter.setEmailAddress(emailAddress);

        presenter.saveSettings();
    }

    @Override
    public void setDaysBeforeExpirationDate(int daysBeforeExpirationDate) {
        this.daysBeforeExpirationDate.setText(String.valueOf(daysBeforeExpirationDate));
    }

    @Override
    public void setCheckbox_pushNotification(boolean isPushNotificationsAllowed) {
        notificationsByPush.setChecked(isPushNotificationsAllowed);
    }

    @Override
    public void setCheckbox_emailNotification(boolean isEmailNotificationsAllowed) {
        notificationsByEmail.setChecked(isEmailNotificationsAllowed);
    }

    @Override
    public void setEmailAddress(String emailAddress) {
        this.emailAddress.setText(emailAddress);
    }

    @Override
    public void setHourOfNotifications(int hourOfNotifications) {
        this.hourOfNotifications.setValue(hourOfNotifications);
    }

    @Override
    public void recreateNotifications() {
        Notification.cancelAllNotifications(context);
        Notification.createNotificationsForAllProducts(context);
    }

    @Override
    public void enableEmailCheckbox(boolean isValidEmail) {
        notificationsByEmail.setEnabled(isValidEmail);
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
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onPositiveClick() {
        presenter.clearDatabase();
    }
}