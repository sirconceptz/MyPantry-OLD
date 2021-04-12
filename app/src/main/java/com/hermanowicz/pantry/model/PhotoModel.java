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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.hermanowicz.pantry.db.product.Product;
import com.hermanowicz.pantry.util.DateHelper;
import com.hermanowicz.pantry.util.PermissionsHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;

public class PhotoModel {

    public final int MAX_CHAR_PHOTO_DESCRIPTION = 50;

    private DatabaseOperations databaseOperations;
    private AppCompatActivity activity;
    private List<Product> productList;

    private File photoFile;
    private String filePath;
    private String fileName;

    public PhotoModel(@NonNull DatabaseOperations databaseOperations){
        this.databaseOperations = databaseOperations;
    }

    public void setProductList(@NonNull List<Product> productList) {
        this.productList = productList;
    }

    public void setDB(@NonNull DatabaseOperations databaseOperations) {
        this.databaseOperations = databaseOperations;
    }

    public void setActivity(@NonNull AppCompatActivity activity){
        this.activity = activity;
        filePath = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES
                + File.separator + "MyPantry").getPath();
    }

    public void addPhotoToDb(@NonNull String photoDescription) {
        for(Product product : productList) {
            product.setPhotoName(fileName);
            product.setPhotoDescription(photoDescription);
        }
        databaseOperations.updateProducts(productList);
    }

    public void deletePhoto() {
        for(Product product : productList) {
            product.setPhotoName("");
            product.setPhotoDescription("");
        }
        databaseOperations.updateProducts(productList);
        photoFile.delete();
    }

    public Bitmap getPhotoBitmap(){
        return BitmapFactory.decodeFile(photoFile.getAbsolutePath());
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

    public int galleryAddPic(@NonNull Bitmap bitmap) {
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
            return -1;
        } catch (Exception e) {
            return  0;
        }
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

    public File getPhotoFile() {
        return photoFile;
    }

    public void setPhotoFile(@NonNull String photoName) {
        String external;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            external = Environment.getExternalStorageDirectory().getAbsoluteFile() + File.separator + Environment.DIRECTORY_PICTURES + File.separator + "MyPantry";
        else
            external = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
        photoFile = new File(external + File.separator + photoName + ".jpg");
    }
}