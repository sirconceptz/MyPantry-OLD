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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.database.annotations.NotNull;
import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.databinding.DialogWeightBinding;
import com.hermanowicz.pantry.filter.FilterModel;
import com.hermanowicz.pantry.interfaces.FilterDialogListener;

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
    private Activity activity;
    private View view;
    private FilterDialogListener dialogListener;
    private int filterWeightSince, filterWeightFor; // state "-1" for disabled

    private EditText weightSince, weightFor;
    private Button clearBtn;

    public WeightFilterDialog(FilterModel filterProduct) {
        this.filterWeightSince = filterProduct.getWeightSince();
        this.filterWeightFor = filterProduct.getWeightFor();
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initView();
        setListeners();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AppThemeDialog);

        builder.setView(view)
                .setTitle(getString(R.string.Product_weight))
                .setNegativeButton(getString(R.string.General_cancel), (dialog, which) -> {
                })
                .setPositiveButton(getString(R.string.MyPantryActivity_set), (dialog, which) -> onClickPositiveButton());
        return builder.create();
    }

    private void initView() {
        activity = getActivity();
        binding = DialogWeightBinding.inflate(activity.getLayoutInflater());
        view = binding.getRoot();

        weightSince = binding.edittextWeightSince;
        weightFor = binding.edittextWeightFor;
        clearBtn = binding.buttonClear;

        if (filterWeightSince >= 0) weightSince.setText(String.valueOf(filterWeightSince));
        if (filterWeightFor >= 0) weightFor.setText(String.valueOf(filterWeightFor));
    }

    private void setListeners() {
        clearBtn.setOnClickListener(view18 -> {
            weightSince.setText("");
            weightFor.setText("");
        });
    }

    private void onClickPositiveButton() {
        try {
            filterWeightSince = Integer.parseInt(weightSince.getText().toString());
        } catch (NumberFormatException e) {
            filterWeightSince = -1;
        }
        try {
            filterWeightFor = Integer.parseInt(weightFor.getText().toString());
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