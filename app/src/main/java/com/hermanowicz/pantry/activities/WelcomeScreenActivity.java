/*
 * Copyright (c) 2019
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

package com.hermanowicz.pantry.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.dialog.LoginDialog;
import com.hermanowicz.pantry.interfaces.WelcomeScreenView;
import com.hermanowicz.pantry.presenters.WelcomeScreenPresenter;
import com.hermanowicz.pantry.utils.Orientation;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeScreenActivity extends AppCompatActivity implements WelcomeScreenView {

    private WelcomeScreenPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        if(Orientation.isTablet(this))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        ButterKnife.bind(this);

        presenter = new WelcomeScreenPresenter();
    }

    @OnClick(R.id.button_register)
    void onRegisterButtonClick(){
        presenter.onRegisterButtonClick();
    }

    @OnClick(R.id.button_login)
    void onLoginButtonClick(){
        presenter.onLoginButtonClick();
    }

    @Override
    public void onLogin() {
        AppCompatDialogFragment dialog = new LoginDialog();
        dialog.show(getSupportFragmentManager(), "");
    }

    @Override
    public void onRegister() {
        Intent registerActivityIntent = new Intent(WelcomeScreenActivity.this, RegistrationActivity.class);
        startActivity(registerActivityIntent);
    }

    @Override
    public void onNavigationToMainActivity() {
        Intent mainActivityIntent = new Intent(WelcomeScreenActivity.this, MainActivity.class);
        startActivity(mainActivityIntent);
    }
}