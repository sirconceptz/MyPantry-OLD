/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <h1>MyPantryActivity</h1>
 * Activity for My Pantry. In this activity is available the list view with products from database.
 * The user can filter search results using product attributes.
 *
 * @author  Mateusz Hermanowicz
 * @version 1.0
 * @since   1.0
 */
public class MyPantryActivity extends AppCompatActivity implements DialogManager.DialogListener {

    private Context          context;
    private DatabaseManager  db;
    private SimpleDateFormat simpleDateFormat;
    private List<Product>    productsFromDB;
    private TextView         emptyPantryStatement;
    private DrawerLayout     drawerLayout;
    private NavigationView   navigationView;
    private ListViewAdapter  listViewAdapter;
    private String           fltrName, fltrExpirationDateSince, fltrExpirationDateFor,
            fltrProductionDateSince, fltrProductionDateFor, fltrTypeOfProduct,
            fltrProductFeatures, fltrTaste, type_of_dialog;
    private int              fltrWeightSince = -1, fltrWeightFor = -1, fltrVolumeSince = -1,
            fltrVolumeFor = -1, fltrHasSugar = -1, fltrHasSalt = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_pantry_drawer_layout);

        context                   = getApplicationContext();
        db                        = new DatabaseManager(context);
        simpleDateFormat          = new SimpleDateFormat("yyyy-MM-dd");
        productsFromDB            = db.getProductsFromDB(buildPantryQuery());
        listViewAdapter           = new ListViewAdapter();
        drawerLayout              = findViewById(R.id.my_pantry_drawer_layout);
        navigationView            = findViewById(R.id.nav_view);
        emptyPantryStatement      = findViewById(R.id.EmptyPantryStatement);
        Toolbar  toolbar          = findViewById(R.id.Toolbar);
        ListView listViewProducts = findViewById(R.id.ListViewProducts);

        listViewProducts.setAdapter(listViewAdapter);
        listViewAdapter.notifyDataSetChanged();

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        if(productsFromDB.size()==0){
            emptyPantryStatement.setText(getResources().getString(R.string.MyPantryActivity_empty_pantry));
        }

        listViewProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent productDetailsActivityIntent = new Intent(context, ProductDetailsActivity.class);
                int productID = position + 1;
                productDetailsActivityIntent.putExtra("product_id", productID);
                Product  selectedProduct = productsFromDB.get(position);
                productDetailsActivityIntent.putExtra("hash_code", Integer.parseInt(selectedProduct.getHashCode()));
                startActivity(productDetailsActivityIntent);
                finish();
            }
        });

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        type_of_dialog = menuItem.getTitle().toString();
                        if (type_of_dialog == getResources().getString(R.string.MyPantryActivity_clear_filters)){
                            clearFilters();
                        }
                        else{
                            openDialog(type_of_dialog);
                        }
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    /**
     * The method to open dialog window to input search filters.
     *
     * @param dialogType value of dialog type
     */
    public void openDialog(String dialogType){

        DialogManager dialog = new DialogManager();

        if(dialogType.equals(getResources().getString(R.string.ProductDetailsActivity_name))) {
            if (fltrName != null) {
                dialog.setFilterName(fltrName);
            }
            else {
                dialog.setFilterName(null);
            }
        }
        else if(dialogType.equals(getResources().getString(R.string.ProductDetailsActivity_expiration_date))) {
            if (fltrExpirationDateSince != null || fltrExpirationDateFor != null) {
                dialog.setFilterExpirationDate(fltrExpirationDateSince, fltrExpirationDateFor);
            } else {
                dialog.setFilterExpirationDate(null, null);
            }
        }
        else if(dialogType.equals(getResources().getString(R.string.ProductDetailsActivity_production_date))) {
            if (fltrProductionDateSince != null || fltrProductionDateFor != null) {
                dialog.setFilterProductionDate(fltrProductionDateSince, fltrProductionDateFor);
            } else {
                dialog.setFilterProductionDate(null, null);
            }
        }
        else if(dialogType.equals(getResources().getString(R.string.ProductDetailsActivity_product_type))) {
            if (fltrTypeOfProduct != null || fltrProductFeatures != null) {
                dialog.setFilterTypeOfProduct(fltrTypeOfProduct, fltrProductFeatures);
            } else {
                dialog.setFilterTypeOfProduct(null, null);
            }
        }
        else if(dialogType.equals(getResources().getString(R.string.ProductDetailsActivity_volume))) {
            if (fltrVolumeSince >= 0 || fltrVolumeFor >= 0) {
                dialog.setFilterVolume(fltrVolumeSince, fltrVolumeFor);
            } else {
                dialog.setFilterVolume(-1, -1);
            }
        }
        else if(dialogType.equals(getResources().getString(R.string.ProductDetailsActivity_weight))) {
            if (fltrWeightSince >= 0 || fltrWeightFor >= 0) {
                dialog.setFilterWeight(fltrWeightSince, fltrWeightFor);
            } else {
                dialog.setFilterWeight(-1, -1);
            }
        }
        else if(dialogType.equals(getResources().getString(R.string.ProductDetailsActivity_taste))) {
            if (fltrTaste != null) {
                dialog.setFilterTaste(fltrTaste);
            } else {
                dialog.setFilterTaste(null);
            }
        }
        else if(dialogType.equals(getResources().getString(R.string.ProductDetailsActivity_product_features))) {
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
        if(emptyPantryStatement.getText().toString() == getResources().getString(R.string.MyPantryActivity_empty_pantry)){}
        else if(productsFromDB.size()==0){
            emptyPantryStatement.setText(getResources().getString(R.string.MyPantryActivity_not_found));
        }
        else{
            emptyPantryStatement.setText("");
        }
        listViewAdapter.notifyDataSetChanged();
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
    public void applyFilterName(@NonNull String filterName) {
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
    public void applyFilterTypeOfProduct(@NonNull String filterTypeOfProduct, String filterProductFeatures) {
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
    public void applyFilterTaste(@NonNull String filterTaste) {
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

    /**
     * <h1>ListViewAdapter</h1>
     *
     *
     * @author  Mateusz Hermanowicz
     * @version 1.0
     * @since   1.0
     */
    class ListViewAdapter extends BaseAdapter {

        /**
         *
         *
         * @return size of list with products selected from database
         */
        @Override
        public int getCount() {
            return productsFromDB.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        /**
         *
         * @return
         */
        @SuppressLint("ViewHolder")
        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.list_view_products, null);

            TextView nameTv            = view.findViewById(R.id.ProductNameValue);
            TextView volumeTv          = view.findViewById(R.id.ProductVolume);
            TextView weightTv          = view.findViewById(R.id.ProductWeight);
            TextView expirationDateTv  = view.findViewById(R.id.ProductExpirationDate);

            Product  selectedProduct      = productsFromDB.get(position);
            Calendar calendar             = Calendar.getInstance();
            Date     expirationDateDt     = calendar.getTime();
            String   volumeString         = getResources().getString(R.string.ProductDetailsActivity_volume) + ": " +  selectedProduct.getVolume() + getResources().getString(R.string.ProductDetailsActivity_volume_unit);
            String   weightString         = getResources().getString(R.string.ProductDetailsActivity_weight) + ": " +  selectedProduct.getWeight() + getResources().getString(R.string.ProductDetailsActivity_weight_unit);
            String   expirationDateString = convertDate(selectedProduct.getExpirationDate());

            nameTv.setText(selectedProduct.getName());
            volumeTv.setText(volumeString);
            weightTv.setText(weightString);
            expirationDateTv.setText(expirationDateString);

            try {
                expirationDateDt = simpleDateFormat.parse(selectedProduct.getExpirationDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calendar.add(Calendar.DAY_OF_MONTH, AppSettingsActivity.getDaysBeforeNotificationFromSettings(context));
            Date dayOfNotification = calendar.getTime();
            if (dayOfNotification.after(expirationDateDt))
                view.setBackgroundColor(getResources().getColor(R.color.background_expired_products));

            return view;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent mainActivityIntent = new Intent(context, MainActivity.class);
            startActivity(mainActivityIntent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}