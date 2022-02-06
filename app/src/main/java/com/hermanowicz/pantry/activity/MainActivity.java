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
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.preference.PreferenceManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.FirebaseException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.databinding.ActivityMainBinding;
import com.hermanowicz.pantry.db.product.Product;
import com.hermanowicz.pantry.db.product.ProductDb;
import com.hermanowicz.pantry.dialog.AuthorDialog;
import com.hermanowicz.pantry.interfaces.AccountView;
import com.hermanowicz.pantry.interfaces.ErrorAndMaintanceWorkJsonPlaceHolder;
import com.hermanowicz.pantry.interfaces.MainView;
import com.hermanowicz.pantry.model.WPError;
import com.hermanowicz.pantry.presenter.MainPresenter;
import com.hermanowicz.pantry.util.DateHelper;
import com.hermanowicz.pantry.util.Orientation;
import com.hermanowicz.pantry.util.PremiumAccess;
import com.hermanowicz.pantry.util.ThemeMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import maes.tech.intentanim.CustomIntent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * <h1>MainActivity</h1>
 * Main activity - main window of the application.
 *
 * @author Mateusz Hermanowicz
 */

public class MainActivity extends AppCompatActivity implements MainView, AccountView, Callback<List<WPError>> {

    private final String TAG = "ProductsRxJava";
    private final String ANIM_TYPE = "fadein-to-fadeout";

    private final CompositeDisposable disposables = new CompositeDisposable();
    private MainPresenter presenter;
    private Context context;

