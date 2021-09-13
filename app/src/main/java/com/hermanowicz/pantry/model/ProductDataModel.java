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

package com.hermanowicz.pantry.model;

import android.content.Context;
import android.content.res.Resources;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.db.category.CategoryDb;
import com.hermanowicz.pantry.db.product.Product;
import com.hermanowicz.pantry.db.product.ProductDb;
import com.hermanowicz.pantry.db.storagelocation.StorageLocation;
import com.hermanowicz.pantry.db.storagelocation.StorageLocationDb;

import java.util.ArrayList;
import java.util.List;

public class ProductDataModel {

    private final ProductDb productDb;
    private final CategoryDb categoryDb;
    private final StorageLocationDb storageLocationDb;
    private final Resources resources;
    private String taste;
    private String productionDate = "-";
    private String expirationDate = "-";
    private List<Product> productList = new ArrayList<>();
    private int oldProductsQuantity;
    private String databaseMode;

    public ProductDataModel(@NonNull Context context){
        productDb = ProductDb.getInstance(context);
        categoryDb = CategoryDb.getInstance(context);
        storageLocationDb = StorageLocationDb.getInstance(context);
        this.resources = context.getResources();
    }

    public void setDatabaseMode(@NonNull String databaseMode) {
     this.databaseMode = databaseMode;
    }

    public void deleteSimilarOfflineProducts(){
        productDb.productsDao().deleteProducts(productList);
    }

    public void deleteSimilarOnlineProducts(){
        DatabaseReference ref;
        for (Product product : productList){
            ref = FirebaseDatabase.getInstance().getReference().
                    child("products/" + FirebaseAuth.getInstance().getUid());
            ref.child(String.valueOf(product.getId())).removeValue();
        }
    }

    public Product getProduct(int productId) {
        Product product = null;
        for(Product productLoop : productList){
            if(productLoop.getId() == productId)
                product = productLoop;
        }
        return product;
    }

    public void setProduct(int productId){
        Product product = getProduct(productId);
        expirationDate = product.getExpirationDate();
        productionDate = product.getProductionDate();
        productList = Product.getSimilarProductsList(product, productList);
        oldProductsQuantity = productList.size();
    }

    public String formatDate(int year, int month, int day) {
        return year + "-" + month + "-" + day;
    }

    public List<Product> getProductList(){
        return productList;
    }

    public boolean isProductListEmpty() {
        return productList == null;
    }

    public boolean isCorrectHashCode(@NonNull String hashCode) {
        boolean isCorrectHashcode = false;
        for (Product product : productList) {
            if (product.getHashCode().equals(hashCode)) {
                isCorrectHashcode = true;
                break;
            }
        }
        return isCorrectHashcode;
    }

    public GroupProducts getGroupProducts() {
        return new GroupProducts(productList.get(0), productList.size());
    }

    public int getProductTypeSpinnerPosition(){
        String[] productTypesArray = resources.getStringArray(R.array.Product_type_of_product_array);
        int selection = 0;
        for(int counter = 0; productTypesArray.length > counter; counter++){
            if(productList.get(0).getTypeOfProduct().equals(productTypesArray[counter]))
                selection = counter;
        }
        return selection;
    }

