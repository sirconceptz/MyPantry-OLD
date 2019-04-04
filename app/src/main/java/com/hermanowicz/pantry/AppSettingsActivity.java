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
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.hermanowicz.pantry.presenters.AppSettingsActivityPresenter;
import com.hermanowicz.pantry.views.AppSettingsActivityView;

import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/**
 * <h1>AppSettingsActivity</h1>
 * Activity for application settings.
 *
 * @author  Mateusz Hermanowicz
 * @version 1.0
 * @since   1.0
 */

public class AppSettingsActivity extends AppCompatActivity implements AppSettingsActivityView {

    private Context           context;
    private DatabaseManager   db;
    private SharedPreferences myPreferences;
    private EditText          daysBeforeExpirationDate, emailAddress;
    private NumberPicker      hourOfNotification;
    private CheckBox          notificationsByEmail, notificationsByPush;
    private Button            saveSettings, clearDatabase;
    private AppSettingsActivityPresenter presenter;

    public static final String PREFERENCES_EMAIL_ADDRESS         = "EMAIL_ADDRESS";
    public static final String PREFERENCES_EMAIL_NOTIFICATIONS   = "EMAIL_NOTIFICATIONS?";
    public static final String PREFERENCES_PUSH_NOTIFICATIONS    = "PUSH_NOTIFICATIONS?";
    public static final String PREFERENCES_DAYS_TO_NOTIFICATIONS = "HOW_MANY_DAYS_BEFORE_EXPIRATION_DATE_SEND_A_NOTIFICATION?";
    public static final String PREFERENCES_HOUR_OF_NOTIFICATIONS = "HOUR_OF_NOTIFICATIONS?";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);

        init();
        updateSettings();

        presenter = new AppSettingsActivityPresenter(this, null);

        emailAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        saveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor preferenceEditor = myPreferences.edit();

                preferenceEditor.putInt(PREFERENCES_DAYS_TO_NOTIFICATIONS, Integer.parseInt(daysBeforeExpirationDate.getText().toString()));
                preferenceEditor.putBoolean(PREFERENCES_PUSH_NOTIFICATIONS, notificationsByPush.isChecked());
                preferenceEditor.putInt(PREFERENCES_HOUR_OF_NOTIFICATIONS, hourOfNotification.getValue());

                setEmailSettings();

                if(isNotificationSettingsChanged()){
                    Notification.cancelAllNotifications(context);
                    Notification.createNotificationsForAllProducts(context);
                }

                if(preferenceEditor.commit()) {
                    Toast.makeText(context, getResources().getString(R.string.AppSettingsActivity_settings_saved_successful), Toast.LENGTH_LONG).show();
                }
            }
        });

        clearDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Notification.cancelAllNotifications(context);
                db.reCreateDB();
                Toast.makeText(context, getResources().getString(R.string.AppSettingsActivity_database_is_clear), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setEmailSettings(){
        SharedPreferences.Editor preferenceEditor = myPreferences.edit();
        if(isValidEmail(emailAddress.getText().toString())) {
            preferenceEditor.putBoolean(PREFERENCES_EMAIL_NOTIFICATIONS, notificationsByEmail.isChecked());
            preferenceEditor.putString(PREFERENCES_EMAIL_ADDRESS, emailAddress.getText().toString());
        }
        else
        {
            preferenceEditor.putBoolean(PREFERENCES_EMAIL_NOTIFICATIONS, false);
            preferenceEditor.putString(PREFERENCES_EMAIL_ADDRESS, "");
        }
    }

    private boolean isNotificationSettingsChanged(){
        boolean isNotificationSettingsChanged = false;
        int actualHourOfNotifications     = getHourOfNotifications(context);
        int newHourOfNotifications        = hourOfNotification.getValue();
        int actualDaysBeforeNotifications = getDaysBeforeNotificationFromSettings(context);
        int newDaysBeforeNotifications    = Integer.parseInt(daysBeforeExpirationDate.getText().toString());

        if(actualHourOfNotifications != newHourOfNotifications)
            isNotificationSettingsChanged = true;
        if(actualDaysBeforeNotifications != newDaysBeforeNotifications)
            isNotificationSettingsChanged = true;

        return isNotificationSettingsChanged;
    }

    private boolean isValidEmail(@NonNull String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public static int getDaysBeforeNotificationFromSettings(@NonNull Context context) {
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return myPreferences.getInt(
                PREFERENCES_DAYS_TO_NOTIFICATIONS, Notification.NOTIFICATION_DEFAULT_DAYS);
    }

    public static int getHourOfNotifications(@NonNull Context context){
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return myPreferences.getInt(PREFERENCES_HOUR_OF_NOTIFICATIONS, Notification.NOTIFICATION_DEFAULT_HOUR);
    }

    public static String getEmailForNotifications(@NonNull Context context){
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return myPreferences.getString(PREFERENCES_EMAIL_ADDRESS, "");
    }

    public static boolean isPushNotificationAllowed(Context context){
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return myPreferences.getBoolean(PREFERENCES_PUSH_NOTIFICATIONS, true);
    }

    public static boolean isEmailNotificationAllowed(Context context){
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return myPreferences.getBoolean(PREFERENCES_EMAIL_NOTIFICATIONS, false);
    }

    private void updateSettings(){
        emailAddress.setText(myPreferences.getString(PREFERENCES_EMAIL_ADDRESS, ""));
        notificationsByEmail.setChecked(myPreferences.getBoolean(PREFERENCES_EMAIL_NOTIFICATIONS, false));
        notificationsByPush.setChecked(myPreferences.getBoolean(PREFERENCES_PUSH_NOTIFICATIONS, true));
        hourOfNotification.setValue(myPreferences.getInt(PREFERENCES_HOUR_OF_NOTIFICATIONS, Notification.NOTIFICATION_DEFAULT_HOUR));
        daysBeforeExpirationDate.setText(String.valueOf(getDaysBeforeNotificationFromSettings(context)));

        if(isValidEmail(emailAddress.getText().toString())){
            notificationsByEmail.setEnabled(true);
        }
        else{
            notificationsByEmail.setEnabled(false);
        }
    }

    private void init(){
        context                  = getApplicationContext();
        db                       = new DatabaseManager(context);
        myPreferences            = PreferenceManager.getDefaultSharedPreferences(context);

        Toolbar toolbar          = findViewById(R.id.toolbar);
        daysBeforeExpirationDate = findViewById(R.id.edittext_daysToNotification);
        notificationsByEmail     = findViewById(R.id.checkbox_emailNotifications);
        notificationsByPush      = findViewById(R.id.checkbox_pushNotifications);
        hourOfNotification       = findViewById(R.id.numberpicker_hourOfNotification);
        emailAddress             = findViewById(R.id.edittext_emailAddress);
        saveSettings             = findViewById(R.id.button_saveSettings);
        clearDatabase            = findViewById(R.id.button_clearDatabase);


        setSupportActionBar(toolbar);

        hourOfNotification.setMinValue(1);
        hourOfNotification.setMaxValue(24);
        hourOfNotification.setWrapSelectorWheel(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent mainActivityIntent = new Intent(context, MainActivity.class);
            startActivity(mainActivityIntent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void setDaysBeforeExpirationDate(String daysBeforeExpirationDate) {

    }

    @Override
    public void setEmailNotificationCheckbox(boolean emailNotificationCheckbox) {

    }

    @Override
    public void setPushNotificationCheckbox(boolean pushNotificationCheckbox) {

    }

    @Override
    public void setEmailAddress(String emailAddress) {

    }

    @Override
    public void setHourOfNotifications(int hourOfNotifications) {

    }
}