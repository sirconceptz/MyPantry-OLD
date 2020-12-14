package com.hermanowicz.pantry.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.databinding.ActivitySplashScreenBinding;
import com.hermanowicz.pantry.models.AppSettingsModel;
import com.hermanowicz.pantry.utils.ThemeMode;

public class SplashScreenActivity extends AppCompatActivity {

    ActivitySplashScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(ThemeMode.getThemeMode(this));
        super.onCreate(savedInstanceState);
        initView();
        setView();
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
    }

    private void setView() {
        binding.appAuthor.setText(String.format("%s: %s", getString(R.string.General_author_label), getString(R.string.Author_name)));

        binding.logo.animate().translationY(100).setDuration(1900);
        binding.appName.animate().translationY(100).setDuration(1900);

        AppSettingsModel appSettingsModel = new AppSettingsModel(PreferenceManager.getDefaultSharedPreferences(this));
        String appVersion = appSettingsModel.getAppVersion();
        binding.appVersion.setText(String.format("%s: %s", getString(R.string.AppSettingsActivity_version), appVersion));
    }

    private void goToMainActivity() {
        Intent i = new Intent(SplashScreenActivity.this, MainActivity.class); startActivity(i);
        finish();
    }

    private void delayAndGoToMainActivity() {
        new Handler().postDelayed(() -> {
            finish();
            goToMainActivity();
        }, 2000);
    }
}