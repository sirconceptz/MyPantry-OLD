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
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.firebase.ui.auth.AuthUI;
import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.interfaces.AccountView;
import com.hermanowicz.pantry.interfaces.AppSettingsView;
import com.hermanowicz.pantry.interfaces.PremiumUserView;
import com.hermanowicz.pantry.presenter.AppSettingsPresenter;
import com.hermanowicz.pantry.util.Notification;
import com.hermanowicz.pantry.util.Orientation;
import com.hermanowicz.pantry.util.PremiumAccess;
import com.hermanowicz.pantry.util.ThemeMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

/**
 * <h1>AppSettingsActivity</h1>
 * Activity for application settings.
 *
 * @author  Mateusz Hermanowicz
 */

public class AppSettingsActivity extends AppCompatActivity {

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

    public static class MyPreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener,
            AppSettingsView, AccountView, PremiumUserView, PurchasesUpdatedListener {

        private final int RC_SIGN_IN = 10;

        private AppSettingsPresenter presenter;
        private Preference qr;
        private Preference scanCamera;
        private Preference emailAddress;
        private Preference notificationDaysBefore;
        private Preference emailNotifications;
        private Preference backupProductDb;
        private Preference restoreProductDb;
        private Preference clearProductDb;
        private Preference backupCategoryDb;
        private Preference restoreCategoryDb;
        private Preference clearCategoryDb;
        private Preference backupStorageLocationDb;
        private Preference restoreStorageLocationDb;
        private Preference clearStorageLocationDb;
        private Preference version;
        private Preference goPremium;
        private Preference activeUser;
        private Preference databaseMode;
        private Preference importDb;
        private BillingClient billingClient;
        private final List<String> skuList = Collections.singletonList("premium");
        private SkuDetails skuDetails;

        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            initView();
            setListeners();
            presenter = new AppSettingsPresenter(this, this, this, getContext());
            presenter.showStoredPreferences(getResources());
            setupBillingClient();
        }

        private void initView(){
            addPreferencesFromResource(R.xml.preferences);
            qr = findPreference(getString(R.string.PreferencesKey_selected_application_theme));
            scanCamera = findPreference(getString(R.string.PreferencesKey_scan_camera));
            notificationDaysBefore = findPreference(getString(R.string.PreferencesKey_notification_days_before_expiration));
            emailAddress = findPreference(getString(R.string.PreferencesKey_email_address));
            emailNotifications = findPreference(getString(R.string.PreferencesKey_email_notifications));
            restoreProductDb = findPreference(getString(R.string.PreferencesKey_restore_product_db));
            backupProductDb = findPreference(getString(R.string.PreferencesKey_backup_product_db));
            clearProductDb = findPreference(getString(R.string.PreferencesKey_clear_product_db));
            restoreCategoryDb = findPreference(getString(R.string.PreferencesKey_restore_category_db));
            backupCategoryDb = findPreference(getString(R.string.PreferencesKey_backup_category_db));
            clearCategoryDb = findPreference(getString(R.string.PreferencesKey_clear_category_db));
            restoreStorageLocationDb = findPreference(getString(R.string.PreferencesKey_restore_storage_location_db));
            backupStorageLocationDb = findPreference(getString(R.string.PreferencesKey_backup_storage_location_db));
            clearStorageLocationDb = findPreference(getString(R.string.PreferencesKey_clear_storage_location_db));
            version = findPreference(getString(R.string.PreferencesKey_version));
            activeUser = findPreference(getString(R.string.PreferencesKey_active_user));
            goPremium = findPreference(getString(R.string.PreferencesKey_go_premium));
            databaseMode = findPreference(getString(R.string.PreferencesKey_database_mode));
            importDb = findPreference(getString(R.string.PreferencesKey_import_db));
        }

