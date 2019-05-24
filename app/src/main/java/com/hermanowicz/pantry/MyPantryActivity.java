/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.navigation.NavigationView;
import com.hermanowicz.pantry.dialog.ExpirationDateFilterDialog;
import com.hermanowicz.pantry.dialog.NameFilterDialog;
import com.hermanowicz.pantry.dialog.ProductFeaturesFilterDialog;
import com.hermanowicz.pantry.dialog.ProductionDateFilterDialog;
import com.hermanowicz.pantry.dialog.TasteFilterDialog;
import com.hermanowicz.pantry.dialog.TypeOfProductFilterDialog;
import com.hermanowicz.pantry.dialog.VolumeFilterDialog;
import com.hermanowicz.pantry.dialog.WeightFilterDialog;
import com.hermanowicz.pantry.interfaces.DialogListener;
import com.hermanowicz.pantry.interfaces.MyPantryActivityView;
import com.hermanowicz.pantry.models.MyPantryActivityModel;
import com.hermanowicz.pantry.models.ProductEntity;
import com.hermanowicz.pantry.presenters.MyPantryActivityPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <h1>MyPantryActivity</h1>
 * Activity for My Pantry. In this activity is available the list view with products from database.
 * The user can filter search results using product attributes.
 *
 * @author  Mateusz Hermanowicz
 * @version 1.0
 * @since   1.0
 */
public class MyPantryActivity extends AppCompatActivity implements MyPantryActivityView, DialogListener {

    public ProductsAdapter adapterProductsRecyclerView;
    public AdRequest adRequest;
    private Context context;
    private Resources resources;
    private ProductDb productDB;
    private List<ProductEntity> productEntityList;
    private SharedPreferences sharedPreferences;

    private MyPantryActivityModel model;
    private MyPantryActivityPresenter presenter;

