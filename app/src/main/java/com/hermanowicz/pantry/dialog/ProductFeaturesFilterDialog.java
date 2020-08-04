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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.filter.Filter;
import com.hermanowicz.pantry.filter.FilterModel;
import com.hermanowicz.pantry.interfaces.FilterDialogListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <h1>ProductFeaturesFilterDialog</h1>
 * The dialog window needed to set filters by product features to search for products in the pantry.
 *
 * @author  Mateusz Hermanowicz
 * @version 1.0
 * @since   1.0
 */

public class ProductFeaturesFilterDialog extends AppCompatDialogFragment {

    @BindView(R.id.checkbox_hasSugar)
    CheckBox checkboxHasSugar;
    @BindView(R.id.checkbox_hasSalt)
    CheckBox checkboxHasSalt;
    @BindView(R.id.button_clear)
    Button btnClear;

    private FilterDialogListener dialogListener;
    private Filter.Set filterHasSugar;
    private Filter.Set filterHasSalt;

    public ProductFeaturesFilterDialog(FilterModel filterProduct) {
        this.filterHasSugar = filterProduct.getHasSugar();
        this.filterHasSalt = filterProduct.getHasSalt();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Activity activity = getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AppThemeDialog);

        LayoutInflater layoutInflater = activity.getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.dialog_product_features, null);

        ButterKnife.bind(this, view);

        if (filterHasSugar == Filter.Set.YES)
            checkboxHasSugar.setChecked(true);

        if (filterHasSalt == Filter.Set.YES)
            checkboxHasSalt.setChecked(true);

        btnClear.setOnClickListener(view13 -> {
            checkboxHasSugar.setChecked(false);
            checkboxHasSalt.setChecked(false);
            filterHasSugar = Filter.Set.DISABLED;
            filterHasSalt = Filter.Set.DISABLED;
        });

        checkboxHasSugar.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (checkboxHasSugar.isChecked())
                        filterHasSugar = Filter.Set.YES;
                    else
                        filterHasSugar = Filter.Set.NO;
                }
        );

        checkboxHasSalt.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (checkboxHasSalt.isChecked())
                        filterHasSalt = Filter.Set.YES;
                    else
                        filterHasSalt = Filter.Set.NO;
                }
        );

        builder.setView(view)
                .setTitle(getString(R.string.ProductDetailsActivity_product_features))
                .setNegativeButton(getString(R.string.MyPantryActivity_cancel), (dialog, which) -> {
                })
                .setPositiveButton(getString(R.string.MyPantryActivity_set), (dialog, which) -> {
                    dialogListener.setProductFeatures(filterHasSugar, filterHasSalt);});
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