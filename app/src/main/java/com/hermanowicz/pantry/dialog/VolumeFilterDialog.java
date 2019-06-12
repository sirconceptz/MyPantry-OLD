/*
 * Copyright (c) 2019
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
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.filter.FilterModel;
import com.hermanowicz.pantry.interfaces.FilterDialogListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <h1>VolumeFilterDialog</h1>
 * The dialog window needed to set filters by volume of product to search for products in the pantry.
 *
 * @author  Mateusz Hermanowicz
 * @version 1.0
 * @since   1.0
 */
public class VolumeFilterDialog extends AppCompatDialogFragment {

    @BindView(R.id.edittext_volumeSince)
    EditText edittextVolumeSince;
    @BindView(R.id.edittext_volumeFor)
    EditText edittextVolumeFor;
    @BindView(R.id.button_clear)
    Button btnClear;

    private FilterDialogListener dialogListener;
    private int filterVolumeSince, filterVolumeFor; // state "-1" for disabled

    public VolumeFilterDialog(FilterModel filterProduct) {
        this.filterVolumeSince = filterProduct.getVolumeSince();
        this.filterVolumeFor = filterProduct.getVolumeFor();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Activity activity = getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AppThemeDialog);

        LayoutInflater layoutInflater = activity.getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.dialog_volume, null);

        ButterKnife.bind(this, view);

        if (filterVolumeSince >= 0) edittextVolumeSince.setText(String.valueOf(filterVolumeSince));
        if (filterVolumeFor >= 0) edittextVolumeFor.setText(String.valueOf(filterVolumeFor));

        btnClear.setOnClickListener(view18 -> {
            edittextVolumeSince.setText("");
            edittextVolumeFor.setText("");
        });
        builder.setView(view)
                .setTitle(getString(R.string.ProductDetailsActivity_volume))
                .setNegativeButton(getString(R.string.MyPantryActivity_cancel), (dialog, which) -> {
                })
                .setPositiveButton(getString(R.string.MyPantryActivity_set), (dialog, which) -> {
                    try {
                        filterVolumeSince = Integer.valueOf(edittextVolumeSince.getText().toString());
                    } catch (NumberFormatException e) {
                        filterVolumeSince = -1;
                    }
                    try {
                        filterVolumeFor = Integer.valueOf(edittextVolumeFor.getText().toString());
                    } catch (NumberFormatException e) {
                        filterVolumeFor = -1;
                    }
                    if (filterVolumeSince >= 0 && filterVolumeFor >= 0) {
                        if (filterVolumeSince < filterVolumeFor || filterVolumeSince == filterVolumeFor) {
                            dialogListener.setFilterVolume(filterVolumeSince, filterVolumeFor);
                        } else {
                            Toast.makeText(getContext(), getString(R.string.Errors_wrong_data), Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                        dialogListener.setFilterVolume(filterVolumeSince, filterVolumeFor);
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