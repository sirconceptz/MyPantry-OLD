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

package com.hermanowicz.pantry.presenter;

import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.activity.AppSettingsActivity;
import com.hermanowicz.pantry.db.category.Category;
import com.hermanowicz.pantry.db.category.CategoryDb;
import com.hermanowicz.pantry.db.product.Product;
import com.hermanowicz.pantry.db.product.ProductDb;
import com.hermanowicz.pantry.db.storagelocation.StorageLocation;
import com.hermanowicz.pantry.db.storagelocation.StorageLocationDb;
import com.hermanowicz.pantry.interfaces.AccountView;
import com.hermanowicz.pantry.interfaces.PremiumUserView;
import com.hermanowicz.pantry.model.AppSettingsModel;
import com.hermanowicz.pantry.model.DatabaseBackup;
import com.hermanowicz.pantry.model.DatabaseMode;
import com.hermanowicz.pantry.util.PremiumAccess;

import java.util.List;

/**
 * <h1>AppSettingsPresenter</h1>
 * Presenter for AppSettingsActivity
 *
 * @author  Mateusz Hermanowicz
 */

public class AppSettingsPresenter {

    private final AppSettingsActivity.MyPreferenceFragment view;
    private final AccountView accountView;
    private final PremiumUserView premiumUserView;
    private final AppSettingsModel model;
    private final PremiumAccess premiumAccess;

    public AppSettingsPresenter(@NonNull AppSettingsActivity.MyPreferenceFragment view,
                                @NonNull AccountView accountView,
                                @NonNull PremiumUserView premiumUserView,
                                @NonNull Context context) {
        this.view = view;
        this.accountView = accountView;
        this.premiumUserView = premiumUserView;
        this.premiumAccess = new PremiumAccess(context);
        this.model = new AppSettingsModel(PreferenceManager.getDefaultSharedPreferences(context));
    }

    public void reCreateNotifications(){
        view.recreateNotifications();
    }

    public void showStoredPreferences(@NonNull Resources resources) {
        showSelectedTheme();
        showSelectedScanCamera();
        showDatabaseMode(resources);
        showEmailAddress();
        showDaysToNotification();
        showVersionCode();
        showActiveUser();
    }

