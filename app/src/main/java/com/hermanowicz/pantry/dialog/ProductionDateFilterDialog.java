/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.interfaces.DialogListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductionDateFilterDialog extends AppCompatDialogFragment implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.edittext_productionDateSince)
    EditText edittextProductionDateSince;
    @BindView(R.id.edittext_productionDateFor)
    EditText edittextProductionDateFor;
    @BindView(R.id.button_clear)
    Button btnClear;
    private Context context;
    private Resources resources;
    private DialogListener dialogListener;
    private Calendar calendar;
    private DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private Date dateProductionSince, dateProductionFor;
    private String filterProductionDateSince;
    private String filterProductionDateFor;
    private String productionDateSinceConverted = "";
    private String productionDateForConverted = "";
    private String[] dateArray;
    private DatePickerDialog.OnDateSetListener productionDateSinceListener, productionDateForListener;
    private int year, month, day;

    public ProductionDateFilterDialog(String filterProductionDateSince, String filterProductionDateFor) {
        this.filterProductionDateSince = filterProductionDateSince;
        this.filterProductionDateFor = filterProductionDateFor;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Activity activity = getActivity();
        assert activity != null;
        context = activity.getApplicationContext();
        resources = context.getResources();

        DATE_FORMAT.setLenient(false);

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater layoutInflater = activity.getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.dialog_production_date, null);

        ButterKnife.bind(this, view);

        if (filterProductionDateSince != null) {
            dateArray = filterProductionDateSince.split("-");
            edittextProductionDateSince.setText(dateArray[2] + "." + dateArray[1] + "." + dateArray[0]);
        }
        if (filterProductionDateFor != null) {
            dateArray = filterProductionDateFor.split("-");
            edittextProductionDateFor.setText(dateArray[2] + "." + dateArray[1] + "." + dateArray[0]);
        }

        edittextProductionDateSince.setOnClickListener(v -> {
            if (edittextProductionDateSince.length() < 1) {
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
            } else {
                String date = edittextProductionDateSince.getText().toString();
                dateArray = date.split("\\.");
                year = Integer.valueOf(dateArray[2]);
                month = Integer.valueOf(dateArray[1]);
                day = Integer.valueOf(dateArray[0]);
            }

            DatePickerDialog dialog = new DatePickerDialog(
                    activity,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    productionDateSinceListener,
                    year, month, day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });

        edittextProductionDateFor.setOnClickListener(v -> {
            if (edittextProductionDateFor.length() < 1) {
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
            } else {
                String date = edittextProductionDateFor.getText().toString();
                dateArray = date.split("\\.");
                year = Integer.valueOf(dateArray[2]);
                month = Integer.valueOf(dateArray[1]);
                day = Integer.valueOf(dateArray[0]);
            }

            DatePickerDialog dialog = new DatePickerDialog(
                    activity,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    productionDateForListener,
                    year, month, day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });

        productionDateSinceListener = (datePicker, year, month, day) -> {
            month = month + 1;
            edittextProductionDateSince.setText(day + "." + month + "." + year);
            productionDateSinceConverted = year + "-" + month + "-" + day;
        };

        productionDateForListener = (datePicker, year, month, day) -> {
            month = month + 1;
            edittextProductionDateFor.setText(day + "." + month + "." + year);
            productionDateForConverted = year + "-" + month + "-" + day;
        };

        btnClear.setOnClickListener(view12 -> {
            edittextProductionDateSince.setText("");
            edittextProductionDateFor.setText("");
        });
        builder.setView(view)
                .setTitle(resources.getString(R.string.ProductDetailsActivity_production_date))
                .setNegativeButton(resources.getString(R.string.MyPantryActivity_cancel), (dialog, which) -> {
                })
                .setPositiveButton(resources.getString(R.string.MyPantryActivity_set), (dialog, which) -> {
                    try {
                        filterProductionDateSince = DATE_FORMAT.format(DATE_FORMAT.parse(productionDateSinceConverted));
                        dateProductionSince = DATE_FORMAT.parse(productionDateSinceConverted);
                    } catch (ParseException e) {
                        if (productionDateSinceConverted.length() < 1) {
                            filterProductionDateSince = null;
                        } else {
                            Toast.makeText(context, resources.getString(R.string.Errors_wrong_data), Toast.LENGTH_LONG).show();
                        }
                        e.printStackTrace();
                    }
                    try {
                        filterProductionDateFor = DATE_FORMAT.format(DATE_FORMAT.parse(productionDateForConverted));
                        dateProductionFor = DATE_FORMAT.parse(productionDateForConverted);
                    } catch (ParseException e) {
                        if (productionDateForConverted.length() < 1) {
                            filterProductionDateFor = null;
                        } else {
                            Toast.makeText(context, resources.getString(R.string.Errors_wrong_data), Toast.LENGTH_LONG).show();
                        }
                        e.printStackTrace();
                    }
                    if (filterProductionDateSince == null && filterProductionDateFor == null) {
                        dialogListener.clearFilterProductionDate();
                    } else {
                        try {
                            if (dateProductionSince.compareTo(dateProductionFor) == 0 || dateProductionSince.compareTo(dateProductionFor) < 0) {
                                dialogListener.setFilterProductionDate(filterProductionDateSince, filterProductionDateFor);
                            } else {
                                Toast.makeText(context, resources.getString(R.string.Errors_wrong_data), Toast.LENGTH_LONG).show();
                            }
                        } catch (NullPointerException e) {
                            dialogListener.setFilterProductionDate(filterProductionDateSince, filterProductionDateFor);
                            e.printStackTrace();
                        }

                    }
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

    @Override
    public void onDateSet(@NonNull DatePicker view, int year, int month, int dayOfMonth) {
    }
}