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
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hermanowicz.pantry.presenters.ProductDetailsActivityPresenter;
import com.hermanowicz.pantry.views.ProductDetailsActivityView;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <h1>ProductDetailsActivity</h1>
 * Activity with details of product. Is opened after choose a product in MyPantryActivity or after
 * scanning the QR code of product.
 *
 * @author  Mateusz Hermanowicz
 * @version 1.0
 * @since   1.0
 */
public class ProductDetailsActivity extends AppCompatActivity implements ProductDetailsActivityView {

    private Context context;
    private Resources resources;
    private DatabaseManager db;
    private Product selectedProduct;
    private ProductDetailsActivityPresenter presenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.text_productTypeValue)
    TextView typeOfProduct;
    @BindView(R.id.text_productFeaturesValue)
    TextView productFeatures;
    @BindView(R.id.text_productExpirationDateValue)
    TextView expirationDate;
    @BindView(R.id.text_productProductionDateValue)
    TextView productionDate;
    @BindView(R.id.text_productCompositionValue)
    TextView composition;
    @BindView(R.id.text_productHealingPropertiesValue)
    TextView healingProperties;
    @BindView(R.id.text_productDosageValue)
    TextView dosage;
    @BindView(R.id.text_productVolumeValue)
    TextView volume;
    @BindView(R.id.text_productWeightValue)
    TextView weight;
    @BindView(R.id.text_productHasSugarValue)
    TextView hasSugar;
    @BindView(R.id.text_productHasSaltValue)
    TextView hasSalt;
    @BindView(R.id.text_productTasteValue)
    TextView taste;
    @BindView(R.id.button_printQRCode)
    Button button_printQRCode;
    @BindView(R.id.button_deleteProduct)
    Button button_deleteProduct;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        ButterKnife.bind(this);

        presenter = new ProductDetailsActivityPresenter(this,null);

        Intent myPantryActivityIntent = getIntent();
        int productID = myPantryActivityIntent.getIntExtra("product_id", 1);

        int hashCode;
        try {
            hashCode = myPantryActivityIntent.getIntExtra("hash_code", 0);
        }
        catch (NumberFormatException n){
            hashCode = 0;
        }

        context = ProductDetailsActivity.this;
        resources = context.getResources();
        db = new DatabaseManager(context);
        selectedProduct = db.getProductsFromDB("SELECT * FROM 'products' DESC").get(productID);

        setSupportActionBar(toolbar);

        if (hashCode == Integer.parseInt(selectedProduct.getHashCode())) {
            Objects.requireNonNull(getSupportActionBar()).setTitle(selectedProduct.getName());
            typeOfProduct.setText(selectedProduct.getTypeOfProduct());
            productFeatures.setText(selectedProduct.getProductFeatures());
            expirationDate.setText(MyPantryActivity.convertDate(selectedProduct.getExpirationDate()));
            productionDate.setText(MyPantryActivity.convertDate(selectedProduct.getProductionDate()));
            composition.setText(selectedProduct.getComposition());
            healingProperties.setText(selectedProduct.getHealingProperties());
            dosage.setText(selectedProduct.getDosage());
            volume.setText(selectedProduct.getVolume() + resources.getString(R.string.ProductDetailsActivity_volume_unit));
            weight.setText(selectedProduct.getWeight() + resources.getString(R.string.ProductDetailsActivity_weight_unit));
            taste.setText(selectedProduct.getTaste());
            if (selectedProduct.getHasSugar() == 1)
                hasSugar.setText(resources.getString(R.string.ProductDetailsActivity_yes));
            else
                hasSugar.setText(resources.getString(R.string.ProductDetailsActivity_no));
            if (selectedProduct.getHasSalt() == 1)
                hasSalt.setText(resources.getString(R.string.ProductDetailsActivity_yes));
            else
                hasSalt.setText(resources.getString(R.string.ProductDetailsActivity_no));
        } else {
            Toast.makeText(context, resources.getString(R.string.Errors_wrong_data), Toast.LENGTH_LONG).show();
        }

        button_printQRCode.setOnClickListener(view -> {
            try {
                ArrayList<Product> productArrayList = new ArrayList<>();
                productArrayList.add(selectedProduct);
                startActivity(PrintQRCodesActivity.createPrintQRCodesActivityIntent(context, productArrayList));
                finish();
            } catch (Exception e) {
                Toast.makeText(context, resources.getString(R.string.Errors_wrong_data), Toast.LENGTH_LONG).show();
            }
        });

        button_deleteProduct.setOnClickListener(view -> {
            if (db.deleteProductFromDB(selectedProduct.getID())) {
                Notification.cancelNotification(context, selectedProduct);
                Toast.makeText(context, resources.getString(R.string.ProductDetailsActivity_product_has_been_removed), Toast.LENGTH_LONG).show();
                Intent myPantryActivityIntent1 = new Intent(context, MyPantryActivity.class);
                startActivity(myPantryActivityIntent1);
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent myPantryActivityIntent = new Intent(context, MyPantryActivity.class);
            startActivity(myPantryActivityIntent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}