    public int getProductFeaturesSpinnerPosition(int productTypeSpinnerPosition){
        String[] productFeaturesArray;
        int selection = 0;
        if(productTypeSpinnerPosition == 1)
            productFeaturesArray = getOwnCategoriesArray();
        else if(productTypeSpinnerPosition == 2)
            productFeaturesArray = resources.getStringArray(R.array.ProductDetailsActivity_store_products_array);
        else if(productTypeSpinnerPosition == 3)
            productFeaturesArray = resources.getStringArray(R.array.Product_ready_meals_array);
        else if(productTypeSpinnerPosition == 4)
            productFeaturesArray = resources.getStringArray(R.array.Product_vegetables_array);
        else if(productTypeSpinnerPosition == 5)
            productFeaturesArray = resources.getStringArray(R.array.Product_fruits_array);
        else if(productTypeSpinnerPosition == 6)
            productFeaturesArray = resources.getStringArray(R.array.Product_herbs_array);
        else if(productTypeSpinnerPosition == 7)
            productFeaturesArray = resources.getStringArray(R.array.Product_liqueurs_array);
        else if(productTypeSpinnerPosition == 8)
            productFeaturesArray = resources.getStringArray(R.array.Product_wines_type_array);
        else if(productTypeSpinnerPosition == 9)
            productFeaturesArray = resources.getStringArray(R.array.Product_mushrooms_array);
        else if(productTypeSpinnerPosition == 10)
            productFeaturesArray = resources.getStringArray(R.array.Product_vinegars_array);
        else if(productTypeSpinnerPosition == 11)
            productFeaturesArray = resources.getStringArray(R.array.Product_chemical_products_array);
        else
            productFeaturesArray = resources.getStringArray(R.array.Product_other_products_array);

        for(int counter = 0; productFeaturesArray.length > counter; counter++){
            if(productList.get(0).getProductFeatures().equals(productFeaturesArray[counter]))
                selection = counter;
        }
        return selection;
    }

    public int getProductStorageLocationPosition() {
        List<StorageLocation> storageLocationList = storageLocationDb.storageLocationDao().getAllStorageLocations();
        int selection = 0;
        for(int counter = 0; storageLocationList.size() > counter; counter++){
            if(productList.get(0).getStorageLocation().equals(storageLocationList.get(counter).getName()))
                selection = counter;
        }
        return selection;
    }

    public void setTaste(@Nullable RadioButton selectedTasteButton){
        if(selectedTasteButton == null)
            taste = "";
        else
            taste = selectedTasteButton.getText().toString();
    }

    public void setExpirationDate(@NonNull String date){
        this.expirationDate = date;
    }

    public void setProductionDate(@NonNull String date){
        this.productionDate = date;
    }

    public int[] getExpirationDateArray() {
        String[] stringArray = expirationDate.split("-");
        int[] expirationDateArray = new int[stringArray.length];
        if (stringArray.length > 1) {
            for (int counter = 0; stringArray.length > counter; counter++) {
                expirationDateArray[counter] = Integer.parseInt(stringArray[counter]);
            }
            expirationDateArray[1] = expirationDateArray[1] - 1;
        }
        return expirationDateArray;
    }

    public int[] getProductionDateArray() {
        String[] stringArray = productionDate.split("-");
        int[] productionDateArray = new int[stringArray.length];
        for (int counter = 0; stringArray.length > counter; counter++) {
            productionDateArray[counter] = Integer.parseInt(stringArray[counter]);
        }
        productionDateArray[1]=productionDateArray[1]-1;
        return productionDateArray;
    }

    public boolean isProductNameNotValid(@NonNull Product product) {
        boolean correctProductName = false;
        if (product.getName().length() > 0)
            correctProductName = true;
        return !correctProductName;
    }

    public boolean isTypeOfProductValid(@NonNull Product product) {
        String[] typeOfProductsArray = resources.getStringArray(R.array.Product_type_of_product_array);
        boolean correctTypeOfProduct = false;
        if (!product.getTypeOfProduct().equals(typeOfProductsArray[0]))
            correctTypeOfProduct = true;
        return correctTypeOfProduct;
    }

