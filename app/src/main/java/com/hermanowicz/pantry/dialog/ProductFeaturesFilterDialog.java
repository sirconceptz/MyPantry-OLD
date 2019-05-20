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
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.interfaces.DialogListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductFeaturesFilterDialog extends AppCompatDialogFragment {

    @BindView(R.id.checkbox_hasSugar)
    CheckBox checkboxHasSugar;
    @BindView(R.id.checkbox_hasSalt)
    CheckBox checkboxHasSalt;
    @BindView(R.id.button_clear)
    Button btnClear;
    private DialogListener dialogListener;
    private int filterHasSugar;
    private int filterHasSalt;

    public ProductFeaturesFilterDialog(int filterHasSugar, int filterHasSalt) {
        this.filterHasSugar = filterHasSugar;
        this.filterHasSalt = filterHasSalt;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Activity activity = getActivity();
        assert activity != null;
        Context context = activity.getApplicationContext();
        Resources resources = context.getResources();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater layoutInflater = activity.getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.dialog_product_features, null);

        ButterKnife.bind(this, view);

        if (filterHasSugar > 0) {
            checkboxHasSugar.setChecked(true);
        }

        if (filterHasSalt > 0) {
            checkboxHasSalt.setChecked(true);
        }

        btnClear.setOnClickListener(view13 -> {
            checkboxHasSugar.setChecked(false);
            checkboxHasSalt.setChecked(false);
            filterHasSugar = -1;
            filterHasSalt = -1;
        });

        checkboxHasSugar.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (checkboxHasSugar.isChecked())
                        filterHasSugar = 1;
                    else
                        filterHasSugar = 0;
                }
        );

        checkboxHasSalt.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (checkboxHasSalt.isChecked())
                        filterHasSalt = 1;
                    else
                        filterHasSalt = 0;
                }
        );

        builder.setView(view)
                .setTitle(resources.getString(R.string.ProductDetailsActivity_product_features))
                .setNegativeButton(resources.getString(R.string.MyPantryActivity_cancel), (dialog, which) -> {
                })
                .setPositiveButton(resources.getString(R.string.MyPantryActivity_set), (dialog, which) -> {
                    if (filterHasSugar == -1 && filterHasSalt == -1) {
                        dialogListener.clearProductFeatures();
                    } else
                        dialogListener.setProductFeatures(filterHasSugar, filterHasSalt);
                });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            dialogListener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }
}