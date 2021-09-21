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

package com.hermanowicz.pantry.model;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hermanowicz.pantry.db.photo.Photo;
import com.hermanowicz.pantry.db.product.Product;
import com.hermanowicz.pantry.db.product.ProductDb;
import com.hermanowicz.pantry.util.DateHelper;
import com.hermanowicz.pantry.util.PermissionsHandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;

public class PhotoModel {

    public final int MAX_CHAR_PHOTO_DESCRIPTION = 50;

    private final ProductDb productDb;
    private final AppCompatActivity activity;
    private List<Product> productList;
    private List<Product> allProductList;
    private File photoFile;
    private final String filePath;
    private String fileName;
    private String databaseMode;
    private List<Photo> photoList;
    private Bitmap photoBitmap;
    private boolean isNewPhoto = false;

    public PhotoModel(@NonNull AppCompatActivity activity){
        this.activity = activity;
        this.productDb = ProductDb.getInstance(activity.getApplicationContext());
        filePath = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES
                + File.separator + "MyPantry").getPath();
    }

    public void setProductList(@NonNull List<Product> productList) {
        this.productList = productList;
    }

    public void addPhotoToDb(@Nullable Bitmap bitmap, @NonNull String photoDescription) {
        if(databaseMode.equals("local")) {
            if(bitmap != null)
                galleryOfflineAddPic(bitmap);
            addPhotoToOfflineDb(photoDescription);
        }
        else
            addPhotoToOnlineDb(bitmap, photoDescription);
    }

    public void addPhotoToOfflineDb(@NonNull String photoDescription) {
        for(Product product : productList) {
            product.setPhotoName(fileName);
            product.setPhotoDescription(photoDescription);
            productDb.productsDao().updateProduct(product);
        }
    }

    public void deleteOfflinePhoto() {
        deleteOfflinePhotoFromDb();
        photoFile.delete();
    }

    public void deleteOnlinePhoto() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference().child("photos/" + FirebaseAuth.getInstance().getUid());
        List<Product> restProductList = allProductList;
        for(Product product : allProductList) {
            if(productList.contains(product))
                restProductList.remove(product);
        }
        boolean isPhotoToRemove = true;
        Photo photoToRemove = new Photo();
        for(Product product : restProductList) {
            if (productList.get(0).getPhotoName().equals(product.getPhotoName())) {
                isPhotoToRemove = false;
            }
            for (Photo photo : photoList) {
                if (photo.getName().equals(product.getPhotoName()))
                    photoToRemove = photo;
            }
            if (isPhotoToRemove) {
                ref.child(String.valueOf(photoToRemove.getId())).removeValue();
            }
        }
        ref = db.getReference().child("products/" + FirebaseAuth.getInstance().getUid());
        deletePhotoData();
        for (Product product : productList) {
            ref.child(String.valueOf(product.getId())).setValue(product);
        }
    }

    private void deletePhotoData() {
        for (Product product : productList) {
            product.setPhotoName("");
            product.setPhotoDescription("");
        }
    }

    private void deleteOfflinePhotoFromDb() {
        deletePhotoData();
        for (Product product : productList) {
            productDb.productsDao().updateProduct(product);
        }
    }

    public Bitmap getPhotoBitmap(){
        return photoBitmap;
    }

    public boolean isPhotoDescriptionNotCorrect(@NonNull String categoryDescription) {
        return categoryDescription.length() > MAX_CHAR_PHOTO_DESCRIPTION;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void createPhotoFile() {
        fileName = "JPEG_" + DateHelper.getTimeStamp();
        try {
            photoFile = File.createTempFile(fileName, ".jpg", new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addPhotoToOnlineDb(@Nullable Bitmap bitmap, @NonNull String photoDescription) {
        String encodedImage = "";
        String photoName = "";
        if(bitmap != null && isNewPhoto) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
            byte[] byteArray = baos.toByteArray();
            encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
            photoName = String.valueOf(encodedImage.hashCode());
        }
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference().child("photos/" + FirebaseAuth.getInstance().getUid());
        boolean photoExist = false;
        int photoId = 0;
        int photoListSize = photoList.size();
        if(photoListSize > 0) {
            photoId = photoList.get(photoListSize - 1).getId() + 1;
            for (Photo photo : photoList) {
                if (photo.getName().equals(photoName)) {
                    photoExist = true;
                    break;
                }
            }
        }
        if(!photoExist && isNewPhoto){
            for(Photo photo : photoList) {
                if(photo.getName().equals(productList.get(0).getPhotoName()))
                    ref.child(String.valueOf(photo.getId())).removeValue();
            }
            Photo newPhoto = new Photo();
            newPhoto.setId(photoId);
            newPhoto.setName(String.valueOf(encodedImage.hashCode()));
            newPhoto.setContent(encodedImage);
            ref.child(String.valueOf(newPhoto.getId())).setValue(newPhoto);
        }
        ref = db.getReference().child("products/" + FirebaseAuth.getInstance().getUid());
        for(Product product : productList) {
            if(encodedImage.length() > 0)
                product.setPhotoName(photoName);
            product.setPhotoDescription(photoDescription);
            ref.child(String.valueOf(product.getId())).setValue(product);
        }
        isNewPhoto = false;
    }

    public void galleryOfflineAddPic(@NonNull Bitmap bitmap) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                OutputStream fos;
                ContentResolver resolver = activity.getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES
                        + File.separator + "MyPantry");
                Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                fos = resolver.openOutputStream(Objects.requireNonNull(imageUri));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                Objects.requireNonNull(fos);
                fos.close();
            }
            else {
                FileOutputStream fileOutputStream = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                         + File.separator + fileName + ".jpg", false);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.close();
            }
        } catch (Exception e) {
            Log.e("Error on insert photo", e.getMessage());
        }
    }

    public File getPhotoFile() {
        return photoFile;
    }

    public void setPhotoFile(@NonNull String photoName) {
        if(databaseMode.equals("local"))
            setPhotoFileOffline(photoName);
        else
            setPhotoFileOnline(photoName);
    }

    private void setPhotoFileOnline(@NonNull String photoName) {
        if(photoList != null) {
            for (Photo photo : photoList) {
                if (photo.getName().equals(photoName)) {
                    byte[] decodedString = Base64.decode(photo.getContent(), Base64.DEFAULT);
                    photoBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                }
            }
        }
    }

    private void setPhotoFileOffline(@NonNull String photoName) {
        String external;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            external = Environment.getExternalStorageDirectory().getAbsoluteFile() + File.separator + Environment.DIRECTORY_PICTURES + File.separator + "MyPantry";
        else
            external = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
        photoFile = new File(external + File.separator + photoName + ".jpg");
        photoBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
    }

    public String getDatabaseMode() {
        return databaseMode;
    }

    public void setDatabaseMode(String databaseMode) {
        this.databaseMode = databaseMode;
    }

    public void setPhotoList(List<Photo> photoList) {
        this.photoList = photoList;
    }

    public List<Photo> getPhotoList() {
        return photoList;
    }

    public boolean isCameraPermission() {
        return PermissionsHandler.isCameraPermission(activity);
    }

    public void requestCameraPermission() {
        PermissionsHandler.requestCameraPermission(activity);
    }

    public boolean isWritePermission() {
        return PermissionsHandler.isWritePermission(activity);
    }

    public void requestWritePermission() {
        PermissionsHandler.requestWritePermission(activity);
    }

    public void setIsNewPhoto(boolean isNewPhoto) {
        this.isNewPhoto = isNewPhoto;
    }

    public void setAllProductList(List<Product> productList) {
        if(databaseMode.equals("local"))
            allProductList = productDb.productsDao().getAllProductsList();
        else
            this.allProductList = productList;
    }
}