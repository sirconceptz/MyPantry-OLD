/*
 * Copyright (c) 2020
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
import android.content.pm.ActivityInfo;
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
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.navigation.NavigationView;
import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.db.Product;
import com.hermanowicz.pantry.db.ProductDb;
import com.hermanowicz.pantry.dialog.DeleteProductsDialog;
import com.hermanowicz.pantry.dialog.ExpirationDateFilterDialog;
import com.hermanowicz.pantry.dialog.NameFilterDialog;
import com.hermanowicz.pantry.dialog.ProductFeaturesFilterDialog;
import com.hermanowicz.pantry.dialog.ProductionDateFilterDialog;
import com.hermanowicz.pantry.dialog.TasteFilterDialog;
import com.hermanowicz.pantry.dialog.TypeOfProductFilterDialog;
import com.hermanowicz.pantry.dialog.VolumeFilterDialog;
import com.hermanowicz.pantry.dialog.WeightFilterDialog;
import com.hermanowicz.pantry.filter.Filter;
import com.hermanowicz.pantry.interfaces.DeleteProductsDialogListener;
import com.hermanowicz.pantry.interfaces.FilterDialogListener;
import com.hermanowicz.pantry.interfaces.MyPantryView;
import com.hermanowicz.pantry.presenters.MyPantryPresenter;
import com.hermanowicz.pantry.utils.Notification;
import com.hermanowicz.pantry.utils.Orientation;
import com.hermanowicz.pantry.utils.ProductsAdapter;
import com.hermanowicz.pantry.utils.RecyclerProductClickListener;
import com.hermanowicz.pantry.utils.ThemeMode;

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
public class MyPantryActivity extends AppCompatActivity implements MyPantryView, FilterDialogListener, DeleteProductsDialogListener {

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
    private ProductDb productDb;
    private SharedPreferences sharedPreferences;
    private ActionMode actionMode;

    private MyPantryPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(ThemeMode.getThemeMode(this));
        if(Orientation.isTablet(this))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_pantry_drawer_layout);

        ButterKnife.bind(this);

        context = getApplicationContext();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        productDb = ProductDb.getInstance(context);

        presenter = new MyPantryPresenter(this);

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        initData();
    }

    private void initData() {
        MobileAds.initialize(context, getResources().getString(R.string.admob_ad_id));

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        presenter.setProductLiveData(productDb.productsDao().getAllProductsAsLivedata());
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
                    Intent productDetailsActivityIntent = new Intent(context, ProductDetailsActivity.class)
                            .putExtra("product_id", productList.get(position).getId())
                            .putExtra("hash_code", productList.get(position).getHashCode());
                    MyPantryActivity.this.startActivity(productDetailsActivityIntent);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (!presenter.getIsMultiSelect()) {
                    presenter.clearSelectList();
                    presenter.setIsMultiSelect(true);

                    if (actionMode == null) {
                        actionMode = startActionMode(actionModeCallback);
                    }
                }
                multiSelect(position);
            }
        }));

        presenter.setProductList(productDb.productsDao().getAllProductsAsList());

        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    int typeOfDialog = menuItem.getItemId();
                    if (typeOfDialog == R.id.filter_clear){
                        presenter.clearFilters();
                    }
                    else{
                        presenter.openDialog(typeOfDialog);
                    }
                    drawerLayout.closeDrawers();
                    return true;
                });
    }

    private void multiSelect(int position) {
        if(!presenter.getIsMultiSelect())
            toolbar.startActionMode(actionModeCallback);
        if (actionMode != null) {
            presenter.addMultiSelectProduct(position);
            if(presenter.getSelectList().size() == 0)
                actionMode.finish();
            else
                actionMode.setTitle(String.valueOf(presenter.getSelectList().size()));
        }
        updateSelectsRecyclerViewAdapter();
    }

    private AppCompatDialogFragment createDialog(int typeOfDialog){
        AppCompatDialogFragment dialog = null;
        if (typeOfDialog == R.id.filter_name) {
            dialog = new NameFilterDialog(presenter.getFilterProduct());
        } else if (typeOfDialog == R.id.filter_expiration_date) {
            dialog = new ExpirationDateFilterDialog(presenter.getFilterProduct());
        } else if (typeOfDialog == R.id.filter_production_date) {
            dialog = new ProductionDateFilterDialog(presenter.getFilterProduct());
        } else if (typeOfDialog == R.id.filter_product_type) {
            dialog = new TypeOfProductFilterDialog(presenter.getFilterProduct());
        } else if (typeOfDialog == R.id.filter_volume) {
            dialog = new VolumeFilterDialog(presenter.getFilterProduct());
        } else if (typeOfDialog == R.id.filter_weight) {
            dialog = new WeightFilterDialog(presenter.getFilterProduct());
        } else if (typeOfDialog == R.id.filter_taste) {
            dialog = new TasteFilterDialog(presenter.getFilterProduct());
        } else if (typeOfDialog == R.id.filter_product_features) {
            dialog = new ProductFeaturesFilterDialog(presenter.getFilterProduct());
        }
            return dialog;
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            presenter.navigateToMainActivity();
        }
        return super.onKeyDown(keyCode, event);
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
    public void openFilterDialog(int typeOfDialog) {
        createDialog(typeOfDialog).show(getSupportFragmentManager(), "");
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
    public void showEmptyPantryStatement(boolean state) {
        if(state)
            emptyPantryStatement.setText(getString(R.string.MyPantryActivity_empty_pantry));
        else
            emptyPantryStatement.setText("");
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
        Intent printQRCodesActivityIntent = new Intent(context, PrintQRCodesActivity.class)
                .putStringArrayListExtra("text_to_qr_code", textToQRCodeList)
                .putStringArrayListExtra("expiration_dates", expirationDatesList)
                .putStringArrayListExtra("names_of_products", namesOfProductsList);
        startActivity(printQRCodesActivityIntent);
    }

    @Override
    public void onDeleteProducts(List<Product> productList) {
        for(Product product : productList) {
            productDb.productsDao().deleteProductById(product.getId());
            Notification.cancelNotification(context, product);
        }
    }

    @Override
    public LiveData<List<Product>> getProductLiveData() {
        return productDb.productsDao().getAllProductsAsLivedata();
    }

    @Override
    public void setFilterName(String filterName) {
        presenter.setProductList(productDb.productsDao().getAllProductsAsList());
        presenter.setFilterName(filterName);
    }

    @Override
    public void setFilterExpirationDate(String filterExpirationDateSince, String filterExpirationDateFor) {
        presenter.setProductList(productDb.productsDao().getAllProductsAsList());
        presenter.setFilterExpirationDate(filterExpirationDateSince, filterExpirationDateFor);
    }

    @Override
    public void setFilterProductionDate(String filterProductionDateSince, String filterProductionDateFor) {
        presenter.setProductList(productDb.productsDao().getAllProductsAsList());
        presenter.setFilterProductionDate(filterProductionDateSince, filterProductionDateFor);
    }

    @Override
    public void setFilterTypeOfProduct(String filterTypeOfProduct, String filterProductFeatures) {
        presenter.setProductList(productDb.productsDao().getAllProductsAsList());
        presenter.setFilterTypeOfProduct(filterTypeOfProduct, filterProductFeatures);
    }

    @Override
    public void setFilterVolume(int filterVolumeSince, int filterVolumeFor) {
        presenter.setProductList(productDb.productsDao().getAllProductsAsList());
        presenter.setFilterVolume(filterVolumeSince, filterVolumeFor);
    }

    @Override
    public void setFilterWeight(int filterWeightSince, int filterWeightFor) {
        presenter.setProductList(productDb.productsDao().getAllProductsAsList());
        presenter.setFilterWeight(filterWeightSince, filterWeightFor);
    }

    @Override
    public void setFilterTaste(String filterTaste) {
        presenter.setProductList(productDb.productsDao().getAllProductsAsList());
        presenter.setFilterTaste(filterTaste);
    }

    @Override
    public void setProductFeatures(Filter.Set filterHasSugar, Filter.Set filterHasSalt) {
        presenter.setProductList(productDb.productsDao().getAllProductsAsList());
        presenter.setProductFeatures(filterHasSugar, filterHasSalt);
    }

    @Override
    public void onDeleteProducts() {
        presenter.deleteSelectedProducts();
        actionMode.finish();
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

    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
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
            actionMode = null;
            presenter.setIsMultiSelect(false);
            presenter.clearSelectList();
            updateSelectsRecyclerViewAdapter();
        }
    };
}