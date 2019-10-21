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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.interfaces.RegistrationView;
import com.hermanowicz.pantry.presenters.RegistrationPresenter;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistrationActivity extends AppCompatActivity implements RegistrationView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.edittext_emailAddress)
    EditText emailAddress;
    @BindView(R.id.edittext_password)
    EditText password;
    @BindView(R.id.edittext_passwordConfirmation)
    EditText passwordConfirmation;

    private Context context;
    private RegistrationPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        ButterKnife.bind(this);

        context = RegistrationActivity.this;
        presenter = new RegistrationPresenter(this);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.RegisterActivity_register));
    }

    @OnClick(R.id.button_register)
    void registerAccount(){

    }

    @Override
    public void accountCorrectRegistered() {
        Toast.makeText(context, getString(R.string.RegisterActivity_accountCorrectRegistered), Toast.LENGTH_LONG).show();
        Intent mainActivityIntent = new Intent(context, MainActivity.class);
        startActivity(mainActivityIntent);
    }

    @Override
    public void showErrorWrongEmail() {
        Toast.makeText(context, getString(R.string.RegisterActivity_errorWrongEmail), Toast.LENGTH_LONG).show();
        emailAddress.setError(getString(R.string.RegisterActivity_errorWrongEmail));
    }
}