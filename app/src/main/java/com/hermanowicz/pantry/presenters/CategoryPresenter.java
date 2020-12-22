package com.hermanowicz.pantry.presenters;

import androidx.annotation.NonNull;

import com.hermanowicz.pantry.db.Category;
import com.hermanowicz.pantry.interfaces.CategoryView;
import com.hermanowicz.pantry.models.CategoryModel;

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