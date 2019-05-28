/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry.models;

import android.content.SharedPreferences;
import android.util.Patterns;

import androidx.annotation.NonNull;

import com.hermanowicz.pantry.Notification;

import java.util.regex.Pattern;

public class AppSettingsActivityModel {

    private static final String PREFERENCES_EMAIL_ADDRESS = "EMAIL_ADDRESS";
    private static final String PREFERENCES_EMAIL_NOTIFICATIONS = "EMAIL_NOTIFICATIONS?";
    private static final String PREFERENCES_PUSH_NOTIFICATIONS = "PUSH_NOTIFICATIONS?";
    private static final String PREFERENCES_DAYS_TO_NOTIFICATIONS = "HOW_MANY_DAYS_BEFORE_EXPIRATION_DATE_SEND_A_NOTIFICATION?";
    private static final String PREFERENCES_HOUR_OF_NOTIFICATIONS = "HOUR_OF_NOTIFICATIONS?";

    private SharedPreferences myPreferences;
    private int daysBeforeExpirationDate, hourOfNotifications;
    private boolean emailNotificationsAllowed, pushNotificationsAllowed;
    private String emailAddress;

    public AppSettingsActivityModel(SharedPreferences myPreferences) {
        this.myPreferences = myPreferences;

        this.daysBeforeExpirationDate = myPreferences.getInt(
                PREFERENCES_DAYS_TO_NOTIFICATIONS, Notification.NOTIFICATION_DEFAULT_DAYS);
        this.hourOfNotifications = myPreferences.getInt(PREFERENCES_HOUR_OF_NOTIFICATIONS,
                Notification.NOTIFICATION_DEFAULT_HOUR);
        this.emailNotificationsAllowed = myPreferences.getBoolean(PREFERENCES_EMAIL_NOTIFICATIONS,
                false);
        this.pushNotificationsAllowed = myPreferences.getBoolean(PREFERENCES_PUSH_NOTIFICATIONS,
                true);
        this.emailAddress = myPreferences.getString(PREFERENCES_EMAIL_ADDRESS, "");
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
        int oldHourOfNotifications = myPreferences.getInt(PREFERENCES_HOUR_OF_NOTIFICATIONS,
                Notification.NOTIFICATION_DEFAULT_HOUR);
        int newHourOfNotifications = hourOfNotifications;
        int oldDaysBeforeNotifications = myPreferences.getInt(PREFERENCES_DAYS_TO_NOTIFICATIONS,
                Notification.NOTIFICATION_DEFAULT_HOUR);
        int newDaysBeforeNotifications = daysBeforeExpirationDate;

        if (oldHourOfNotifications != newHourOfNotifications)
            isNotificationSettingsChanged = true;
        if (oldDaysBeforeNotifications != newDaysBeforeNotifications)
            isNotificationSettingsChanged = true;

        return isNotificationSettingsChanged;
    }

    public void saveSettings() {
        SharedPreferences.Editor preferenceEditor = myPreferences.edit();

        preferenceEditor.putInt(PREFERENCES_DAYS_TO_NOTIFICATIONS, daysBeforeExpirationDate);
        preferenceEditor.putBoolean(PREFERENCES_PUSH_NOTIFICATIONS, pushNotificationsAllowed);
        preferenceEditor.putInt(PREFERENCES_HOUR_OF_NOTIFICATIONS, hourOfNotifications);

        if (isValidEmail(emailAddress)) {
            preferenceEditor.putBoolean(PREFERENCES_EMAIL_NOTIFICATIONS, emailNotificationsAllowed);
            preferenceEditor.putString(PREFERENCES_EMAIL_ADDRESS, emailAddress);
        } else {
            preferenceEditor.putBoolean(PREFERENCES_EMAIL_NOTIFICATIONS, false);
            preferenceEditor.putString(PREFERENCES_EMAIL_ADDRESS, "");
        }

        preferenceEditor.apply();
    }
}