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
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
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
import com.hermanowicz.pantry.interfaces.MyPantryActivityView;
import com.hermanowicz.pantry.models.MyPantryActivityModel;
import com.hermanowicz.pantry.models.Product;
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
public class MyPantryActivity extends AppCompatActivity implements DialogManager.DialogListener, MyPantryActivityView {

    private Context          context;
    private Resources        resources;
    private DatabaseManager  db;
    private List<Product>    productsFromDB;
    private ProductsAdapter  adapterProductsRecyclerView;
    private String           fltrName, fltrExpirationDateSince, fltrExpirationDateFor,
                             fltrProductionDateSince, fltrProductionDateFor, fltrTypeOfProduct,
                             fltrProductFeatures, fltrTaste, type_of_dialog;
    private int              fltrWeightSince = -1, fltrWeightFor = -1, fltrVolumeSince = -1,
                             fltrVolumeFor = -1, fltrHasSugar = -1, fltrHasSalt = -1;
    public  AdRequest        adRequest;

    private MyPantryActivityModel model;
    private MyPantryActivityPresenter presenter;

    @BindView(R.id.recyclerview_products)
    RecyclerView recyclerViewProducts;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_pantry_drawer_layout);

        ButterKnife.bind(this);

        context = getApplicationContext();
        resources = context.getResources();
        db = new DatabaseManager(context);
        productsFromDB = db.getProductsFromDB(buildPantryQuery());

        model = new MyPantryActivityModel();
        presenter = new MyPantryActivityPresenter(this, model);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-4025776034769422~3797748160");

        adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        ProductDB productDB = Room.databaseBuilder(context, ProductDB.class, "Article").allowMainThreadQueries().build();

        adapterProductsRecyclerView = new ProductsAdapter(productsFromDB, product -> {
            Intent productDetailsActivityIntent = new Intent(context, ProductDetailsActivity.class);
            int productID = product.getID() - 1;
            productDetailsActivityIntent.putExtra("product_id", productID);
            productDetailsActivityIntent.putExtra("hash_code", Integer.parseInt(product.getHashCode()));
            startActivity(productDetailsActivityIntent);
            finish();
        });
        recyclerViewProducts.setAdapter(adapterProductsRecyclerView);
        adapterProductsRecyclerView.notifyDataSetChanged();
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewProducts.setHasFixedSize(true);

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        if(productsFromDB.size()==0){
            emptyPantryStatement.setText(resources.getString(R.string.MyPantryActivity_empty_pantry));
        }

        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    type_of_dialog = menuItem.getTitle().toString();
                    if (type_of_dialog.equals(resources.getString(R.string.MyPantryActivity_clear_filters))){
                        presenter.clearFilters();
                        clearFilters();
                    }
                    else{
                        openDialog(type_of_dialog);
                        presenter.openDialog(type_of_dialog);
                    }
                    drawerLayout.closeDrawers();
                    return true;
                });
    }

    /**
     * The method to open dialog window to input search filters.
     *
     * @param dialogType value of dialog type
     */
    public void openDialog(String dialogType){

        DialogManager dialog = new DialogManager();

        if(dialogType.equals(resources.getString(R.string.ProductDetailsActivity_name))) {
            if (fltrName != null) {
                dialog.setFilterName(fltrName);
            }
            else {
                dialog.setFilterName(null);
            }
        }
        else if(dialogType.equals(resources.getString(R.string.ProductDetailsActivity_expiration_date))) {
            if (fltrExpirationDateSince != null || fltrExpirationDateFor != null) {
                dialog.setFilterExpirationDate(fltrExpirationDateSince, fltrExpirationDateFor);
            } else {
                dialog.setFilterExpirationDate(null, null);
            }
        }
        else if(dialogType.equals(resources.getString(R.string.ProductDetailsActivity_production_date))) {
            if (fltrProductionDateSince != null || fltrProductionDateFor != null) {
                dialog.setFilterProductionDate(fltrProductionDateSince, fltrProductionDateFor);
            } else {
                dialog.setFilterProductionDate(null, null);
            }
        }
        else if(dialogType.equals(resources.getString(R.string.ProductDetailsActivity_product_type))) {
            if (fltrTypeOfProduct != null || fltrProductFeatures != null) {
                dialog.setFilterTypeOfProduct(fltrTypeOfProduct, fltrProductFeatures);
            } else {
                dialog.setFilterTypeOfProduct(null, null);
            }
        }
        else if(dialogType.equals(resources.getString(R.string.ProductDetailsActivity_volume))) {
            if (fltrVolumeSince >= 0 || fltrVolumeFor >= 0) {
                dialog.setFilterVolume(fltrVolumeSince, fltrVolumeFor);
            } else {
                dialog.setFilterVolume(-1, -1);
            }
        }
        else if(dialogType.equals(resources.getString(R.string.ProductDetailsActivity_weight))) {
            if (fltrWeightSince >= 0 || fltrWeightFor >= 0) {
                dialog.setFilterWeight(fltrWeightSince, fltrWeightFor);
            } else {
                dialog.setFilterWeight(-1, -1);
            }
        }
        else if(dialogType.equals(resources.getString(R.string.ProductDetailsActivity_taste))) {
            if (fltrTaste != null) {
                dialog.setFilterTaste(fltrTaste);
            } else {
                dialog.setFilterTaste(null);
            }
        }
        else if(dialogType.equals(resources.getString(R.string.ProductDetailsActivity_product_features))) {
            if (fltrHasSugar >= 0 || fltrHasSalt >= 0) {
                dialog.setFilterProductFeatures(fltrHasSugar, fltrHasSalt);
            } else {
                dialog.setFilterProductFeatures(-1,-1);
            }
        }
        dialog.setDialogType(dialogType);
        dialog.show(getSupportFragmentManager(), "");
    }

    public void refreshListView(){
        productsFromDB = db.getProductsFromDB(buildPantryQuery());
        if(emptyPantryStatement.getText().toString() == resources.getString(R.string.MyPantryActivity_empty_pantry)){}
        else if(productsFromDB.size()==0){
            emptyPantryStatement.setText(resources.getString(R.string.MyPantryActivity_not_found));
        }
        else{
            emptyPantryStatement.setText("");
        }
        adapterProductsRecyclerView.notifyDataSetChanged();
    }

    public void clearFilters() {
        fltrName                = null;
        fltrTypeOfProduct       = null;
        fltrProductFeatures     = null;
        fltrExpirationDateSince = null;
        fltrExpirationDateFor   = null;
        fltrProductionDateSince = null;
        fltrProductionDateFor   = null;
        fltrVolumeSince         = -1;
        fltrVolumeFor           = -1;
        fltrWeightSince         = -1;
        fltrWeightFor           = -1;
        fltrHasSugar            = -1;
        fltrHasSalt             = -1;
        fltrTaste               = null;
        productsFromDB          = db.getProductsFromDB(buildPantryQuery());

        int sizeOfMenu = navigationView.getMenu().size();
        for (int i = 1; i < sizeOfMenu-1; i++) {
            navigationView.getMenu().getItem(i).setIcon(R.drawable.ic_check_box_false);
        }
        refreshListView();
    }

    /**
     * The method to build a string for database query to search a products.
     *
     * @return query to select data from database.
     */
    public String buildPantryQuery() {
        String selectQuery = "SELECT * FROM 'products'";

        if(fltrName != null){
            selectQuery = selectQuery + " WHERE name LIKE '%" + fltrName + "%'";
        }
        if(fltrExpirationDateSince != null && fltrExpirationDateFor == null){
            if(fltrName == null){
                selectQuery = selectQuery + " WHERE ";
            }
            else{
                selectQuery = selectQuery + " AND ";
            }
            selectQuery = selectQuery + "expiration_date >= '" + fltrExpirationDateSince + "'";
        }
        if(fltrExpirationDateSince == null && fltrExpirationDateFor != null){
            if(fltrName == null){
                selectQuery = selectQuery + " WHERE ";
            }
            else{
                selectQuery = selectQuery + " AND ";
            }
            selectQuery = selectQuery + "expiration_date <= '" + fltrExpirationDateFor + "'";
        }
        if(fltrExpirationDateSince != null && fltrExpirationDateFor != null){
            if(fltrName == null){
                selectQuery = selectQuery + " WHERE ";
            }
            else{
                selectQuery = selectQuery + " AND ";
            }
            selectQuery = selectQuery + "expiration_date BETWEEN '" + fltrExpirationDateSince + "' AND '" + fltrExpirationDateFor + "'";
        }
        if(fltrProductionDateSince != null && fltrProductionDateFor == null){
            if(fltrName == null && fltrExpirationDateSince == null && fltrExpirationDateFor == null){
                selectQuery = selectQuery + " WHERE ";
            }
            else{
                selectQuery = selectQuery + " AND ";
            }
            selectQuery = selectQuery  + "production_date >= '" + fltrProductionDateSince + "'";
        }
        if(fltrProductionDateSince == null && fltrProductionDateFor != null){
            if(fltrName == null && fltrExpirationDateSince == null && fltrExpirationDateFor == null){
                selectQuery = selectQuery + " WHERE ";
            }
            else{
                selectQuery = selectQuery + " AND ";
            }
            selectQuery = selectQuery + "production_date <= '" + fltrProductionDateFor + "'";
        }
        if(fltrProductionDateSince != null && fltrProductionDateFor != null){
            if(fltrName == null && fltrExpirationDateSince == null && fltrExpirationDateFor == null){
                selectQuery = selectQuery + " WHERE ";
            }
            else{
                selectQuery = selectQuery + " AND ";
            }
            selectQuery = selectQuery + "production_date BETWEEN'" + fltrProductionDateSince + "' AND '" + fltrProductionDateFor + "'";
        }
        if(fltrTypeOfProduct != null && fltrProductFeatures == null){
            if(fltrName == null && fltrExpirationDateSince == null && fltrExpirationDateFor == null && fltrProductionDateSince == null
                    && fltrProductionDateFor == null){
                selectQuery = selectQuery + " WHERE ";
            }
            else{
                selectQuery = selectQuery + " AND ";
            }
            selectQuery = selectQuery + "type_of_product LIKE '%" + fltrTypeOfProduct + "%'";
        }
        if(fltrTypeOfProduct == null && fltrProductFeatures!= null){
            if(fltrName == null && fltrExpirationDateSince == null && fltrExpirationDateFor == null && fltrProductionDateSince == null
                    && fltrProductionDateFor == null){
                selectQuery = selectQuery + " WHERE ";
            }
            else{
                selectQuery = selectQuery + " AND ";
            }
            selectQuery = selectQuery + "product_features LIKE '%" + fltrProductFeatures + "%'";
        }
        if(fltrTypeOfProduct != null && fltrProductFeatures != null){
            if(fltrName == null && fltrExpirationDateSince == null && fltrExpirationDateFor == null && fltrProductionDateSince == null
                    && fltrProductionDateFor == null){
                selectQuery = selectQuery + " WHERE ";
            }
            else{
                selectQuery = selectQuery + " AND ";
            }
            selectQuery = selectQuery + "type_of_product LIKE '%" + fltrTypeOfProduct + "%' AND product_features LIKE '%" + fltrProductFeatures + "%'";
        }
        if(fltrVolumeSince >= 0 && fltrVolumeFor == -1){
            if(fltrName == null && fltrExpirationDateSince == null && fltrExpirationDateFor == null && fltrProductionDateSince == null
                    && fltrProductionDateFor == null && fltrTypeOfProduct == null && fltrProductFeatures == null){
                selectQuery = selectQuery + " WHERE ";
            }
            else{
                selectQuery = selectQuery + " AND ";
            }
            selectQuery = selectQuery  + "volume >= '" + fltrVolumeSince + "'";
        }
        if(fltrVolumeSince == -1 && fltrVolumeFor >= 0){
            if(fltrName == null && fltrExpirationDateSince == null && fltrExpirationDateFor == null && fltrProductionDateSince == null
                    && fltrProductionDateFor == null && fltrTypeOfProduct == null && fltrProductFeatures == null){
                selectQuery = selectQuery + " WHERE ";
            }
            else{
                selectQuery = selectQuery + " AND ";
            }
            selectQuery = selectQuery + "volume <= '" + fltrVolumeFor + "'";
        }
        if(fltrVolumeSince >= 0 && fltrVolumeFor >= 0 ){
            if(fltrName == null && fltrExpirationDateSince == null && fltrExpirationDateFor == null && fltrProductionDateSince == null
                    && fltrProductionDateFor == null && fltrTypeOfProduct == null && fltrProductFeatures == null){
                selectQuery = selectQuery + " WHERE ";
            }
            else{
                selectQuery = selectQuery + " AND ";
            }
            selectQuery = selectQuery + "volume BETWEEN '" + fltrVolumeSince  + "' AND '" + fltrVolumeFor + "'";
        }
        if(fltrWeightSince >= 0 && fltrWeightFor == -1){
            if(fltrName == null && fltrExpirationDateSince == null && fltrExpirationDateFor == null && fltrProductionDateSince == null
                    && fltrProductionDateFor == null && fltrTypeOfProduct == null && fltrProductFeatures == null
                    && fltrVolumeSince == -1 && fltrVolumeFor == -1){
                selectQuery = selectQuery + " WHERE ";
            }
            else{
                selectQuery = selectQuery + " AND ";
            }
            selectQuery = selectQuery  + "weight >= '" + fltrWeightSince + "'";
        }
        if(fltrWeightSince == -1 && fltrWeightFor >= 0){
            if(fltrName == null && fltrExpirationDateSince == null && fltrExpirationDateFor == null && fltrProductionDateSince == null
                    && fltrProductionDateFor == null && fltrTypeOfProduct == null && fltrProductFeatures == null
                    && fltrVolumeSince == -1 && fltrVolumeFor == -1){
                selectQuery = selectQuery + " WHERE ";
            }
            else{
                selectQuery = selectQuery + " AND ";
            }
            selectQuery = selectQuery + "weight <= '" + fltrWeightFor + "'";
        }
        if(fltrWeightSince >= 0 && fltrWeightFor >= 0 ){
            if(fltrName == null && fltrExpirationDateSince == null && fltrExpirationDateFor == null && fltrProductionDateSince == null
                    && fltrProductionDateFor == null  && fltrTypeOfProduct == null && fltrProductFeatures == null
                    && fltrVolumeSince == -1 && fltrVolumeFor == -1){
                selectQuery = selectQuery + " WHERE ";
            }
            else{
                selectQuery = selectQuery + " AND ";
            }
            selectQuery = selectQuery + "weight BETWEEN '" + fltrWeightSince  + "' AND '" + fltrWeightFor + "'";
        }
        if(fltrHasSugar >= 0){
            if(fltrName == null && fltrExpirationDateSince == null && fltrExpirationDateFor == null && fltrProductionDateSince == null
                    && fltrProductionDateFor == null && fltrTypeOfProduct == null && fltrProductFeatures == null
                    && fltrVolumeSince == -1 && fltrVolumeFor == -1 && fltrWeightSince == -1 && fltrWeightFor == -1){
                selectQuery = selectQuery + " WHERE ";
            }
            else{
                selectQuery = selectQuery + " AND ";
            }
            selectQuery = selectQuery + "has_sugar='" + fltrHasSugar + "'";
        }
        if(fltrHasSalt >= 0){
            if(fltrName == null && fltrExpirationDateSince == null && fltrExpirationDateFor == null && fltrProductionDateSince == null
                    && fltrProductionDateFor == null && fltrTypeOfProduct == null && fltrProductFeatures == null
                    && fltrVolumeSince == -1 && fltrVolumeFor == -1 && fltrWeightSince == -1 && fltrWeightFor == -1
                    && fltrHasSugar == -1){
                selectQuery = selectQuery + " WHERE ";
            }
            else{
                selectQuery = selectQuery + " AND ";
            }
            selectQuery = selectQuery + "has_salt='" + fltrHasSalt + "'";
        }
        if(fltrTaste != null){
            if(fltrName == null && fltrExpirationDateSince == null && fltrExpirationDateFor == null && fltrProductionDateSince == null
                    && fltrProductionDateFor == null && fltrTypeOfProduct == null && fltrProductFeatures == null
                    && fltrVolumeSince == -1 && fltrVolumeFor == -1 && fltrWeightSince == -1 && fltrWeightFor == -1
                    && fltrHasSugar == -1  && fltrHasSalt == -1){
                selectQuery = selectQuery + " WHERE ";
            }
            else{
                selectQuery = selectQuery + " AND ";
            }
            selectQuery = selectQuery + "taste='" + fltrTaste + "'";
        }
        selectQuery = selectQuery + " ORDER BY expiration_date ASC";
        return selectQuery;
    }

    /**
     * The method to convert date to show in local format.
     *
     * @param dateToConvert date to convert
     * @return              converted date
     */
    public static String convertDate(@NonNull String dateToConvert){
        String[] dateArray = dateToConvert.split("-");
        String convertedDate = "";
        if (dateArray.length > 1)
            convertedDate = dateArray[2] + "." + dateArray[1] + "." + dateArray[0];
        return convertedDate;
    }

    public static String[] splitDate(@NonNull String dateToSplit){
        String[] dateArray = dateToSplit.split("\\.");
        return dateArray;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void applyFilterName(String filterName) {
        navigationView.getMenu().getItem(1).setIcon(R.drawable.ic_check_box_true);
        fltrName = filterName;
        refreshListView();
    }

    @Override
    public void applyFilterExpirationDate(String filterExpirationDateSince, String filterExpirationDateFor) {
        navigationView.getMenu().getItem(2).setIcon(R.drawable.ic_check_box_true);
        fltrExpirationDateSince = filterExpirationDateSince;
        fltrExpirationDateFor   = filterExpirationDateFor;
        refreshListView();
    }

    @Override
    public void applyFilterProductionDate(String filterProductionDateSince, String filterProductionDateFor) {
        navigationView.getMenu().getItem(3).setIcon(R.drawable.ic_check_box_true);
        fltrProductionDateSince = filterProductionDateSince;
        fltrProductionDateFor   = filterProductionDateFor;
        refreshListView();
    }

    @Override
    public void applyFilterTypeOfProduct( String filterTypeOfProduct, String filterProductFeatures) {
        navigationView.getMenu().getItem(4).setIcon(R.drawable.ic_check_box_true);
        fltrTypeOfProduct   = filterTypeOfProduct;
        fltrProductFeatures = filterProductFeatures;
        refreshListView();
    }

    @Override
    public void applyFilterVolume(int filterVolumeSince, int filterVolumeFor) {
        navigationView.getMenu().getItem(5).setIcon(R.drawable.ic_check_box_true);
        fltrVolumeSince = filterVolumeSince;
        fltrVolumeFor   = filterVolumeFor;
        refreshListView();
    }

    @Override
    public void applyFilterWeight(int filterWeightSince, int filterWeightFor) {
        navigationView.getMenu().getItem(6).setIcon(R.drawable.ic_check_box_true);
        fltrWeightSince = filterWeightSince;
        fltrWeightFor = filterWeightFor;
        refreshListView();
    }

    @Override
    public void applyFilterTaste(String filterTaste) {
        navigationView.getMenu().getItem(7).setIcon(R.drawable.ic_check_box_true);
        fltrTaste = filterTaste;
        refreshListView();
    }

    @Override
    public void applyProductFeatures(int filterHasSugar, int filterHasSalt) {
        navigationView.getMenu().getItem(8).setIcon(R.drawable.ic_check_box_true);
        fltrHasSugar = filterHasSugar;
        fltrHasSalt  = filterHasSalt;
        refreshListView();
    }

    @Override
    public void clearFilterName() {
        navigationView.getMenu().getItem(1).setIcon(R.drawable.ic_check_box_false);
        fltrName = null;
        refreshListView();
    }

    @Override
    public void clearFilterExpirationDate() {
        navigationView.getMenu().getItem(2).setIcon(R.drawable.ic_check_box_false);
        fltrExpirationDateSince = null;
        fltrExpirationDateFor   = null;
        refreshListView();
    }

    @Override
    public void clearFilterProductionDate() {
        navigationView.getMenu().getItem(3).setIcon(R.drawable.ic_check_box_false);
        fltrProductionDateSince = null;
        fltrProductionDateFor   = null;
        refreshListView();
    }

    @Override
    public void clearFilterTypeOfProduct() {
        navigationView.getMenu().getItem(4).setIcon(R.drawable.ic_check_box_false);
        fltrTypeOfProduct   = null;
        fltrProductFeatures = null;
        refreshListView();
    }

    @Override
    public void clearFilterVolume() {
        navigationView.getMenu().getItem(5).setIcon(R.drawable.ic_check_box_false);
        fltrVolumeSince = -1;
        fltrVolumeFor   = -1;
        refreshListView();
    }

    @Override
    public void clearFilterWeight() {
        navigationView.getMenu().getItem(6).setIcon(R.drawable.ic_check_box_false);
        fltrWeightSince = -1;
        fltrWeightFor   = -1;
        refreshListView();
    }

    @Override
    public void clearFilterTaste() {
        navigationView.getMenu().getItem(7).setIcon(R.drawable.ic_check_box_false);
        fltrTaste = null;
        refreshListView();
    }

    @Override
    public void clearProductFeatures() {
        navigationView.getMenu().getItem(8).setIcon(R.drawable.ic_check_box_false);
        fltrHasSugar = -1;
        fltrHasSalt  = -1;
        refreshListView();
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
}