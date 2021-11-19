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

package com.hermanowicz.pantry.model;

import android.content.SharedPreferences;
import android.util.Patterns;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hermanowicz.pantry.BuildConfig;
import com.hermanowicz.pantry.db.category.Category;
import com.hermanowicz.pantry.db.product.Product;
import com.hermanowicz.pantry.db.storagelocation.StorageLocation;

import java.util.List;
import java.util.regex.Pattern;

public class AppSettingsModel {

    private final SharedPreferences preferences;

    public AppSettingsModel(@NonNull SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public static boolean isSettingsUpdated(@NonNull SharedPreferences preferences) {
        return preferences.getBoolean("SETTINGS_UPDATED", false);
    }

    public static void deleteOldSettings(@NonNull SharedPreferences preferences) {
        preferences.edit().clear().putBoolean("SETTINGS_UPDATED", true).apply();
    }

    public void cleanOnlineDb() {
        FirebaseDatabase.getInstance().getReference()
                .child("products/" + FirebaseAuth.getInstance().getUid()).removeValue();
        FirebaseDatabase.getInstance().getReference()
                .child("categories/" + FirebaseAuth.getInstance().getUid()).removeValue();
        FirebaseDatabase.getInstance().getReference()
                .child("storage_locations/" + FirebaseAuth.getInstance().getUid()).removeValue();
    }

    public void importDbOfflineToOnline(List<Product> productList, List<Category> categoryList,
                                        List<StorageLocation> storageLocationsList){
        importProductDb(productList);
        importCategoryDb(categoryList);
        importStorageLocationDb(storageLocationsList);
    }

    private void importStorageLocationDb(List<StorageLocation> storageLocationsList) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("storage_locations/"+ FirebaseAuth.getInstance().getUid());
        for(StorageLocation storageLocation : storageLocationsList){
            ref.child(String.valueOf(storageLocation.getId())).setValue(storageLocation);
        }
    }

    private void importCategoryDb(List<Category> categoryList) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("categories/"+ FirebaseAuth.getInstance().getUid());
        for(Category category : categoryList){
            ref.child(String.valueOf(category.getId())).setValue(category);
        }
    }

    private void importProductDb(List<Product> productList) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("products/"+ FirebaseAuth.getInstance().getUid());
        for(Product product : productList){
            ref.child(String.valueOf(product.getId())).setValue(product);
        }
    }

    public int getSelectedAppTheme() {
        try {
            return Integer.parseInt(preferences.getString("SELECTED_APPLICATION_THEME", "0"));
        } catch (Exception e) {
            return 0;
        }
    }

    public int getSelectedScanCamera() {
        try {
            return Integer.parseInt(preferences.getString("SCAN_CAMERA", "0"));
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean getScannerVibrationMode() {
        return preferences.getBoolean("VIBRATION_ON_SCANNER?", true);
    }

    public boolean getScannerSoundMode() {
        return preferences.getBoolean("SOUND_ON_SCANNER?", true);
    }

    public boolean isValidEmail() {
        String address = preferences.getString("EMAIL_ADDRESS", "");
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(address).matches();
    }

    public String getAppVersion(){
        return BuildConfig.VERSION_NAME;
    }

    public String getEmailAddress() {
        return preferences.getString("EMAIL_ADDRESS", "");
    }

    public int getDaysToNotification() {
        try {
            return Integer.parseInt(preferences.getString("HOW_MANY_DAYS_BEFORE_EXPIRATION_DATE_SEND_A_NOTIFICATION?", "3"));
        } catch (NumberFormatException e) {
            return 3;
        }
    }

    public void clearEmailAddress(){
        preferences.edit().putString("EMAIL_ADDRESS", "").apply();
    }

    public String getDatabaseMode() {
        String databaseMode = preferences.getString("DATABASE_MODE", "local");
        if (databaseMode.equals("local") || databaseMode.equals("online"))
            return databaseMode;
        else
            return "local";
    }

    public void setDatabaseMode(String databaseMode) {
        preferences.edit().putString("DATABASE_MODE", databaseMode).apply();
    }

    public void setPremiumIsRestored() {
        preferences.edit().putBoolean("PREMIUM_IS_RESTORED", true).apply();
    }

    public boolean isPremiumRestored() {
        return preferences.getBoolean("PREMIUM_IS_RESTORED", false);
    }

    public void setErrorIsShowed(boolean state) {
        preferences.edit().putBoolean("ERRORS_SHOWED", state).apply();
    }

    public boolean getIsErrorShowed() {
        return preferences.getBoolean("ERRORS_SHOWED", false);
    }
}