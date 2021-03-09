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

import androidx.annotation.NonNull;

import com.hermanowicz.pantry.db.category.Category;
import com.hermanowicz.pantry.interfaces.NewCategoryView;
import com.hermanowicz.pantry.model.CategoryModel;

public class NewCategoryPresenter {

    private final NewCategoryView view;
    private final CategoryModel model;

    public NewCategoryPresenter(@NonNull NewCategoryView view, @NonNull CategoryModel model){
        this.view = view;
        this.model = model;
    }

    public void initCharCounters(){
        view.updateNameCharCounter(0, model.MAX_CHAR_CATEGORY_NAME);
        view.updateDescriptionCharCounter(0, model.MAX_CHAR_CATEGORY_DESCRIPTION);
    }

    public void isCategoryNameCorrect(@NonNull String categoryName){
        if(model.isCategoryNameNotCorrect(categoryName))
            view.showNameFieldError();
        view.updateNameCharCounter(categoryName.length(), model.MAX_CHAR_CATEGORY_NAME);
    }

    public void isCategoryDescriptionCorrect(@NonNull String categoryDescription){
        if(model.isCategoryDescriptionNotCorrect(categoryDescription))
            view.showDescriptionFieldError();
        view.updateDescriptionCharCounter(categoryDescription.length(), model.MAX_CHAR_CATEGORY_DESCRIPTION);
    }

    public void onPressAddCategory(@NonNull Category category) {
        if(model.isCategoryNameNotCorrect(category.getName()))
            view.showNameFieldError();
        else if(model.isCategoryDescriptionNotCorrect(category.getDescription()))
            view.showDescriptionFieldError();
        else
            view.onAddCategory(category);
    }
}