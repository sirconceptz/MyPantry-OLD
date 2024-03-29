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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.databinding.ActivityCategoryDetailsBinding;
import com.hermanowicz.pantry.db.category.Category;
import com.hermanowicz.pantry.interfaces.CategoryDetailsView;
import com.hermanowicz.pantry.presenter.CategoryDetailsPresenter;
import com.hermanowicz.pantry.util.Orientation;
import com.hermanowicz.pantry.util.ThemeMode;

import maes.tech.intentanim.CustomIntent;

/**
 * <h1>CategoriesActivity</h1>
 * Activity for add new category.
 *
 * @author Mateusz Hermanowicz
 */

public class CategoryDetailsActivity extends AppCompatActivity implements CategoryDetailsView {

    private CategoryDetailsPresenter presenter;
    private Context context;

    private TextView categoryName;
    private TextView categoryDescription;
    private TextView nameCharCounter;
    private TextView descriptionCharCounter;
    private Button updateCategory;
    private Button deleteCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(ThemeMode.getThemeMode(this));
        if (Orientation.isTablet(this))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        super.onCreate(savedInstanceState);
        initView();
        setListeners();
    }

    private void initView() {
        context = getApplicationContext();
        ActivityCategoryDetailsBinding binding = ActivityCategoryDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        categoryName = binding.categoryName;
        categoryDescription = binding.categoryDescription;
        nameCharCounter = binding.nameCharCounter;
        descriptionCharCounter = binding.descriptionCharCounter;
        updateCategory = binding.buttonUpdateCategory;
        deleteCategory = binding.buttonDeleteCategory;

        Intent categoryIntent = getIntent();
        Category category = (Category) categoryIntent.getSerializableExtra("category");
        presenter = new CategoryDetailsPresenter(this, context);
        presenter.setCategory(category);
    }

    private void setListeners() {
        updateCategory.setOnClickListener(view -> onClickUpdateCategory());
        deleteCategory.setOnClickListener(view -> onClickDeleteCategory());

        categoryName.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                presenter.isCategoryNameCorrect(categoryName.getText().toString());
            }
        });

        categoryDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                presenter.isCategoryDescriptionCorrect(categoryDescription.getText().toString());
            }
        });
    }

    private void onClickUpdateCategory() {
        Category category = presenter.getCategory();
        category.setName(categoryName.getText().toString());
        category.setDescription(categoryDescription.getText().toString());
        presenter.updateCategory(category);
    }

    private void onClickDeleteCategory() {
        new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppThemeDialog))
                .setMessage(R.string.CategoryDetailsActivity_category_delete_warning)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> presenter.deleteCategory())
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void showErrorOnUpdateCategory() {
        Toast.makeText(context, getString(R.string.Error_wrong_data), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showCategoryUpdated() {
        Toast.makeText(context, getString(R.string.CategoryDetailsActivity_category_was_saved), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showCategoryDetails(@NonNull Category category) {
        categoryName.setText(category.getName());
        categoryDescription.setText(category.getDescription());
    }

    @Override
    public void showCategoryNameError() {
        categoryName.setError(getText(R.string.Error_char_counter));
    }

    @Override
    public void showCategoryDescriptionError() {
        categoryDescription.setError(getText(R.string.Error_char_counter));
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void updateNameCharCounter(int charCounter, int maxChar) {
        nameCharCounter.setText(String.format("%s: %d/%d",
                getText(R.string.General_char_counter).toString(), charCounter, maxChar));
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void updateDescriptionCharCounter(int charCounter, int maxChar) {
        descriptionCharCounter.setText(String.format("%s: %d/%d",
                getText(R.string.General_char_counter).toString(), charCounter, maxChar));
    }

    @Override
    public void navigateToCategoriesActivity() {
        Intent intent = new Intent(getApplicationContext(), CategoriesActivity.class);
        startActivity(intent);
        CustomIntent.customType(this, "fadein-to-fadeout");
    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this, "fadein-to-fadeout");
    }
}