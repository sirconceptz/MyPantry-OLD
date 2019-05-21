/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.hermanowicz.pantry.interfaces.AppSettingsActivityView;
import com.hermanowicz.pantry.models.AppSettingsActivityModel;
import com.hermanowicz.pantry.presenters.AppSettingsActivityPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <h1>AppSettingsActivity</h1>
 * Activity for application settings.
 *
 * @author  Mateusz Hermanowicz
 * @version 1.0
 * @since   1.0
 */

public class AppSettingsActivity extends AppCompatActivity implements AppSettingsActivityView {

    private Context context;

    private AppSettingsActivityModel model;
    private AppSettingsActivityPresenter presenter;

    @BindView(R.id.edittext_daysToNotification)
    EditText edittext_daysBeforeExpirationDate;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.checkbox_emailNotifications)
    CheckBox checkbox_notificationsByEmail;
    @BindView(R.id.checkbox_pushNotifications)
    CheckBox checkbox_notificationsByPush;
    @BindView(R.id.numberpicker_hourOfNotification)
    NumberPicker numberpicker_hourOfNotifications;
    @BindView(R.id.edittext_emailAddress)
    EditText edittext_emailAddress;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);

        ButterKnife.bind(this);

        context = getApplicationContext();
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        setSupportActionBar(toolbar);

        model = new AppSettingsActivityModel(myPreferences);
        presenter = new AppSettingsActivityPresenter(this, model);

        presenter.loadSettings();

        setNumberpickerSettings();

        edittext_emailAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String emailAddress = edittext_emailAddress.getText().toString();
                presenter.enableEmailCheckbox(emailAddress);
            }
        });
    }

    void setNumberpickerSettings() {
        numberpicker_hourOfNotifications.setMinValue(1);
        numberpicker_hourOfNotifications.setMaxValue(24);
        numberpicker_hourOfNotifications.setWrapSelectorWheel(false);
    }

    @OnClick(R.id.button_clearDatabase)
    void onClickClearDatabaseButton() {
        presenter.clearDatabase();
    }

    @OnClick(R.id.button_saveSettings)
    void onClickSaveSettingsButton() {
        int daysToNotification = Integer.parseInt(edittext_daysBeforeExpirationDate.getText().toString());
        boolean isEmailNotificationsAllowed = checkbox_notificationsByEmail.isChecked();
        boolean isPushNotificationsAllowed = checkbox_notificationsByPush.isChecked();
        int hourOfNotifications = numberpicker_hourOfNotifications.getValue();
        String emailAddress = edittext_emailAddress.getText().toString();

        presenter.setDaysBeforeExpirationDate(daysToNotification);
        presenter.setIsEmailNotificationsAllowed(isEmailNotificationsAllowed);
        presenter.setIsPushNotificationsAllowed(isPushNotificationsAllowed);
        presenter.setHourOfNotifications(hourOfNotifications);
        presenter.setEmailAddress(emailAddress);

        presenter.saveSettings();
    }

    @Override
    public void setEdittext_daysBeforeExpirationDate(int daysBeforeExpirationDate) {
        edittext_daysBeforeExpirationDate.setText(String.valueOf(daysBeforeExpirationDate));
    }

    @Override
    public void setCheckbox_pushNotification(boolean isPushNotificationsAllowed) {
        checkbox_notificationsByPush.setChecked(isPushNotificationsAllowed);
    }

    @Override
    public void setEdittext_emailAddress(String emailAddress) {
        edittext_emailAddress.setText(emailAddress);
    }

    @Override
    public void setNumberpicker_hourOfNotifications(int hourOfNotifications) {
        numberpicker_hourOfNotifications.setValue(hourOfNotifications);
    }

    @Override
    public void recreateNotifications() {
        Notification.cancelAllNotifications(context);
        Notification.createNotificationsForAllProducts(context);
    }

    @Override
    public void enableEmailCheckbox(boolean isValidEmail) {
        checkbox_notificationsByEmail.setEnabled(isValidEmail);
    }

    @Override
    public void onSettingsSaved() {
        Toast.makeText(context, getResources().getString(R.string.AppSettingsActivity_settings_saved_successful), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDatabaseClear() {
        DatabaseManager db = new DatabaseManager(context);
        db.recreateDB();
        Notification.cancelAllNotifications(context);
        Toast.makeText(context, getResources().getString(R.string.AppSettingsActivity_database_is_clear), Toast.LENGTH_LONG).show();
    }

    @Override
    public void navigateToMainActivity() {
        Intent mainActivityIntent = new Intent(context, MainActivity.class);
        startActivity(mainActivityIntent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            presenter.navigateToMainActivity();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter = new AppSettingsActivityPresenter(this, model);

    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
}