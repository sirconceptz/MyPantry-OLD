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

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hermanowicz.pantry.db.category.Category;
import com.hermanowicz.pantry.db.category.CategoryDb;

import java.util.ArrayList;
import java.util.List;

public class CategoryModel {

    public final int MAX_CHAR_CATEGORY_NAME = 30;
    public final int MAX_CHAR_CATEGORY_DESCRIPTION = 200;

    private final CategoryDb db;
    private Category category;
    private List<Category> categoryList = new ArrayList<>();
    private String databaseMode;

    public CategoryModel(@NonNull Context context) {
        this.db = CategoryDb.getInstance(context);
    }

    public Category getCategory(){
        return this.category;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategory(@NonNull Category category) {
        this.category = category;
    }

    public void setCategoryList(@NonNull List<Category> categoryList) {
        this.categoryList = categoryList;
    }
    public void setOfflineDbCategoryList(){
        List<Category> categoryList = db.categoryDao().getAllOwnCategories();
        setCategoryList(categoryList);
    }

    public void updateCategory(@NonNull Category category) {
        if(databaseMode.equals("local"))
            updateOfflineCategory(category);
        else
            updateOnlineCategory(category);
    }

    private void updateOnlineCategory(@NonNull Category category) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("categories/" + FirebaseAuth.getInstance().getUid());
        ref.child(String.valueOf(category.getId())).setValue(category);
    }

    private void updateOfflineCategory(@NonNull Category category) {
        db.categoryDao().updateCategory(category);
    }

    public void deleteOfflineDbCategory(@NonNull Category category) {
        db.categoryDao().deleteCategory(category);
    }

    public void deleteOnlineCategory(@NonNull Category category) {
        DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference().child("categories/" + FirebaseAuth.getInstance().getUid());
        ref.child(String.valueOf(category.getId())).removeValue();
    }

    public boolean addCategory(@NonNull Category newCategory){
        boolean result;
        if(databaseMode.equals("local"))
            result = addOfflineDbCategory(newCategory);
        else
            result = addOnlineDbCategory(newCategory);
        return result;
    }

    private boolean addOnlineDbCategory(@NonNull Category newCategory) {
        boolean result = false;
        if(checkIsCategoryValid(newCategory, categoryList)){
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("categories/" + FirebaseAuth.getInstance().getUid());
            int nextId;
            if (categoryList.size() == 0)
                nextId = 0;
            else
                nextId = categoryList.get(categoryList.size()-1).getId() + 1;
            newCategory.setId(nextId);
            ref.child(String.valueOf(nextId)).setValue(newCategory);
            result = true;
        }
        return result;
    }

    private boolean addOfflineDbCategory(@NonNull Category newCategory) {
        boolean result = false;
        if(checkIsCategoryValid(newCategory, categoryList)){
            db.categoryDao().addCategory(newCategory);
            result = true;
        }
        return result;
    }

    private boolean checkIsCategoryValid(@NonNull Category newCategory,
                                         @NonNull List<Category> categoryList){
        boolean correct = true;
        for (Category category : categoryList) {
            if (category.getName().equals(newCategory.getName())) {
                correct = false;
                break;
            }
        }
        return correct;
    }

    public boolean isCategoryNameNotCorrect(@NonNull String categoryName) {
        return (categoryName.length() > MAX_CHAR_CATEGORY_NAME || categoryName.length() < 1);
    }

    public boolean isCategoryDescriptionNotCorrect(@NonNull String categoryDescription) {
        return categoryDescription.length() > MAX_CHAR_CATEGORY_DESCRIPTION;
    }

    public void setDatabaseMode(String databaseMode) {
        this.databaseMode = databaseMode;
    }

    public String getDatabaseMode() {
        return databaseMode;
    }
}