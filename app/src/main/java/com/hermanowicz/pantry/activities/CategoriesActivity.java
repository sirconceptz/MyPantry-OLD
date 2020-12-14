package com.hermanowicz.pantry.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.databinding.ActivityCategoriesBinding;
import com.hermanowicz.pantry.db.Category;
import com.hermanowicz.pantry.db.CategoryDb;
import com.hermanowicz.pantry.db.ProductDb;
import com.hermanowicz.pantry.dialog.NewCategoryDialog;
import com.hermanowicz.pantry.interfaces.CategoryView;
import com.hermanowicz.pantry.interfaces.DialogCategoryListener;
import com.hermanowicz.pantry.models.CategoryModel;
import com.hermanowicz.pantry.models.DatabaseOperations;
import com.hermanowicz.pantry.presenters.CategoryPresenter;
import com.hermanowicz.pantry.utils.CategoriesAdapter;
import com.hermanowicz.pantry.utils.Orientation;
import com.hermanowicz.pantry.utils.RecyclerClickListener;
import com.hermanowicz.pantry.utils.ThemeMode;

import java.util.List;

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

    private CategoriesAdapter categoriesAdapter;
    private CategoryPresenter presenter;
    private Context context;
    private AdView adView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(ThemeMode.getThemeMode(this));
        if(Orientation.isTablet(this))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        MobileAds.initialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        binding = ActivityCategoriesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context = getApplicationContext();
        setSupportActionBar(binding.toolbar);

        adView = binding.adview;

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        categoriesAdapter = new CategoriesAdapter();

        DatabaseOperations databaseOperations = new DatabaseOperations(ProductDb.getInstance(this), CategoryDb.getInstance(this));
        presenter = new CategoryPresenter(this, new CategoryModel(databaseOperations));
        presenter.updateCategoryList();

        binding.recyclerviewCategories.setAdapter(categoriesAdapter);
        binding.recyclerviewCategories.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerviewCategories.setHasFixedSize(true);
        binding.recyclerviewCategories.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerviewCategories.addOnItemTouchListener(new RecyclerClickListener(this, binding.recyclerviewCategories, new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                    List<Category> categoryList = CategoryDb.getInstance(context).categoryDao().getAllOwnCategories();
                    Intent intent = new Intent(context, CategoryDetailsActivity.class)
                            .putExtra("category_id", categoryList.get(position).getId());
                    startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
    public void showEmptyCategoryListStatement() {
        binding.textStatement.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateCategoryList(List<Category> categoryList) {
        categoriesAdapter.setData(categoryList);
        categoriesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSuccessAddNewCategory() {
        Toast.makeText(this, R.string.CategoryDetailsActivity_category_was_saved, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onErrorAddNewCategory() {
        Toast.makeText(this, R.string.Error_wrong_category_data, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            presenter.navigateToMainActivity();
        }
        return super.onKeyDown(keyCode, event);
    }
}