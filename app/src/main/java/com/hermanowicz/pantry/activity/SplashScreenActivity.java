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

package com.hermanowicz.pantry.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.databinding.ActivitySplashScreenBinding;
import com.hermanowicz.pantry.model.AppSettingsModel;
import com.hermanowicz.pantry.util.ThemeMode;

import maes.tech.intentanim.CustomIntent;

public class SplashScreenActivity extends AppCompatActivity {

    private ActivitySplashScreenBinding binding;

    private ImageView logo;
    private TextView appName, appAuthor, appVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(ThemeMode.getThemeMode(this));
        super.onCreate(savedInstanceState);
        initView();
        delayAndGoToMainActivity();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        goToMainActivity();
    }

    private void initView() {
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        logo = binding.logo;
        appName = binding.appName;
        appAuthor = binding.appAuthor;
        appVersion = binding.appVersion;

        appAuthor.setText(String.format("%s: %s", getString(R.string.General_author_label), getString(R.string.Author_name)));

        logo.animate().translationY(100).setDuration(1900);
        appName.animate().translationY(100).setDuration(1900);

        AppSettingsModel appSettingsModel = new AppSettingsModel(PreferenceManager.getDefaultSharedPreferences(this));
        appVersion.setText(String.format("%s: %s", getString(R.string.AppSettingsActivity_version), appSettingsModel.getAppVersion()));
    }


    private void goToMainActivity() {
        Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
        startActivity(i);
        CustomIntent.customType(this, "fadein-to-fadeout");
    }

    private void delayAndGoToMainActivity() {
        new Handler().postDelayed(() -> {
            finish();
            goToMainActivity();
        }, 2000);
    }
}