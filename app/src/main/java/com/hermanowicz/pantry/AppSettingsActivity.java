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
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import java.util.List;
import java.util.regex.Pattern;

/**
 * <h1>AppSettingsActivity</h1>
 * Activity for application settings.
 *
 * @author  Mateusz Hermanowicz
 * @version 1.0
 * @since   1.0
 */

public class AppSettingsActivity extends AppCompatActivity {

    private Context           context;
    private DatabaseManager   db;
    private SharedPreferences myPreferences;
    private EditText          daysBeforeExpirationDate, emailAddress;
    private NumberPicker      hourOfNotification;
    private CheckBox          notificationsByEmail, notificationsByPush;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);

        context                  = getApplicationContext();
        daysBeforeExpirationDate = findViewById(R.id.DaysToNotification);
        notificationsByEmail     = findViewById(R.id.EmailNotificationsCheckbox);
        notificationsByPush      = findViewById(R.id.PushNotificationsCheckbox);
        hourOfNotification       = findViewById(R.id.HourOfNotification);
        emailAddress             = findViewById(R.id.EmailAddress);
        Toolbar toolbar          = findViewById(R.id.Toolbar);
        Button saveSettings      = findViewById(R.id.SaveSettingsButton);
        Button clearDatabase     = findViewById(R.id.ClearDatabaseButton);
        db                       = new DatabaseManager(context);
        myPreferences            = PreferenceManager.getDefaultSharedPreferences(context);

        setSupportActionBar(toolbar);

        hourOfNotification.setMinValue(1);
        hourOfNotification.setMaxValue(24);
        hourOfNotification.setWrapSelectorWheel(false);

        try {
            emailAddress.setText(myPreferences.getString(Const.PREFERENCES_EMAIL_ADDRESS, ""));
        } catch (NullPointerException e) {
            emailAddress.setText("");
        }

        try {
            daysBeforeExpirationDate.setText(Integer.toString(getDaysBeforeNotificationFromSettings(context)));
        } catch (NullPointerException e) {
            daysBeforeExpirationDate.setText(Const.NOTIFICATION_DEFAULT_DAYS);
        }
        try {
            notificationsByEmail.setChecked(myPreferences.getBoolean(Const.PREFERENCES_EMAIL_NOTIFICATIONS, false));
        } catch (NullPointerException e) {
            notificationsByEmail.setChecked(false);
        }
        try {
            notificationsByPush.setChecked(myPreferences.getBoolean(Const.PREFERENCES_PUSH_NOTIFICATIONS, true));

        } catch (NullPointerException e) {
            notificationsByPush.setChecked(true);
        }
        try {
            hourOfNotification.setValue(myPreferences.getInt(Const.PREFERENCES_HOUR_OF_NOTIFICATIONS, Const.NOTIFICATION_DEFAULT_HOUR));
        } catch (NullPointerException e) {
            hourOfNotification.setValue(Const.NOTIFICATION_DEFAULT_HOUR);
        }

        if(isValidEmail(emailAddress.getText().toString())){
            notificationsByEmail.setEnabled(true);
        }
        else{
            notificationsByEmail.setEnabled(false);
        }

        emailAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            if(isValidEmail(emailAddress.getText().toString())){
                notificationsByEmail.setEnabled(true);
            }
            else{
                notificationsByEmail.setEnabled(false);
            }
            }
        });

        saveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor preferenceEditor = myPreferences.edit();

                preferenceEditor.putInt(Const.PREFERENCES_DAYS_TO_NOTIFICATIONS, Integer.parseInt(daysBeforeExpirationDate.getText().toString()));
                preferenceEditor.putBoolean(Const.PREFERENCES_PUSH_NOTIFICATIONS, notificationsByPush.isChecked());
                preferenceEditor.putInt(Const.PREFERENCES_HOUR_OF_NOTIFICATIONS, hourOfNotification.getValue());
                if(isValidEmail(emailAddress.getText().toString())) {
                    preferenceEditor.putBoolean(Const.PREFERENCES_EMAIL_NOTIFICATIONS, notificationsByEmail.isChecked());
                    preferenceEditor.putString(Const.PREFERENCES_EMAIL_ADDRESS, emailAddress.getText().toString());
                }
                else
                {
                    preferenceEditor.putBoolean(Const.PREFERENCES_EMAIL_NOTIFICATIONS, false);
                    preferenceEditor.putString(Const.PREFERENCES_EMAIL_ADDRESS, "");
                }

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
                List<Product> productsList = db.getProductsFromDB("SELECT * FROM 'products'");
                Notification.cancelAllNotifications(context);
                db.reCreateDB();
                Toast.makeText(context, getResources().getString(R.string.AppSettingsActivity_database_is_clear), Toast.LENGTH_LONG).show();
            }
        });
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
                Const.PREFERENCES_DAYS_TO_NOTIFICATIONS, Const.NOTIFICATION_DEFAULT_DAYS);
    }

    public static int getHourOfNotifications(@NonNull Context context){
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return myPreferences.getInt(Const.PREFERENCES_HOUR_OF_NOTIFICATIONS, 12);
    }

    public static String getEmailForNotifications(@NonNull Context context){
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return myPreferences.getString(Const.PREFERENCES_EMAIL_ADDRESS, "");
    }

    public static boolean isPushNotificationAllowed(Context context){
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return myPreferences.getBoolean(Const.PREFERENCES_PUSH_NOTIFICATIONS, true);
    }

    public static boolean isEmailNotificationAllowed(Context context){
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return myPreferences.getBoolean(Const.PREFERENCES_EMAIL_NOTIFICATIONS, false);
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
}