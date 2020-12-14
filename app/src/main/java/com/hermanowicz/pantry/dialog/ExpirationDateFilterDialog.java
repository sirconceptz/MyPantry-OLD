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
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.databinding.DialogExpirationDateBinding;
import com.hermanowicz.pantry.filter.FilterModel;
import com.hermanowicz.pantry.interfaces.FilterDialogListener;
import com.hermanowicz.pantry.utils.DateHelper;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <h1>ExpirationDateFilterDialog</h1>
 * The dialog window needed to set filters by expiration date to search for products in the pantry.
 *
 * @author  Mateusz Hermanowicz
 * @version 1.0
 * @since   1.0
 */

public class ExpirationDateFilterDialog extends AppCompatDialogFragment {

    private DialogExpirationDateBinding binding;

    private Activity activity;
    private FilterDialogListener dialogListener;
    private Calendar calendar;
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private String filterExpirationDateSince;
    private String filterExpirationDateFor;
    private DatePickerDialog.OnDateSetListener expirationDateSinceListener, expirationDateForListener;
    private int year, month, day;

    public ExpirationDateFilterDialog(FilterModel filterProduct) {
        this.filterExpirationDateSince = filterProduct.getExpirationDateSince();
        this.filterExpirationDateFor = filterProduct.getExpirationDateFor();
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        activity = getActivity();
        dateFormat.setLenient(false);
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AppThemeDialog);

        binding = DialogExpirationDateBinding.inflate(activity.getLayoutInflater());

        View view = binding.getRoot();

        if (filterExpirationDateSince != null) {
            DateHelper date = new DateHelper(filterExpirationDateSince);
            binding.edittextExpirationDateSince.setText(date.getDateInLocalFormat());
        }
        if (filterExpirationDateFor != null) {
            DateHelper date = new DateHelper(filterExpirationDateFor);
            binding.edittextExpirationDateFor.setText(date.getDateInLocalFormat());
        }

        binding.edittextExpirationDateSince.setOnClickListener(v -> {
            if (binding.edittextExpirationDateSince.length() < 1) {
                year = DateHelper.getActualYear();
                month = DateHelper.getActualMonth();
                day = DateHelper.getActualDay(0);
            } else {
                DateHelper date = new DateHelper(filterExpirationDateSince);
                year = date.getYearFromDate();
                month = date.getMonthFromDate()-1;
                day = date.getDayFromDate();
            }
            DatePickerDialog dialog = new DatePickerDialog(
                    activity,
                    R.style.AppThemeDatePicker,
                    expirationDateSinceListener,
                    year, month, day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });

        binding.edittextExpirationDateFor.setOnClickListener(v -> {
            if (binding.edittextExpirationDateFor.length() < 1) {
                year = DateHelper.getActualYear();
                month = DateHelper.getActualMonth();
                day = DateHelper.getActualDay(0);
            } else {
                DateHelper date = new DateHelper(filterExpirationDateFor);
                year = date.getYearFromDate();
                month = date.getMonthFromDate()-1;
                day = date.getDayFromDate();
            }

            DatePickerDialog dialog = new DatePickerDialog(
                    activity,
                    R.style.AppThemeDatePicker,
                    expirationDateForListener,
                    year, month, day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getDatePicker();
            dialog.show();
        });

        expirationDateSinceListener = (datePicker, year, month, day) -> {
            calendar.set(year, month, day);
            Date date = calendar.getTime();
            DateHelper dateHelper = new DateHelper(dateFormat.format(date));
            binding.edittextExpirationDateSince.setText(dateHelper.getDateInLocalFormat());
            filterExpirationDateSince = dateHelper.getDateInSqlFormat();
        };

        expirationDateForListener = (datePicker, year, month, day) -> {
            calendar.set(year, month, day);
            Date date = calendar.getTime();
            DateHelper dateHelper = new DateHelper(dateFormat.format(date));
            binding.edittextExpirationDateFor.setText(dateHelper.getDateInLocalFormat());
            filterExpirationDateFor = dateHelper.getDateInSqlFormat();
        };

        binding.buttonClear.setOnClickListener(view12 -> {
            binding.edittextExpirationDateSince.setText("");
            binding.edittextExpirationDateFor.setText("");
            filterExpirationDateSince = null;
            filterExpirationDateFor = null;
        });

        builder.setView(view)
                .setTitle(getString(R.string.Product_expiration_date))
                .setNegativeButton(getString(R.string.General_cancel), (dialog, which) -> {
                })
                .setPositiveButton(getString(R.string.MyPantryActivity_set), (dialog, which) -> {
                    if (binding.edittextExpirationDateSince.length() < 1)
                        filterExpirationDateSince = null;
                    if (binding.edittextExpirationDateFor.length() < 1)
                        filterExpirationDateFor = null;
                    dialogListener.setFilterExpirationDate(filterExpirationDateSince, filterExpirationDateFor);
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