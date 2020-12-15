package com.hermanowicz.pantry.presenters;

import androidx.annotation.NonNull;

import com.hermanowicz.pantry.db.Category;
import com.hermanowicz.pantry.interfaces.NewCategoryView;
import com.hermanowicz.pantry.models.CategoryModel;

public class NewCategoryPresenter {

    private final NewCategoryView view;
    private final CategoryModel model;

    public NewCategoryPresenter(@NonNull NewCategoryView view, @NonNull CategoryModel model){
        this.view = view;
        this.model = model;
    }

    public void initCharCounters(){
        view.updateNameCharCounter(0, model.MAX_CHAR_CATEGORY_NAME);
        view.updateDescriptionCharCounter(0, model.MAX_CHAR_CATEGORY_DESCRIPTION);
    }

    public void isCategoryNameCorrect(@NonNull String categoryName){
        if(model.isCategoryNameNotCorrect(categoryName))
            view.showNameFieldError();
        view.updateNameCharCounter(categoryName.length(), model.MAX_CHAR_CATEGORY_NAME);
    }

    public void isCategoryDescriptionCorrect(@NonNull String categoryDescription){
        if(model.isCategoryDescriptionNotCorrect(categoryDescription))
            view.showDescriptionFieldError();
        view.updateDescriptionCharCounter(categoryDescription.length(), model.MAX_CHAR_CATEGORY_DESCRIPTION);
    }

    public void onPressAddCategory(@NonNull Category category) {
        if(model.isCategoryNameNotCorrect(category.getName()))
            view.showNameFieldError();
        else if(model.isCategoryDescriptionNotCorrect(category.getDescription()))
            view.showDescriptionFieldError();
        else
            view.onAddCategory(category);
    }
}