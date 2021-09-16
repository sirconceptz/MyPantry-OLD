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

package com.hermanowicz.pantry.presenter;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.hermanowicz.pantry.db.photo.Photo;
import com.hermanowicz.pantry.db.product.Product;
import com.hermanowicz.pantry.interfaces.AddPhotoView;
import com.hermanowicz.pantry.model.AppSettingsModel;
import com.hermanowicz.pantry.model.PhotoModel;
import com.hermanowicz.pantry.util.PremiumAccess;

import java.io.File;
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
    private PremiumAccess premiumAccess;

    public AddPhotoPresenter(@NonNull AddPhotoView view, @NonNull AppCompatActivity activity){
        this.view = view;
        this.model = new PhotoModel(activity);
        AppSettingsModel appSettingsModel = new AppSettingsModel(PreferenceManager.getDefaultSharedPreferences(activity));
        model.setDatabaseMode(appSettingsModel.getDatabaseMode());
    }

    public void setPremiumAccess(@NonNull PremiumAccess premiumAccess){
        this.premiumAccess = premiumAccess;
    }

    public void setProductList(@Nullable List<Product> productList){
        if(productList != null) {
            model.setProductList(productList);
            Product singleProduct = productList.get(0);
            if(!(singleProduct.getPhotoDescription() == null)) {
                model.setPhotoFile(singleProduct.getPhotoName());
                view.showPhoto(singleProduct, model.getPhotoBitmap());
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
        model.addPhotoToDb(bitmap, photoDescription);
    }

    public File getPhotoFile() {
        return model.getPhotoFile();
    }

    public boolean isPremium(){
        return premiumAccess.isPremium();
    }

    public void setPhotoList(@Nullable List<Photo> photoList) {
        model.setPhotoList(photoList);
    }

    public boolean isOfflineDb() {
        return model.getDatabaseMode().equals("local");
    }

    public void setIsNewPhoto(boolean isNewPhoto) {
        model.setIsNewPhoto(isNewPhoto);
    }

    public void setAllProductList(List<Product> productList) {
        model.setAllProductList(productList);
    }
}