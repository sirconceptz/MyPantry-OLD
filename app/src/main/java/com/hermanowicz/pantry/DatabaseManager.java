/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import androidx.annotation.NonNull;

import com.hermanowicz.pantry.models.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>DatabaseManager</h1>
 * Adapter for database. All methods and data for database support. In database stored are
 * all products with details.
 *
 * @author  Mateusz Hermanowicz
 * @version 1.0
 * @since   1.0
 */
public class DatabaseManager extends SQLiteOpenHelper {
    private static final int    DB_VERSION                 = 1;
    private static final String DB_NAME                    = "database.db";
    private static final String DB_PRODUCTS_TABLE          = "products";

    private static final String KEY_ID                     = "_id";
    private static final String ID_OPTIONS                 = "INTEGER PRIMARY KEY AUTOINCREMENT";
    private static final String KEY_NAME                   = "name";
    private static final String NAME_OPTIONS               = "TEXT NOT NULL";
    private static final String KEY_HASHCODE               = "hashcode";
    private static final String HASHCODE_OPTIONS           = "INTEGER NOT NULL";
    private static final String KEY_TYPE_OF_PRODUCT        = "type_of_product";
    private static final String TYPE_OF_PRODUCT_OPTIONS    = "TEXT NOT NULL";
    private static final String KEY_PRODUCT_FEATURES       = "product_features";
    private static final String PRODUCT_FEATURES_OPTIONS   = "TEXT";
    private static final String KEY_EXPIRATION_DATE        = "expiration_date";
    private static final String EXPIRATION_DATE_OPTIONS    = "DATE NOT NULL";
    private static final String KEY_PRODUCTION_DATE        = "production_date";
    private static final String PRODUCTION_DATE_OPTIONS    = "DATE";
    private static final String KEY_COMPOSITION            = "composition";
    private static final String COMPOSITION_OPTIONS        = "TEXT";
    private static final String KEY_HEALING_PROPERTIES     = "healing_properties";
    private static final String HEALING_PROPERTIES_OPTIONS = "TEXT";
    private static final String KEY_DOSAGE                 = "dosage";
    private static final String DOSAGE_OPTIONS             = "TEXT";
    private static final String KEY_VOLUME                 = "volume";
    private static final String VOLUME_OPTIONS             = "INTEGER";
    private static final String KEY_WEIGHT                 = "weight";
    private static final String WEIGHT_OPTIONS             = "INTEGER";
    private static final String KEY_HAS_SUGAR              = "has_sugar";
    private static final String HAS_SUGAR_OPTIONS          = "INTEGER";
    private static final String KEY_HAS_SALT               = "has_salt";
    private static final String HAS_SALT_OPTIONS           = "INTEGER";
    private static final String KEY_TASTE                  = "taste";
    private static final String TASTE_OPTIONS              = "TEXT";

    private static final String DB_CREATE_PRODUCTS_TABLE =
            "CREATE TABLE IF NOT EXISTS " + DB_PRODUCTS_TABLE + "( " +
                    KEY_ID + " " + ID_OPTIONS + ", " +
                    KEY_NAME + " " + NAME_OPTIONS + ", " +
                    KEY_HASHCODE + " " + HASHCODE_OPTIONS + ", " +
                    KEY_TYPE_OF_PRODUCT + " " + TYPE_OF_PRODUCT_OPTIONS + ", " +
                    KEY_PRODUCT_FEATURES + " " + PRODUCT_FEATURES_OPTIONS + ", " +
                    KEY_EXPIRATION_DATE + " " + EXPIRATION_DATE_OPTIONS + ", " +
                    KEY_PRODUCTION_DATE + " " + PRODUCTION_DATE_OPTIONS + ", " +
                    KEY_COMPOSITION + " " + COMPOSITION_OPTIONS + ", " +
                    KEY_HEALING_PROPERTIES + " " + HEALING_PROPERTIES_OPTIONS + ", " +
                    KEY_DOSAGE + " " + DOSAGE_OPTIONS + ", " +
                    KEY_VOLUME + " " + VOLUME_OPTIONS + ", " +
                    KEY_WEIGHT + " " + WEIGHT_OPTIONS + ", " +
                    KEY_HAS_SUGAR + " " + HAS_SUGAR_OPTIONS + ", " +
                    KEY_HAS_SALT + " " + HAS_SALT_OPTIONS + ", " +
                    KEY_TASTE + " " + TASTE_OPTIONS +
                    ");";

