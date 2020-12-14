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
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.databinding.DialogTasteBinding;
import com.hermanowicz.pantry.filter.FilterModel;
import com.hermanowicz.pantry.interfaces.FilterDialogListener;

import org.jetbrains.annotations.NotNull;

/**
 * <h1>TasteFilterDialog</h1>
 * The dialog window needed to set filters by taste to search for products in the pantry.
 *
 * @author  Mateusz Hermanowicz
 * @version 1.0
 * @since   1.0
 */

public class TasteFilterDialog extends AppCompatDialogFragment {

    private DialogTasteBinding binding;

    private FilterDialogListener dialogListener;
    private String filterTaste;

    public TasteFilterDialog(FilterModel filterProduct) {
        this.filterTaste = filterProduct.getTaste();
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Activity activity = getActivity();
        assert activity != null;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AppThemeDialog);

        binding = DialogTasteBinding.inflate(activity.getLayoutInflater());
        View view = binding.getRoot();

        String[] tasteArray = getResources().getStringArray(R.array.Product_taste_array);

        ArrayAdapter<CharSequence> tasteAdapter = ArrayAdapter.createFromResource(getContext(), R.array.Product_taste_array, R.layout.custom_spinner);
        binding.spinnerTaste.setAdapter(tasteAdapter);

        try {
            for (int i = 1; i < tasteArray.length; i++) {
                if (filterTaste.equals(tasteArray[i])) {
                    binding.spinnerTaste.setSelection(i);
                    binding.spinnerTaste.setBackgroundColor(Color.rgb(200, 255, 200));
                }
            }
        } catch (NullPointerException e) {
            binding.spinnerTaste.setSelection(0);
            binding.spinnerTaste.setBackgroundColor(Color.TRANSPARENT);
        }

        filterTaste = String.valueOf(binding.spinnerTaste.getSelectedItem());

        binding.buttonClear.setOnClickListener(view14 -> {
            binding.spinnerTaste.setSelection(0);
            binding.spinnerTaste.setBackgroundColor(Color.TRANSPARENT);
            filterTaste = null;
        });

        binding.spinnerTaste.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        builder.setView(view)
                .setTitle(getString(R.string.Product_taste))
                .setNegativeButton(getString(R.string.General_cancel), (dialog, which) -> {
                })
                .setPositiveButton(getString(R.string.MyPantryActivity_set), (dialog, which) -> {
                    if(!String.valueOf(binding.spinnerTaste.getSelectedItem()).equals(tasteArray[0]))
                        filterTaste = String.valueOf(binding.spinnerTaste.getSelectedItem());
                    dialogListener.setFilterTaste(filterTaste);
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