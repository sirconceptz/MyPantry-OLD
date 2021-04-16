/*
 * Copyright (c) 2019-2021
 * Mateusz Hermanowicz - All rights reserved.
 * My Pantry
 * https://www.mypantry.eu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hermanowicz.pantry.model;

import android.content.SharedPreferences;
import android.util.Patterns;

import androidx.annotation.NonNull;

import com.hermanowicz.pantry.BuildConfig;

import java.util.regex.Pattern;

public class AppSettingsModel {

    private final SharedPreferences preferences;

    public AppSettingsModel(@NonNull SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public int getSelectedAppTheme() {
        return Integer.parseInt(preferences.getString("SELECTED_APPLICATION_THEME", "0"));
    }

    public int getSelectedScanCamera() {
        return Integer.parseInt(preferences.getString("SCAN_CAMERA", "0"));
    }

    public boolean getScannerVibrationMode() {
        return preferences.getBoolean("VIBRATION_ON_SCANNER?", true);
    }

    public boolean getScannerSoundMode() {
        return preferences.getBoolean("SOUND_ON_SCANNER?", true);
    }

    public boolean isValidEmail() {
        String address = preferences.getString("EMAIL_ADDRESS", "");
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(address).matches();
    }
    
    public String getAppVersion(){
        return BuildConfig.VERSION_NAME;
    }

    public String getEmailAddress() {
        return preferences.getString("EMAIL_ADDRESS", "");
    }

    public int getDaysToNotification() {
        return Integer.parseInt(preferences.getString("HOW_MANY_DAYS_BEFORE_EXPIRATION_DATE_SEND_A_NOTIFICATION?", "3"));
    }

    public void clearEmailAddress(){
        preferences.edit().putString("EMAIL_ADDRESS", "").apply();
    }
}