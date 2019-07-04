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
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.filter.FilterModel;
import com.hermanowicz.pantry.interfaces.FilterDialogListener;
import com.hermanowicz.pantry.utils.DateHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <h1>ProductionDateFilterDialog</h1>
 * The dialog window needed to set filters by production date to search for products in the pantry.
 *
 * @author  Mateusz Hermanowicz
 * @version 1.0
 * @since   1.0
 */
public class ProductionDateFilterDialog extends AppCompatDialogFragment {

    @BindView(R.id.edittext_productionDateSince)
    EditText edittextProductionDateSince;
    @BindView(R.id.edittext_productionDateFor)
    EditText edittextProductionDateFor;
    @BindView(R.id.button_clear)
    Button btnClear;

    private Activity activity;
    private FilterDialogListener dialogListener;
    private Calendar calendar = Calendar.getInstance();
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private String filterProductionDateSince;
    private String filterProductionDateFor;
    private DatePickerDialog.OnDateSetListener productionDateSinceListener, productionDateForListener;
    private int year, month, day;

    public ProductionDateFilterDialog(FilterModel filterProduct) {
        this.filterProductionDateSince = filterProduct.getProductionDateSince();
        this.filterProductionDateFor = filterProduct.getProductionDateFor();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        activity = getActivity();

        dateFormat.setLenient(false);

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AppThemeDialog);

        LayoutInflater layoutInflater = activity.getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.dialog_production_date, null);

        ButterKnife.bind(this, view);

        if (filterProductionDateSince != null) {
            DateHelper date = new DateHelper(filterProductionDateSince);
            edittextProductionDateSince.setText(date.getDateInLocalFormat());
        }
        if (filterProductionDateFor != null) {
            DateHelper date = new DateHelper(filterProductionDateFor);
            edittextProductionDateFor.setText(date.getDateInLocalFormat());
        }

        edittextProductionDateSince.setOnClickListener(v -> {
            if (edittextProductionDateSince.length() < 1) {
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

        edittextProductionDateFor.setOnClickListener(v -> {
            if (edittextProductionDateFor.length() < 1) {
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
            edittextProductionDateSince.setText(dateHelper.getDateInLocalFormat());
            filterProductionDateSince = dateHelper.getDateInSqlFormat();
        };

        productionDateForListener = (datePicker, year, month, day) -> {
            calendar.set(year, month, day);
            Date date = calendar.getTime();
            DateHelper dateHelper = new DateHelper(dateFormat.format(date));
            edittextProductionDateFor.setText(dateHelper.getDateInLocalFormat());
            filterProductionDateFor = dateHelper.getDateInSqlFormat();
        };

        btnClear.setOnClickListener(view12 -> {
            edittextProductionDateSince.setText("");
            edittextProductionDateFor.setText("");
            filterProductionDateSince = null;
            filterProductionDateFor = null;
        });

        builder.setView(view)
                .setTitle(getString(R.string.ProductDetailsActivity_production_date))
                .setNegativeButton(getString(R.string.MyPantryActivity_cancel), (dialog, which) -> {
                })
                .setPositiveButton(getString(R.string.MyPantryActivity_set), (dialog, which) -> {
                    if (edittextProductionDateSince.length() < 1)
                        filterProductionDateSince = null;
                    if (edittextProductionDateFor.length() < 1)
                        filterProductionDateFor = null;
                    dialogListener.setFilterProductionDate(filterProductionDateSince, filterProductionDateFor);
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