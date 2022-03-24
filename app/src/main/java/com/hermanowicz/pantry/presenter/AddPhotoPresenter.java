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

package com.hermanowicz.pantry.presenter;

import android.content.SharedPreferences;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.hermanowicz.pantry.db.photo.Photo;
import com.hermanowicz.pantry.db.product.Product;
import com.hermanowicz.pantry.interfaces.AddPhotoView;
import com.hermanowicz.pantry.model.AppSettingsModel;
import com.hermanowicz.pantry.model.DatabaseMode;
import com.hermanowicz.pantry.model.PhotoModel;
import com.hermanowicz.pantry.util.PremiumAccess;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>AddPhotoPresenter</h1>
 * Presenter for AddPhotoActivity
 *
 * @author  Mateusz Hermanowicz
 */

public class AddPhotoPresenter{

    private final AddPhotoView view;
    private final PhotoModel model;
    private final DatabaseMode dbMode = new DatabaseMode();
    private PremiumAccess premiumAccess;

    public AddPhotoPresenter(@NonNull AddPhotoView view, @NonNull AppCompatActivity activity) {
        this.view = view;
        this.model = new PhotoModel(activity);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        AppSettingsModel appSettingsModel = new AppSettingsModel(sharedPreferences);
        dbMode.setDatabaseMode(appSettingsModel.getDatabaseMode());
    }

    public void setPremiumAccess(@NonNull PremiumAccess premiumAccess){
        this.premiumAccess = premiumAccess;
    }

    public void setProductList(@Nullable ArrayList<Product> productList){
        if(productList != null) {
            model.setProductList(productList);
            Product singleProduct = productList.get(0);
            if(!(singleProduct.getPhotoDescription() == null)) {
                String photoName = singleProduct.getPhotoName();
                Bitmap photoBitmap = model.getPhotoBitmap();
                model.setPhotoFile(photoName, dbMode);
                view.showPhoto(singleProduct, photoBitmap);
            }
        }
    }

    public void onClickTakePhoto(){
        model.createPhotoFile();
        if(!model.isCameraPermission()){
            model.requestCameraPermission();
        }
        if(!model.isWritePermission()){
            model.requestWritePermission();
        }
        if(model.isCameraPermission() && model.isWritePermission()) {
            view.onTakePhoto(model.getPhotoFile());
        }
    }

    public void onClickSavePhoto(){
        view.onSavePhoto();
    }

    public void onClickDeletePhoto(){
        if(isOfflineDb())
            model.deleteOfflinePhoto();
        else
            model.deleteOnlinePhoto();
        view.onDeletePhoto();
    }

    public void isPhotoDescriptionCorrect(String photoDescription) {
        if(model.isPhotoDescriptionNotCorrect(photoDescription))
            view.showDescriptionFieldError();
    }

    public void addPhotoToDb(@Nullable Bitmap bitmap, @NonNull String photoDescription) {
        if(isOfflineDb())
            model.addPhotoToOfflineDb(bitmap, photoDescription);
        else
            model.addPhotoToOnlineDb(bitmap, photoDescription);
    }

    public File getPhotoFile() {
        return model.getPhotoFile();
    }

    public boolean isPremium(){
        return premiumAccess.isPremium();
    }

    public void setPhotoList(@Nullable ArrayList<Photo> photoList) {
        model.setPhotoList(photoList);
        showPhoto();
    }

    public boolean isOfflineDb() {
        return dbMode.getDatabaseMode() == DatabaseMode.Mode.LOCAL;
    }

    public void setIsNewPhoto(boolean isNewPhoto) {
        model.setIsNewPhoto(isNewPhoto);
    }

    public void setAllProductList(ArrayList<Product> productList) {
        if(isOfflineDb())
            model.setOfflineAllProductList();
        else
            model.setAllProductList(productList);
    }

    public void showPhoto() {
        List<Product> productList = model.getProductList();
        Product product = productList.get(0);
        String photoName = product.getPhotoName();
        if(!photoName.equals("")) {
            model.setPhotoFile(photoName, dbMode);
            Bitmap bitmap = model.getPhotoBitmap();
            view.showPhoto(product, bitmap);
        }
    }
}