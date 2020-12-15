/*
 * Copyright (c) 2020
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
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.databinding.DialogProductFeaturesBinding;
import com.hermanowicz.pantry.filter.Filter;
import com.hermanowicz.pantry.filter.FilterModel;
import com.hermanowicz.pantry.interfaces.FilterDialogListener;

import org.jetbrains.annotations.NotNull;

/**
 * <h1>ProductFeaturesFilterDialog</h1>
 * The dialog window needed to set filters by product features to search for products in the pantry.
 *
 * @author  Mateusz Hermanowicz
 * @version 1.0
 * @since   1.0
 */

public class ProductFeaturesFilterDialog extends AppCompatDialogFragment {

    private DialogProductFeaturesBinding binding;
    private Activity activity;
    private View view;
    private FilterDialogListener dialogListener;
    private Filter.Set filterHasSugar;
    private Filter.Set filterHasSalt;

    private CheckBox productHasSugar, productHasSalt;
    private Button clearBtn;

    public ProductFeaturesFilterDialog(FilterModel filterProduct) {
        this.filterHasSugar = filterProduct.getHasSugar();
        this.filterHasSalt = filterProduct.getHasSalt();
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initView();
        setListeners();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AppThemeDialog);

        builder.setView(view)
                .setTitle(getString(R.string.Product_features))
                .setNegativeButton(getString(R.string.General_cancel), (dialog, which) -> {
                })
                .setPositiveButton(getString(R.string.MyPantryActivity_set), (dialog, which) -> dialogListener.setProductFeatures(filterHasSugar, filterHasSalt));
        return builder.create();
    }

    private void initView() {
        activity = getActivity();
        binding = DialogProductFeaturesBinding.inflate(activity.getLayoutInflater());
        view = binding.getRoot();

        productHasSugar = binding.checkboxHasSugar;
        productHasSalt = binding.checkboxHasSalt;
        clearBtn = binding.buttonClear;

        if (filterHasSugar == Filter.Set.YES)
            binding.checkboxHasSugar.setChecked(true);

        if (filterHasSalt == Filter.Set.YES)
            binding.checkboxHasSalt.setChecked(true);
    }

    private void setListeners() {
        clearBtn.setOnClickListener(view13 -> {
            productHasSugar.setChecked(false);
            productHasSalt.setChecked(false);
            filterHasSugar = Filter.Set.DISABLED;
            filterHasSalt = Filter.Set.DISABLED;
        });

        productHasSugar.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (productHasSugar.isChecked())
                        filterHasSugar = Filter.Set.YES;
                    else
                        filterHasSugar = Filter.Set.NO;
                }
        );

        productHasSalt.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (productHasSalt.isChecked())
                        filterHasSalt = Filter.Set.YES;
                    else
                        filterHasSalt = Filter.Set.NO;
                }
        );
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            dialogListener = (FilterDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }
}