    private CardView myPantry;
    private CardView scanProduct;
    private CardView newProduct;
    private CardView ownCategories;
    private CardView storageLocations;
    private CardView appSettings;
    private TextView loggedUser;
    private ImageView authorInfo;
    private AdView adView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(ThemeMode.getThemeMode(this));
        if (Orientation.isTablet(this))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        super.onCreate(savedInstanceState);
        initView();
        setListeners();
        initRetrofit();
    }

    private void initView() {
        com.hermanowicz.pantry.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = getApplicationContext();

        MobileAds.initialize(context);

        adView = binding.adview;
        authorInfo = binding.authorInfo;
        myPantry = binding.myPantryCV;
        scanProduct = binding.scanProductCV;
        newProduct = binding.newProductCV;
        ownCategories = binding.ownCategoriesCV;
        storageLocations = binding.storageLocationsCV;
        appSettings = binding.appSettingsCV;
        loggedUser = binding.loggedUser;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        presenter = new MainPresenter(this, this, sharedPreferences);
        presenter.setPremiumAccess(new PremiumAccess(context));
        presenter.updateUserData();

        if (!presenter.isPremium()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }

        if (presenter.isOfflineDb()) {
            ProductDb db = ProductDb.getInstance(context);
            List<Product> productList = db.productsDao().getAllProductsList();
            presenter.restoreNotifications(context, productList);
        } else
            setProductListObserver();
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

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.mypantry.eu/wp-json/wp/v2/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        ErrorAndMaintanceWorkJsonPlaceHolder errorAndMaintanceWorkJsonPlaceHolder
                = retrofit.create(ErrorAndMaintanceWorkJsonPlaceHolder.class);

        Call<List<WPError>> call = errorAndMaintanceWorkJsonPlaceHolder
                .getErrorsAndMaintanceWork();

        call.enqueue(this);
    }

    private Observable<List<Product>> productList() {
        return Observable.create(emitter -> {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            Query query = database.getReference().child("products");
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

    private void setListeners() {
        myPantry.setOnClickListener(view -> presenter.navigateToMyPantryActivity());
        scanProduct.setOnClickListener(view -> presenter.navigateToScanProductActivity());
        newProduct.setOnClickListener(view -> presenter.navigateToNewProductActivity());
        ownCategories.setOnClickListener(view -> presenter.navigateToCategoriesActivity());
        storageLocations.setOnClickListener(view -> presenter.navigateToStorageLocationsActivity());
        appSettings.setOnClickListener(view -> presenter.navigateToAppSettingsActivity());
        authorInfo.setOnClickListener(view -> presenter.showAuthorInfoDialog());
    }

    @Override
    public void onResponse(Call<List<WPError>> call, Response<List<WPError>> response) {
        StringBuilder retrofitResponse = new StringBuilder();
        if (!response.isSuccessful()) {
            Toast.makeText(context, "Code: " + response.code(), Toast.LENGTH_LONG).show();
        }
        List<WPError> errors = response.body();

        for (WPError error : errors) {
            String content = "";
            String date = DateHelper.getFullTimestampInLocalFormat(error.getDate());
            content += getString(R.string.ErrorActivity_title) +
                    ": " + error.getTitle().getRendered() + "\n";
            content += getString(R.string.ErrorActivity_date) +
                    ": " + date + "\n";
            content += getString(R.string.ErrorActivity_content) +
                    ": " + Html.fromHtml(error.getContent().getRendered()) + "\n\n";

            retrofitResponse.append(content);
            String responseString = retrofitResponse.toString();
            presenter.setFoundError(responseString);
        }
    }

    @Override
    public void onFailure(Call<List<WPError>> call, Throwable t) {
        Toast.makeText(context, "Code: " + t.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNavigationToMyPantryActivity() {
        Intent myPantryActivityIntent = new Intent(MainActivity.this, MyPantryActivity.class);
        startActivity(myPantryActivityIntent);
        CustomIntent.customType(this, ANIM_TYPE);
    }

    @Override
    public void onNavigationToScanProductActivity() {
        Intent scanProductActivityIntent = new Intent(MainActivity.this, ScanProductActivity.class);
        startActivity(scanProductActivityIntent);
        CustomIntent.customType(this, ANIM_TYPE);
    }

    @Override
    public void onNavigationToNewProductActivity() {
        Intent newProductActivityIntent = new Intent(MainActivity.this, NewProductActivity.class);
        startActivity(newProductActivityIntent);
        CustomIntent.customType(this, ANIM_TYPE);
    }

    @Override
    public void onNavigationToCategoriesActivity() {
        Intent categoriesActivity = new Intent(MainActivity.this, CategoriesActivity.class);
        startActivity(categoriesActivity);
        CustomIntent.customType(this, ANIM_TYPE);
    }

    @Override
    public void onNavigationToStorageLocationsActivity() {
        Intent storageLocationsActivity = new Intent(MainActivity.this, StorageLocationsActivity.class);
        startActivity(storageLocationsActivity);
        CustomIntent.customType(this, ANIM_TYPE);
    }

    @Override
    public void onNavigationToAppSettingsActivity() {
        Intent appSettingsActivityIntent = new Intent(MainActivity.this, AppSettingsActivity.class);
        startActivity(appSettingsActivityIntent);
        CustomIntent.customType(this, ANIM_TYPE);
    }

    @Override
    public void showAuthorInfoDialog() {
        AuthorDialog authorDialog = new AuthorDialog();
        authorDialog.show(getSupportFragmentManager(), "");
    }

    @Override
    public void onNavigationToErrorActivity(String responseString) {
        Intent errorActivity = new Intent(MainActivity.this, ErrorActivity.class);
        errorActivity.putExtra("response", responseString);
        startActivity(errorActivity);
        CustomIntent.customType(this, ANIM_TYPE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        long pressedTime = presenter.getPressedBackTime();
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.General_press_back_agait_to_exit), Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
        presenter.setPressedBackTime(pressedTime);
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int RC_SIGN_IN = 10;
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                presenter.updateUserData();
            }
        }
    }

    @Override
    public void signIn() {
    }

    @Override
    public void signOut() {
    }

    @Override
    public void updateUserData(String userEmail) {
        if(userEmail == null)
            loggedUser.setText(String.format("%s: %s", getString(R.string.General_user), getString(R.string.General_loggedOut)));
        else
            loggedUser.setText(String.format("%s: %s", getString(R.string.General_user), userEmail));
    }

    @Override
    public void onResume() {
        super.onResume();
        assert adView != null;
        adView.resume();
    }

    @Override
    public void onPause() {
        super.onResume();
        assert adView != null;
        adView.pause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        assert adView != null;
        adView.destroy();
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this, ANIM_TYPE);
    }

    public void onProductResponse(List<Product> productList) {
        presenter.restoreNotifications(context, productList);
    }
}