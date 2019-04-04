/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry.views;

public interface AppSettingsActivityView {
    void setDaysBeforeExpirationDate(String daysBeforeExpirationDate);
    void setEmailNotificationCheckbox(boolean emailNotificationCheckbox);
    void setPushNotificationCheckbox(boolean pushNotificationCheckbox);
    void setEmailAddress(String emailAddress);
    void setHourOfNotifications(int hourOfNotifications);
}