package com.hermanowicz.pantry.interfaces;

import com.hermanowicz.pantry.db.Category;

public interface CategoryDetailsView {
    void showErrorOnUpdateCategory();

    void showCategoryUpdated();

    void showCategoryDetails(Category category);

    void showCategoryNameError();

    void showCategoryDescriptionError();

    void updateNameCharCounter(int charCounter, int maxChar);

    void updateDescriptionCharCounter(int charCounter, int maxChar);

    void navigateToCategoriesActivity();
}