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
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.database.annotations.NotNull;
import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.databinding.DialogProductFeaturesBinding;
import com.hermanowicz.pantry.filter.Filter;
import com.hermanowicz.pantry.filter.FilterModel;
import com.hermanowicz.pantry.interfaces.FilterDialogListener;

/**
 * <h1>ProductFeaturesFilterDialog</h1>
 * The dialog window needed to set filters by product features to search for products in the pantry.
 *
 * @author  Mateusz Hermanowicz
 */

public class ProductFeaturesFilterDialog extends AppCompatDialogFragment {

    private Activity activity;
    private FilterDialogListener dialogListener;
    private Filter.Set filterHasSugar;
    private Filter.Set filterHasSalt;
    private Filter.Set filterIsBio;
    private Filter.Set filterIsVege;

    private View view;
    private CheckBox productHasSugar;
    private CheckBox productHasSalt;
    private CheckBox productIsBio;
    private CheckBox productIsVege;
    private Button clearBtn;

    public ProductFeaturesFilterDialog(@NonNull FilterModel filterProduct) {
        this.filterHasSugar = filterProduct.getHasSugar();
        this.filterHasSalt = filterProduct.getHasSalt();
        this.filterIsBio = filterProduct.getIsBio();
        this.filterIsVege = filterProduct.getIsVege();
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
                .setPositiveButton(getString(R.string.MyPantryActivity_set), (dialog, which) ->
                        dialogListener.setFilterProductFeatures(filterHasSugar, filterHasSalt, filterIsBio, filterIsVege));
        return builder.create();
    }

    private void initView() {
        activity = getActivity();
        com.hermanowicz.pantry.databinding.DialogProductFeaturesBinding binding = DialogProductFeaturesBinding.inflate(activity.getLayoutInflater());
        view = binding.getRoot();

        productHasSugar = binding.checkboxHasSugar;
        productHasSalt = binding.checkboxHasSalt;
        productIsBio = binding.checkboxIsBio;
        productIsVege = binding.checkboxIsVege;
        clearBtn = binding.buttonClear;

        if (filterHasSugar == Filter.Set.YES)
            binding.checkboxHasSugar.setChecked(true);
        if (filterHasSalt == Filter.Set.YES)
            binding.checkboxHasSalt.setChecked(true);
        if (filterIsBio == Filter.Set.YES)
            binding.checkboxIsBio.setChecked(true);
        if (filterIsVege == Filter.Set.YES)
            binding.checkboxIsVege.setChecked(true);
    }

    private void setListeners() {
        clearBtn.setOnClickListener(view13 -> {
            productHasSugar.setChecked(false);
            productHasSalt.setChecked(false);
            productIsBio.setChecked(false);
            productIsVege.setChecked(false);
            filterHasSugar = Filter.Set.DISABLED;
            filterHasSalt = Filter.Set.DISABLED;
            filterIsBio = Filter.Set.DISABLED;
            filterIsVege = Filter.Set.DISABLED;
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

        productIsBio.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (productIsBio.isChecked())
                        filterIsBio = Filter.Set.YES;
                    else
                        filterIsBio = Filter.Set.NO;
                }
        );

        productIsVege.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (productIsVege.isChecked())
                        filterIsVege = Filter.Set.YES;
                    else
                        filterIsVege = Filter.Set.NO;
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