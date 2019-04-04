/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry.presenters;

import com.hermanowicz.pantry.repositories.AppSettingsRepository;
import com.hermanowicz.pantry.views.AppSettingsActivityView;

public class AppSettingsActivityPresenter {

    private AppSettingsActivityView view;
    private AppSettingsRepository repository;

    public AppSettingsActivityPresenter(AppSettingsActivityView view, AppSettingsRepository repository) {
        this.view = view;
        this.repository = repository;

    }

    public void setDaysBeforeExpirationDate(String daysBeforeExpirationDate){

    }

    public void setEmailNotificationCheckbox(boolean emailNotificationCheckbox){

    }

    public void setPushNotificationCheckbox(boolean pushNotificationCheckbox){

    }

    public void setEmailAddress(String emailAddress){

    }

    public void setHourOfNotifications(int hourOfNotifications){

    }

}