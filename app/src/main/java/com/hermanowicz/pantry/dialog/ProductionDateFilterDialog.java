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
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.database.annotations.NotNull;
import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.databinding.DialogProductionDateBinding;
import com.hermanowicz.pantry.filter.FilterModel;
import com.hermanowicz.pantry.interfaces.FilterDialogListener;
import com.hermanowicz.pantry.util.DateHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <h1>ProductionDateFilterDialog</h1>
 * The dialog window needed to set filters by production date to search for products in the pantry.
 *
 * @author  Mateusz Hermanowicz
 * @version 1.0
 * @since   1.0
 */

public class ProductionDateFilterDialog extends AppCompatDialogFragment {

    private DialogProductionDateBinding binding;
    private Activity activity;
    private View view;
    private FilterDialogListener dialogListener;
    private Calendar calendar = Calendar.getInstance();
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private String filterProductionDateSince;
    private String filterProductionDateFor;
    private DatePickerDialog.OnDateSetListener productionDateSinceListener, productionDateForListener;
    private int year, month, day;

    private EditText productionDateSince, productionDateFor;
    private Button clearBtn;

    public ProductionDateFilterDialog(FilterModel filterProduct) {
        this.filterProductionDateSince = filterProduct.getProductionDateSince();
        this.filterProductionDateFor = filterProduct.getProductionDateFor();
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initView();
        setListeners();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AppThemeDialog);

        builder.setView(view)
                .setTitle(getString(R.string.Product_production_date))
                .setNegativeButton(getString(R.string.General_cancel), (dialog, which) -> {
                })
                .setPositiveButton(getString(R.string.MyPantryActivity_set), (dialog, which) -> {
                    if (binding.edittextProductionDateSince.length() < 1)
                        filterProductionDateSince = null;
                    if (binding.edittextProductionDateFor.length() < 1)
                        filterProductionDateFor = null;
                    dialogListener.setFilterProductionDate(filterProductionDateSince, filterProductionDateFor);
                });
        return builder.create();
    }

    private void initView() {
        activity = getActivity();
        dateFormat.setLenient(false);
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        binding = DialogProductionDateBinding.inflate(activity.getLayoutInflater());
        view = binding.getRoot();

        productionDateSince = binding.edittextProductionDateSince;
        productionDateFor = binding.edittextProductionDateFor;
        clearBtn = binding.buttonClear;

        if (filterProductionDateSince != null) {
            DateHelper date = new DateHelper(filterProductionDateSince);
            productionDateSince.setText(date.getDateInLocalFormat());
        }
        if (filterProductionDateFor != null) {
            DateHelper date = new DateHelper(filterProductionDateFor);
            productionDateFor.setText(date.getDateInLocalFormat());
        }
    }

    private void setListeners() {
        productionDateSince.setOnClickListener(v -> {
            if (productionDateSince.length() < 1) {
                year = DateHelper.getActualYear();
                month = DateHelper.getActualMonth();
                day = DateHelper.getActualDay(0);
            } else {
                DateHelper date = new DateHelper(filterProductionDateSince);
                year = date.getYearFromDate();
                month = date.getMonthFromDate()-1;
                day = date.getDayFromDate();
            }
            DatePickerDialog dialog = new DatePickerDialog(
                    activity,
                    R.style.AppThemeDatePicker,
                    productionDateSinceListener,
                    year, month, day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });

        productionDateFor.setOnClickListener(v -> {
            if (productionDateFor.length() < 1) {
                year = DateHelper.getActualYear();
                month = DateHelper.getActualMonth();
                day = DateHelper.getActualDay(0);
            } else {
                DateHelper date = new DateHelper(filterProductionDateFor);
                year = date.getYearFromDate();
                month = date.getMonthFromDate()-1;
                day = date.getDayFromDate();
            }

            DatePickerDialog dialog = new DatePickerDialog(
                    activity,
                    R.style.AppThemeDatePicker,
                    productionDateForListener,
                    year, month, day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getDatePicker();
            dialog.show();
        });

        productionDateSinceListener = (datePicker, year, month, day) -> {
            calendar.set(year, month, day);
            Date date = calendar.getTime();
            DateHelper dateHelper = new DateHelper(dateFormat.format(date));
            productionDateSince.setText(dateHelper.getDateInLocalFormat());
            filterProductionDateSince = dateHelper.getDateInSqlFormat();
        };

        productionDateForListener = (datePicker, year, month, day) -> {
            calendar.set(year, month, day);
            Date date = calendar.getTime();
            DateHelper dateHelper = new DateHelper(dateFormat.format(date));
            productionDateFor.setText(dateHelper.getDateInLocalFormat());
            filterProductionDateFor = dateHelper.getDateInSqlFormat();
        };

        clearBtn.setOnClickListener(view12 -> {
            productionDateSince.setText("");
            productionDateFor.setText("");
            filterProductionDateSince = null;
            filterProductionDateFor = null;
        });
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