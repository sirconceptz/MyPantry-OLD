/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <h1>DialogManager</h1>
 * Adapter for dialog windows. Dialog window will be opened after choosing the type of dialog.
 *
 * @author  Mateusz Hermanowicz
 * @version 1.0
 * @since   1.0
 */
public class DialogManager extends AppCompatDialogFragment implements DatePickerDialog.OnDateSetListener {

    private Context                            context;
    private EditText                           editTextName, editTextExpirationDateSince, editTextExpirationDateFor,
                                               editTextProductionDateSince, editTextProductionDateFor, editTextVolumeSince,
                                               editTextVolumeFor, editTextWeightSince, editTextWeightFor;
    private Spinner                            spinnerTypeOfProduct, spinnerProductFeatures, spinnerTaste;
    private CheckBox                           checkBoxHasSugar, checkBoxHasSalt;
    private String[]                           productFeaturesArray, productTypesArray, filterTypeOfProductArray;
    private ArrayAdapter<CharSequence>         productFeaturesAdapter;
    private String                             filterName, filterExpirationDateSince, filterExpirationDateFor,
                                               filterProductionDateSince, filterProductionDateFor, filterTypeOfProduct,
                                               filterProductFeatures, filterTaste, dialogType, selectedProductType,
                                               expirationDateSinceConverted = "", expirationDateForConverted = "";
    private int                                filterWeightSince, filterWeightFor, filterVolumeSince, filterVolumeFor, filterHasSugar, filterHasSalt;
    private DialogListener                     dialogListener;
    private DateFormat                         DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private Date                               dateExpirationSince, dateExpirationFor, dateProductionSince, dateProductionFor;
    private int                                day, month, year;
    private boolean                            isTypeOfProductTouched, isProductFeaturesTouched;
    private Calendar                           calendar;
    private DatePickerDialog.OnDateSetListener productionDateSinceListener, productionDateForListener,
                                               expirationDateSinceListener, expirationDateForListener;

    /**
     * Setter for dialog type.
     *
     * @param dialogType this value is needed to choose a type of dialog window
     */
    public void setDialogType(@NonNull String dialogType) {
        this.dialogType = dialogType;
    }

    public void setFilterName(@NonNull String filterName) {
        this.filterName = filterName;
    }

    public void setFilterExpirationDate(String filterExpirationDateSince, String filterExpirationDateFor) {
        this.filterExpirationDateSince = filterExpirationDateSince;
        this.filterExpirationDateFor   = filterExpirationDateFor;
    }

    public void setFilterProductionDate(String filterProductionDateSince, String filterProductionDateFor) {
        this.filterProductionDateSince = filterProductionDateSince;
        this.filterProductionDateFor   = filterProductionDateFor;
    }

    public void setFilterTypeOfProduct(@NonNull String filterTypeOfProduct, String filterProductFeatures){
        this.filterTypeOfProduct   = filterTypeOfProduct;
        this.filterProductFeatures = filterProductFeatures;
    }

    public void setFilterVolume(int filterVolumeSince, int filterVolumeFor) {
        this.filterVolumeSince = filterVolumeSince;
        this.filterVolumeFor   = filterVolumeFor;
    }

    public void setFilterWeight(int filterWeightSince, int filterWeightFor) {
        this.filterWeightSince = filterWeightSince;
        this.filterWeightFor   = filterWeightFor;
    }

    public void setFilterProductFeatures(int filterHasSugarValue, int filterHasSaltValue){
        this.filterHasSugar = filterHasSugarValue;
        this.filterHasSalt = filterHasSaltValue;
    }

