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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.databinding.ActivityAddPhotoBinding;
import com.hermanowicz.pantry.db.photo.Photo;
import com.hermanowicz.pantry.db.product.Product;
import com.hermanowicz.pantry.interfaces.AddPhotoView;
import com.hermanowicz.pantry.presenter.AddPhotoPresenter;
import com.hermanowicz.pantry.util.ImageRotation;
import com.hermanowicz.pantry.util.Orientation;
import com.hermanowicz.pantry.util.PremiumAccess;
import com.hermanowicz.pantry.util.ThemeMode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import maes.tech.intentanim.CustomIntent;

/**
 * <h1>AppSettingsActivity</h1>
 * Activity for add and remove photo for products.
 *
 * @author Mateusz Hermanowicz
 */

public class AddPhotoActivity extends AppCompatActivity implements AddPhotoView {

    private final String TAG = "PhotosRxJava";
    private static final int REQUEST_IMAGE_CAPTURE_CODE = 42;

    private Context context;
    private AddPhotoPresenter presenter;
    private final CompositeDisposable disposables = new CompositeDisposable();

    private ImageView imageViewPhoto;
    private EditText description;
    private Button takePhoto, savePhoto, deletePhoto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(ThemeMode.getThemeMode(this));
        if(Orientation.isTablet(this))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        super.onCreate(savedInstanceState);
        initView();
        setListeners();
        setPhotoListObserver();
    }

    private void initView() {
        ActivityAddPhotoBinding binding = ActivityAddPhotoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = AddPhotoActivity.this;
        presenter = new AddPhotoPresenter(this, this);

        Toolbar toolbar = binding.toolbar;
        AdView adView = binding.adview;
        imageViewPhoto = binding.imageviewPhoto;
        description = binding.edittextPhotoDescription;
        takePhoto = binding.buttonTakePhoto;
        savePhoto = binding.buttonSavePhoto;
        deletePhoto = binding.buttonDeletePhoto;

        List<Product> productList = (List<Product>) getIntent().getSerializableExtra("product_list");
        presenter.setProductList(productList);
        presenter.setAllProductList(productList);
        presenter.setPremiumAccess(new PremiumAccess(context));

        if(!presenter.isPremium()) {
            MobileAds.initialize(context);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }

        setSupportActionBar(toolbar);
    }

    private void setListeners() {
        takePhoto.setOnClickListener(view -> presenter.onClickTakePhoto());
        savePhoto.setOnClickListener(view -> presenter.onClickSavePhoto());
        deletePhoto.setOnClickListener(view -> presenter.onClickDeletePhoto());

        description.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable editable) {
                }

                @Override
                public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                presenter.isPhotoDescriptionCorrect(description.getText().toString());
            }
        });
    }

    private void setPhotoListObserver() {
        if (!presenter.isOfflineDb()) {
            disposables.add(photoList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<List<Photo>>() {
                        @Override
                        public void onComplete() {
                            Log.d(TAG, "onComplete()");
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            Log.e(TAG, "onError()", e);
                        }

                        @Override
                        public void onNext(@NonNull List<Photo> photoList) {
                            Log.i(TAG, "onNext()");
                        }
                    }));
        }
    }

    private Observable<List<Photo>> photoList() {
        return Observable.create(emitter -> {
            String user = FirebaseAuth.getInstance().getUid();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            assert user != null;
            Query query = database.getReference().child("photos").child(user);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<Photo> list = new ArrayList<>();
                    Iterable<DataSnapshot> snapshotIterable = snapshot.getChildren();

                    for (DataSnapshot dataSnapshot : snapshotIterable) {
                        Photo photo = dataSnapshot.getValue(Photo.class);
                        list.add(photo);
                    }
                    emitter.onNext(list);
                    onPhotoResponse(list);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    emitter.onError(new FirebaseException(error.getMessage()));
                }
            });
        });
    }

    @Override
    public void takePictureIntent(File photoFile) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri fileProvider = FileProvider.getUriForFile(context, "com.hermanowicz.pantry.provider", photoFile);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE_CODE);
        } else
            Toast.makeText(this, getText(R.string.AddPhotoActivity_no_camera_access), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE_CODE && resultCode == Activity.RESULT_OK){
            File photoFile = presenter.getPhotoFile();
            Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());

            Matrix m = new Matrix();
            m.postRotate(ImageRotation.neededRotation(photoFile));

            takenImage = Bitmap.createBitmap(takenImage,
                    0, 0, takenImage.getWidth(), takenImage.getHeight(),
                    m, true);

            imageViewPhoto.setImageBitmap(takenImage);
            presenter.setIsNewPhoto(true);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            takenImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void showPhoto(@NonNull Product singleProduct, Bitmap photo) {
        description.setText(singleProduct.getPhotoDescription());
        imageViewPhoto.setImageBitmap(photo);
    }

    @Override
    public void onPhotoAddSuccess() {
        Toast.makeText(context, getText(R.string.AddPhotoActivity_add_photo_successful), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPhotoAddError() {
        Toast.makeText(context, getText(R.string.AddPhotoActivity_add_photo_not_successful), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTakePhoto(@NonNull File photoFile) {
        takePictureIntent(photoFile);
    }

    @Override
    public void onSavePhoto() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageViewPhoto.getDrawable();
        Bitmap bitmap = null;
        if(bitmapDrawable != null) {
            bitmap = bitmapDrawable.getBitmap();
        }
        String photoDescription = description.getText().toString();
        presenter.addPhotoToDb(bitmap, photoDescription);
    }

    @Override
    public void onDeletePhoto() {
        Toast.makeText(context, getText(R.string.AddPhotoActivity_delete_photo_successful), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showDescriptionFieldError() {
        description.setError(getText(R.string.AddPhotoActivity_invalid_photo_description));
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.add_photo_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_take_photo:
                presenter.onClickTakePhoto();
                return true;
            case R.id.action_save_photo:
                presenter.onClickSavePhoto();
                return true;
            case R.id.action_delete_photo:
                presenter.onClickDeletePhoto();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            presenter.onClickTakePhoto();
        }
    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this, "fadein-to-fadeout");
    }

    public void onPhotoResponse(List<Photo> photoList) {
        presenter.setPhotoList(photoList);
    }
}