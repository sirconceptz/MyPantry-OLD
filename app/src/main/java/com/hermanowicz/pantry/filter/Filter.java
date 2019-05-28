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

public class Filter {

    private List<Product> productList;

    private FilterModel filterProduct;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public Filter(@NonNull List<Product> productList){
        this.productList = productList;
    }

    public LiveData<List<Product>> filterByProduct (FilterModel product){
        this.filterProduct = product;
        MutableLiveData<List<Product>> productList = new MutableLiveData<>();
        List<Product> tempProductList = new ArrayList<>();

        for(int i = 0; this.productList.size() > i; i++){
            Log.d("Petla:", String.valueOf(i));
            if(isProductNameValid(this.productList.get(i).getName())
                    && isProductTypeOfProductValid(this.productList.get(i).getTypeOfProduct(), this.productList.get(i).getProductFeatures())
                    && isProductExpirationDateValid(this.productList.get(i).getExpirationDate())
                    && isProductProductionDateValid(this.productList.get(i).getProductionDate())
                    && isProductVolumeValid(this.productList.get(i).getVolume())
                    && isProductWeightValid(this.productList.get(i).getWeight())
                    && isProductHasSugarAndSaltValid(this.productList.get(i).getHasSugar(), this.productList.get(i).getHasSalt())
                    && isProductTasteValid(this.productList.get(i).getTaste()))
                tempProductList.add(this.productList.get(i));
            }
        productList.setValue(tempProductList);
        return productList;
    }

    private boolean isProductNameValid(String name){
        boolean isNameValid;

        if (this.filterProduct.getName() != null){
            isNameValid = name.contains(filterProduct.getName());
        }
        else
            isNameValid = true;
        return isNameValid;
    }

    private boolean isProductTypeOfProductValid(String typeOfProduct, String productFeatures){
        boolean isTypeOfProductAndProductFeaturesValid, isTypeOfProductValid, isProductFeaturesValid;

        if(this.filterProduct.getTypeOfProduct() != null) {
                isTypeOfProductValid = typeOfProduct.equals(filterProduct.getTypeOfProduct());
        }
        else
            isTypeOfProductValid = true;

        if(this.filterProduct.getProductFeatures() != null) {
            isProductFeaturesValid = productFeatures.equals(filterProduct.getProductFeatures());
        }
        else
            isProductFeaturesValid = true;

        isTypeOfProductAndProductFeaturesValid = isTypeOfProductValid && isProductFeaturesValid;
        return isTypeOfProductAndProductFeaturesValid;
    }

    private boolean isProductExpirationDateValid(String expirationDate){
        boolean isExpirationDateValid, isExpirationDateSinceValid, isExpirationDateForValid;

        if(filterProduct.getExpirationDateSince() == null)
            isExpirationDateSinceValid = true;
        else{
            try {
                Date productExpirationDate = sdf.parse(expirationDate);
                Date filterExpirationDateSince = sdf.parse(filterProduct.getExpirationDateSince());
                isExpirationDateSinceValid = productExpirationDate.after(filterExpirationDateSince);
            } catch (ParseException e) {
                isExpirationDateSinceValid = false; }
        }
        if(filterProduct.getExpirationDateFor() == null)
            isExpirationDateForValid = true;
        else{
            try {
                Date productExpirationDate = sdf.parse(expirationDate);
                Date filterExpirationDateFor = sdf.parse(filterProduct.getExpirationDateFor());
                isExpirationDateForValid = productExpirationDate.before(filterExpirationDateFor);
            } catch (ParseException e) {
                isExpirationDateForValid = false; }
        }

        isExpirationDateValid = isExpirationDateSinceValid && isExpirationDateForValid;
        return isExpirationDateValid;
    }

    private boolean isProductProductionDateValid(String productionDate){
        boolean isProductionDateValid, isProductionDateSinceValid, isProductionDateForValid;

        if(filterProduct.getProductionDateSince() == null)
            isProductionDateSinceValid = true;
        else{
            try {
                Date productProductionDate = sdf.parse(productionDate);
                Date filterProductionDateSince = sdf.parse(filterProduct.getProductionDateSince());
                isProductionDateSinceValid = productProductionDate.after(filterProductionDateSince);
            } catch (ParseException e) {
                isProductionDateSinceValid = false; }
        }
        if(filterProduct.getProductionDateFor() == null)
            isProductionDateForValid = true;
        else{
            try {
                Date productProductionDate = sdf.parse(productionDate);
                Date filterProductionDateFor = sdf.parse(filterProduct.getProductionDateFor());
                isProductionDateForValid = productProductionDate.before(filterProductionDateFor);
            } catch (ParseException e) {
                isProductionDateForValid = false; }
        }

        isProductionDateValid = isProductionDateSinceValid && isProductionDateForValid;
        return isProductionDateValid;
    }

    private boolean isProductVolumeValid(int volume){
        boolean isProductVolumeValid, isProductVolumeSinceValid, isProductVolumeForValid;

        if(filterProduct.getVolumeSince() == -1)
            isProductVolumeSinceValid = true;
        else
            isProductVolumeSinceValid = filterProduct.getVolumeSince() <= volume;

        if(filterProduct.getVolumeFor() == -1)
            isProductVolumeForValid = true;
        else
            isProductVolumeForValid = filterProduct.getVolumeFor() >= volume;

        isProductVolumeValid = isProductVolumeSinceValid && isProductVolumeForValid;
        return isProductVolumeValid;
    }

    private boolean isProductWeightValid(int weight){
        boolean isProductWeightValid, isProductWeightSinceValid, isProductWeightForValid;

        if(filterProduct.getWeightSince() == -1)
            isProductWeightSinceValid = true;
        else
            isProductWeightSinceValid = filterProduct.getWeightSince() <= weight;

        if(filterProduct.getWeightFor() == -1)
            isProductWeightForValid = true;
        else
            isProductWeightForValid = filterProduct.getWeightFor() >= weight;

        isProductWeightValid = isProductWeightSinceValid && isProductWeightForValid;
        return isProductWeightValid;
    }

    private boolean isProductHasSugarAndSaltValid(boolean hasSugar, boolean hasSalt){
        boolean isProductHasSugarAndSaltValid, isProductHasSugarValid, isProductHasSaltValid;

        if(filterProduct.getHasSugar() == -1)
            isProductHasSugarValid = true;
        else
            isProductHasSugarValid = (filterProduct.getHasSugar() == 1 && hasSugar) || (filterProduct.getHasSugar() == 0 && !hasSugar);
        if(filterProduct.getHasSalt() == -1)
            isProductHasSaltValid = true;
        else
            isProductHasSaltValid = (filterProduct.getHasSalt() == 1 && hasSalt) || (filterProduct.getHasSalt() == 0 && !hasSalt);

        isProductHasSugarAndSaltValid = isProductHasSugarValid && isProductHasSaltValid;
        return isProductHasSugarAndSaltValid;
    }

    private boolean isProductTasteValid(String taste){
        boolean isTasteValid;

        if (this.filterProduct.getTaste() != null){
            isTasteValid = taste.contains(filterProduct.getTaste());
        }
        else
            isTasteValid = true;
        return isTasteValid;
    }
}