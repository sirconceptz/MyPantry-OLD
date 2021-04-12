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

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.databinding.ActivityCategoriesBinding;
import com.hermanowicz.pantry.db.category.Category;
import com.hermanowicz.pantry.db.category.CategoryDb;
import com.hermanowicz.pantry.dialog.NewCategoryDialog;
import com.hermanowicz.pantry.interfaces.CategoryView;
import com.hermanowicz.pantry.interfaces.DialogCategoryListener;
import com.hermanowicz.pantry.model.CategoryModel;
import com.hermanowicz.pantry.model.DatabaseOperations;
import com.hermanowicz.pantry.presenter.CategoryPresenter;
import com.hermanowicz.pantry.util.CategoriesAdapter;
import com.hermanowicz.pantry.util.Orientation;
import com.hermanowicz.pantry.util.RecyclerClickListener;
import com.hermanowicz.pantry.util.ThemeMode;

import java.util.List;

import maes.tech.intentanim.CustomIntent;

/**
 * <h1>CategoriesActivity</h1>
 * Categories activity - user can create own categories to user's pantry.
 *
 * @author  Mateusz Hermanowicz
 * @version 1.5
 * @since   1.5
 */

public class CategoriesActivity extends AppCompatActivity implements DialogCategoryListener, CategoryView {

    private ActivityCategoriesBinding binding;
    private CategoryPresenter presenter;
    private Context context;
    private final CategoriesAdapter categoriesAdapter = new CategoriesAdapter();

    private AdView adView;
    private RecyclerView categoryRecyclerView;
    private TextView statement;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(ThemeMode.getThemeMode(this));
        if(Orientation.isTablet(this))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        MobileAds.initialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        initView();
        setListeners();
    }

    private void initView() {
        binding = ActivityCategoriesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = getApplicationContext();

        Toolbar toolbar = binding.toolbar;
        adView = binding.adview;
        categoryRecyclerView = binding.recyclerviewCategories;
        statement = binding.textStatement;

        setSupportActionBar(toolbar);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        DatabaseOperations databaseOperations = new DatabaseOperations(context);
        presenter = new CategoryPresenter(this, new CategoryModel(databaseOperations));
        presenter.updateCategoryList();

        categoryRecyclerView.setAdapter(categoriesAdapter);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        categoryRecyclerView.setHasFixedSize(true);
        categoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void setListeners() {
        categoryRecyclerView.addOnItemTouchListener(new RecyclerClickListener(this, binding.recyclerviewCategories, new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                List<Category> categoryList = CategoryDb.getInstance(context).categoryDao().getAllOwnCategories();
                Intent intent = new Intent(context, CategoryDetailsActivity.class)
                        .putExtra("CATEGORY_ID", categoryList.get(position).getId());
                startActivity(intent);
                CustomIntent.customType(view.getContext(), "fadein-to-fadeout");
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        }));
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.new_item_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_new_item) {
            NewCategoryDialog newCategoryDialog = new NewCategoryDialog();
            newCategoryDialog.show(getSupportFragmentManager(), "");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAddCategory(Category category) {
        presenter.addCategory(category);
    }

    @Override
    public void showEmptyCategoryListStatement(boolean visible) {
        if(visible)
            statement.setVisibility(View.VISIBLE);
        else
            statement.setVisibility(View.INVISIBLE);
    }

    @Override
    public void updateCategoryList(@NonNull List<Category> categoryList) {
        categoriesAdapter.setData(categoryList);
        categoriesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSuccessAddNewCategory() {
        Toast.makeText(this, R.string.CategoryDetailsActivity_category_was_saved, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onErrorAddNewCategory() {
        Toast.makeText(this, R.string.Error_wrong_data, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        CustomIntent.customType(this, "bottom-to-up");
    }

    @Override
    public void onResume() {
        super.onResume();
        adView.resume();
    }

    @Override
    public void onPause() {
        adView.pause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        adView.destroy();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            presenter.navigateToMainActivity();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this, "up-to-bottom");
    }
}