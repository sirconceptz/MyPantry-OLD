/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry.interfaces;

public interface AppSettingsActivityView {
    void setEdittext_daysBeforeExpirationDate(int daysBeforeExpirationDate);

    void setCheckbox_pushNotification(boolean isPushNotificationsAllowed);

    void setEdittext_emailAddress(String emailAddress);

    void setNumberpicker_hourOfNotifications(int hourOfNotifications);

    void recreateNotifications();

    void enableEmailCheckbox(boolean isValidEmail);

    void onSettingsSaved();

    void onDatabaseClear();

    void navigateToMainActivity();
}