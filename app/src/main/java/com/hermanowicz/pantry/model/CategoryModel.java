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

import com.hermanowicz.pantry.db.category.Category;

import java.util.List;

public class CategoryModel {

    public final int MAX_CHAR_CATEGORY_NAME = 30;
    public final int MAX_CHAR_CATEGORY_DESCRIPTION = 200;

    private final DatabaseOperations db;

    public CategoryModel(DatabaseOperations databaseOperations){
        this.db = databaseOperations;
    }

    public Category getCategory(int id) {
        return db.getCategory(id);
    }

    public void updateCategory(Category category) {
        db.updateCategory(category);
    }

    public void deleteCategory(int id) {
        db.deleteCategory(id);
    }

    public List<Category> getCategoryList(){
        return db.getOwnCategoriesList();
    }

    public boolean addCategory(Category newCategory){
        List<Category> categoryList = db.getOwnCategoriesList();
        boolean correct = true;
        for(Category category : categoryList){
            if (category.getName().equals(newCategory.getName())) {
                correct = false;
                break;
            }
        }
        if(correct) {
            db.addCategory(newCategory);
            return true;
        }
        else
            return false;
    }

    public boolean isCategoryNameNotCorrect(String categoryName) {
        return (categoryName.length() > MAX_CHAR_CATEGORY_NAME || categoryName.length() < 1);
    }

    public boolean isCategoryDescriptionNotCorrect(String categoryDescription) {
        return categoryDescription.length() > MAX_CHAR_CATEGORY_DESCRIPTION;
    }
}