/*
 * Copyright (c) 2019-2022
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
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.databinding.ActivityScanProductBinding;
import com.hermanowicz.pantry.db.product.Product;
import com.hermanowicz.pantry.db.product.ProductDb;
import com.hermanowicz.pantry.interfaces.ScanProductView;
import com.hermanowicz.pantry.presenter.ScanProductPresenter;
import com.hermanowicz.pantry.util.Orientation;
import com.hermanowicz.pantry.util.ThemeMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import maes.tech.intentanim.CustomIntent;

/**
 * <h1>ScanProductActivity/h1>
 * Activity to scan QR code. Uses camera and zxing library.
 *
 * @author Mateusz Hermanowicz
 */

public class ScanProductActivity extends AppCompatActivity implements ScanProductView {

    private final String TAG = "ProductsRxJava";

    private ScanProductPresenter presenter;
    private Context context;
    private final CompositeDisposable disposables = new CompositeDisposable();

    private CardView scanBarcode;
    private CardView scanQrCode;
    private CardView enterBarcodeManually;

    static final int VIBRATE_DURATION = 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(ThemeMode.getThemeMode(this));
        if (Orientation.isTablet(this))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        super.onCreate(savedInstanceState);
        initView();
        setListeners();
        setProductListObserver();
    }

    private void initView() {
        ActivityScanProductBinding binding = ActivityScanProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        scanBarcode = findViewById(R.id.cardview_scanBarCode);
        scanQrCode = findViewById(R.id.cardview_scanQrCode);
        enterBarcodeManually = findViewById(R.id.cardview_enterBarcodeManually);

        context = getApplicationContext();

        presenter = new ScanProductPresenter(this, PreferenceManager.getDefaultSharedPreferences(context), ProductDb.getInstance(context));

        if(presenter.isOfflineDb())
            presenter.setOfflineAllProductList();

        List<Product> productListToAddBarcode = (List<Product>) getIntent().getSerializableExtra("product_list_to_add_barcode");
        if(productListToAddBarcode != null) {
            presenter.addBarcodeToProductList(productListToAddBarcode, getResources());
        }
    }

    private void setListeners() {
        Resources resources = getResources();
        scanBarcode.setOnClickListener(click -> presenter.initScanner(true, resources));
        scanQrCode.setOnClickListener(click -> presenter.initScanner(false, resources));
        enterBarcodeManually.setOnClickListener(click -> presenter.enterBarcodeManually());
    }

    private Observable<List<Product>> productList() {
        return Observable.create(emitter -> {
            String user = FirebaseAuth.getInstance().getUid();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            assert user != null;
            Query query = database.getReference().child("products").child(user);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<Product> list = new ArrayList<>();
                    Iterable<DataSnapshot> snapshotIterable = snapshot.getChildren();

                    for (DataSnapshot dataSnapshot : snapshotIterable) {
                        Product product = dataSnapshot.getValue(Product.class);
                        list.add(product);
                    }
                    emitter.onNext(list);
                    onProductResponse(list);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    emitter.onError(new FirebaseException(error.getMessage()));
                }
            });
        });
    }

    private void setProductListObserver() {
        if (!presenter.isOfflineDb()) {
            disposables.add(productList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<List<Product>>() {
                        @Override
                        public void onComplete() {
                            Log.d(TAG, "onComplete()");
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            Log.e(TAG, "onError()", e);
                        }

                        @Override
                        public void onNext(@NonNull List<Product> productList) {
                            Log.i(TAG, "onNext()");
                        }
                    }));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null)
            if (result.getContents() == null) {
                presenter.showErrorProductNotFound();
                presenter.navigateToMainActivity();
            } else {
                presenter.onScanResult(result.getContents());
            }
        else
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showErrorProductNotFound() {
        Toast.makeText(context, getString(R.string.ScanProductActivity_product_not_found), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onVibration() {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && vibrator != null)
            vibrator.vibrate(VibrationEffect.createOneShot(VIBRATE_DURATION, VibrationEffect.DEFAULT_AMPLITUDE));
    }

    @Override
    public void setScanner(boolean scannerSoundMode, String message) {
        int cameraId = presenter.getSelectedCamera(); //0 - Rear camera, 1 - Front camera
        IntentIntegrator qrCodeScanner = new IntentIntegrator(this);
        qrCodeScanner.setPrompt(message);
        qrCodeScanner.setOrientationLocked(true);
        qrCodeScanner.setBeepEnabled(scannerSoundMode);
        qrCodeScanner.setCameraId(cameraId);
        qrCodeScanner.initiateScan();
    }

    @Override
    public void navigateToProductDetailsActivity(List<Integer> decodedScanResultAsList, List<Product> productList) {
        Intent productDetailsIntent = new Intent(context, ProductDetailsActivity.class);
        productDetailsIntent.putExtra("product_id", decodedScanResultAsList.get(0));
        productDetailsIntent.putExtra("product_list", (Serializable) productList);
        productDetailsIntent.putExtra("hash_code", String.valueOf(decodedScanResultAsList.get(1)));
        startActivity(productDetailsIntent);
        CustomIntent.customType(this, "fadein-to-fadeout");
    }

    @Override
    public void navigateToMainActivity() {
        Intent mainActivityIntent = new Intent(context, MainActivity.class);
        startActivity(mainActivityIntent);
        CustomIntent.customType(this, "fadein-to-fadeout");
    }

    @Override
    public void navigateToNewProductActivity(@NonNull String barcode, @Nullable List<Product> productList) {
        Intent newProductIntent = new Intent(context, NewProductActivity.class);
        newProductIntent.putExtra("barcode", barcode);
        newProductIntent.putExtra("product_list", (Serializable) productList);
        startActivity(newProductIntent);
        CustomIntent.customType(this, "fadein-to-fadeout");
    }

    @Override
    public void navigateToNewProductActivity(Product product) {
        Intent intent = new Intent(context, NewProductActivity.class)
                .putExtra("product", product);
        startActivity(intent);
        CustomIntent.customType(this, "fadein-to-fadeout");
    }

    @Override
    public void onSelectedEnterBarcodeManually() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppThemeDialog);
        builder.setTitle(getString(R.string.ScanProductActivity_enter_barcode_manually));
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input)
                .setPositiveButton(getString(R.string.General_save), (dialog, click) -> {
                    String barcode = input.getText().toString();
                    presenter.onSetBarcodeManually(barcode);
                })
                .setNegativeButton(getString(R.string.General_cancel), (dialog, click) -> dialog.cancel())
                .show();
    }

    @Override
    public void showErrorBarcodeIsIncorrect() {
        Toast.makeText(context, R.string.Error_incorrect_barcode_value, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showIsBarcodeUpdated() {
        Toast.makeText(context, R.string.ScanProductActivity_barcode_is_updated, Toast.LENGTH_LONG).show();
    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this, "fadein-to-fadeout");
    }

    public void onProductResponse(List<Product> productList) {
        presenter.setProductList(productList);
    }
}