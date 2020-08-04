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

package com.hermanowicz.pantry.filter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hermanowicz.pantry.db.Product;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <h1>Filter</h1>
 * Class for filtering products according to data from the filter model.
 *
 * @author  Mateusz Hermanowicz
 * @version 1.0
 * @since   1.0
 */

public class Filter {

    private List<Product> productList;

    private FilterModel filterProduct;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public Filter(@NonNull List<Product> productList){
        this.productList = productList;
    }

    public LiveData<List<Product>> filterByProduct (FilterModel filterProduct){
        this.filterProduct = filterProduct;
        MutableLiveData<List<Product>> tempList = new MutableLiveData<>();
        List<Product> tempProductList = new ArrayList<>();

        for(Product product : productList){
            if(isProductNameValid(product.getName())
                    && isProductTypeOfProductValid(product.getTypeOfProduct(), product.getProductFeatures())
                    && isProductExpirationDateValid(product.getExpirationDate())
                    && isProductProductionDateValid(product.getProductionDate())
                    && isProductVolumeValid(product.getVolume())
                    && isProductWeightValid(product.getWeight())
                    && isProductHasSugarAndSaltValid(product.getHasSugar(), product.getHasSalt())
                    && isProductTasteValid(product.getTaste()))
                tempProductList.add(product);
            }
        tempList.setValue(tempProductList);
        return tempList;
    }

    private boolean isProductNameValid(String name){
        boolean isNameValid = true;

        if (filterProduct.getName() != null)
            isNameValid = name.contains(filterProduct.getName());

        return isNameValid;
    }

    private boolean isProductTypeOfProductValid(String typeOfProduct, String productFeatures){
        boolean isTypeOfProductAndProductFeaturesValid,
                isTypeOfProductValid = true, isProductFeaturesValid = true;

        if(filterProduct.getTypeOfProduct() != null)
                isTypeOfProductValid = typeOfProduct.equals(filterProduct.getTypeOfProduct());

        if(filterProduct.getProductFeatures() != null)
            isProductFeaturesValid = productFeatures.equals(filterProduct.getProductFeatures());

        isTypeOfProductAndProductFeaturesValid = isTypeOfProductValid && isProductFeaturesValid;
        return isTypeOfProductAndProductFeaturesValid;
    }

    private boolean isProductExpirationDateValid(String expirationDate){
        boolean isExpirationDateValid,
                isExpirationDateSinceValid = true, isExpirationDateForValid = true;

        if(filterProduct.getExpirationDateSince() != null)
            isExpirationDateSinceValid = isDateAfter(expirationDate, filterProduct.getExpirationDateSince());
        if(filterProduct.getExpirationDateFor() != null)
            isExpirationDateForValid = isDateAfter(filterProduct.getExpirationDateFor(), expirationDate);

        isExpirationDateValid = isExpirationDateSinceValid && isExpirationDateForValid;
        return isExpirationDateValid;
    }

    private boolean isProductProductionDateValid(String productionDate){
        boolean isProductionDateValid,
                isProductionDateSinceValid = true, isProductionDateForValid = true;

        if(filterProduct.getExpirationDateSince() != null)
            isProductionDateSinceValid = isDateAfter(productionDate, filterProduct.getProductionDateFor());
        if(filterProduct.getExpirationDateFor() != null)
            isProductionDateForValid = isDateAfter(filterProduct.getProductionDateFor(), productionDate);

        isProductionDateValid = isProductionDateSinceValid && isProductionDateForValid;
        return isProductionDateValid;
    }

    private boolean isProductVolumeValid(int volume){
        boolean isProductVolumeValid,
                isProductVolumeSinceValid = true, isProductVolumeForValid = true;

        if(filterProduct.getVolumeSince() > -1)
            isProductVolumeSinceValid = filterProduct.getVolumeSince() <= volume;

        if(filterProduct.getVolumeFor() > -1)
            isProductVolumeForValid = filterProduct.getVolumeFor() >= volume;

        isProductVolumeValid = isProductVolumeSinceValid && isProductVolumeForValid;
        return isProductVolumeValid;
    }

    private boolean isProductWeightValid(int weight){
        boolean isProductWeightValid,
                isProductWeightSinceValid = true, isProductWeightForValid = true;

        if(filterProduct.getWeightSince() > -1)
            isProductWeightSinceValid = filterProduct.getWeightSince() <= weight;

        if(filterProduct.getWeightFor() > -1)
            isProductWeightForValid = filterProduct.getWeightFor() >= weight;

        isProductWeightValid = isProductWeightSinceValid && isProductWeightForValid;
        return isProductWeightValid;
    }

    private boolean isProductHasSugarAndSaltValid(boolean isHasSugar, boolean isHasSalt){
        boolean isProductHasSugarAndSaltValid,
                isProductHasSugarValid = true, isProductHasSaltValid = true;

        if(filterProduct.getHasSugar() != Set.DISABLED)
            isProductHasSugarValid = (filterProduct.getHasSugar() == Set.YES && isHasSugar) || (filterProduct.getHasSugar() == Set.NO && !isHasSugar);
        if(filterProduct.getHasSalt() != Set.DISABLED)
            isProductHasSaltValid = (filterProduct.getHasSalt() == Set.YES && isHasSalt) || (filterProduct.getHasSalt() == Set.NO && !isHasSalt);

        isProductHasSugarAndSaltValid = isProductHasSugarValid && isProductHasSaltValid;
        return isProductHasSugarAndSaltValid;
    }

    private boolean isProductTasteValid(String taste){
        boolean isTasteValid = true;

        if (filterProduct.getTaste() != null)
            isTasteValid = taste.contains(filterProduct.getTaste());

        return isTasteValid;
    }

    private boolean isDateAfter(String productDateString, String filterDate){
        boolean isDateAfter = true;
        if(productDateString != null && filterDate != null) {
            try {
                Date productDate = sdf.parse(productDateString);
                Date filterDateSince = sdf.parse(filterDate);
                isDateAfter = productDate.after(filterDateSince);
            } catch (ParseException e) {
                Log.e("ProductDate", e.toString());
            }
        }
        return isDateAfter;
    }

    public enum Set {
        YES, NO, DISABLED
    }
}