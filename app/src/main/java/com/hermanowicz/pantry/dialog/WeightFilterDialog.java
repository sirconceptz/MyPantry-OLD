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
import android.content.res.Resources;
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
import com.hermanowicz.pantry.interfaces.IFilterDialogListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeightFilterDialog extends AppCompatDialogFragment {

    @BindView(R.id.edittext_weightSince)
    EditText edittextWeightSince;
    @BindView(R.id.edittext_weightFor)
    EditText edittextWeightFor;
    @BindView(R.id.button_clear)
    Button btnClear;

    private IFilterDialogListener dialogListener;
    private int filterWeightSince, filterWeightFor;

    public WeightFilterDialog(int filterWeightSince, int filterWeightFor) {
        this.filterWeightSince = filterWeightSince;
        this.filterWeightFor = filterWeightFor;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Activity activity = getActivity();
        assert activity != null;
        Context context = activity.getApplicationContext();
        Resources resources = context.getResources();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AppThemeDialog);

        LayoutInflater layoutInflater = activity.getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.dialog_weight, null);

        ButterKnife.bind(this, view);

        if (filterWeightSince >= 0) {
            edittextWeightSince.setText(String.valueOf(filterWeightSince));
        } else {
            edittextWeightSince.setText("");
        }
        if (filterWeightFor >= 0) {
            edittextWeightFor.setText(String.valueOf(filterWeightFor));
        } else {
            edittextWeightFor.setText("");
        }

        btnClear.setOnClickListener(view18 -> {
            edittextWeightSince.setText("");
            edittextWeightFor.setText("");
        });
        builder.setView(view)
                .setTitle(resources.getString(R.string.ProductDetailsActivity_weight))
                .setNegativeButton(resources.getString(R.string.MyPantryActivity_cancel), (dialog, which) -> {
                })
                .setPositiveButton(resources.getString(R.string.MyPantryActivity_set), (dialog, which) -> {
                    try {
                        filterWeightSince = Integer.valueOf(edittextWeightSince.getText().toString());
                        if (filterWeightSince <= -1) {
                            Toast.makeText(context, resources.getString(R.string.Errors_wrong_data), Toast.LENGTH_LONG).show();
                        }
                    } catch (NumberFormatException e) {
                        filterWeightSince = -1;
                    }
                    try {
                        filterWeightFor = Integer.valueOf(edittextWeightFor.getText().toString());
                        if (filterWeightFor <= -1) {
                            Toast.makeText(context, resources.getString(R.string.Errors_wrong_data), Toast.LENGTH_LONG).show();
                        }
                    } catch (NumberFormatException e) {
                        filterWeightFor = -1;
                    }
                    if (filterWeightSince >= 0 || filterWeightFor >= 0) {
                        if (filterWeightSince >= 0 && filterWeightFor >= 0) {
                            if (filterWeightSince < filterWeightFor || filterWeightSince == filterWeightFor) {
                                dialogListener.setFilterWeight(filterWeightSince, filterWeightFor);
                            } else {
                                Toast.makeText(context, resources.getString(R.string.Errors_wrong_data), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            dialogListener.setFilterWeight(filterWeightSince, filterWeightFor);
                        }
                    } else {
                        dialogListener.clearFilterVolume();
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            dialogListener = (IFilterDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }
}