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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.databinding.ActivityAddPhotoBinding;
import com.hermanowicz.pantry.db.product.Product;
import com.hermanowicz.pantry.interfaces.AddPhotoView;
import com.hermanowicz.pantry.model.DatabaseOperations;
import com.hermanowicz.pantry.presenter.AddPhotoPresenter;
import com.hermanowicz.pantry.util.ImageRotation;
import com.hermanowicz.pantry.util.Orientation;
import com.hermanowicz.pantry.util.ThemeMode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class AddPhotoActivity extends AppCompatActivity implements AddPhotoView {

    private static final int REQUEST_IMAGE_CAPTURE_CODE = 42;

    private ActivityAddPhotoBinding binding;
    private Context context;
    private AddPhotoPresenter presenter;

    private ImageView imageViewPhoto;
    private EditText description;
    private TextView descriptionCharCounter;
    private Button takePhoto, savePhoto, deletePhoto;

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
        binding = ActivityAddPhotoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = AddPhotoActivity.this;

        Toolbar toolbar = binding.toolbar;
        imageViewPhoto = binding.photoIv;
        description = binding.photoDescription;
        descriptionCharCounter = binding.descriptionCharCounter;
        takePhoto = binding.buttonTakePhoto;
        savePhoto = binding.buttonSavePhoto;
        deletePhoto = binding.buttonDeletePhoto;

        List<Product> productList = (List<Product>) getIntent().getSerializableExtra("PRODUCT_LIST");
        presenter = new AddPhotoPresenter(this);
        presenter.setActivity(this);
        presenter.setDb(new DatabaseOperations(context));
        presenter.setProductList(productList);
        presenter.updateDescriptionCharCounter(description.getText().toString());

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

    @Override
    public void takePictureIntent(File photoFile) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri fileProvider = FileProvider.getUriForFile(context, "com.hermanowicz.pantry.provider", photoFile);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
        if(takePictureIntent.resolveActivity(this.getPackageManager()) != null){
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE_CODE);
        }
        else
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
    public void onTakePhoto(File photoFile) {
        takePictureIntent(photoFile);
    }

    @Override
    public void onSavePhoto() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageViewPhoto.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        presenter.galleryAddPic(bitmap);
        String photoDescription = description.getText().toString();
        presenter.addPhotoToDb(photoDescription);
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
    public void updateDescriptionCharCounter(int charCounter, int maxChar) {
        descriptionCharCounter.setText(String.format("%s: %d/%d", getText(R.string.General_char_counter).toString(), charCounter, maxChar));
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
            case R.id.action_delete_product:
                presenter.onClickDeletePhoto();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this, "fadein-to-fadeout");
    }
}