    public void updateProductsQuantityInOnlineDb(int newProductsQuantity, @NonNull GroupProducts groupProducts) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference().child("products/" + FirebaseAuth.getInstance().getUid());
        for(Product product : productList){
            product.setName(groupProducts.getProduct().getName());
            product.setTypeOfProduct(groupProducts.getProduct().getTypeOfProduct());
            product.setProductFeatures(groupProducts.getProduct().getProductFeatures());
            product.setStorageLocation(groupProducts.getProduct().getStorageLocation());
            product.setHealingProperties(groupProducts.getProduct().getHealingProperties());
            product.setComposition(groupProducts.getProduct().getComposition());
            product.setDosage(groupProducts.getProduct().getDosage());
            product.setWeight(groupProducts.getProduct().getWeight());
            product.setVolume(groupProducts.getProduct().getVolume());
            product.setHasSugar(groupProducts.getProduct().getHasSugar());
            product.setHasSalt(groupProducts.getProduct().getHasSugar());
            product.setIsBio(groupProducts.getProduct().getIsBio());
            product.setIsVege(groupProducts.getProduct().getIsVege());
            product.setBarcode(groupProducts.getProduct().getBarcode());
            product.setPhotoName(groupProducts.getProduct().getPhotoName());
            product.setPhotoDescription(groupProducts.getProduct().getPhotoDescription());
            product.setProductionDate(productionDate);
            product.setExpirationDate(expirationDate);
            product.setTaste(taste);
            ref.child(String.valueOf(product.getId())).setValue(product);
        }
        List<Product> newProductList = new ArrayList<>();
        int productListSize = productList.size();
        int nextId = productList.get(productListSize - 1).getId();
        if(newProductsQuantity > oldProductsQuantity) {
            for (int newProductsCount = newProductsQuantity - oldProductsQuantity; newProductsCount > 0; newProductsCount--) {
                nextId++;
                Product newProduct = new Product();
                newProduct.setId(nextId);
                newProduct.setName(groupProducts.getProduct().getName());
                newProduct.setTypeOfProduct(groupProducts.getProduct().getTypeOfProduct());
                newProduct.setProductFeatures(groupProducts.getProduct().getProductFeatures());
                newProduct.setStorageLocation(groupProducts.getProduct().getStorageLocation());
                newProduct.setExpirationDate(expirationDate);
                newProduct.setProductionDate(productionDate);
                newProduct.setVolume(groupProducts.getProduct().getVolume());
                newProduct.setWeight(groupProducts.getProduct().getWeight());
                newProduct.setComposition(groupProducts.getProduct().getComposition());
                newProduct.setHealingProperties(groupProducts.getProduct().getHealingProperties());
                newProduct.setDosage(groupProducts.getProduct().getDosage());
                newProduct.setHasSugar(groupProducts.getProduct().getHasSugar());
                newProduct.setHasSalt(groupProducts.getProduct().getHasSalt());
                newProduct.setIsVege(groupProducts.getProduct().getIsVege());
                newProduct.setIsBio(groupProducts.getProduct().getIsBio());
                newProduct.setBarcode(groupProducts.getProduct().getBarcode());
                newProduct.setPhotoName(groupProducts.getProduct().getPhotoName());
                newProduct.setPhotoDescription(groupProducts.getProduct().getPhotoDescription());
                newProduct.setTaste(taste);
                newProduct.setHashCode(String.valueOf(newProduct.hashCode()));
                if (groupProducts.getProduct().getProductFeatures().equals(resources.getString(R.string.Product_choose)))
                    newProduct.setProductFeatures("");
                else
                    newProduct.setProductFeatures(groupProducts.getProduct().getProductFeatures());
                newProductList.add(newProduct);
            }
            for (Product product : newProductList) {
                ref.child(String.valueOf(product.getId())).setValue(product);
            }
        }
        if(oldProductsQuantity > newProductsQuantity) {
            List<Product> productListToRemove = new ArrayList<>();
            int quantityToRemove = oldProductsQuantity - newProductsQuantity;
            for(; quantityToRemove > 0; quantityToRemove--) {
                productListToRemove.add(productList.get(quantityToRemove-1));
            }
            for(Product product : productListToRemove) {
                ref.child(String.valueOf(product.getId())).removeValue();
            }
        }
    }

    private void updateProductsQuantityInOfflineDb(int newProductsQuantity, @NonNull GroupProducts groupProducts) {
        for(Product product : productList){
            product.setName(groupProducts.getProduct().getName());
            product.setTypeOfProduct(groupProducts.getProduct().getTypeOfProduct());
            product.setProductFeatures(groupProducts.getProduct().getProductFeatures());
            product.setStorageLocation(groupProducts.getProduct().getStorageLocation());
            product.setHealingProperties(groupProducts.getProduct().getHealingProperties());
            product.setComposition(groupProducts.getProduct().getComposition());
            product.setDosage(groupProducts.getProduct().getDosage());
            product.setWeight(groupProducts.getProduct().getWeight());
            product.setVolume(groupProducts.getProduct().getVolume());
            product.setHasSugar(groupProducts.getProduct().getHasSugar());
            product.setHasSalt(groupProducts.getProduct().getHasSugar());
            product.setIsBio(groupProducts.getProduct().getIsBio());
            product.setIsVege(groupProducts.getProduct().getIsVege());
            product.setBarcode(groupProducts.getProduct().getBarcode());
            product.setPhotoName(groupProducts.getProduct().getPhotoName());
            product.setPhotoDescription(groupProducts.getProduct().getPhotoDescription());
            product.setProductionDate(productionDate);
            product.setExpirationDate(expirationDate);
            product.setTaste(taste);
            productDb.productsDao().updateProduct(product);
        }
        List<Product> newProductList = new ArrayList<>();
        if(newProductsQuantity > oldProductsQuantity){
            for(int newProductsCount = newProductsQuantity - oldProductsQuantity; newProductsCount > 0 ; newProductsCount--){
                Product newProduct = new Product();
                newProduct.setName(groupProducts.getProduct().getName());
                newProduct.setTypeOfProduct(groupProducts.getProduct().getTypeOfProduct());
                newProduct.setProductFeatures(groupProducts.getProduct().getProductFeatures());
                newProduct.setStorageLocation(groupProducts.getProduct().getStorageLocation());
                newProduct.setExpirationDate(expirationDate);
                newProduct.setProductionDate(productionDate);
                newProduct.setVolume(groupProducts.getProduct().getVolume());
                newProduct.setWeight(groupProducts.getProduct().getWeight());
                newProduct.setComposition(groupProducts.getProduct().getComposition());
                newProduct.setHealingProperties(groupProducts.getProduct().getHealingProperties());
                newProduct.setDosage(groupProducts.getProduct().getDosage());
                newProduct.setHasSugar(groupProducts.getProduct().getHasSugar());
                newProduct.setHasSalt(groupProducts.getProduct().getHasSalt());
                newProduct.setIsVege(groupProducts.getProduct().getIsVege());
                newProduct.setIsBio(groupProducts.getProduct().getIsBio());
                newProduct.setBarcode(groupProducts.getProduct().getBarcode());
                newProduct.setPhotoName(groupProducts.getProduct().getPhotoName());
                newProduct.setPhotoDescription(groupProducts.getProduct().getPhotoDescription());
                newProduct.setTaste(taste);
                newProduct.setHashCode(String.valueOf(newProduct.hashCode()));
                if(groupProducts.getProduct().getProductFeatures().equals(resources.getString(R.string.Product_choose)))
                    newProduct.setProductFeatures("");
                else
                    newProduct.setProductFeatures(groupProducts.getProduct().getProductFeatures());
                newProductList.add(newProduct);
            }
            productDb.productsDao().addProducts(newProductList);
        }
        if(oldProductsQuantity > newProductsQuantity){
            List<Product> productListToRemove = new ArrayList<>();
            int quantityToRemove = oldProductsQuantity - newProductsQuantity;
            for(; quantityToRemove > 0; quantityToRemove--){
                productListToRemove.add(productList.get(quantityToRemove-1));
            }
            productDb.productsDao().deleteProducts(productListToRemove);
        }
    }

    public void updateDatabase(@NonNull GroupProducts groupProducts){
        int newProductsQuantity = groupProducts.getQuantity();
        if(databaseMode.equals("local")) {
            updateProductsQuantityInOfflineDb(newProductsQuantity, groupProducts);
        }
        else
            updateProductsQuantityInOnlineDb(newProductsQuantity, groupProducts);
    }

    public String[] getOwnCategoriesArray(){
        return categoryDb.categoryDao().getAllCategoriesArray();
    }

    public String[] getStorageLocationsArray() {
        return storageLocationDb.storageLocationDao().getAllStorageLocationsArray();
    }

    public void setProduct(@NonNull Product product) {
        productList.clear();
        productList.add(product);
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}