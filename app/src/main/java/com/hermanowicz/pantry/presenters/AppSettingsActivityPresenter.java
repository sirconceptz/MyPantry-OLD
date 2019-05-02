/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry.presenters;

import com.hermanowicz.pantry.interfaces.AppSettingsActivityView;
import com.hermanowicz.pantry.models.AppSettingsActivityModel;

public class AppSettingsActivityPresenter implements com.hermanowicz.pantry.interfaces.IAppSettingsActivityPresenter {

    private AppSettingsActivityView view;
    private AppSettingsActivityModel model;

    public AppSettingsActivityPresenter(AppSettingsActivityView view, AppSettingsActivityModel model) {
        this.view = view;
        this.model = model;
    }

    public void onDestroy() {
        view = null;
        model = null;
    }

    @Override
    public void setDaysBeforeExpirationDate(int daysBeforeExpirationDate) {
        model.setDaysBeforeExpirationDate(daysBeforeExpirationDate);
    }

    @Override
    public void setIsEmailNotificationsAllowed(boolean isEmailNotificationsAllowed) {
        model.setIsEmailNotificationsAllowed(isEmailNotificationsAllowed);
    }

    @Override
    public void setIsPushNotificationsAllowed(boolean isPushNotificationsAllowed) {
        model.setIsPushNotificationsAllowed(isPushNotificationsAllowed);
    }

    @Override
    public void setHourOfNotifications(int hourOfNotifications) {
        model.setHourOfNotifications(hourOfNotifications);
    }

    @Override
    public void setEmailAddress(String emailAddress) {
        model.setEmailAddress(emailAddress);
    }

    @Override
    public void enableEmailCheckbox(String emailAddress) {
        if (model.isValidEmail(emailAddress) && !emailAddress.isEmpty())
            view.enableEmailCheckbox(true);
        else
            view.enableEmailCheckbox(false);
    }

    @Override
    public void loadSettings() {
        int daysBeforeExpirationDate = model.getDaysBeforeExpirationDate();
        boolean pushNotificationsAllowed = model.isPushNotificationsAllowed();
        int hourOfNotifications = model.getHourOfNotifications();
        String emailAddress = model.getEmailAddress();

        view.setEdittext_daysBeforeExpirationDate(daysBeforeExpirationDate);
        view.setCheckbox_pushNotification(pushNotificationsAllowed);
        view.setNumberpicker_hourOfNotifications(hourOfNotifications);
        view.setEdittext_emailAddress(emailAddress);

        enableEmailCheckbox(emailAddress);
    }

    @Override
    public void saveSettings() {
        model.saveSettings();

        if (model.isNotificationsSettingsChanged())
            view.recreateNotifications();
        view.onSettingsSaved();
    }

    @Override
    public void clearDatabase() {
        view.onDatabaseClear();
    }

    @Override
    public void navigateToMainActivity() {
        view.navigateToMainActivity();
    }
}