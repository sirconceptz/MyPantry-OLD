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

    private FilterDialogListener dialogListener;
    private Filter.Set filterHasSugar;
    private Filter.Set filterHasSalt;

    public ProductFeaturesFilterDialog(FilterModel filterProduct) {
        this.filterHasSugar = filterProduct.getHasSugar();
        this.filterHasSalt = filterProduct.getHasSalt();
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Activity activity = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AppThemeDialog);

        binding = DialogProductFeaturesBinding.inflate(activity.getLayoutInflater());
        View view = binding.getRoot();

        if (filterHasSugar == Filter.Set.YES)
            binding.checkboxHasSugar.setChecked(true);

        if (filterHasSalt == Filter.Set.YES)
            binding.checkboxHasSalt.setChecked(true);

        binding.buttonClear.setOnClickListener(view13 -> {
            binding.checkboxHasSugar.setChecked(false);
            binding.checkboxHasSalt.setChecked(false);
            filterHasSugar = Filter.Set.DISABLED;
            filterHasSalt = Filter.Set.DISABLED;
        });

        binding.checkboxHasSugar.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (binding.checkboxHasSugar.isChecked())
                        filterHasSugar = Filter.Set.YES;
                    else
                        filterHasSugar = Filter.Set.NO;
                }
        );

        binding.checkboxHasSalt.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (binding.checkboxHasSalt.isChecked())
                        filterHasSalt = Filter.Set.YES;
                    else
                        filterHasSalt = Filter.Set.NO;
                }
        );

        builder.setView(view)
                .setTitle(getString(R.string.Product_category))
                .setNegativeButton(getString(R.string.General_cancel), (dialog, which) -> {
                })
                .setPositiveButton(getString(R.string.MyPantryActivity_set), (dialog, which) -> dialogListener.setProductFeatures(filterHasSugar, filterHasSalt));
        return builder.create();
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