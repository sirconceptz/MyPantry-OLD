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
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;

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
    private SharedPreferences myPreferences;
    private EditText          daysBeforeExpirationDate, emailAddress;
    private NumberPicker      hourOfNotification;
    private CheckBox          notificationsByEmail, notificationsByPush;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);

        context = getApplicationContext();
        daysBeforeExpirationDate = findViewById(R.id.DaysToNotification);
        notificationsByEmail = findViewById(R.id.EmailNotificationsCheckbox);
        notificationsByPush = findViewById(R.id.PushNotificationsCheckbox);
        hourOfNotification = findViewById(R.id.HourOfNotification);
        emailAddress = findViewById(R.id.EmailAddress);
        Toolbar toolbar = findViewById(R.id.Toolbar);
        Button saveSettings = findViewById(R.id.SaveSettingsButton);
        Button clearDatabase = findViewById(R.id.ClearDatabaseButton);
        myPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        setSupportActionBar(toolbar);

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