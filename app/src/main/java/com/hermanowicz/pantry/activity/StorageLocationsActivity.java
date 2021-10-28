/*
 * Copyright (c) 2019-2021
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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.databinding.ActivityStorageLocationsBinding;
import com.hermanowicz.pantry.db.storagelocation.StorageLocation;
import com.hermanowicz.pantry.dialog.NewStorageLocationDialog;
import com.hermanowicz.pantry.interfaces.DialogStorageLocationListener;
import com.hermanowicz.pantry.interfaces.StorageLocationDbResponse;
import com.hermanowicz.pantry.interfaces.StorageLocationView;
import com.hermanowicz.pantry.presenter.StorageLocationPresenter;
import com.hermanowicz.pantry.util.Orientation;
import com.hermanowicz.pantry.util.RecyclerClickListener;
import com.hermanowicz.pantry.util.StorageLocationsAdapter;
import com.hermanowicz.pantry.util.ThemeMode;

import java.util.ArrayList;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

/**
 * <h1>StorageLocationActivity</h1>
 * StorageLocation activity - user can see all storage locations and create new one.
 *
 * @author  Mateusz Hermanowicz
 */

public class StorageLocationsActivity extends AppCompatActivity implements DialogStorageLocationListener,
        StorageLocationView, StorageLocationDbResponse {

    private StorageLocationPresenter presenter;
    private Context context;
    private final StorageLocationsAdapter storageLocationsAdapter = new StorageLocationsAdapter();

    private RecyclerView storageLocationsRecyclerView;
    private TextView statement;
    private AdView adView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(ThemeMode.getThemeMode(this));
        if(Orientation.isTablet(this))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        super.onCreate(savedInstanceState);
        initView();
        setListeners();
    }

    private void initView() {
        ActivityStorageLocationsBinding binding = ActivityStorageLocationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = getApplicationContext();

        Toolbar toolbar = binding.toolbar;
        adView = binding.adview;
        storageLocationsRecyclerView = binding.recyclerviewStorageLocations;
        statement = binding.textStatement;

        setSupportActionBar(toolbar);

        presenter = new StorageLocationPresenter(this, context, this);
        setOnlineDbStorageLocationList(this);

        if(!presenter.isPremium()) {
            MobileAds.initialize(context);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }

        presenter.updateStorageLocationListView();
        storageLocationsRecyclerView.setAdapter(storageLocationsAdapter);
        storageLocationsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        storageLocationsRecyclerView.setHasFixedSize(true);
        storageLocationsRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void setOnlineDbStorageLocationList(StorageLocationDbResponse response) {
        DatabaseReference ref;
        List<StorageLocation> onlineStorageLocationList = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference().child("storage_locations/" + FirebaseAuth.getInstance().getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                    onlineStorageLocationList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    StorageLocation storageLocation = dataSnapshot.getValue(StorageLocation.class);
                    onlineStorageLocationList.add(storageLocation);
                }
                response.onResponse(onlineStorageLocationList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("FirebaseDB", error.getMessage());
            }
        });
    }

    private void setListeners() {
        storageLocationsRecyclerView.addOnItemTouchListener(new RecyclerClickListener(this,
                storageLocationsRecyclerView, new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                List<StorageLocation> storageLocationList = presenter.getStorageLocationList();
                Intent intent = new Intent(context, StorageLocationDetailsActivity.class)
                        .putExtra("storage_location", storageLocationList.get(position));
                startActivity(intent);
                CustomIntent.customType(view.getContext(), "fadein-to-fadeout");
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        }));
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.new_item_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_new_item) {
            NewStorageLocationDialog newStorageLocationDialog = new NewStorageLocationDialog();
            newStorageLocationDialog.show(getSupportFragmentManager(), "");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAddStorageLocation(@NonNull StorageLocation storageLocation) {
        presenter.addStorageLocation(storageLocation);
    }

    @Override
    public void showEmptyStorageLocationListStatement(boolean visible) {
        if(visible)
            statement.setVisibility(View.VISIBLE);
        else
            statement.setVisibility(View.INVISIBLE);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void updateStorageLocationViewAdapter(@NonNull List<StorageLocation> storageLocationList) {
        storageLocationsAdapter.setData(storageLocationList);
        storageLocationsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSuccessAddNewStorageLocation() {
        Toast.makeText(this, R.string.StorageLocationDetailsActivity_storage_location_was_saved, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorAddNewStorageLocation() {
        Toast.makeText(this, R.string.Error_wrong_data, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        CustomIntent.customType(this, "fadein-to-fadeout");
    }

    @Override
    public void onResponse(List<StorageLocation> storageLocationList) {
        presenter.setOnlineDbStorageLocationList(storageLocationList);
        presenter.updateStorageLocationListView();
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
    public void finish() {
        super.finish();
        CustomIntent.customType(this, "fadein-to-fadeout");
    }
}