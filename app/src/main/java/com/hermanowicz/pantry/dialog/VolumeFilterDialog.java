/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

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

public class VolumeFilterDialog extends AppCompatDialogFragment {

    @BindView(R.id.edittext_volumeSince)
    EditText edittextVolumeSince;
    @BindView(R.id.edittext_volumeFor)
    EditText edittextVolumeFor;
    @BindView(R.id.button_clear)
    Button btnClear;

    private IFilterDialogListener dialogListener;
    private int filterVolumeSince, filterVolumeFor;

    public VolumeFilterDialog(int filterVolumeSince, int filterVolumeFor) {
        this.filterVolumeSince = filterVolumeSince;
        this.filterVolumeFor = filterVolumeFor;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Activity activity = getActivity();
        assert activity != null;
        Context context = activity.getApplicationContext();
        Resources resources = context.getResources();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AppThemeDialog);

        LayoutInflater layoutInflater = activity.getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.dialog_volume, null);

        ButterKnife.bind(this, view);

        if (filterVolumeSince >= 0) {
            edittextVolumeSince.setText(String.valueOf(filterVolumeSince));
        } else {
            edittextVolumeSince.setText("");
        }
        if (filterVolumeFor >= 0) {
            edittextVolumeFor.setText(String.valueOf(filterVolumeFor));
        } else {
            edittextVolumeFor.setText("");
        }

        btnClear.setOnClickListener(view18 -> {
            edittextVolumeSince.setText("");
            edittextVolumeFor.setText("");
        });
        builder.setView(view)
                .setTitle(resources.getString(R.string.ProductDetailsActivity_volume))
                .setNegativeButton(resources.getString(R.string.MyPantryActivity_cancel), (dialog, which) -> {
                })
                .setPositiveButton(resources.getString(R.string.MyPantryActivity_set), (dialog, which) -> {
                    try {
                        filterVolumeSince = Integer.valueOf(edittextVolumeSince.getText().toString());
                        if (filterVolumeSince <= -1) {
                            Toast.makeText(context, resources.getString(R.string.Errors_wrong_data), Toast.LENGTH_LONG).show();
                        }
                    } catch (NumberFormatException e) {
                        filterVolumeSince = -1;
                    }
                    try {
                        filterVolumeFor = Integer.valueOf(edittextVolumeFor.getText().toString());
                        if (filterVolumeFor <= -1) {
                            Toast.makeText(context, resources.getString(R.string.Errors_wrong_data), Toast.LENGTH_LONG).show();
                        }
                    } catch (NumberFormatException e) {
                        filterVolumeFor = -1;
                    }
                    if (filterVolumeSince >= 0 || filterVolumeFor >= 0) {
                        if (filterVolumeSince >= 0 && filterVolumeFor >= 0) {
                            if (filterVolumeSince < filterVolumeFor || filterVolumeSince == filterVolumeFor) {
                                dialogListener.setFilterVolume(filterVolumeSince, filterVolumeFor);
                            } else {
                                Toast.makeText(context, resources.getString(R.string.Errors_wrong_data), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            dialogListener.setFilterVolume(filterVolumeSince, filterVolumeFor);
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