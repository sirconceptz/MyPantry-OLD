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
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.navigation.NavigationView;
import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.db.Product;
import com.hermanowicz.pantry.dialog.DeleteProductsDialog;
import com.hermanowicz.pantry.dialog.ExpirationDateFilterDialog;
import com.hermanowicz.pantry.dialog.NameFilterDialog;
import com.hermanowicz.pantry.dialog.ProductFeaturesFilterDialog;
import com.hermanowicz.pantry.dialog.ProductionDateFilterDialog;
import com.hermanowicz.pantry.dialog.TasteFilterDialog;
import com.hermanowicz.pantry.dialog.TypeOfProductFilterDialog;
import com.hermanowicz.pantry.dialog.VolumeFilterDialog;
import com.hermanowicz.pantry.dialog.WeightFilterDialog;
import com.hermanowicz.pantry.interfaces.IDeleteProductsDialogListener;
import com.hermanowicz.pantry.interfaces.IFilterDialogListener;
import com.hermanowicz.pantry.interfaces.IMyPantryActivityView;
import com.hermanowicz.pantry.models.MyPantryActivityModel;
import com.hermanowicz.pantry.presenters.MyPantryActivityPresenter;
import com.hermanowicz.pantry.utils.Notification;
import com.hermanowicz.pantry.utils.ProductsAdapter;
import com.hermanowicz.pantry.utils.ProductsViewModel;
import com.hermanowicz.pantry.utils.RecyclerProductClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <h1>MyPantryActivity</h1>
 * Activity for My Pantry. In this activity is available the list view with products from database.
 * The user can filter search results using product attributes. After a long click on the product, the multiselect mode is activated.
 *
 * @author  Mateusz Hermanowicz
 * @version 1.0
 * @since   1.0
 */
public class MyPantryActivity extends AppCompatActivity implements IMyPantryActivityView, IFilterDialogListener, IDeleteProductsDialogListener {

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

    private ProductsAdapter adapterProductsRecyclerView;
    private Context context;
    private Resources resources;
    private SharedPreferences sharedPreferences;
    private ProductsViewModel productsViewModel;
    private ActionMode mActionMode;

    private MyPantryActivityModel model;
    private MyPantryActivityPresenter presenter;

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

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        initData();
    }

    private void initData() {
        MobileAds.initialize(context, "ca-app-pub-4025776034769422~3797748160");

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        productsViewModel = ViewModelProviders.of(this).get(ProductsViewModel.class);
        presenter.setProductLiveData(productsViewModel.getAllProducts());
        presenter.getProductLiveData().observe(this, productList -> {
            presenter.setProductList(productList);
            adapterProductsRecyclerView.setData(productList);
        });
        adapterProductsRecyclerView = new ProductsAdapter(sharedPreferences);
        recyclerviewProducts.setAdapter(adapterProductsRecyclerView);
        adapterProductsRecyclerView.notifyDataSetChanged();
        recyclerviewProducts.setLayoutManager(new LinearLayoutManager(context));
        recyclerviewProducts.setHasFixedSize(true);
        recyclerviewProducts.setItemAnimator(new DefaultItemAnimator());
        recyclerviewProducts.addOnItemTouchListener(new RecyclerProductClickListener(this, recyclerviewProducts, new RecyclerProductClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (presenter.getIsMultiSelect())
                    multiSelect(position);
                else {
                    List<Product> productList = presenter.getProductList();
                    Intent productDetailsActivityIntent = new Intent(context, ProductDetailsActivity.class);
                    productDetailsActivityIntent.putExtra("product_id", productList.get(position).getId());
                    productDetailsActivityIntent.putExtra("hash_code", productList.get(position).getHashCode());
                    MyPantryActivity.this.startActivity(productDetailsActivityIntent);
                    MyPantryActivity.this.finish();
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (!presenter.getIsMultiSelect()) {
                    presenter.clearSelectList();
                    presenter.setIsMultiSelect(true);

                    if (mActionMode == null) {
                        mActionMode = startActionMode(mActionModeCallback);
                    }
                }
                multiSelect(position);
            }
        }));

        presenter.setProductList(productsViewModel.getAllProductsAsList());

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

    private void multiSelect(int position) {
        if(!presenter.getIsMultiSelect())
            toolbar.startActionMode(mActionModeCallback);
        if (mActionMode != null) {
            presenter.addMultiSelectProduct(position);
            mActionMode.setTitle(String.valueOf(presenter.getSelectList().size()));
        }
        updateSelectsRecyclerViewAdapter();
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
    public void openFilterDialog(String typeOfDialog) {
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
    public void navigateToMainActivity() {
        Intent mainActivityIntent = new Intent(context, MainActivity.class);
        startActivity(mainActivityIntent);
        finish();
    }

    @Override
    public void updateSelectsRecyclerViewAdapter() {
        adapterProductsRecyclerView.setMultiSelectList(presenter.getSelectList());
        adapterProductsRecyclerView.notifyDataSetChanged();
    }

    @Override
    public void updateProductsRecyclerViewAdapter() {
        presenter.getProductLiveData().observe(this, productList -> presenter.setProductList(productList));
        adapterProductsRecyclerView.setData(presenter.getProductList());
        adapterProductsRecyclerView.notifyDataSetChanged();
    }

    @Override
    public void onPrintProducts(ArrayList<String> textToQRCodeList, ArrayList<String> namesOfProductsList, ArrayList<String> expirationDatesList) {
        Intent printQRCodesActivityIntent = new Intent(context, PrintQRCodesActivity.class);

        printQRCodesActivityIntent.putStringArrayListExtra("text_to_qr_code", textToQRCodeList);
        printQRCodesActivityIntent.putStringArrayListExtra("expiration_dates", expirationDatesList);
        printQRCodesActivityIntent.putStringArrayListExtra("names_of_products", namesOfProductsList);

        startActivity(printQRCodesActivityIntent);
        finish();
    }

    @Override
    public void onDeleteProducts(List<Product> productList) {
        for(int i = 0; productList.size() > i; i++) {
            productsViewModel.deleteProductById(productList.get(i).getId());
            Notification.cancelNotification(context, productList.get(i));
        }
        adapterProductsRecyclerView.notifyDataSetChanged();
    }

    @Override
    public LiveData<List<Product>> getProductLiveData() {
        return productsViewModel.getAllProducts();
    }

    @Override
    public void setFilterName(String filterName) {
        presenter.setProductList(productsViewModel.getAllProductsAsList());
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
        presenter.setProductList(productsViewModel.getAllProductsAsList());
        presenter.setFilterVolume(filterVolumeSince, filterVolumeFor);
    }

    @Override
    public void setFilterWeight(int filterWeightSince, int filterWeightFor) {
        presenter.setProductList(productsViewModel.getAllProductsAsList());
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

    @Override
    public void onDeleteProducts() {
        presenter.deleteSelectedProducts();
        presenter.setIsMultiSelect(false);
        mActionMode.finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_print:
                return true;
            case R.id.action_delete:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.multi_select_products, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    DeleteProductsDialog deleteProductsDialog = new DeleteProductsDialog();
                    deleteProductsDialog.show(getSupportFragmentManager(), "");
                    return true;
                case R.id.action_print:
                    presenter.printSelectedProducts();
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            presenter.setIsMultiSelect(false);
            presenter.clearSelectList();
            updateSelectsRecyclerViewAdapter();
        }
    };
}