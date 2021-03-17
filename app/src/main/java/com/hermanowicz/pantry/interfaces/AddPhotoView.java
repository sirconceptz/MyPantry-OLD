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

package com.hermanowicz.pantry.interfaces;

import android.graphics.Bitmap;

import com.hermanowicz.pantry.db.product.Product;

import java.io.File;

public interface AddPhotoView {
    void takePictureIntent(File photoFile);

    void showPhoto(Product product, Bitmap photo);

    void onPhotoAddSuccess();

    void onPhotoAddError();

    void onTakePhoto(File photoFile);

    void onSavePhoto();

    void onDeletePhoto();

    void showDescriptionFieldError();

    void updateDescriptionCharCounter(int charCounter, int maxChar);
}