        public void setListeners(){
            goPremium.setOnPreferenceClickListener(preference -> {
                presenter.goPremium();
                return false;
            });

            activeUser.setOnPreferenceClickListener(preference -> {
                presenter.signInOrSignOut();
                return false;
            });

            backupProductDb.setOnPreferenceClickListener(preference -> {
                presenter.onClickBackupProductDatabase();

                return false;
            });
            restoreProductDb.setOnPreferenceClickListener(preference -> {
                presenter.onClickRestoreProductDatabase();

                return false;
            });
            clearProductDb.setOnPreferenceClickListener(preference -> {
                presenter.onClickClearProductDatabase();
                return false;
            });

            backupCategoryDb.setOnPreferenceClickListener(preference -> {
                presenter.onClickBackupCategoryDatabase();
                return false;
            });
            restoreCategoryDb.setOnPreferenceClickListener(preference -> {
                presenter.onClickRestoreCategoryDatabase();
                return false;
            });
            clearCategoryDb.setOnPreferenceClickListener(preference -> {
                presenter.onClickClearCategoryDatabase();
                return false;
            });

            backupStorageLocationDb.setOnPreferenceClickListener(preference -> {
                presenter.onClickBackupStorageLocationDatabase();
                return false;
            });
            restoreStorageLocationDb.setOnPreferenceClickListener(preference -> {
                presenter.onClickRestoreStorageLocationDatabase();
                return false;
            });
            clearStorageLocationDb.setOnPreferenceClickListener(preference -> {
                presenter.onClickClearStorageLocationDatabase();
                return false;
            });
            importDb.setOnPreferenceClickListener(preference -> {
                presenter.onClickImportDb();
                return false;
            });
        }

