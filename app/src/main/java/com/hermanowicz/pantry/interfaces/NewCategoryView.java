package com.hermanowicz.pantry.interfaces;

import com.hermanowicz.pantry.db.Category;

public interface NewCategoryView {
    void onAddCategory(Category category);

    void updateNameCharCounter(int charCounter, int maxChar);

    void updateDescriptionCharCounter(int charCounter, int maxChar);

    void showNameFieldError();

    void showDescriptionFieldError();
}