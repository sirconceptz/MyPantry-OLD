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

package com.hermanowicz.pantry.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.database.annotations.NotNull;
import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.databinding.DialogNewCategoryBinding;
import com.hermanowicz.pantry.db.category.Category;
import com.hermanowicz.pantry.interfaces.DialogCategoryListener;
import com.hermanowicz.pantry.interfaces.NewCategoryView;
import com.hermanowicz.pantry.model.CategoryModel;
import com.hermanowicz.pantry.presenter.NewCategoryPresenter;

/**
 * <h1>NewCategoryDialog</h1>
 * The dialog window used to add new category
 *
 * @author Mateusz Hermanowicz
 */

public class NewCategoryDialog extends AppCompatDialogFragment implements NewCategoryView {

    private NewCategoryPresenter presenter;
    private Activity activity;
    private DialogCategoryListener dialogListener;

    private View view;
    private EditText categoryName;
    private EditText categoryDescription;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initView();
        setListeners();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AppThemeDialog);
        builder.setView(view)
                .setTitle(getString(R.string.CategoriesActivity_new_category))
                .setNegativeButton(getString(R.string.General_cancel), (dialog, which) -> {
                })
                .setPositiveButton(getString(R.string.General_save), (dialog, which) -> {
                    Category category = new Category();
                    category.setName(categoryName.getText().toString());
                    category.setDescription(categoryDescription.getText().toString());
                    presenter.onPressAddCategory(category);
                });
        return builder.create();
    }

    private void initView() {
        activity = getActivity();
        com.hermanowicz.pantry.databinding.DialogNewCategoryBinding binding = DialogNewCategoryBinding.inflate(activity.getLayoutInflater());

        categoryName = binding.edittextName;
        categoryDescription = binding.edittextDescription;

        presenter = new NewCategoryPresenter(this, new CategoryModel(getActivity().getApplicationContext()));

        view = binding.getRoot();
    }

    private void setListeners(){
        categoryName.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                presenter.isCategoryNameCorrect(categoryName.getText().toString());
            }
        });

        categoryDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                presenter.isCategoryDescriptionCorrect(categoryDescription.getText().toString());
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            dialogListener = (DialogCategoryListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }

    @Override
    public void onAddCategory(@NonNull Category category) {
        dialogListener.onAddCategory(category);
    }

    @Override
    public void showNameFieldError() {
        categoryName.setError(getText(R.string.Error_char_counter));
    }

    @Override
    public void showDescriptionFieldError() {
        categoryDescription.setError(getText(R.string.Error_char_counter));
    }
}