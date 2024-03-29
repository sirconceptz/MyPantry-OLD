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

package com.hermanowicz.pantry.util;

import android.app.Activity;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import com.hermanowicz.pantry.model.AppSettingsModel;

/**
 * <h1>ThemeMode</h1>
 * Class to check what theme of application the user set
 *
 * @author  Mateusz Hermanowicz
 */

public class ThemeMode {
    public static int getThemeMode(@NonNull Activity activity) {
        AppSettingsModel model = new AppSettingsModel(PreferenceManager.getDefaultSharedPreferences(activity));
        if(model.getSelectedAppTheme() == 0){
            return AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
        }
        else if (model.getSelectedAppTheme() == 1){
            return AppCompatDelegate.MODE_NIGHT_NO;
        }
        else
            return AppCompatDelegate.MODE_NIGHT_YES;
    }
}