    public void showActiveUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            view.showActiveUser(user.getEmail());
            view.enableDatabaseModeSelection(true);
        }
        else {
            view.showActiveUser(null);
            view.enableDatabaseModeSelection(false);
            model.setDatabaseMode(DatabaseMode.Mode.LOCAL);
        }
    }

    public void showDatabaseMode(@NonNull Resources resources) {
        String[] databaseModeList = resources.getStringArray(R.array.AppSettingsActivity_database_mode);
        if(model.getDatabaseMode() == DatabaseMode.Mode.ONLINE)
            view.showDatatabaseMode(databaseModeList[1]);
        else
            view.showDatatabaseMode(databaseModeList[0]);
    }

    public void showSelectedTheme() {
        view.showSelectedTheme(model.getSelectedAppTheme());
    }

    public void showSelectedScanCamera() {
        view.showSelectedScanCamera(model.getSelectedScanCamera());
    }

    public void showEmailAddress() {
        if(model.isValidEmail())
            view.showEmailAddress(model.getEmailAddress());
        else{
            model.clearEmailAddress();
            view.setEmailPreferences();
        }
    }

    public void showDaysToNotification() {
        view.showDaysToNotification(model.getDaysToNotification());
    }

    private void showVersionCode() {
        view.showVersionCode(model.getAppVersion());
    }

    public void backupProductDatabase(@NonNull Context context) {
        DatabaseBackup.backupProductDb(context);
    }

    public void restoreProductDatabase(@NonNull Context context) {
        DatabaseBackup.restoreProductDb(context);
        view.showDbBackupHasBeenMade();
    }

    public void clearProductDatabase(@NonNull Context context) {
        DatabaseBackup.clearProductDb(context);
        view.onProductDatabaseClear();
    }

    public void backupCategoryDatabase(@NonNull Context context) {
        DatabaseBackup.backupCategoryDb(context);
        view.showDbBackupHasBeenMade();
    }

    public void restoreCategoryDatabase(@NonNull Context context) {
        DatabaseBackup.restoreCategoryDb(context);
        view.showDbHasBeenRestored();
    }

    public void clearCategoryDatabase(@NonNull Context context) {
        DatabaseBackup.clearCategoryDb(context);
        view.showDbHasBeenClear();
    }

    public void backupStorageLocationDatabase(@NonNull Context context) {
        DatabaseBackup.backupStorageLocationDb(context);
        view.showDbBackupHasBeenMade();
    }

    public void restoreStorageLocationDatabase(@NonNull Context context) {
        DatabaseBackup.restoreStorageLocationDb(context);
        view.showDbHasBeenRestored();
    }

    public void clearStorageLocationDatabase(@NonNull Context context) {
        DatabaseBackup.clearStorageLocationDb(context);
        view.showDbHasBeenClear();
    }

    public void onClickBackupProductDatabase() {
        if(premiumAccess.isPremium())
            view.showDialogBackupProductDb();
        else
            premiumUserView.showInfoForPremiumUserOnly();
    }

    public void onClickRestoreProductDatabase() {
        if(premiumAccess.isPremium())
            view.showDialogRestoreProductDb();
        else
            premiumUserView.showInfoForPremiumUserOnly();
    }

    public void onClickClearProductDatabase() {
        view.showDialogClearProductDb();
    }

    public void onClickBackupCategoryDatabase() {
        if(premiumAccess.isPremium())
            view.showDialogBackupCategoryDb();
        else
            premiumUserView.showInfoForPremiumUserOnly();
    }

    public void onClickRestoreCategoryDatabase() {
        if(premiumAccess.isPremium())
            view.showDialogRestoreCategoryDb();
        else
            premiumUserView.showInfoForPremiumUserOnly();
    }

    public void onClickClearCategoryDatabase() {
        view.showDialogClearCategoryDb();
    }

    public void onClickBackupStorageLocationDatabase() {
        if(premiumAccess.isPremium())
            view.showDialogBackupStorageLocationDb();
        else
            premiumUserView.showInfoForPremiumUserOnly();
    }

    public void onClickRestoreStorageLocationDatabase() {
        if(premiumAccess.isPremium())
            view.showDialogRestoreStorageLocationDb();
        else
            premiumUserView.showInfoForPremiumUserOnly();
    }

    public void onClickClearStorageLocationDatabase() {
        view.showDialogClearStorageLocationDb();
    }

    public void signInOrSignOut() {
        if(premiumAccess.isPremium()) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null)
                accountView.signIn();
            else {
                if(model.getDatabaseMode() == DatabaseMode.Mode.ONLINE)
                    model.setDatabaseMode(DatabaseMode.Mode.LOCAL);
                accountView.signOut();
                updateUserData();
                view.refreshActivity();
            }
        }
        else
            premiumUserView.showInfoForPremiumUserOnly();
    }

    public void updateUserData(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null)
            accountView.updateUserData("");
        else
            accountView.updateUserData(user.getEmail());
    }

    public void goPremium() {
        view.buyPremiumFeatures();
    }

    public void onClickImportDb() {
        if(premiumAccess.isPremium())
            view.showDialogImportDatabase();
        else
            view.showInfoForPremiumUserOnly();
    }

    public void onImportDb(@NonNull Context context) {
        if(premiumAccess.isPremium()){
            List<Product> productList = ProductDb.getInstance(context).productsDao().
                    getAllProductsList();
            List<Category> categoryList = CategoryDb.getInstance(context).categoryDao().
                    getAllOwnCategories();
            List<StorageLocation> storageLocations = StorageLocationDb.getInstance(context).
                    storageLocationDao().getAllStorageLocations();
            model.cleanOnlineDb();
            model.importDbOfflineToOnline(productList, categoryList, storageLocations);
        }
        else
            view.showInfoForPremiumUserOnly();
    }

    public void billingClientNotReady() {
        view.showBillingClientNotReady();
    }

    public void setPremiumIsRestored() {
        model.setPremiumIsRestored();
    }

    public boolean isPremiumRestored() {
        return model.isPremiumRestored();
    }

    public void setDatabaseMode(String mode) {
        model.setDatabaseMode(mode);
    }
}