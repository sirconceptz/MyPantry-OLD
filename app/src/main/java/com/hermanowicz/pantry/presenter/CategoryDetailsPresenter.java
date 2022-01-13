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

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import com.hermanowicz.pantry.db.category.Category;
import com.hermanowicz.pantry.interfaces.CategoryDetailsView;
import com.hermanowicz.pantry.model.AppSettingsModel;
import com.hermanowicz.pantry.model.CategoryModel;

import java.util.List;

/**
 * <h1>CategoryDetailsPresenter</h1>
 * Presenter for CategoryDetailsActivity
 *
 * @author Mateusz Hermanowicz
 */

public class CategoryDetailsPresenter {

    private final CategoryModel model;
    private final CategoryDetailsView view;

    public CategoryDetailsPresenter(@NonNull CategoryDetailsView view,
                                    @NonNull Context context) {
        this.model = new CategoryModel(context);
        this.view = view;
        AppSettingsModel appSettingsModel = new AppSettingsModel(PreferenceManager.
                getDefaultSharedPreferences(context));
        model.setDatabaseMode(appSettingsModel.getDatabaseMode());
    }

    public void setCategory(@NonNull Category category) {
        model.setCategory(category);
        view.showCategoryDetails(category);
    }

    public Category getCategory(){
        return model.getCategory();
    }

    public void deleteCategory() {
        if(model.getDatabaseMode().equals("local"))
            model.deleteOfflineDbCategory(model.getCategory());
        else
            model.deleteOnlineCategory(model.getCategory());
        view.navigateToCategoriesActivity();
    }

    public void updateCategory(@NonNull Category category) {
        if(model.isCategoryNameNotCorrect(category.getName()) || model.isCategoryDescriptionNotCorrect(category.getDescription()))
            view.showErrorOnUpdateCategory();
        else {
            model.updateCategory(category);
            view.showCategoryUpdated();
            view.navigateToCategoriesActivity();
        }
    }

    public void isCategoryNameCorrect(@NonNull String categoryName) {
        if(model.isCategoryNameNotCorrect(categoryName))
            view.showCategoryNameError();
        view.updateNameCharCounter(categoryName.length(), model.MAX_CHAR_CATEGORY_NAME);
    }

    public void isCategoryDescriptionCorrect(@NonNull String categoryDescription) {
        if(model.isCategoryDescriptionNotCorrect(categoryDescription))
            view.showCategoryDescriptionError();
        view.updateDescriptionCharCounter(categoryDescription.length(), model.MAX_CHAR_CATEGORY_DESCRIPTION);
    }

    public void onResponse(List<Category> categoryList) {
        model.setCategory(categoryList.get(0));
    }
}