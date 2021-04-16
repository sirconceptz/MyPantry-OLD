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

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.db.product.ProductDb;
import com.hermanowicz.pantry.interfaces.AppSettingsView;
import com.hermanowicz.pantry.presenter.AppSettingsPresenter;
import com.hermanowicz.pantry.util.Notification;
import com.hermanowicz.pantry.util.Orientation;
import com.hermanowicz.pantry.util.ThemeMode;

import maes.tech.intentanim.CustomIntent;

/**
 * <h1>AppSettingsActivity</h1>
 * Activity for application settings.
 *
 * @author  Mateusz Hermanowicz
 * @version 1.0
 * @since   1.0
 */

public class AppSettingsActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(ThemeMode.getThemeMode(this));
        if(Orientation.isTablet(this))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    private void navigateToMainActivity(){
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        startActivity(mainActivityIntent);
        CustomIntent.customType(this, "fadein-to-fadeout");
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            navigateToMainActivity();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this, "fadein-to-fadeout");
    }

    public static class MyPreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener, AppSettingsView {

        AppSettingsPresenter presenter;
        Preference selectedTheme, scanCamera, emailAddress, notificationDaysBefore,
                emailNotifications, clearDb, version;

        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            initView();
            setListeners();
            presenter = new AppSettingsPresenter(this, PreferenceManager.getDefaultSharedPreferences(getContext()));
            presenter.showStoredPreferences();
        }

        private void initView(){
            addPreferencesFromResource(R.xml.preferences);
            selectedTheme = findPreference(getString(R.string.PreferencesKey_selected_application_theme));
            scanCamera = findPreference(getString(R.string.PreferencesKey_scan_camera));
            notificationDaysBefore = findPreference(getString(R.string.PreferencesKey_notification_days_before_expiration));
            emailAddress = findPreference(getString(R.string.PreferencesKey_email_address));
            emailNotifications = findPreference(getString(R.string.PreferencesKey_email_notifications));
            clearDb = findPreference(getString(R.string.PreferencesKey_clear_db));
            version = findPreference(getString(R.string.PreferencesKey_version));
        }

        public void setListeners(){
            clearDb.setOnPreferenceClickListener(preference -> {
                onClickClearDatabaseButton();
                return false;
            });
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }

        private void onClickClearDatabaseButton() {
            new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AppThemeDialog))
                    .setMessage(R.string.AppSettingsActivity_clear_database_statement)
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> presenter.clearDatabase())
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
        {
            if(key.equals(getString(R.string.PreferencesKey_push_notifications)) || key.equals(getString(R.string.PreferencesKey_notification_days_before_expiration)))
                presenter.reCreateNotifications();
            if(key.equals(getString(R.string.PreferencesKey_selected_application_theme)))
                presenter.showSelectedTheme();
                presenter.refreshActivity();
            if(key.equals(getString(R.string.PreferencesKey_scan_camera)))
                presenter.showSelectedScanCamera();
            if(key.equals(getString(R.string.PreferencesKey_notification_days_before_expiration)))
                presenter.showDaysToNotification();
            if(key.equals(getString(R.string.PreferencesKey_email_address))){
                presenter.showEmailAddress();
            }
        }

        @Override
        public void recreateNotifications() {
            Notification.cancelAllNotifications(getContext());
            Notification.createNotificationsForAllProducts(getContext());
        }

        @Override
        public void onDatabaseClear() {
            ProductDb productDb = ProductDb.getInstance(getContext());
            productDb.productsDao().clearDb();
            Notification.cancelAllNotifications(getContext());
            Toast.makeText(getContext(), getString(R.string.AppSettingsActivity_database_is_clear), Toast.LENGTH_LONG).show();
        }

        @Override
        public void showSelectedTheme(int themeId) {
            String[] themeList = getResources().getStringArray(R.array.AppSettingsActivity_darkmode_selector);
            selectedTheme.setSummary(themeList[themeId]);
        }

        @Override
        public void showSelectedScanCamera(int scanCameraId) {
            String[] scanCameraList = getResources().getStringArray(R.array.AppSettingsActivity_camera_to_scan);
            scanCamera.setSummary(scanCameraList[scanCameraId]);
        }

        @Override
        public void showEmailAddress(String address) {
            emailAddress.setSummary(address);
            emailNotifications.setEnabled(true);
        }

        @Override
        public void showDaysToNotification(int quantity) {
            notificationDaysBefore.setSummary(String.valueOf(quantity));
        }

        @Override
        public void showVersionCode(String appVersion) {
            version.setSummary(appVersion);
        }

        @Override
        public void setEmailPreferences() {
            emailAddress.setSummary("");
            emailNotifications.setEnabled(false);
        }

        @Override
        public void refreshActivity() {
            getActivity().finish();
            startActivity(getActivity().getIntent());
        }
    }
}