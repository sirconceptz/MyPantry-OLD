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
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.interfaces.DialogListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TypeOfProductFilterDialog extends AppCompatDialogFragment {

    @BindView(R.id.spinner_typeOfProduct)
    Spinner spinnerTypeOfProduct;
    @BindView(R.id.spinner_productFeatures)
    Spinner spinnerProductFeatures;
    @BindView(R.id.button_clear)
    Button btnClear;
    private Context context;
    private Resources resources;
    private DialogListener dialogListener;
    private String filterTypeOfProduct, filterProductFeatures, selectedProductType;
    private String[] typeOfProductArray, productFeaturesArray;
    private ArrayAdapter<CharSequence> productFeaturesAdapter;
    private boolean isTypeOfProductTouched, isProductFeaturesTouched;

    public TypeOfProductFilterDialog(String filterTypeOfProduct, String filterProductFeatures) {
        this.filterTypeOfProduct = filterTypeOfProduct;
        this.filterProductFeatures = filterProductFeatures;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Activity activity = getActivity();
        assert activity != null;
        context = activity.getApplicationContext();
        resources = context.getResources();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater layoutInflater = activity.getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.dialog_type_of_product, null);

        ButterKnife.bind(this, view);

        selectedProductType = String.valueOf(spinnerTypeOfProduct.getSelectedItem());
        typeOfProductArray = resources.getStringArray(R.array.ProductDetailsActivity_type_of_product_array);
        productFeaturesArray = resources.getStringArray(R.array.ProductDetailsActivity_choose_array);

        productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_choose_array, android.R.layout.simple_spinner_item);
        productFeaturesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProductFeatures.setAdapter(productFeaturesAdapter);

        updateProductFeaturesSpinnerAndSelectTypeOfProduct();

        if (filterProductFeatures != null) {
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

        btnClear.setOnClickListener(view1 -> {
            spinnerTypeOfProduct.setSelection(0, false);
            spinnerTypeOfProduct.setBackgroundColor(Color.TRANSPARENT);
            filterTypeOfProduct = null;
            spinnerProductFeatures.setSelection(0, false);
            spinnerProductFeatures.setBackgroundColor(Color.TRANSPARENT);
            filterProductFeatures = null;
        });

        spinnerTypeOfProduct.setOnTouchListener((v, event) -> {
            isTypeOfProductTouched = true;
            return false;
        });
        spinnerTypeOfProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (isTypeOfProductTouched) {
                    selectedProductType = String.valueOf(spinnerTypeOfProduct.getSelectedItem());
                    typeOfProductArray = resources.getStringArray(R.array.ProductDetailsActivity_type_of_product_array);
                    updateProductFeaturesSpinner();
                    if (selectedProductType.equals(typeOfProductArray[0]))
                        filterTypeOfProduct = null;
                    else
                        filterTypeOfProduct = String.valueOf(spinnerTypeOfProduct.getSelectedItem());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerProductFeatures.setOnTouchListener((v, event) -> {
            isProductFeaturesTouched = true;
            return false;
        });

        spinnerProductFeatures.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (isProductFeaturesTouched) {
                    if (String.valueOf(spinnerProductFeatures.getSelectedItem()).equals(productFeaturesArray[0])) {
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
                .setTitle(resources.getString(R.string.ProductDetailsActivity_product_type))
                .setNegativeButton(resources.getString(R.string.MyPantryActivity_cancel), (dialog, which) -> {
                })
                .setPositiveButton(resources.getString(R.string.MyPantryActivity_set), (dialog, which) -> {
                    if (filterTypeOfProduct != null) {
                        dialogListener.setFilterTypeOfProduct(filterTypeOfProduct, filterProductFeatures);
                    } else {
                        dialogListener.clearFilterTypeOfProduct();
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

    private void updateProductFeaturesSpinnerAndSelectTypeOfProduct() {
        try {
            if (filterTypeOfProduct.equals(typeOfProductArray[0])) {
                productFeaturesArray = resources.getStringArray(R.array.ProductDetailsActivity_choose_array);
                productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_choose_array, android.R.layout.simple_spinner_item);
                productFeaturesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerProductFeatures.setAdapter(productFeaturesAdapter);
                productFeaturesAdapter.notifyDataSetChanged();
                spinnerTypeOfProduct.setSelection(0, false);
                spinnerTypeOfProduct.setBackgroundColor(Color.rgb(200, 255, 200));
            } else if (filterTypeOfProduct.equals(typeOfProductArray[1])) {
                productFeaturesArray = resources.getStringArray(R.array.ProductDetailsActivity_store_products_array);
                productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_store_products_array, android.R.layout.simple_spinner_item);
                productFeaturesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerProductFeatures.setAdapter(productFeaturesAdapter);
                productFeaturesAdapter.notifyDataSetChanged();
                spinnerTypeOfProduct.setSelection(1, false);
                spinnerTypeOfProduct.setBackgroundColor(Color.rgb(200, 255, 200));
            } else if (filterTypeOfProduct.equals(typeOfProductArray[2])) {
                productFeaturesArray = resources.getStringArray(R.array.ProductDetailsActivity_ready_meals_array);
                productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_ready_meals_array, android.R.layout.simple_spinner_item);
                productFeaturesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerProductFeatures.setAdapter(productFeaturesAdapter);
                productFeaturesAdapter.notifyDataSetChanged();
                spinnerTypeOfProduct.setSelection(2, false);
                spinnerTypeOfProduct.setBackgroundColor(Color.rgb(200, 255, 200));
            } else if (filterTypeOfProduct.equals(typeOfProductArray[3])) {
                productFeaturesArray = resources.getStringArray(R.array.ProductDetailsActivity_vegetables_array);
                productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_vegetables_array, android.R.layout.simple_spinner_item);
                productFeaturesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerProductFeatures.setAdapter(productFeaturesAdapter);
                productFeaturesAdapter.notifyDataSetChanged();
                spinnerTypeOfProduct.setSelection(3, false);
                spinnerTypeOfProduct.setBackgroundColor(Color.rgb(200, 255, 200));
            } else if (filterTypeOfProduct.equals(typeOfProductArray[4])) {
                productFeaturesArray = resources.getStringArray(R.array.ProductDetailsActivity_fruits_array);
                productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_fruits_array, android.R.layout.simple_spinner_item);
                productFeaturesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerProductFeatures.setAdapter(productFeaturesAdapter);
                productFeaturesAdapter.notifyDataSetChanged();
                spinnerTypeOfProduct.setSelection(4, false);
                spinnerTypeOfProduct.setBackgroundColor(Color.rgb(200, 255, 200));
            } else if (filterTypeOfProduct.equals(typeOfProductArray[5])) {
                productFeaturesArray = resources.getStringArray(R.array.ProductDetailsActivity_herbs_array);
                productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_herbs_array, android.R.layout.simple_spinner_item);
                productFeaturesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerProductFeatures.setAdapter(productFeaturesAdapter);
                productFeaturesAdapter.notifyDataSetChanged();
                spinnerTypeOfProduct.setSelection(5, false);
                spinnerTypeOfProduct.setBackgroundColor(Color.rgb(200, 255, 200));
            } else if (filterTypeOfProduct.equals(typeOfProductArray[6])) {
                productFeaturesArray = resources.getStringArray(R.array.ProductDetailsActivity_liqueurs_array);
                productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_liqueurs_array, android.R.layout.simple_spinner_item);
                productFeaturesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerProductFeatures.setAdapter(productFeaturesAdapter);
                productFeaturesAdapter.notifyDataSetChanged();
                spinnerTypeOfProduct.setSelection(6, false);
                spinnerTypeOfProduct.setBackgroundColor(Color.rgb(200, 255, 200));
            } else if (filterTypeOfProduct.equals(typeOfProductArray[7])) {
                productFeaturesArray = resources.getStringArray(R.array.ProductDetailsActivity_wines_type_array);
                productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_wines_type_array, android.R.layout.simple_spinner_item);
                productFeaturesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerProductFeatures.setAdapter(productFeaturesAdapter);
                productFeaturesAdapter.notifyDataSetChanged();
                spinnerTypeOfProduct.setSelection(7, false);
                spinnerTypeOfProduct.setBackgroundColor(Color.rgb(200, 255, 200));
            } else if (filterTypeOfProduct.equals(typeOfProductArray[8])) {
                productFeaturesArray = resources.getStringArray(R.array.ProductDetailsActivity_mushrooms_array);
                productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_mushrooms_array, android.R.layout.simple_spinner_item);
                productFeaturesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerProductFeatures.setAdapter(productFeaturesAdapter);
                productFeaturesAdapter.notifyDataSetChanged();
                spinnerTypeOfProduct.setSelection(8, false);
                spinnerTypeOfProduct.setBackgroundColor(Color.rgb(200, 255, 200));
            } else if (filterTypeOfProduct.equals(typeOfProductArray[9])) {
                productFeaturesArray = resources.getStringArray(R.array.ProductDetailsActivity_vinegars_array);
                productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_vinegars_array, android.R.layout.simple_spinner_item);
                productFeaturesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerProductFeatures.setAdapter(productFeaturesAdapter);
                productFeaturesAdapter.notifyDataSetChanged();
                spinnerTypeOfProduct.setSelection(9, false);
                spinnerTypeOfProduct.setBackgroundColor(Color.rgb(200, 255, 200));
            } else if (filterTypeOfProduct.equals(typeOfProductArray[10])) {
                productFeaturesArray = resources.getStringArray(R.array.ProductDetailsActivity_other_products_array);
                productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_other_products_array, android.R.layout.simple_spinner_item);
                productFeaturesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerProductFeatures.setAdapter(productFeaturesAdapter);
                productFeaturesAdapter.notifyDataSetChanged();
                spinnerTypeOfProduct.setSelection(10, false);
                spinnerTypeOfProduct.setBackgroundColor(Color.rgb(200, 255, 200));
            }
        } catch (NullPointerException e) {
            productFeaturesArray = resources.getStringArray(R.array.ProductDetailsActivity_choose_array);
            spinnerTypeOfProduct.setSelection(0, false);
            spinnerTypeOfProduct.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    private void updateProductFeaturesSpinner() {
        if (selectedProductType.equals(typeOfProductArray[0]))
            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_choose_array, android.R.layout.simple_spinner_item);
        else if (selectedProductType.equals(typeOfProductArray[1]))
            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_store_products_array, android.R.layout.simple_spinner_item);
        else if (selectedProductType.equals(typeOfProductArray[2]))
            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_ready_meals_array, android.R.layout.simple_spinner_item);
        else if (selectedProductType.equals(typeOfProductArray[3]))
            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_vegetables_array, android.R.layout.simple_spinner_item);
        else if (selectedProductType.equals(typeOfProductArray[4]))
            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_fruits_array, android.R.layout.simple_spinner_item);
        else if (selectedProductType.equals(typeOfProductArray[5]))
            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_herbs_array, android.R.layout.simple_spinner_item);
        else if (selectedProductType.equals(typeOfProductArray[6]))
            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_liqueurs_array, android.R.layout.simple_spinner_item);
        else if (selectedProductType.equals(typeOfProductArray[7]))
            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_wines_type_array, android.R.layout.simple_spinner_item);
        else if (selectedProductType.equals(typeOfProductArray[8]))
            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_mushrooms_array, android.R.layout.simple_spinner_item);
        else if (selectedProductType.equals(typeOfProductArray[9]))
            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_vinegars_array, android.R.layout.simple_spinner_item);
        else if (selectedProductType.equals(typeOfProductArray[10]))
            productFeaturesAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_other_products_array, android.R.layout.simple_spinner_item);

        productFeaturesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProductFeatures.setAdapter(productFeaturesAdapter);
        productFeaturesAdapter.notifyDataSetChanged();
    }
}