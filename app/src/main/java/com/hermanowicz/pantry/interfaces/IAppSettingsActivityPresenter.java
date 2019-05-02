/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry.interfaces;

public interface IAppSettingsActivityPresenter {
    void setDaysBeforeExpirationDate(int daysBeforeExpirationDate);

    void setIsEmailNotificationsAllowed(boolean isEmailNotificationsAllowed);

    void setIsPushNotificationsAllowed(boolean isPushNotificationsAllowed);

    void setHourOfNotifications(int hourOfNotifications);

    void setEmailAddress(String emailAddress);

    void enableEmailCheckbox(String emailAddress);

    void loadSettings();

    void saveSettings();

    void clearDatabase();

    void navigateToMainActivity();
}