        private void setupBillingClient() {
            billingClient = BillingClient.newBuilder(getActivity())
                    .setListener(this)
                    .enablePendingPurchases()
                    .build();
            billingClient.startConnection(new BillingClientStateListener() {
                @Override
                public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        loadAllSKUs();
                        restorePremium();
                    } else
                        presenter.billingClientNotReady();
                }
                @Override
                public void onBillingServiceDisconnected() {
                    Log.e("BillingClient:", billingClient.toString());
                }
            });
        }

        private void restorePremium() {
            if(!presenter.isPremiumRestored()) {
                billingClient.queryPurchasesAsync(BillingClient.SkuType.INAPP, (billingResult, list) -> {
                    for (Purchase purchase : list) {
                        ArrayList<String> thisSkuList = purchase.getSkus();
                        for (String sku : thisSkuList) {
                            if (sku.equals(skuList.get(0))) {
                                presenter.setPremiumIsRestored();
                                enablePremiumFeatures();
                                break;
                            }
                        }
                    }
                });
            }
        }

        private void loadAllSKUs() {
            if(billingClient.isReady()){
                SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
                billingClient.querySkuDetailsAsync(params.build(),
                        (billingResult, skuDetailsList) -> {
                            if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                                assert skuDetailsList != null;
                                if (!skuDetailsList.isEmpty()) for (Object skuDetailsObject : skuDetailsList) {
                                    SkuDetails testSku = (SkuDetails) skuDetailsObject;
                                    if (testSku.equals(skuDetailsList.get(0))) {
                                        skuDetails = testSku;
                                        goPremium.setEnabled(true);
                                    }
                                }
                            }
                        });
            }
            else
                Toast.makeText(getContext(), getString(R.string.Error_billing_client_not_ready), Toast.LENGTH_SHORT).show();
        }

        private void enablePremiumFeatures() {
            new PremiumAccess(getContext()).enablePremiumAccess();
            refreshActivity();
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

        @Override
        public void onSharedPreferenceChanged(@NonNull SharedPreferences sharedPreferences, @NonNull String key)
        {
            refreshActivity();
            if(key.equals(getString(R.string.PreferencesKey_push_notifications)) || key.equals(getString(R.string.PreferencesKey_notification_days_before_expiration)))
                presenter.reCreateNotifications();
            if(key.equals(getString(R.string.PreferencesKey_selected_application_theme)))
                presenter.showSelectedTheme();
            if(key.equals(getString(R.string.PreferencesKey_scan_camera)))
                presenter.showSelectedScanCamera();
            if(key.equals(getString(R.string.PreferencesKey_notification_days_before_expiration)))
                presenter.showDaysToNotification();
            if(key.equals(getString(R.string.PreferencesKey_email_address))){
                presenter.showEmailAddress();
            }
            if(key.equals(getString(R.string.PreferencesKey_database_mode))){
                presenter.showDatabaseMode(getResources());
            }
        }

        @Override
        public void recreateNotifications() {
            Context context = getContext();
            Notification.cancelAllNotifications(context);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            sharedPreferences.edit().putBoolean("IS_NOTIFICATIONS_TO_RESTORE", true).apply();
        }

        @Override
        public void onProductDatabaseClear() {
            Notification.cancelAllNotifications(getContext());
            Toast.makeText(getContext(), getString(R.string.AppSettingsActivity_database_is_clear), Toast.LENGTH_LONG).show();
        }

        @Override
        public void showSelectedTheme(int themeId) {
            String[] themeList = getResources().getStringArray(R.array.AppSettingsActivity_darkmode_selector);
            qr.setSummary(themeList[themeId]);
        }

        @Override
        public void showSelectedScanCamera(int scanCameraId) {
            String[] scanCameraList = getResources().getStringArray(R.array.AppSettingsActivity_camera_to_scan);
            scanCamera.setSummary(scanCameraList[scanCameraId]);
        }

        @Override
        public void showEmailAddress(@NonNull String address) {
            emailAddress.setSummary(address);
            emailNotifications.setEnabled(true);
        }

        @Override
        public void showDaysToNotification(int quantity) {
            notificationDaysBefore.setSummary(String.valueOf(quantity));
        }

        @Override
        public void showActiveUser(String activeUser) {
            if(activeUser == null)
                this.activeUser.setSummary(R.string.General_loggedOut);
            else
                this.activeUser.setSummary(activeUser);
        }

        @Override
        public void showDatatabaseMode(@NonNull String databaseMode) {
            this.databaseMode.setSummary(databaseMode);
        }

        @Override
        public void showVersionCode(@NonNull String appVersion) {
            version.setSummary(appVersion);
        }

        @Override
        public void setEmailPreferences() {
            emailAddress.setSummary("");
            emailNotifications.setEnabled(false);
        }

        @Override
        public void showDialogBackupProductDb() {
            new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AppThemeDialog))
                    .setMessage(R.string.AppSettingsActivity_export_product_database)
                    .setPositiveButton(android.R.string.yes, (dialog, which) ->
                            presenter.backupProductDatabase(getContext()))
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        @Override
        public void showDialogRestoreProductDb() {
            new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AppThemeDialog))
                    .setMessage(R.string.AppSettingsActivity_import_product_database)
                    .setPositiveButton(android.R.string.yes, (dialog, which) ->
                            presenter.restoreProductDatabase(getContext()))
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        @Override
        public void showDialogClearProductDb() {
            new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AppThemeDialog))
                    .setMessage(R.string.AppSettingsActivity_clear_database_statement)
                    .setPositiveButton(android.R.string.yes, (dialog, which) ->
                            presenter.clearProductDatabase(getContext()))
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        @Override
        public void showDialogBackupCategoryDb() {
            new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AppThemeDialog))
                    .setMessage(R.string.AppSettingsActivity_export_category_database)
                    .setPositiveButton(android.R.string.yes, (dialog, which) ->
                            presenter.backupCategoryDatabase(getContext()))
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        @Override
        public void showDialogRestoreCategoryDb() {
            new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AppThemeDialog))
                    .setMessage(R.string.AppSettingsActivity_import_category_database)
                    .setPositiveButton(android.R.string.yes, (dialog, which) ->
                            presenter.restoreCategoryDatabase(getContext()))
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        @Override
        public void showDialogClearCategoryDb() {
            new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AppThemeDialog))
                    .setMessage(R.string.AppSettingsActivity_clear_database_statement)
                    .setPositiveButton(android.R.string.yes, (dialog, which) ->
                            presenter.clearCategoryDatabase(getContext()))
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        @Override
        public void showDialogBackupStorageLocationDb() {
            new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AppThemeDialog))
                    .setMessage(R.string.AppSettingsActivity_export_storage_location_database)
                    .setPositiveButton(android.R.string.yes, (dialog, which) ->
                            presenter.backupStorageLocationDatabase(getContext()))
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        @Override
        public void showDialogRestoreStorageLocationDb() {
            new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AppThemeDialog))
                    .setMessage(R.string.AppSettingsActivity_import_storage_location_database)
                    .setPositiveButton(android.R.string.yes, (dialog, which) ->
                            presenter.restoreStorageLocationDatabase(getContext()))
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        @Override
        public void showDialogClearStorageLocationDb() {
            new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AppThemeDialog))
                    .setMessage(R.string.AppSettingsActivity_clear_database_statement)
                    .setPositiveButton(android.R.string.yes, (dialog, which) ->
                            presenter.clearStorageLocationDatabase(getContext()))
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        @Override
        public void showDbBackupHasBeenMade() {
            Toast.makeText(getContext(), getString(R.string.AppSettingsActivity_db_backup_has_been_made),
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void showDbHasBeenRestored() {
            Toast.makeText(getContext(), getString(R.string.AppSettingsActivity_db_has_been_restored),
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void showDbHasBeenClear() {
            Toast.makeText(getContext(), getString(R.string.AppSettingsActivity_db_has_been_clear),
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void signIn() {
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build());
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setTheme(R.style.AppTheme)
                            .build(),
                    RC_SIGN_IN);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == RC_SIGN_IN) {
                if (resultCode == RESULT_OK) {
                    presenter.updateUserData();
                    refreshActivity();
                }
            }
        }

        @Override
        public void signOut() {
            AuthUI.getInstance().signOut(getContext());
            presenter.setDatabaseMode("local");
            refreshActivity();
        }

        @Override
        public void updateUserData(String userEmail) {
            activeUser.setSummary(userEmail);
        }

        @Override
        public void refreshActivity() {
            getActivity().finish();
            startActivity(getActivity().getIntent());
        }

        @Override
        public void showInfoUserIsPremium() {
            Toast.makeText(getContext(), getString(R.string.General_user_is_premium), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void enableDatabaseModeSelection(@NonNull Boolean isEnabled) {
            databaseMode.setEnabled(isEnabled);
        }

        @Override
        public void buyPremiumFeatures() {
            BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                    .setSkuDetails(skuDetails)
                    .build();
            int responseCode = billingClient.launchBillingFlow(getActivity(), billingFlowParams).getResponseCode();
        }

        @Override
        public void showDialogImportDatabase() {
            new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AppThemeDialog))
                    .setMessage(R.string.AppSettingsActivity_import_db_statement)
                    .setPositiveButton(android.R.string.yes, (dialog, which) ->
                            presenter.onImportDb(getContext()))
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        @Override
        public void enableImportDatabaseSelection(boolean isEnabled) {
            importDb.setEnabled(isEnabled);
        }

        @Override
        public void showBillingClientNotReady() {
            Toast.makeText(getContext(), getString(R.string.Error_billing_client_not_ready), Toast.LENGTH_LONG).show();
        }

        @Override
        public void showInfoForPremiumUserOnly() {
            Toast.makeText(getContext(), getString(R.string.Error_for_premium_users_only), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> purchaseList) {
            int responseCode = billingResult.getResponseCode();
            if(responseCode == BillingClient.BillingResponseCode.OK && purchaseList != null){
                for(Purchase purchase : purchaseList){
                    handlePurchase(purchase);
                }
            }
        }

        private void handlePurchase(@NonNull Purchase purchase) {
            if(purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED){
                if (!purchase.isAcknowledged()) {
                    AcknowledgePurchaseParams acknowledgePurchaseParams =
                            AcknowledgePurchaseParams.newBuilder()
                                    .setPurchaseToken(purchase.getPurchaseToken())
                                    .build();
                    enablePremiumFeatures();
                    billingClient.acknowledgePurchase(acknowledgePurchaseParams, acknowledgePurchaseResponseListener);
                }
                Toast.makeText(getContext(), getString(R.string.General_premium_purchase_done), Toast.LENGTH_LONG).show();
            }
        }

        private final AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener = billingResult -> {
        };
    }
}