package com.hermanowicz.pantry.activities;

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
import com.hermanowicz.pantry.models.AppSettingsModel;
import com.hermanowicz.pantry.utils.ThemeMode;

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