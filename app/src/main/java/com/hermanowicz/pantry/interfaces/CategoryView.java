package com.hermanowicz.pantry.interfaces;

import com.hermanowicz.pantry.db.Category;

import java.util.List;

public interface CategoryView {
    void showEmptyCategoryListStatement();

    void updateCategoryList(List<Category> categoryList);

    void onSuccessAddNewCategory();

    void onErrorAddNewCategory();

    void navigateToMainActivity();
}