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
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hermanowicz.pantry.db.product.Product;
import com.hermanowicz.pantry.interfaces.AccountView;
import com.hermanowicz.pantry.interfaces.MainView;
import com.hermanowicz.pantry.model.AppSettingsModel;
import com.hermanowicz.pantry.model.DatabaseMode;
import com.hermanowicz.pantry.util.Notification;
import com.hermanowicz.pantry.util.PremiumAccess;

import java.util.List;

/**
 * <h1>MainPresenter</h1>
 * Presenter for MainActivity
 *
 * @author  Mateusz Hermanowicz
 */

public class MainPresenter {

    private final MainView view;
    private final AccountView accountView;
    private final DatabaseMode dbMode = new DatabaseMode();
    private final SharedPreferences sharedPreferences;
    private PremiumAccess premiumAccess;
    private long pressedBackTime;
    private final AppSettingsModel appSettingsModel;

    public MainPresenter(@NonNull MainView view,
                         @NonNull AccountView accountView, @NonNull SharedPreferences sharedPreferences) {
        this.view = view;
        this.accountView = accountView;
        this.sharedPreferences = sharedPreferences;
        this.appSettingsModel = new AppSettingsModel(sharedPreferences);
        dbMode.setDatabaseMode(appSettingsModel.getDatabaseMode());
    }

    public void setPremiumAccess(@NonNull PremiumAccess premiumAccess){
        this.premiumAccess = premiumAccess;
    }

    public void navigateToMyPantryActivity() {
        view.onNavigationToMyPantryActivity();
    }

    public void navigateToScanProductActivity() {
        view.onNavigationToScanProductActivity();
    }

    public void navigateToNewProductActivity() {
        view.onNavigationToNewProductActivity();
    }

    public void navigateToCategoriesActivity() { view.onNavigationToCategoriesActivity(); }

    public void navigateToStorageLocationsActivity() { view.onNavigationToStorageLocationsActivity(); }

    public void navigateToAppSettingsActivity() {
        view.onNavigationToAppSettingsActivity();
    }

    public void showAuthorInfoDialog() {
        view.showAuthorInfoDialog();
    }

    public void updateUserData(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null)
            accountView.updateUserData(null);
        else
            accountView.updateUserData(user.getEmail());
    }

    public boolean isPremium(){
        return premiumAccess.isPremium();
    }

    public void restoreNotifications(@NonNull Context context, @NonNull List<Product> productList) {
        boolean isNotificationsToRestore = sharedPreferences.getBoolean("IS_NOTIFICATIONS_TO_RESTORE", false);
        if (isNotificationsToRestore) {
            Notification.createNotificationsForAllProducts(context, productList);
            sharedPreferences.edit().putBoolean("IS_NOTIFICATIONS_TO_RESTORE", false).apply();
        }
    }

    public boolean isOfflineDb() {
        return dbMode.getDatabaseMode() == DatabaseMode.Mode.LOCAL;
    }

    public long getPressedBackTime() {
        return pressedBackTime;
    }

    public void setPressedBackTime(long pressedTime) {
        this.pressedBackTime = pressedTime;
    }

    public void setFoundError(@NonNull String responseString) {
        if (!appSettingsModel.getIsErrorShowed()) {
            appSettingsModel.setErrorIsShowed(true);
            view.onNavigationToErrorActivity(responseString);
        }
    }
}