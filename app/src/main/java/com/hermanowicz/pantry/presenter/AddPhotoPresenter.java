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
import androidx.appcompat.app.AppCompatActivity;

import com.hermanowicz.pantry.db.product.Product;
import com.hermanowicz.pantry.interfaces.AddPhotoView;
import com.hermanowicz.pantry.model.AddPhotoModel;
import com.hermanowicz.pantry.model.DatabaseOperations;

import java.io.File;
import java.util.List;

public class AddPhotoPresenter{

    private final AddPhotoView view;
    private AddPhotoModel model;

    public AddPhotoPresenter(AddPhotoView view){
        this.view = view;
    }

    public void setProductList(@NonNull List<Product> productList){
        model.setProductList(productList);
        Product singleProduct = productList.get(0);
        if(!(singleProduct.getPhotoDescription() == null)){
            model.setPhotoFile(singleProduct.getPhotoName());
            view.showPhoto(singleProduct);
        }
    }

    public void setActivity(@NonNull AppCompatActivity activity){
        model = new AddPhotoModel(new DatabaseOperations(activity.getApplicationContext()));
        model.setActivity(activity);
    }

    public void setDb(DatabaseOperations databaseOperations){
        model.setDB(databaseOperations);
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
        model.deletePhoto();
        view.onDeletePhoto();
    }

    public void isPhotoDescriptionCorrect(String photoDescription) {
        if(model.isPhotoDescriptionNotCorrect(photoDescription))
            view.showDescriptionFieldError();
        view.updateDescriptionCharCounter(photoDescription.length(), model.MAX_CHAR_PHOTO_DESCRIPTION);
    }

    public void updateDescriptionCharCounter(String description){
        view.updateDescriptionCharCounter(description.length(), model.MAX_CHAR_PHOTO_DESCRIPTION);
    }

    public void addPhotoToDb(@NonNull String photoDescription) {
        model.addPhotoToDb(photoDescription);
    }

    public File getPhotoFile() {
        return model.getPhotoFile();
    }

    public void galleryAddPic(Bitmap bitmap) {
        int result = model.galleryAddPic(bitmap);
        if(result == -1)
            view.onPhotoAddSuccess();
        else
            view.onPhotoAddError();
    }
}