package com.hermanowicz.pantry.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.databinding.DialogNewCategoryBinding;
import com.hermanowicz.pantry.db.Category;
import com.hermanowicz.pantry.db.CategoryDb;
import com.hermanowicz.pantry.db.ProductDb;
import com.hermanowicz.pantry.interfaces.DialogCategoryListener;
import com.hermanowicz.pantry.interfaces.NewCategoryView;
import com.hermanowicz.pantry.models.CategoryModel;
import com.hermanowicz.pantry.models.DatabaseOperations;
import com.hermanowicz.pantry.presenters.NewCategoryPresenter;

import org.jetbrains.annotations.NotNull;

public class NewCategoryDialog extends AppCompatDialogFragment implements NewCategoryView {

    private DialogNewCategoryBinding binding;
    private DialogCategoryListener dialogListener;
    private EditText categoryName, categoryDescription;
    private TextView nameCharCounter, descriptionCharCounter;

    private Activity activity;
    private NewCategoryPresenter presenter;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initView();
        initListeners();

        DatabaseOperations databaseOperations = new DatabaseOperations(ProductDb.getInstance(getContext()), CategoryDb.getInstance(getContext()));
        presenter = new NewCategoryPresenter(this, new CategoryModel(databaseOperations));
        presenter.initCharCounters();

        View view = binding.getRoot();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AppThemeDialog);
        builder.setView(view)
                .setTitle(getString(R.string.CategoriesActivity_new_category))
                .setNegativeButton(getString(R.string.General_cancel), (dialog, which) -> {
                })
                .setPositiveButton(getString(R.string.CategoriesActivity_save), (dialog, which) -> {
                    Category category = new Category();
                    category.setName(binding.nameValue.getText().toString());
                    category.setDescription(binding.descriptionValue.getText().toString());
                    presenter.onPressAddCategory(category);
                });


        return builder.create();
    }

    private void initView() {
        activity = getActivity();

        assert activity != null;
        binding = DialogNewCategoryBinding.inflate(activity.getLayoutInflater());

        categoryName = binding.nameValue;
        categoryDescription = binding.descriptionValue;

        nameCharCounter = binding.nameCharCounter;
        descriptionCharCounter = binding.descriptionCharCounter;
    }

    private void initListeners(){
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
    public void onAddCategory(Category category) {
        dialogListener.onAddCategory(category);
    }

    @Override
    public void updateNameCharCounter(int charCounter, int maxChar) {
        nameCharCounter.setText(String.format("%s: %d/%d", getText(R.string.General_char_counter).toString(), charCounter, maxChar));
    }

    @Override
    public void updateDescriptionCharCounter(int charCounter, int maxChar) {
        descriptionCharCounter.setText(String.format("%s: %d/%d", getText(R.string.General_char_counter).toString(), charCounter, maxChar));
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