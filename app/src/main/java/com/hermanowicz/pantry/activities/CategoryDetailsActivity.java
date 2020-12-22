package com.hermanowicz.pantry.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
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
import com.hermanowicz.pantry.db.Category;
import com.hermanowicz.pantry.interfaces.CategoryDetailsView;
import com.hermanowicz.pantry.models.CategoryModel;
import com.hermanowicz.pantry.models.DatabaseOperations;
import com.hermanowicz.pantry.presenters.CategoryDetailsPresenter;
import com.hermanowicz.pantry.utils.Orientation;
import com.hermanowicz.pantry.utils.ThemeMode;

public class CategoryDetailsActivity extends AppCompatActivity implements CategoryDetailsView {

    private ActivityCategoryDetailsBinding binding;
    private CategoryDetailsPresenter presenter;
    private Context context;
    private int categoryId;

    private TextView categoryName, categoryDescription, nameCharCounter, descriptionCharCounter;
    private Button updateCategory, deleteCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(ThemeMode.getThemeMode(this));
        if(Orientation.isTablet(this))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        super.onCreate(savedInstanceState);
        initView();
        setListeners();
    }

    private void initView() {
        context = getApplicationContext();
        binding = ActivityCategoryDetailsBinding.inflate(getLayoutInflater());
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
        categoryId = categoryIntent.getIntExtra("category_id", 0);
        DatabaseOperations databaseOperations = new DatabaseOperations(context);
        presenter = new CategoryDetailsPresenter(this, new CategoryModel(databaseOperations));
        presenter.setCategoryId(categoryId);
    }

    private void setListeners(){
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

    private void onClickUpdateCategory(){
        Category category = presenter.getCategory(categoryId);
        category.setName(categoryName.getText().toString());
        category.setDescription(categoryDescription.getText().toString());
        presenter.updateCategory(category);
    }

    private void onClickDeleteCategory() {
        new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppThemeDialog))
                .setMessage(R.string.CategoryDetailsActivity_category_delete_warning)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> presenter.deleteCategory(categoryId))
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
    public void showCategoryDetails(Category category) {
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

    @Override
    public void updateNameCharCounter(int charCounter, int maxChar) {
        nameCharCounter.setText(String.format("%s: %d/%d", getText(R.string.General_char_counter).toString(), charCounter, maxChar));
    }

    @Override
    public void updateDescriptionCharCounter(int charCounter, int maxChar) {
        descriptionCharCounter.setText(String.format("%s: %d/%d", getText(R.string.General_char_counter).toString(), charCounter, maxChar));
    }

    @Override
    public void navigateToCategoriesActivity() {
        Intent intent = new Intent (getApplicationContext(), CategoriesActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            presenter.navigateToCategoriesActivity();
        }
        return super.onKeyDown(keyCode, event);
    }
}