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
import com.hermanowicz.pantry.databinding.DialogVolumeBinding;
import com.hermanowicz.pantry.filter.FilterModel;
import com.hermanowicz.pantry.interfaces.FilterDialogListener;

/**
 * <h1>VolumeFilterDialog</h1>
 * The dialog window needed to set filters by volume of product to search for products in the pantry.
 *
 * @author  Mateusz Hermanowicz
 * @version 1.0
 * @since   1.0
 */

public class VolumeFilterDialog extends AppCompatDialogFragment {

    private DialogVolumeBinding binding;
    private Activity activity;
    private View view;
    private FilterDialogListener dialogListener;
    private int filterVolumeSince, filterVolumeFor; // state "-1" for disabled

    private EditText volumeSince, volumeFor;
    private Button clearBtn;

    public VolumeFilterDialog(FilterModel filterProduct) {
        this.filterVolumeSince = filterProduct.getVolumeSince();
        this.filterVolumeFor = filterProduct.getVolumeFor();
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initView();
        setListeners();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AppThemeDialog);

        builder.setView(view)
                .setTitle(getString(R.string.Product_volume))
                .setNegativeButton(getString(R.string.General_cancel), (dialog, which) -> {
                })
                .setPositiveButton(getString(R.string.MyPantryActivity_set), (dialog, which) -> onClickPositiveButton());
        return builder.create();
    }

    private void initView() {
        activity = getActivity();
        binding = DialogVolumeBinding.inflate(activity.getLayoutInflater());
        view = binding.getRoot();

        volumeSince = binding.edittextVolumeSince;
        volumeFor = binding.edittextVolumeFor;
        clearBtn = binding.buttonClear;

        if (filterVolumeSince >= 0) volumeSince.setText(String.valueOf(filterVolumeSince));
        if (filterVolumeFor >= 0) volumeFor.setText(String.valueOf(filterVolumeFor));
    }

    private void setListeners() {
        clearBtn.setOnClickListener(view18 -> {
            binding.edittextVolumeSince.setText("");
            binding.edittextVolumeFor.setText("");
        });
    }

    private void onClickPositiveButton() {
        try {
            filterVolumeSince = Integer.parseInt(volumeSince.getText().toString());
        } catch (NumberFormatException e) {
            filterVolumeSince = -1;
        }
        try {
            filterVolumeFor = Integer.parseInt(volumeFor.getText().toString());
        } catch (NumberFormatException e) {
            filterVolumeFor = -1;
        }
        if (filterVolumeSince >= 0 && filterVolumeFor >= 0) {
            if (filterVolumeSince < filterVolumeFor || filterVolumeSince == filterVolumeFor) {
                dialogListener.setFilterVolume(filterVolumeSince, filterVolumeFor);
            } else {
                Toast.makeText(getContext(), getString(R.string.Error_wrong_data), Toast.LENGTH_LONG).show();
            }
        }
        else
            dialogListener.setFilterVolume(filterVolumeSince, filterVolumeFor);
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