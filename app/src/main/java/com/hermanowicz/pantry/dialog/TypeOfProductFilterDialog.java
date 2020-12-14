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
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.databinding.DialogTypeOfProductBinding;
import com.hermanowicz.pantry.db.CategoryDb;
import com.hermanowicz.pantry.filter.FilterModel;
import com.hermanowicz.pantry.interfaces.FilterDialogListener;

import org.jetbrains.annotations.NotNull;

/**
 * <h1>TypeOfProductFilterDialog</h1>
 * The dialog window needed to set filters by type of product to search for products in the pantry.
 *
 * @author  Mateusz Hermanowicz
 * @version 1.0
 * @since   1.0
 */

public class TypeOfProductFilterDialog extends AppCompatDialogFragment {

    private DialogTypeOfProductBinding binding;

    private Context context;
    private Resources resources;
    private CategoryDb categoryDb;
    private FilterDialogListener dialogListener;
    private String filterTypeOfProduct, filterProductFeatures, selectedProductType;
    private String[] productTypeArray, productCategoryArray;
    private ArrayAdapter<CharSequence> typeOfProductAdapter;
    private ArrayAdapter<CharSequence> productCategoryAdapter;
    private boolean isTypeOfProductTouched;

    public TypeOfProductFilterDialog(FilterModel filterProduct) {
        this.filterTypeOfProduct = filterProduct.getTypeOfProduct();
        this.filterProductFeatures = filterProduct.getProductFeatures();
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Activity activity = getActivity();
        if (activity != null) {
            context = activity.getApplicationContext();
            resources = context.getResources();
            categoryDb = CategoryDb.getInstance(context);
        }

        assert activity != null;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AppThemeDialog);

        binding = DialogTypeOfProductBinding.inflate(activity.getLayoutInflater());
        View view = binding.getRoot();

        selectedProductType = String.valueOf(binding.spinnerProductType.getSelectedItem());
        productTypeArray = resources.getStringArray(R.array.Product_type_of_product_array);
        productCategoryArray = resources.getStringArray(R.array.Product_choose_array);

        typeOfProductAdapter = ArrayAdapter.createFromResource(context, R.array.Product_type_of_product_array, R.layout.custom_spinner);
        binding.spinnerProductType.setAdapter(typeOfProductAdapter);

        productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_choose_array, R.layout.custom_spinner);
        binding.spinnerProductCategory.setAdapter(productCategoryAdapter);

        updateProductFeaturesSpinnerAndSelectTypeOfProduct();

        if (filterProductFeatures != null) {
            try {
                for (int i = 0; i < productCategoryArray.length; i++)
                    if (productCategoryArray[i].equals(filterProductFeatures)) {
                        binding.spinnerProductCategory.setSelection(i);
                        binding.spinnerProductCategory.setBackgroundColor(Color.rgb(200, 255, 200));
                    }
            } catch (NullPointerException e) {
                binding.spinnerProductCategory.setBackgroundColor(Color.TRANSPARENT);
            }
        }

        binding.buttonClear.setOnClickListener(view1 -> {
            binding.spinnerProductType.setSelection(0, false);
            binding.spinnerProductType.setBackgroundColor(Color.TRANSPARENT);
            filterTypeOfProduct = null;
            binding.spinnerProductCategory.setSelection(0, false);
            binding.spinnerProductCategory.setBackgroundColor(Color.TRANSPARENT);
            filterProductFeatures = null;
        });

        binding.spinnerProductType.setOnTouchListener((v, event) -> {
            isTypeOfProductTouched = true;
            return false;
        });
        binding.spinnerProductType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (isTypeOfProductTouched) {
                    selectedProductType = String.valueOf(binding.spinnerProductType.getSelectedItem());
                    productTypeArray = resources.getStringArray(R.array.Product_type_of_product_array);
                    updateProductFeaturesSpinner();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        binding.spinnerProductCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        builder.setView(view)
                .setTitle(getString(R.string.Product_type))
                .setNegativeButton(getString(R.string.General_cancel), (dialog, which) -> {
                })
                .setPositiveButton(getString(R.string.MyPantryActivity_set), (dialog, which) -> {
                    if (String.valueOf(binding.spinnerProductType.getSelectedItem()).equals(productTypeArray[0]))
                        dialogListener.setFilterTypeOfProduct(null, null);
                    else{
                        filterTypeOfProduct = String.valueOf(binding.spinnerProductType.getSelectedItem());
                        filterProductFeatures = null;
                        if (!String.valueOf(binding.spinnerProductCategory.getSelectedItem()).equals(productCategoryArray[0]))
                            filterProductFeatures = String.valueOf(binding.spinnerProductCategory.getSelectedItem());
                        dialogListener.setFilterTypeOfProduct(filterTypeOfProduct, filterProductFeatures);
                    }
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

    private void updateProductFeaturesSpinnerAndSelectTypeOfProduct() {
        if(filterTypeOfProduct != null) {
            if (filterTypeOfProduct.equals(productTypeArray[1])) {
                String[] categoryArray = categoryDb.categoryDao().getAllCategoriesArray();
                productCategoryAdapter = new ArrayAdapter<>(context, R.layout.custom_spinner, categoryArray);
                binding.spinnerProductType.setSelection(1, false);
            }
            else if (filterTypeOfProduct.equals(productTypeArray[2])) {
                productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_store_products_array, R.layout.custom_spinner);
                binding.spinnerProductType.setSelection(2, false);
            }
            else if (filterTypeOfProduct.equals(productTypeArray[3])) {
                productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_ready_meals_array, R.layout.custom_spinner);
                binding.spinnerProductType.setSelection(3, false);
            }
            else if (filterTypeOfProduct.equals(productTypeArray[4])) {
                productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_vegetables_array, R.layout.custom_spinner);
                binding.spinnerProductType.setSelection(4, false);
            }
            else if (filterTypeOfProduct.equals(productTypeArray[5])) {
                productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_fruits_array, R.layout.custom_spinner);
                binding.spinnerProductType.setSelection(5, false);
            }
            else if (filterTypeOfProduct.equals(productTypeArray[6])) {
                productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_herbs_array, R.layout.custom_spinner);
                binding.spinnerProductType.setSelection(6, false);
            }
            else if (filterTypeOfProduct.equals(productTypeArray[7])) {
                productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_liqueurs_array, R.layout.custom_spinner);
                binding.spinnerProductType.setSelection(7, false);
            }
            else if (filterTypeOfProduct.equals(productTypeArray[8])) {
                productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_wines_type_array, R.layout.custom_spinner);
                binding.spinnerProductType.setSelection(8, false);
            }
            else if (filterTypeOfProduct.equals(productTypeArray[9])) {
                productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_mushrooms_array, R.layout.custom_spinner);
                binding.spinnerProductType.setSelection(9, false);
            }
            else if (filterTypeOfProduct.equals(productTypeArray[10])) {
                productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_vinegars_array, R.layout.custom_spinner);
                binding.spinnerProductType.setSelection(10, false);
            }
            else if (filterTypeOfProduct.equals(productTypeArray[11])) {
                productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_other_products_array, R.layout.custom_spinner);
                binding.spinnerProductType.setSelection(11, false);
            }
            binding.spinnerProductCategory.setAdapter(productCategoryAdapter);
            binding.spinnerProductType.setBackgroundColor(Color.rgb(200, 255, 200));
            }
        else{
            productCategoryArray = resources.getStringArray(R.array.Product_choose_array);
            binding.spinnerProductType.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    private void updateProductFeaturesSpinner() {
        if (selectedProductType.equals(productTypeArray[0]))
            productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_choose_array, R.layout.custom_spinner);
        else if (selectedProductType.equals(productTypeArray[1]))
            productCategoryAdapter = new ArrayAdapter<>(context, R.layout.custom_spinner, categoryDb.categoryDao().getAllCategoriesArray());
        else if (selectedProductType.equals(productTypeArray[2]))
            productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.ProductDetailsActivity_store_products_array, R.layout.custom_spinner);
        else if (selectedProductType.equals(productTypeArray[3]))
            productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_ready_meals_array, R.layout.custom_spinner);
        else if (selectedProductType.equals(productTypeArray[4]))
            productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_vegetables_array, R.layout.custom_spinner);
        else if (selectedProductType.equals(productTypeArray[5]))
            productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_fruits_array, R.layout.custom_spinner);
        else if (selectedProductType.equals(productTypeArray[6]))
            productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_herbs_array, R.layout.custom_spinner);
        else if (selectedProductType.equals(productTypeArray[7]))
            productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_liqueurs_array, R.layout.custom_spinner);
        else if (selectedProductType.equals(productTypeArray[8]))
            productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_wines_type_array, R.layout.custom_spinner);
        else if (selectedProductType.equals(productTypeArray[9]))
            productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_mushrooms_array, R.layout.custom_spinner);
        else if (selectedProductType.equals(productTypeArray[10]))
            productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_vinegars_array, R.layout.custom_spinner);
        else if (selectedProductType.equals(productTypeArray[11]))
            productCategoryAdapter = ArrayAdapter.createFromResource(context, R.array.Product_other_products_array, R.layout.custom_spinner);

        binding.spinnerProductCategory.setAdapter(productCategoryAdapter);
    }
}