    public void setFilterTaste(@NonNull String filterTaste){
        this.filterTaste = filterTaste;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Activity activity = getActivity();
        assert activity != null;
        context = activity.getApplicationContext();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater layoutInflater = activity.getLayoutInflater();

        DATE_FORMAT.setLenient(false);

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        if(dialogType.equals(getResources().getString(R.string.ProductDetailsActivity_name))) {
            View view    = layoutInflater.inflate(R.layout.dialog_name, null);
            editTextName = view.findViewById(R.id.name);
            Button btnClear = view.findViewById(R.id.btn_clear);

            if(filterName != null && !filterName.equals("")){
                editTextName.setText(filterName);
            }

            btnClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editTextName.setText("");
                    filterName = null;
                    }});

            builder.setView(view)
                    .setTitle(dialogType)
                    .setNegativeButton(getResources().getString(R.string.MyPantryActivity_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setPositiveButton(getResources().getString(R.string.MyPantryActivity_set), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            filterName = editTextName.getText().toString();

                            if (filterName.equals("")){
                                dialogListener.clearFilterName();
                            }
                            else{
                                dialogListener.applyFilterName(filterName);
                            }
                        }
                    });
        }
        else if(dialogType.equals(getResources().getString(R.string.ProductDetailsActivity_expiration_date))) {
            View view                   = layoutInflater.inflate(R.layout.dialog_expiration_date, null);
            editTextExpirationDateSince = view.findViewById(R.id.expiration_date_since);
            editTextExpirationDateFor   = view.findViewById(R.id.expiration_date_for);
            Button btnClear = view.findViewById(R.id.btn_clear);

            if(filterExpirationDateSince != null){
                editTextExpirationDateSince.setText(filterExpirationDateSince);
            }
            if(filterExpirationDateFor != null){
                editTextExpirationDateFor.setText(filterExpirationDateFor);
            }

            editTextExpirationDateSince.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editTextExpirationDateSince.length() < 1) {
                        year = calendar.get(Calendar.YEAR);
                        month = calendar.get(Calendar.MONTH);
                        day = calendar.get(Calendar.DAY_OF_MONTH);
                    } else {
                        String[] splitedDate = MyPantryActivity.splitDate(editTextExpirationDateSince.getText().toString());
                        year = Integer.valueOf(splitedDate[2]);
                        month = Integer.valueOf(splitedDate[1]);
                        day = Integer.valueOf(splitedDate[0]);
                    }

                    DatePickerDialog dialog = new DatePickerDialog(
                            context,
                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                            expirationDateSinceListener,
                            year,month,day);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
            });

            editTextExpirationDateFor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editTextExpirationDateFor.length() < 1) {
                        year  = calendar.get(Calendar.YEAR);
                        month = calendar.get(Calendar.MONTH);
                        day   = calendar.get(Calendar.DAY_OF_MONTH);
                    }
                    else{
                        String[] splitedDate = MyPantryActivity.splitDate(editTextExpirationDateFor.getText().toString());
                        year  = Integer.valueOf(splitedDate[2]);
                        month = Integer.valueOf(splitedDate[1]);
                        day   = Integer.valueOf(splitedDate[0]);
                    }

                    DatePickerDialog dialog = new DatePickerDialog(
                            context,
                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                            expirationDateForListener,
                            year,month,day);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
            });

            expirationDateSinceListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    month = month + 1;
                    editTextExpirationDateSince.setText(day + "." + month + "." + year);
                    expirationDateSinceConverted = year + "-" + month + "-" + day;
                }
            };

            expirationDateForListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    month = month + 1;
                    editTextExpirationDateFor.setText(day + "." + month + "." + year);
                    expirationDateForConverted = year + "-" + month + "-" + day;
                }
            };

            btnClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editTextExpirationDateSince.setText("");
                    editTextExpirationDateFor.setText("");
                }});
            builder.setView(view)
                    .setTitle(dialogType)
                    .setNegativeButton(getResources().getString(R.string.MyPantryActivity_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setPositiveButton(getResources().getString(R.string.MyPantryActivity_set), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try{
                                filterExpirationDateSince = DATE_FORMAT.format(DATE_FORMAT.parse(expirationDateSinceConverted));
                                dateExpirationSince = DATE_FORMAT.parse(expirationDateSinceConverted);
                            }
                            catch (ParseException e) {
                                if(expirationDateSinceConverted.length() < 1) {
                                    filterExpirationDateSince = null;
                                }
                                else {
                                    Toast.makeText(context, getResources().getString(R.string.Errors_wrong_data), Toast.LENGTH_LONG).show();
                                }
                                e.printStackTrace();
                            }
                            try{
                                    filterExpirationDateFor = DATE_FORMAT.format(DATE_FORMAT.parse(expirationDateForConverted));
                                    dateExpirationFor = DATE_FORMAT.parse(expirationDateForConverted);
                            }
                            catch (ParseException e) {
                                if(expirationDateForConverted.length() < 1) {
                                    filterExpirationDateFor = null;
                                }
                                else {
                                    Toast.makeText(context, getResources().getString(R.string.Errors_wrong_data), Toast.LENGTH_LONG).show();
                                }
                                e.printStackTrace();
                            }
                            if (filterExpirationDateSince == null && filterExpirationDateFor == null){
                                dialogListener.clearFilterExpirationDate();
                            }
                            else {
                                try {
                                    if (dateExpirationSince.compareTo(dateExpirationFor) == 0 || dateExpirationSince.compareTo(dateExpirationFor) < 0) {
                                        dialogListener.applyFilterExpirationDate(filterExpirationDateSince, filterExpirationDateFor);
                                    } else {
                                        Toast.makeText(context, getResources().getString(R.string.Errors_wrong_data), Toast.LENGTH_LONG).show();
                                    }
                                }
                                catch(NullPointerException e){
                                    dialogListener.applyFilterExpirationDate(filterExpirationDateSince, filterExpirationDateFor);
                                    e.printStackTrace();
                                }

                            }
                        }
                    });
        }
        else if(dialogType.equals(getResources().getString(R.string.ProductDetailsActivity_production_date))) {
            View view                   = layoutInflater.inflate(R.layout.dialog_production_date, null);
            editTextProductionDateSince = view.findViewById(R.id.production_date_since);
            editTextProductionDateFor   = view.findViewById(R.id.production_date_for);
            Button btnClear = view.findViewById(R.id.btn_clear);

            if(filterProductionDateSince != null){
                editTextProductionDateSince.setText(filterProductionDateSince);
            }
            if(filterProductionDateFor != null){
                editTextProductionDateFor.setText(filterProductionDateFor);
            }

            editTextProductionDateSince.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editTextProductionDateSince.length() < 1) {
                        year  = calendar.get(Calendar.YEAR);
                        month = calendar.get(Calendar.MONTH);
                        day   = calendar.get(Calendar.DAY_OF_MONTH);
                    }
                    else{
                        String[] splitedDate = MyPantryActivity.splitDate(editTextProductionDateSince.getText().toString());
                        year  = Integer.valueOf(splitedDate[2]);
                        month = Integer.valueOf(splitedDate[1]);
                        day   = Integer.valueOf(splitedDate[0]);
                    }

                    DatePickerDialog dialog = new DatePickerDialog(
                            context,
                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                            productionDateSinceListener,
                            year,month,day);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
            });

            editTextProductionDateFor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editTextProductionDateFor.length() < 1) {
                        year  = calendar.get(Calendar.YEAR);
                        month = calendar.get(Calendar.MONTH);
                        day   = calendar.get(Calendar.DAY_OF_MONTH);
                    }
                    else{
                        String[] splitedDate = MyPantryActivity.splitDate(editTextProductionDateFor.getText().toString());
                        year  = Integer.valueOf(splitedDate[2]);
                        month = Integer.valueOf(splitedDate[1]);
                        day   = Integer.valueOf(splitedDate[0]);
                    }

                    DatePickerDialog dialog = new DatePickerDialog(
                            context,
                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                            productionDateForListener,
                            year,month,day);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
            });

            productionDateSinceListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    month = month + 1;
                    String date = year + "-" + month + "-" + day;
                    editTextProductionDateSince.setText(date);
                }
            };

            productionDateForListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    month = month + 1;
                    String date = year + "-" + month + "-" + day;
                    editTextProductionDateFor.setText(date);
                }
            };

            btnClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editTextProductionDateSince.setText("");
                    editTextProductionDateFor.setText("");
                }});
            builder.setView(view)
                    .setTitle(dialogType)
                    .setNegativeButton(getResources().getString(R.string.MyPantryActivity_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton(getResources().getString(R.string.MyPantryActivity_set), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try{
                                filterProductionDateSince = DATE_FORMAT.format(DATE_FORMAT.parse(editTextProductionDateSince.getText().toString()));
                                dateProductionSince = DATE_FORMAT.parse(editTextProductionDateSince.getText().toString());
                            }
                            catch (ParseException e) {
                                if(editTextProductionDateSince.length() < 1) {
                                    filterProductionDateSince = null;
                                }
                                else {
                                    Toast.makeText(context, getResources().getString(R.string.Errors_wrong_data), Toast.LENGTH_LONG).show();
                                }
                                e.printStackTrace();
                            }
                            try{
                                filterProductionDateFor = DATE_FORMAT.format(DATE_FORMAT.parse(editTextProductionDateFor.getText().toString()));
                                dateProductionFor = DATE_FORMAT.parse(editTextProductionDateFor.getText().toString());
                            }
                            catch (ParseException e) {
                                if(editTextProductionDateFor.length() < 1) {
                                    filterProductionDateFor = null;
                                }
                                else {
                                    Toast.makeText(context, getResources().getString(R.string.Errors_wrong_data), Toast.LENGTH_LONG).show();
                                }
                                e.printStackTrace();
                            }
                            if (filterProductionDateSince == null && filterProductionDateFor == null){
                                dialogListener.clearFilterProductionDate();
                            }
                            else {
                                try {
                                    if (dateProductionSince.compareTo(dateProductionFor) == 0 || dateProductionSince.compareTo(dateProductionFor) < 0) {
                                        dialogListener.applyFilterProductionDate(filterProductionDateSince, filterProductionDateFor);
                                    } else {
                                        Toast.makeText(context, getResources().getString(R.string.Errors_wrong_data), Toast.LENGTH_LONG).show();
                                    }
                                }
                                catch(NullPointerException e){
                                    dialogListener.applyFilterProductionDate(filterProductionDateSince, filterProductionDateFor);
                                    e.printStackTrace();
                                }

                            }
                        }
                    });
        }
        else if(dialogType.equals(getResources().getString(R.string.ProductDetailsActivity_product_type))) {
            View view              = layoutInflater.inflate(R.layout.dialog_type_of_product, null);
            spinnerTypeOfProduct   = view.findViewById(R.id.type_of_product);
            spinnerProductFeatures = view.findViewById(R.id.product_features);
            Button btnClear = view.findViewById(R.id.btn_clear);

            filterTypeOfProductArray = getResources().getStringArray(R.array.ProductDetailsActivity_type_of_product_array);

            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_choose_array, android.R.layout.simple_spinner_item);
            productFeaturesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerProductFeatures.setAdapter(productFeaturesAdapter);

            updateProductFeaturesSpinnerAndSelectTypeOfProduct();

            if(filterProductFeatures != null) {
                try {
                    for (int i = 0; i < productFeaturesArray.length; i++)
                        if (productFeaturesArray[i].equals(filterProductFeatures)) {
                            spinnerProductFeatures.setSelection(i);
                            spinnerProductFeatures.setBackgroundColor(Color.rgb(200, 255, 200));
                        }
                } catch (NullPointerException e) {
                    spinnerProductFeatures.setBackgroundColor(Color.TRANSPARENT);
                }
            }

            btnClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    spinnerTypeOfProduct.setSelection(0, false);
                    spinnerTypeOfProduct.setBackgroundColor(Color.TRANSPARENT);
                    filterTypeOfProduct = null;
                    spinnerProductFeatures.setSelection(0, false);
                    spinnerProductFeatures.setBackgroundColor(Color.TRANSPARENT);
                    filterProductFeatures = null;
                }});

            spinnerTypeOfProduct.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    isTypeOfProductTouched = true;
                    return false;
                }
            });
            spinnerTypeOfProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    if(isTypeOfProductTouched) {
                        selectedProductType = String.valueOf(spinnerTypeOfProduct.getSelectedItem());
                        productTypesArray   = getResources().getStringArray(R.array.ProductDetailsActivity_type_of_product_array);
                        updateProductFeaturesSpinner();
                        if(selectedProductType.equals(filterTypeOfProductArray[0]))
                            filterTypeOfProduct = null;
                        else
                            filterTypeOfProduct = String.valueOf(spinnerTypeOfProduct.getSelectedItem());
                        }
                    }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            spinnerProductFeatures.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    isProductFeaturesTouched = true;
                    return false;
                }
            });

            spinnerProductFeatures.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    if(isProductFeaturesTouched) {
                        if(String.valueOf(spinnerProductFeatures.getSelectedItem()).equals(productFeaturesArray[0])){
                            filterProductFeatures = null;
                        } else {
                            filterProductFeatures = String.valueOf(spinnerProductFeatures.getSelectedItem());
                        }
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });

            builder.setView(view)
                    .setTitle(dialogType)
                    .setNegativeButton(getResources().getString(R.string.MyPantryActivity_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setPositiveButton(getResources().getString(R.string.MyPantryActivity_set), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(filterTypeOfProduct != null){
                                dialogListener.applyFilterTypeOfProduct(filterTypeOfProduct, filterProductFeatures);
                            }
                            else{
                                dialogListener.clearFilterTypeOfProduct();
                            }
                        }
                    });
        }
        else if(dialogType.equals(getResources().getString(R.string.ProductDetailsActivity_volume))) {
            View view           = layoutInflater.inflate(R.layout.dialog_volume, null);
            editTextVolumeSince = view.findViewById(R.id.volume_since);
            editTextVolumeFor   = view.findViewById(R.id.volume_for);
            Button btnClear = view.findViewById(R.id.btn_clear);

            if (filterVolumeSince >= 0){
                editTextVolumeSince.setText(String.valueOf(filterVolumeSince));
            }
            else {
                editTextVolumeSince.setText("");
            }
            if (filterVolumeFor >= 0){
                editTextVolumeFor.setText(String.valueOf(filterVolumeFor));
            }
            else {
                editTextVolumeFor.setText("");
            }

            btnClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editTextVolumeSince.setText("");
                    editTextVolumeFor.setText("");
                }});
            builder.setView(view)
                    .setTitle(dialogType)
                    .setNegativeButton(getResources().getString(R.string.MyPantryActivity_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setPositiveButton(getResources().getString(R.string.MyPantryActivity_set), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                filterVolumeSince = Integer.valueOf(editTextVolumeSince.getText().toString());
                                if(filterVolumeSince <= -1){
                                    Toast.makeText(context, getResources().getString(R.string.Errors_wrong_data), Toast.LENGTH_LONG).show();
                                }
                            }
                            catch(NumberFormatException e){
                                filterVolumeSince = -1;
                            }
                            try {
                                filterVolumeFor = Integer.valueOf(editTextVolumeFor.getText().toString());
                                if(filterVolumeFor <= -1){
                                    Toast.makeText(context, getResources().getString(R.string.Errors_wrong_data), Toast.LENGTH_LONG).show();
                                }
                            }
                            catch(NumberFormatException e){
                                filterVolumeFor = -1;
                            }
                            if (filterVolumeSince >= 0 || filterVolumeFor >= 0){
                                if(filterVolumeSince >= 0 && filterVolumeFor >= 0){
                                    if (filterVolumeSince < filterVolumeFor || filterVolumeSince == filterVolumeFor){
                                        dialogListener.applyFilterVolume(filterVolumeSince, filterVolumeFor);
                                    }
                                    else{
                                        Toast.makeText(context, getResources().getString(R.string.Errors_wrong_data), Toast.LENGTH_LONG).show();
                                    }
                                }
                                else {
                                    dialogListener.applyFilterVolume(filterVolumeSince, filterVolumeFor);
                                }
                            }
                            else {
                                dialogListener.clearFilterVolume();
                            }
                        }
                    });
        }
        else if(dialogType.equals(getResources().getString(R.string.ProductDetailsActivity_weight))) {
            View view           = layoutInflater.inflate(R.layout.dialog_weight, null);
            editTextWeightSince = view.findViewById(R.id.weight_since);
            editTextWeightFor   = view.findViewById(R.id.weight_for);
            Button btnClear = view.findViewById(R.id.btn_clear);

            if (filterWeightSince >= 0){
                editTextWeightSince.setText(String.valueOf(filterWeightSince));
            }
            else {
                editTextWeightSince.setText("");
            }
            if (filterWeightFor >= 0){
                editTextWeightFor.setText(String.valueOf(filterWeightFor));
            }
            else {
                editTextWeightFor.setText("");
            }

            btnClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editTextWeightSince.setText("");
                    editTextWeightFor.setText("");
                }});
            builder.setView(view)
                    .setTitle(dialogType)
                    .setNegativeButton(getResources().getString(R.string.MyPantryActivity_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setPositiveButton(getResources().getString(R.string.MyPantryActivity_set), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                filterWeightSince = Integer.valueOf(editTextWeightSince.getText().toString());
                                if(filterWeightSince <= -1){
                                    Toast.makeText(context, getResources().getString(R.string.Errors_wrong_data), Toast.LENGTH_LONG).show();
                                }
                            }
                            catch(NumberFormatException e){
                                filterWeightSince = -1;
                            }
                            try {
                                filterWeightFor = Integer.valueOf(editTextWeightFor.getText().toString());
                                if(filterWeightFor <= -1){
                                    Toast.makeText(context, getResources().getString(R.string.Errors_wrong_data), Toast.LENGTH_LONG).show();
                                }
                            }
                            catch(NumberFormatException e){
                                filterWeightFor = -1;
                            }
                            if (filterWeightSince >= 0 || filterWeightFor >= 0){
                                if(filterWeightSince >= 0 && filterWeightFor >= 0){
                                    if (filterWeightSince < filterWeightFor || filterWeightSince == filterWeightFor){
                                        dialogListener.applyFilterWeight(filterWeightSince, filterWeightFor);
                                    }
                                    else{
                                        Toast.makeText(context, getResources().getString(R.string.Errors_wrong_data), Toast.LENGTH_LONG).show();
                                    }
                                }
                                else {
                                    dialogListener.applyFilterWeight(filterWeightSince, filterWeightFor);
                                }
                            }
                            else {
                                dialogListener.clearFilterWeight();
                            }
                        }
                    });
        }
        else if(dialogType.equals(getResources().getString(R.string.ProductDetailsActivity_taste))) {
            View view    = layoutInflater.inflate(R.layout.dialog_taste, null);
            spinnerTaste = view.findViewById(R.id.taste);
            Button btnClear = view.findViewById(R.id.btn_clear);

            String[] tasteArray = getResources().getStringArray(R.array.ProductDetailsActivity_taste_array);

            try{
                for(int i = 0; i < tasteArray.length; i++){
                    if (filterTaste.equals(tasteArray[i])){
                        spinnerTaste.setSelection(i);
                        spinnerTaste.setBackgroundColor(Color.rgb(200,255,200));
                    }
                }
            }
            catch(NullPointerException e){
                spinnerTaste.setSelection(0);
                spinnerTaste.setBackgroundColor(Color.TRANSPARENT);
            }

            filterTaste = String.valueOf(spinnerTaste.getSelectedItem());

            btnClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    spinnerTaste.setSelection(0);
                    spinnerTaste.setBackgroundColor(Color.TRANSPARENT);
                    filterTaste = null;
                }});

            spinnerTaste.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    filterTaste = String.valueOf(spinnerTaste.getSelectedItem());
                }
                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });

            builder.setView(view)
                    .setTitle(dialogType)
                    .setNegativeButton(getResources().getString(R.string.MyPantryActivity_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setPositiveButton(getResources().getString(R.string.MyPantryActivity_set), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(filterTaste != null){
                                dialogListener.applyFilterTaste(filterTaste);
                            }
                            else{
                                dialogListener.clearFilterTaste();
                            }
                        }
                    });
        }
        else if(dialogType.equals(getResources().getString(R.string.ProductDetailsActivity_product_features))) {
            View view = layoutInflater.inflate(R.layout.dialog_product_features, null);
            checkBoxHasSugar = view.findViewById(R.id.has_sugar);
            checkBoxHasSalt  = view.findViewById(R.id.has_salt);
            Button btnClear = view.findViewById(R.id.btn_clear);

            if(filterHasSugar > 0){
                checkBoxHasSugar.setChecked(true);
            }

            if(filterHasSalt > 0){
                checkBoxHasSalt.setChecked(true);
            }

            btnClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkBoxHasSugar.setChecked(false);
                    checkBoxHasSalt.setChecked(false);
                    filterHasSugar = -1;
                    filterHasSalt = -1;
                }});

            checkBoxHasSugar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(checkBoxHasSugar.isChecked())
                    filterHasSugar = 1;
                else
                    filterHasSugar = 0;
                }}
            );

            checkBoxHasSalt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(checkBoxHasSalt.isChecked())
                        filterHasSalt = 1;
                    else
                        filterHasSalt = 0;
                }}
            );

            builder.setView(view)
                    .setTitle(dialogType)
                    .setNegativeButton(getResources().getString(R.string.MyPantryActivity_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setPositiveButton(getResources().getString(R.string.MyPantryActivity_set), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (filterHasSugar == -1 && filterHasSalt == -1) {
                                dialogListener.clearProductFeatures();
                            }
                            else
                                dialogListener.applyProductFeatures(filterHasSugar, filterHasSalt);
                        }
                    });
        }
        return builder.create();
    }

    private void updateProductFeaturesSpinnerAndSelectTypeOfProduct(){
        try{
            if (filterTypeOfProduct.equals(filterTypeOfProductArray[0])){
                productFeaturesArray = getResources().getStringArray(R.array.ProductDetailsActivity_choose_array);
                productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_choose_array, android.R.layout.simple_spinner_item);
                productFeaturesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerProductFeatures.setAdapter(productFeaturesAdapter);
                productFeaturesAdapter.notifyDataSetChanged();
                spinnerTypeOfProduct.setSelection(0, false);
                spinnerTypeOfProduct.setBackgroundColor(Color.rgb(200,255,200));
            }
            else if (filterTypeOfProduct.equals(filterTypeOfProductArray[1])){
                productFeaturesArray = getResources().getStringArray(R.array.ProductDetailsActivity_store_products_array);
                productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_store_products_array, android.R.layout.simple_spinner_item);
                productFeaturesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerProductFeatures.setAdapter(productFeaturesAdapter);
                productFeaturesAdapter.notifyDataSetChanged();
                spinnerTypeOfProduct.setSelection(1, false);
                spinnerTypeOfProduct.setBackgroundColor(Color.rgb(200,255,200));
            }
            else if (filterTypeOfProduct.equals(filterTypeOfProductArray[2])){
                productFeaturesArray = getResources().getStringArray(R.array.ProductDetailsActivity_ready_meals_array);
                productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_ready_meals_array, android.R.layout.simple_spinner_item);
                productFeaturesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerProductFeatures.setAdapter(productFeaturesAdapter);
                productFeaturesAdapter.notifyDataSetChanged();
                spinnerTypeOfProduct.setSelection(2, false);
                spinnerTypeOfProduct.setBackgroundColor(Color.rgb(200,255,200));
            }
            else if (filterTypeOfProduct.equals(filterTypeOfProductArray[3])){
                productFeaturesArray = getResources().getStringArray(R.array.ProductDetailsActivity_vegetables_array);
                productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_vegetables_array, android.R.layout.simple_spinner_item);
                productFeaturesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerProductFeatures.setAdapter(productFeaturesAdapter);
                productFeaturesAdapter.notifyDataSetChanged();
                spinnerTypeOfProduct.setSelection(3, false);
                spinnerTypeOfProduct.setBackgroundColor(Color.rgb(200,255,200));
            }
            else if (filterTypeOfProduct.equals(filterTypeOfProductArray[4])){
                productFeaturesArray = getResources().getStringArray(R.array.ProductDetailsActivity_fruits_array);
                productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_fruits_array, android.R.layout.simple_spinner_item);
                productFeaturesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerProductFeatures.setAdapter(productFeaturesAdapter);
                productFeaturesAdapter.notifyDataSetChanged();
                spinnerTypeOfProduct.setSelection(4, false);
                spinnerTypeOfProduct.setBackgroundColor(Color.rgb(200,255,200));
            }
            else if (filterTypeOfProduct.equals(filterTypeOfProductArray[5])){
                productFeaturesArray = getResources().getStringArray(R.array.ProductDetailsActivity_herbs_array);
                productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_herbs_array, android.R.layout.simple_spinner_item);
                productFeaturesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerProductFeatures.setAdapter(productFeaturesAdapter);
                productFeaturesAdapter.notifyDataSetChanged();
                spinnerTypeOfProduct.setSelection(5, false);
                spinnerTypeOfProduct.setBackgroundColor(Color.rgb(200,255,200));
            }
            else if (filterTypeOfProduct.equals(filterTypeOfProductArray[6])){
                productFeaturesArray = getResources().getStringArray(R.array.ProductDetailsActivity_liqueurs_array);
                productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_liqueurs_array, android.R.layout.simple_spinner_item);
                productFeaturesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerProductFeatures.setAdapter(productFeaturesAdapter);
                productFeaturesAdapter.notifyDataSetChanged();
                spinnerTypeOfProduct.setSelection(6, false);
                spinnerTypeOfProduct.setBackgroundColor(Color.rgb(200,255,200));
            }
            else if (filterTypeOfProduct.equals(filterTypeOfProductArray[7])){
                productFeaturesArray = getResources().getStringArray(R.array.ProductDetailsActivity_wines_type_array);
                productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_wines_type_array, android.R.layout.simple_spinner_item);
                productFeaturesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerProductFeatures.setAdapter(productFeaturesAdapter);
                productFeaturesAdapter.notifyDataSetChanged();
                spinnerTypeOfProduct.setSelection(7, false);
                spinnerTypeOfProduct.setBackgroundColor(Color.rgb(200,255,200));
            }
            else if (filterTypeOfProduct.equals(filterTypeOfProductArray[8])){
                productFeaturesArray = getResources().getStringArray(R.array.ProductDetailsActivity_mushrooms_array);
                productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_mushrooms_array, android.R.layout.simple_spinner_item);
                productFeaturesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerProductFeatures.setAdapter(productFeaturesAdapter);
                productFeaturesAdapter.notifyDataSetChanged();
                spinnerTypeOfProduct.setSelection(8, false);
                spinnerTypeOfProduct.setBackgroundColor(Color.rgb(200,255,200));
            }
            else if (filterTypeOfProduct.equals(filterTypeOfProductArray[9])){
                productFeaturesArray = getResources().getStringArray(R.array.ProductDetailsActivity_vinegars_array);
                productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_vinegars_array, android.R.layout.simple_spinner_item);
                productFeaturesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerProductFeatures.setAdapter(productFeaturesAdapter);
                productFeaturesAdapter.notifyDataSetChanged();
                spinnerTypeOfProduct.setSelection(9, false);
                spinnerTypeOfProduct.setBackgroundColor(Color.rgb(200,255,200));
            }
            else if (filterTypeOfProduct.equals(filterTypeOfProductArray[10])){
                productFeaturesArray = getResources().getStringArray(R.array.ProductDetailsActivity_other_products_array);
                productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_other_products_array, android.R.layout.simple_spinner_item);
                productFeaturesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerProductFeatures.setAdapter(productFeaturesAdapter);
                productFeaturesAdapter.notifyDataSetChanged();
                spinnerTypeOfProduct.setSelection(10, false);
                spinnerTypeOfProduct.setBackgroundColor(Color.rgb(200,255,200));
            }
        }
        catch(NullPointerException e){
            productFeaturesArray = getResources().getStringArray(R.array.ProductDetailsActivity_choose_array);
            spinnerTypeOfProduct.setSelection(0,false);
            spinnerTypeOfProduct.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    private void updateProductFeaturesSpinner(){
        if (selectedProductType.equals(productTypesArray[0]))
            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_choose_array, android.R.layout.simple_spinner_item);
        else if (selectedProductType.equals(productTypesArray[1]))
            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_store_products_array, android.R.layout.simple_spinner_item);
        else if (selectedProductType.equals(productTypesArray[2]))
            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_ready_meals_array, android.R.layout.simple_spinner_item);
        else if (selectedProductType.equals(productTypesArray[3]))
            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_vegetables_array, android.R.layout.simple_spinner_item);
        else if (selectedProductType.equals(productTypesArray[4]))
            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_fruits_array, android.R.layout.simple_spinner_item);
        else if (selectedProductType.equals(productTypesArray[5]))
            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_herbs_array, android.R.layout.simple_spinner_item);
        else if (selectedProductType.equals(productTypesArray[6]))
            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_liqueurs_array, android.R.layout.simple_spinner_item);
        else if (selectedProductType.equals(productTypesArray[7]))
            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_wines_type_array, android.R.layout.simple_spinner_item);
        else if (selectedProductType.equals(productTypesArray[8]))
            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_mushrooms_array, android.R.layout.simple_spinner_item);
        else if (selectedProductType.equals(productTypesArray[9]))
            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_vinegars_array, android.R.layout.simple_spinner_item);
        else if (selectedProductType.equals(productTypesArray[10]))
            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_other_products_array, android.R.layout.simple_spinner_item);

        productFeaturesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProductFeatures.setAdapter(productFeaturesAdapter);
        productFeaturesAdapter.notifyDataSetChanged();
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

    /**
     * <h1>DialogListener</h1>
     * Interface to set values from dialog windows.
     *
     * @author  Mateusz Hermanowicz
     * @version 1.0
     * @since   1.0
     */
    public interface DialogListener {
        void applyFilterName(String filterName);
        void applyFilterExpirationDate(String filterExpirationDateSince, String filterExpirationDateFor);
        void applyFilterProductionDate(String filterProductionDateSince, String filterProductionDateFor);
        void applyFilterTypeOfProduct(String filterTypeOfProduct, String filterProductFeatures);
        void applyFilterVolume(int filterVolumeSince, int filterVolumeFor);
        void applyFilterWeight(int filterWeightSince, int filterWeightFor);
        void applyFilterTaste(String filterTaste);
        void applyProductFeatures(int filterHasSugar, int filterHasSalt);
        void clearFilterName();
        void clearFilterExpirationDate();
        void clearFilterProductionDate();
        void clearFilterTypeOfProduct();
        void clearFilterVolume();
        void clearFilterWeight();
        void clearFilterTaste();
        void clearProductFeatures();
    }
}