package com.hermanowicz.pantry.presenters;

import com.hermanowicz.pantry.db.Category;
import com.hermanowicz.pantry.interfaces.CategoryView;
import com.hermanowicz.pantry.models.CategoryModel;

import java.util.List;

public class CategoryPresenter {

    private final CategoryView view;
    private final CategoryModel model;

    public CategoryPresenter (CategoryView view, CategoryModel model){
        this.view = view;
        this.model = model;
    }

    public void updateCategoryList(){
        List<Category> categoryList = model.getCategoryList();
        view.updateCategoryList(categoryList);
        if(categoryList.size() == 0)
            view.showEmptyCategoryListStatement();
    }

    public void addCategory(Category category) {
        if(model.addCategory(category)) {
            view.onSuccessAddNewCategory();
            view.updateCategoryList(model.getCategoryList());
        }
        else
            view.onErrorAddNewCategory();
    }

    public void navigateToMainActivity() {
        view.navigateToMainActivity();
    }
}