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
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.databinding.DialogChooseProductToCopyBinding;
import com.hermanowicz.pantry.interfaces.ProductToCopyView;

/**
 * <h1>WeightFilterDialog</h1>
 * The dialog window needed to set filters by weight of product to search for products in the pantry.
 *
 * @author Mateusz Hermanowicz
 */

public class ChooseProductToCopyDialog extends AppCompatDialogFragment {

    private Activity activity;
    private final String[] namesProductList;

    private View view;
    private ProductToCopyView productToCopyView;
    private Spinner chooseProductToCopySpinner;

    public ChooseProductToCopyDialog(@NonNull String[] namesProductList) {
        this.namesProductList = namesProductList;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initView();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AppThemeDialog);

        builder.setView(view).setNegativeButton(getString(R.string.General_cancel), (dialog, which) -> {
        })
                .setPositiveButton(getString(R.string.General_choose), (dialog, which) -> {
                    productToCopyView.setSelectedProductToCopy(chooseProductToCopySpinner.getSelectedItemPosition());
                });
        return builder.create();
    }

    private void initView() {
        activity = getActivity();
        com.hermanowicz.pantry.databinding.DialogChooseProductToCopyBinding binding = DialogChooseProductToCopyBinding.inflate(activity.getLayoutInflater());
        view = binding.getRoot();
        chooseProductToCopySpinner = view.findViewById(R.id.spinner_chooseProductToCopy);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner, namesProductList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseProductToCopySpinner.setAdapter(adapter);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            productToCopyView = (ProductToCopyView) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }
}