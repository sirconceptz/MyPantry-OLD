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
import com.hermanowicz.pantry.interfaces.CategoryView;
import com.hermanowicz.pantry.model.CategoryModel;

import java.util.List;

public class CategoryPresenter {

    private final CategoryView view;
    private final CategoryModel model;

    public CategoryPresenter (@NonNull CategoryView view, @NonNull CategoryModel model){
        this.view = view;
        this.model = model;
    }

    public void updateCategoryList(){
        List<Category> categoryList = model.getCategoryList();
        view.updateCategoryList(categoryList);
        view.showEmptyCategoryListStatement(categoryList.size() == 0);
    }

    public void addCategory(Category category) {
        if(model.isCategoryNameNotCorrect(category.getName()) || model.isCategoryDescriptionNotCorrect(category.getDescription()))
            view.onErrorAddNewCategory();
        else if(model.addCategory(category)) {
            view.onSuccessAddNewCategory();
            view.updateCategoryList(model.getCategoryList());
            view.showEmptyCategoryListStatement(model.getCategoryList().size() == 0);
        }
        else
            view.onErrorAddNewCategory();
    }

    public void navigateToMainActivity() {
        view.navigateToMainActivity();
    }
}