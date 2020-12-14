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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.databinding.DialogWeightBinding;
import com.hermanowicz.pantry.filter.FilterModel;
import com.hermanowicz.pantry.interfaces.FilterDialogListener;

import org.jetbrains.annotations.NotNull;

/**
 * <h1>WeightFilterDialog</h1>
 * The dialog window needed to set filters by weight of product to search for products in the pantry.
 *
 * @author  Mateusz Hermanowicz
 * @version 1.0
 * @since   1.0
 */

public class WeightFilterDialog extends AppCompatDialogFragment {

    private DialogWeightBinding binding;

    private FilterDialogListener dialogListener;
    private int filterWeightSince, filterWeightFor; // state "-1" for disabled

    public WeightFilterDialog(FilterModel filterProduct) {
        this.filterWeightSince = filterProduct.getWeightSince();
        this.filterWeightFor = filterProduct.getWeightFor();
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Activity activity = getActivity();
        assert activity != null;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AppThemeDialog);

        binding = DialogWeightBinding.inflate(activity.getLayoutInflater());
        View view = binding.getRoot();

        if (filterWeightSince >= 0) binding.edittextWeightSince.setText(String.valueOf(filterWeightSince));

        if (filterWeightFor >= 0) binding.edittextWeightFor.setText(String.valueOf(filterWeightFor));


        binding.buttonClear.setOnClickListener(view18 -> {
            binding.edittextWeightSince.setText("");
            binding.edittextWeightFor.setText("");
        });
        builder.setView(view)
                .setTitle(getString(R.string.Product_weight))
                .setNegativeButton(getString(R.string.General_cancel), (dialog, which) -> {
                })
                .setPositiveButton(getString(R.string.MyPantryActivity_set), (dialog, which) -> {
                    try {
                        filterWeightSince = Integer.parseInt(binding.edittextWeightSince.getText().toString());
                    } catch (NumberFormatException e) {
                        filterWeightSince = -1;
                    }
                    try {
                        filterWeightFor = Integer.parseInt(binding.edittextWeightFor.getText().toString());
                    } catch (NumberFormatException e) {
                        filterWeightFor = -1;
                    }
                    if (filterWeightSince >= 0 && filterWeightFor >= 0) {
                        if (filterWeightSince < filterWeightFor || filterWeightSince == filterWeightFor) {
                            dialogListener.setFilterWeight(filterWeightSince, filterWeightFor);
                        } else {
                            Toast.makeText(getContext(), getString(R.string.Error_wrong_data), Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                        dialogListener.setFilterWeight(filterWeightSince, filterWeightFor);
                });
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