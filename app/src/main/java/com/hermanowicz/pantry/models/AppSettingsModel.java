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

package com.hermanowicz.pantry.models;

import android.content.SharedPreferences;
import android.util.Patterns;

import androidx.annotation.NonNull;

import com.hermanowicz.pantry.BuildConfig;
import com.hermanowicz.pantry.utils.Notification;

import java.util.regex.Pattern;

public class AppSettingsModel {

    private static final String PREFERENCES_SELECTED_APP_THEME = "SELECTED_APPLICATION_THEME";
    private static final String PREFERENCES_SELECTED_SCAN_CAMERA = "SCAN_CAMERA";
    private static final String PREFERENCES_EMAIL_ADDRESS = "EMAIL_ADDRESS";
    private static final String PREFERENCES_EMAIL_NOTIFICATIONS = "EMAIL_NOTIFICATIONS?";
    private static final String PREFERENCES_PUSH_NOTIFICATIONS = "PUSH_NOTIFICATIONS?";
    private static final String PREFERENCES_DAYS_TO_NOTIFICATIONS = "HOW_MANY_DAYS_BEFORE_EXPIRATION_DATE_SEND_A_NOTIFICATION?";
    private static final String PREFERENCES_HOUR_OF_NOTIFICATIONS = "HOUR_OF_NOTIFICATIONS?";
    private static final String PREFERENCES_VIBRATION_MODE = "VIBRATION_ON_SCANNER?";
    private static final String PREFERENCES_SOUND_MODE = "SOUND_ON_SCANNER?";

    private final SharedPreferences preferences;
    private int selectedAppTheme, selectedCamera, daysBeforeExpirationDate, hourOfNotifications;
    private boolean emailNotificationsAllowed, pushNotificationsAllowed, scannerVibrationMode, scannerSoundMode;
    private String emailAddress;

    public AppSettingsModel(SharedPreferences preferences) {
        this.preferences = preferences;

        this.selectedAppTheme = preferences.getInt(PREFERENCES_SELECTED_APP_THEME, 0); //0 - Auto, 1 - Day, 2 - Night
        this.selectedCamera = preferences.getInt(PREFERENCES_SELECTED_SCAN_CAMERA, 0); //0 - Rear camera, 1 - Front camera
        this.scannerVibrationMode = preferences.getBoolean(PREFERENCES_VIBRATION_MODE, true);
        this.scannerSoundMode = preferences.getBoolean(PREFERENCES_SOUND_MODE, true);
        this.daysBeforeExpirationDate = preferences.getInt(
                PREFERENCES_DAYS_TO_NOTIFICATIONS, Notification.NOTIFICATION_DEFAULT_DAYS);
        this.hourOfNotifications = preferences.getInt(PREFERENCES_HOUR_OF_NOTIFICATIONS,
                Notification.NOTIFICATION_DEFAULT_HOUR);
        this.emailNotificationsAllowed = preferences.getBoolean(PREFERENCES_EMAIL_NOTIFICATIONS,
                false);
        this.pushNotificationsAllowed = preferences.getBoolean(PREFERENCES_PUSH_NOTIFICATIONS,
                true);
        this.emailAddress = preferences.getString(PREFERENCES_EMAIL_ADDRESS, "");
    }

    public int getSelectedAppTheme(){
        return selectedAppTheme;
    }

    public void setSelectedTheme(int selectedAppTheme){
        this.selectedAppTheme = selectedAppTheme;
    }

    public int getSelectedCamera() {
        return selectedCamera;
    }

    public void setSelectedCamera(int selectedCamera) {
        this.selectedCamera = selectedCamera;
    }

    public boolean getScannerVibrationMode() {
        return scannerVibrationMode;
    }

    public void setScannerVibrationMode(Boolean vibrationMode) {
        this.scannerVibrationMode = vibrationMode;
    }

    public boolean getScannerSoundMode() {
        return scannerSoundMode;
    }

    public void setScannerSoundMode(Boolean soundMode) {
        this.scannerSoundMode = soundMode;
    }

    public int getDaysBeforeExpirationDate() {
        return daysBeforeExpirationDate;
    }

    public void setDaysBeforeExpirationDate(int daysBeforeExpirationDate) {
        this.daysBeforeExpirationDate = daysBeforeExpirationDate;
    }

    public int getHourOfNotifications() {
        return hourOfNotifications;
    }

    public void setHourOfNotifications(int hourOfNotifications) {
        this.hourOfNotifications = hourOfNotifications;
    }

    public boolean isPushNotificationsAllowed() {
        return pushNotificationsAllowed;
    }

    public boolean isEmailNotificationsAllowed() {
        return emailNotificationsAllowed;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setIsEmailNotificationsAllowed(boolean emailNotificationsAllowed) {
        this.emailNotificationsAllowed = emailNotificationsAllowed;
    }

    public void setIsPushNotificationsAllowed(boolean pushNotificationsAllowed) {
        this.pushNotificationsAllowed = pushNotificationsAllowed;
    }

    public boolean isValidEmail(@NonNull String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public boolean isNotificationsSettingsChanged() {
        boolean isNotificationSettingsChanged = false;
        int oldHourOfNotifications = preferences.getInt(PREFERENCES_HOUR_OF_NOTIFICATIONS,
                Notification.NOTIFICATION_DEFAULT_HOUR);
        int newHourOfNotifications = hourOfNotifications;
        int oldDaysBeforeNotifications = preferences.getInt(PREFERENCES_DAYS_TO_NOTIFICATIONS,
                Notification.NOTIFICATION_DEFAULT_HOUR);
        int newDaysBeforeNotifications = daysBeforeExpirationDate;

        if (oldHourOfNotifications != newHourOfNotifications)
            isNotificationSettingsChanged = true;
        if (oldDaysBeforeNotifications != newDaysBeforeNotifications)
            isNotificationSettingsChanged = true;

        return isNotificationSettingsChanged;
    }

    public void saveSettings() {
        SharedPreferences.Editor preferenceEditor = preferences.edit();

        preferenceEditor.putInt(PREFERENCES_SELECTED_APP_THEME, selectedAppTheme);
        preferenceEditor.putInt(PREFERENCES_SELECTED_SCAN_CAMERA, selectedCamera);
        preferenceEditor.putInt(PREFERENCES_DAYS_TO_NOTIFICATIONS, daysBeforeExpirationDate);
        preferenceEditor.putBoolean(PREFERENCES_PUSH_NOTIFICATIONS, pushNotificationsAllowed);
        preferenceEditor.putInt(PREFERENCES_HOUR_OF_NOTIFICATIONS, hourOfNotifications);
        preferenceEditor.putBoolean(PREFERENCES_VIBRATION_MODE, scannerVibrationMode);
        preferenceEditor.putBoolean(PREFERENCES_SOUND_MODE, scannerSoundMode);

        if (isValidEmail(emailAddress)) {
            preferenceEditor.putBoolean(PREFERENCES_EMAIL_NOTIFICATIONS, emailNotificationsAllowed);
            preferenceEditor.putString(PREFERENCES_EMAIL_ADDRESS, emailAddress);
        } else {
            preferenceEditor.putBoolean(PREFERENCES_EMAIL_NOTIFICATIONS, false);
            preferenceEditor.putString(PREFERENCES_EMAIL_ADDRESS, "");
        }

        preferenceEditor.apply();
    }

    public String getAppVersion(){
        return BuildConfig.VERSION_NAME;
    }
}