    @BindView(R.id.recyclerview_products)
    RecyclerView recyclerviewProducts;
    @BindView(R.id.my_pantry_drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.text_emptyPantryStatement)
    TextView emptyPantryStatement;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.adBanner)
    AdView adView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_pantry_drawer_layout);

        ButterKnife.bind(this);

        context = getApplicationContext();
        resources = context.getResources();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        model = new MyPantryActivityModel();
        presenter = new MyPantryActivityPresenter(this, model);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-4025776034769422~3797748160");

        adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        presenter.initRecyclerViewData();

        adapterProductsRecyclerView = new ProductsAdapter(productEntityList, product -> {
            Intent productDetailsActivityIntent = new Intent(context, ProductDetailsActivity.class);
            productDetailsActivityIntent.putExtra("product_id", product.getId());
            productDetailsActivityIntent.putExtra("hash_code", product.getHashCode());
            startActivity(productDetailsActivityIntent);
            finish();
        }, sharedPreferences);
        recyclerviewProducts.setAdapter(adapterProductsRecyclerView);
        adapterProductsRecyclerView.notifyDataSetChanged();
        recyclerviewProducts.setLayoutManager(new LinearLayoutManager(context));
        recyclerviewProducts.setHasFixedSize(true);

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        if (productEntityList.size() == 0) {
            emptyPantryStatement.setText(resources.getString(R.string.MyPantryActivity_empty_pantry));
        }

        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    String type_of_dialog = menuItem.getTitle().toString();
                    if (type_of_dialog.equals(resources.getString(R.string.MyPantryActivity_clear_filters))){
                        presenter.clearFilters();
                    }
                    else{
                        presenter.openDialog(type_of_dialog);
                    }
                    drawerLayout.closeDrawers();
                    return true;
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            presenter.navigateToMainActivity();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter = new MyPantryActivityPresenter(this, model);
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
        presenter.onDestroy();

        super.onDestroy();
    }

    @Override
    public void openDialog(String typeOfDialog) {
        if (typeOfDialog.equals(resources.getString(R.string.ProductDetailsActivity_name))) {
            NameFilterDialog nameFilterDialog = new NameFilterDialog(presenter.getFilterName());
            nameFilterDialog.show(getSupportFragmentManager(), "");
        } else if (typeOfDialog.equals(resources.getString(R.string.ProductDetailsActivity_expiration_date))) {
            ExpirationDateFilterDialog expirationDateFilterDialog = new ExpirationDateFilterDialog(presenter.getFilterExpirationDateSince(), presenter.getFilterExpirationDateFor());
            expirationDateFilterDialog.show(getSupportFragmentManager(), "");
        } else if (typeOfDialog.equals(resources.getString(R.string.ProductDetailsActivity_production_date))) {
            ProductionDateFilterDialog productionDateFilterDialog = new ProductionDateFilterDialog(presenter.getFilterProductionDateSince(), presenter.getFilterProductionDateFor());
            productionDateFilterDialog.show(getSupportFragmentManager(), "");
        } else if (typeOfDialog.equals(resources.getString(R.string.ProductDetailsActivity_product_type))) {
            TypeOfProductFilterDialog typeOfProductFilterDialog = new TypeOfProductFilterDialog(presenter.getFilterTypeOfProduct(), presenter.getFilterProductFeatures());
            typeOfProductFilterDialog.show(getSupportFragmentManager(), "");
        } else if (typeOfDialog.equals(resources.getString(R.string.ProductDetailsActivity_volume))) {
            VolumeFilterDialog volumeFilterDialog = new VolumeFilterDialog(presenter.getFilterVolumeSince(), presenter.getFilterVolumeFor());
            volumeFilterDialog.show(getSupportFragmentManager(), "");
        } else if (typeOfDialog.equals(resources.getString(R.string.ProductDetailsActivity_weight))) {
            WeightFilterDialog weightFilterDialog = new WeightFilterDialog(presenter.getFilterWeightSince(), presenter.getFilterWeightFor());
            weightFilterDialog.show(getSupportFragmentManager(), "");
        } else if (typeOfDialog.equals(resources.getString(R.string.ProductDetailsActivity_taste))) {
            TasteFilterDialog tasteFilterDialog = new TasteFilterDialog(presenter.getFilterTaste());
            tasteFilterDialog.show(getSupportFragmentManager(), "");
        } else if (typeOfDialog.equals(resources.getString(R.string.ProductDetailsActivity_product_features))) {
            ProductFeaturesFilterDialog productFeaturesFilterDialog = new ProductFeaturesFilterDialog(presenter.getFilterHasSugar(), presenter.getFilterHasSalt());
            productFeaturesFilterDialog.show(getSupportFragmentManager(), "");
        }
    }

    @Override
    public void setFilterIcon(int position) {
        navigationView.getMenu().getItem(position).setIcon(R.drawable.ic_check_box_true);
    }

    @Override
    public void clearFilterIcon(int position) {
        navigationView.getMenu().getItem(position).setIcon(R.drawable.ic_check_box_false);
    }

    @Override
    public void showEmptyPantryStatement() {
        emptyPantryStatement.setText(resources.getString(R.string.MyPantryActivity_empty_pantry));
    }

    @Override
    public void clearFilterIcons() {
        int sizeOfMenu = navigationView.getMenu().size();
        for (int i = 1; i < sizeOfMenu - 1; i++) {
            navigationView.getMenu().getItem(i).setIcon(R.drawable.ic_check_box_false);
        }
    }

    @Override
    public void initData(String pantryQuery) {
        productDB = Room.databaseBuilder(context, ProductDb.class, "Products").allowMainThreadQueries().build();
        productEntityList = productDB.productsDao().getAllProducts();
    }

    @Override
    public void refreshListView(String pantryQuery) {
        if (emptyPantryStatement.getText().toString() != resources.getString(R.string.MyPantryActivity_empty_pantry)) {
            if (productEntityList.size() == 0) {
                emptyPantryStatement.setText(resources.getString(R.string.MyPantryActivity_not_found));
            } else {
                emptyPantryStatement.setText("");
            }
        }
        adapterProductsRecyclerView = new ProductsAdapter(productEntityList, product -> {
            Intent productDetailsActivityIntent = new Intent(context, ProductDetailsActivity.class);
            productDetailsActivityIntent.putExtra("product_id", product.getId());
            productDetailsActivityIntent.putExtra("hash_code", product.getHashCode());
            startActivity(productDetailsActivityIntent);
            finish();
        }, sharedPreferences);
        recyclerviewProducts.setAdapter(adapterProductsRecyclerView);
        adapterProductsRecyclerView.notifyDataSetChanged();
    }

    @Override
    public void navigateToMainActivity() {
        Intent mainActivityIntent = new Intent(context, MainActivity.class);
        startActivity(mainActivityIntent);
        finish();
    }

    @Override
    public void setFilterName(String filterName) {
        presenter.setFilterName(filterName);
    }

    @Override
    public void setFilterExpirationDate(String filterExpirationDateSince, String filterExpirationDateFor) {
        presenter.setFilterExpirationDate(filterExpirationDateSince, filterExpirationDateFor);
    }

    @Override
    public void setFilterProductionDate(String filterProductionDateSince, String filterProductionDateFor) {
        presenter.setFilterProductionDate(filterProductionDateSince, filterProductionDateFor);
    }

    @Override
    public void setFilterTypeOfProduct(String filterTypeOfProduct, String filterProductFeatures) {
        presenter.setFilterTypeOfProduct(filterTypeOfProduct, filterProductFeatures);
    }

    @Override
    public void setFilterVolume(int filterVolumeSince, int filterVolumeFor) {
        presenter.setFilterVolume(filterVolumeSince, filterVolumeFor);
    }

    @Override
    public void setFilterWeight(int filterWeightSince, int filterWeightFor) {
        presenter.setFilterWeight(filterWeightSince, filterWeightFor);
    }

    @Override
    public void setFilterTaste(String filterTaste) {
        presenter.setFilterTaste(filterTaste);
    }

    @Override
    public void setProductFeatures(int filterHasSugar, int filterHasSalt) {
        presenter.setProductFeatures(filterHasSugar, filterHasSalt);
    }

    @Override
    public void clearFilterName() {
        presenter.clearFilterName();
    }

    @Override
    public void clearFilterExpirationDate() {
        presenter.clearFilterExpirationDate();
    }

    @Override
    public void clearFilterProductionDate() {
        presenter.clearFilterProductionDate();
    }

    @Override
    public void clearFilterTypeOfProduct() {
        presenter.clearFilterTypeOfProduct();
    }

    @Override
    public void clearFilterVolume() {
        presenter.clearFilterVolume();
    }

    @Override
    public void clearFilterWeight() {
        presenter.clearFilterWeight();
    }

    @Override
    public void clearFilterTaste() {
        presenter.clearFilterTaste();
    }

    @Override
    public void clearProductFeatures() {
        presenter.clearProductFeatures();
    }
}