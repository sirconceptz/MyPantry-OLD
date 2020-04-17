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

package com.hermanowicz.pantry.presenters;

import android.content.SharedPreferences;

import com.hermanowicz.pantry.interfaces.AppSettingsView;
import com.hermanowicz.pantry.models.AppSettingsModel;

public class AppSettingsPresenter {

    private AppSettingsView view;
    private AppSettingsModel model;

    public AppSettingsPresenter(AppSettingsView view, SharedPreferences preferences) {
        this.view = view;
        this.model = new AppSettingsModel(preferences);
    }

    public void setSelectedScanCamera(int selectedScanCamera) {
        model.setSelectedCamera(selectedScanCamera);
    }

    public void setDaysBeforeExpirationDate(int daysBeforeExpirationDate) {
        model.setDaysBeforeExpirationDate(daysBeforeExpirationDate);
    }

    public void setIsEmailNotificationsAllowed(boolean isEmailNotificationsAllowed) {
        model.setIsEmailNotificationsAllowed(isEmailNotificationsAllowed);
    }

    public void setIsPushNotificationsAllowed(boolean isPushNotificationsAllowed) {
        model.setIsPushNotificationsAllowed(isPushNotificationsAllowed);
    }

    public void setHourOfNotifications(int hourOfNotifications) {
        model.setHourOfNotifications(hourOfNotifications);
    }

    public void setEmailAddress(String emailAddress) {
        model.setEmailAddress(emailAddress);
    }

    public void enableEmailCheckbox(String emailAddress) {
        if (model.isValidEmail(emailAddress))
            view.enableEmailCheckbox(true);
        else
            view.enableEmailCheckbox(false);
    }

    public void loadSettings() {
        int selectedCamera = model.getSelectedCamera();
        int daysBeforeExpirationDate = model.getDaysBeforeExpirationDate();
        boolean pushNotificationsAllowed = model.isPushNotificationsAllowed();
        boolean emailNotificationsAllowed = model.isEmailNotificationsAllowed();
        int hourOfNotifications = model.getHourOfNotifications();
        String emailAddress = model.getEmailAddress();

        view.setScanCamera(selectedCamera);
        view.setDaysBeforeExpirationDate(daysBeforeExpirationDate);
        view.setCheckbox_pushNotification(pushNotificationsAllowed);
        view.setCheckbox_emailNotification(emailNotificationsAllowed);
        view.setHourOfNotifications(hourOfNotifications);
        view.setEmailAddress(emailAddress);

        enableEmailCheckbox(emailAddress);
        view.showCodeVersion(model.getAppVersion());
    }

    public void saveSettings() {
        model.saveSettings();

        if (model.isNotificationsSettingsChanged())
            view.recreateNotifications();
        view.onSettingsSaved();
    }

    public void clearDatabase() {
        view.onDatabaseClear();
    }

    public void navigateToMainActivity() {
        view.navigateToMainActivity();
    }
}