    private static final String DROP_PRODUCTS_TABLE =
            "DROP TABLE IF EXISTS " + DB_PRODUCTS_TABLE;

    public DatabaseManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public void recreateDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(DROP_PRODUCTS_TABLE);
        onCreate(db);
        db.close();
    }

    boolean deleteProductFromDB(int productID){
        boolean result;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String[] product = {String.valueOf(productID)};
            result = db.delete(DB_PRODUCTS_TABLE, KEY_ID + " =?", product) > 0;
            db.close();
        }
        catch (Exception e){
            result=false;
            Log.d("DatabaseManager","Error while deleting product from database.");
        }
        return result;
    }

    boolean insertProductToDB(@NonNull Product product) {
        long result = 0;
        try{
            SQLiteDatabase db = this.getWritableDatabase();

            String sql = "INSERT INTO " + DB_PRODUCTS_TABLE + " VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            SQLiteStatement statement = db.compileStatement(sql);

            statement.clearBindings();
            statement.bindString(1, product.getName());
            statement.bindLong  (2, product.hashCode());
            statement.bindString(3, product.getTypeOfProduct());
            statement.bindString(4, product.getProductFeatures());
            statement.bindString(5, product.getExpirationDate());
            statement.bindString(6, product.getProductionDate());
            statement.bindString(7, product.getComposition());
            statement.bindString(8, product.getHealingProperties());
            statement.bindString(9, product.getDosage());
            statement.bindLong  (10, product.getVolume());
            statement.bindLong  (11, product.getWeight());
            statement.bindLong  (12, product.getHasSugar());
            statement.bindLong  (13, product.getHasSalt());
            statement.bindString(14, product.getTaste());
            result = statement.executeInsert();
            db.close();
        }
        catch (Exception e){
            Log.d("DatabaseManager", "Error while inserting product to database.");
            result = 0;
        }
        return result != -1;
    }

    List<Product> getProductsFromDB(@NonNull String selectQuery) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Product> products = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    Product newProduct = new Product.Builder()
                            .setID(cursor.getInt(cursor.getColumnIndex(KEY_ID)))
                            .setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)))
                            .setHashCode(cursor.getString(cursor.getColumnIndex(KEY_HASHCODE)))
                            .setTypeOfProduct(cursor.getString(cursor.getColumnIndex(KEY_TYPE_OF_PRODUCT)))
                            .setProductFeatures(cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_FEATURES)))
                            .setExpirationDate(cursor.getString(cursor.getColumnIndex(KEY_EXPIRATION_DATE)))
                            .setProductionDate(cursor.getString(cursor.getColumnIndex(KEY_PRODUCTION_DATE)))
                            .setComposition(cursor.getString(cursor.getColumnIndex(KEY_COMPOSITION)))
                            .setHealingProperties(cursor.getString(cursor.getColumnIndex(KEY_HEALING_PROPERTIES)))
                            .setDosage(cursor.getString(cursor.getColumnIndex(KEY_DOSAGE)))
                            .setVolume(cursor.getInt(cursor.getColumnIndex(KEY_VOLUME)))
                            .setWeight(cursor.getInt(cursor.getColumnIndex(KEY_WEIGHT)))
                            .setHasSugar(cursor.getInt(cursor.getColumnIndex(KEY_HAS_SUGAR)))
                            .setHasSalt(cursor.getInt(cursor.getColumnIndex(KEY_HAS_SALT)))
                            .setTaste(cursor.getString(cursor.getColumnIndex(KEY_TASTE)))
                            .createProduct();

                    products.add(newProduct);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("DatabaseManager", "Error while trying to get product from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        db.close();
        return products;
    }

    int idOfLastProductInDB(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM 'products'", null);
        cursor.moveToLast();
        int id = 0;
        try{
            id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
        }
        catch (CursorIndexOutOfBoundsException e){
            id = 0;
        }
        cursor.close();
        return id;
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        db.execSQL(DB_CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_PRODUCTS_TABLE);
        onCreate(db);
    }
}