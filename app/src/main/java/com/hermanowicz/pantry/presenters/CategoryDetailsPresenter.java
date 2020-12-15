package com.hermanowicz.pantry.presenters;

import androidx.annotation.NonNull;

import com.hermanowicz.pantry.db.Category;
import com.hermanowicz.pantry.interfaces.CategoryDetailsView;
import com.hermanowicz.pantry.models.CategoryModel;

public class CategoryDetailsPresenter {

    private final CategoryModel model;
    private final CategoryDetailsView view;

    public CategoryDetailsPresenter (@NonNull CategoryDetailsView view, @NonNull CategoryModel model) {
        this.model = model;
        this.view = view;
    }

    public void setCategoryId(int id) {
        Category category = model.getCategory(id);
        view.showCategoryDetails(category);
    }

    public Category getCategory(int id){
        return model.getCategory(id);
    }

    public void deleteCategory(int id) {
        model.deleteCategory(id);
        view.navigateToCategoriesActivity();
    }

    public void updateCategory(@NonNull Category category) {
        if(model.isCategoryNameNotCorrect(category.getName()) || model.isCategoryDescriptionNotCorrect(category.getDescription()))
            view.showErrorOnUpdateProduct();
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

    public void navigateToCategoriesActivity() {
        view.navigateToCategoriesActivity();
    }
}