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

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.hermanowicz.pantry.db.category.Category;
import com.hermanowicz.pantry.db.category.CategoryDb;
import com.hermanowicz.pantry.db.product.Product;
import com.hermanowicz.pantry.db.product.ProductDb;
import com.hermanowicz.pantry.db.storagelocation.StorageLocation;
import com.hermanowicz.pantry.db.storagelocation.StorageLocationDb;

import java.util.ArrayList;
import java.util.List;

public class DatabaseOperations {

    private final ProductDb productDb;
    private final CategoryDb categoryDb;
    private final StorageLocationDb storageLocationDb;

    public DatabaseOperations(Context context){
        this.productDb = ProductDb.getInstance(context);
        this.categoryDb = CategoryDb.getInstance(context);
        this.storageLocationDb = StorageLocationDb.getInstance(context);
    }

    public int getIdOfLastProductInDb(){
        return productDb.productsDao().getIdLastProduct();
    }

    public Product getProductFromDb(int productId){
        return productDb.productsDao().getProduct(productId);
    }

    public LiveData<List<Product>> getProductLiveData(){
        return productDb.productsDao().getAllProductsLivedata();
    }

    public List<Product> getAllProducts(){
        return productDb.productsDao().getAllProductsList();
    }

    public void deleteSimilarProducts(int productId){
        Product productToDelete = productDb.productsDao().getProduct(productId);
        List<Product> productsListToDelete = getSimilarProductsList(productToDelete);
        productDb.productsDao().deleteProducts(productsListToDelete);
    }

    public void deleteProducts(@NonNull List<Product> productList){
        productDb.productsDao().deleteProducts(productList);
    }

    public void addProducts(@NonNull List<Product> productList){
        productDb.productsDao().addProducts(productList);
    }

    public void updateProducts(@NonNull List<Product> productList){
        for(Product product : productList)
            productDb.productsDao().updateProduct(product);
    }

    public List<Product> getSimilarProductsList(@NonNull Product testedProduct){
        List<Product> allProducts = productDb.productsDao().getAllProductsList();
        List<Product> productList = new ArrayList<>();
        for(Product singleProduct : allProducts){
            if(singleProduct.getName().equals(testedProduct.getName())
                    && singleProduct.getTypeOfProduct().equals(testedProduct.getTypeOfProduct())
                    && singleProduct.getProductFeatures().equals(testedProduct.getProductFeatures())
                    && singleProduct.getExpirationDate().equals(testedProduct.getExpirationDate())
                    && singleProduct.getProductionDate().equals(testedProduct.getProductionDate())
                    && singleProduct.getHealingProperties().equals(testedProduct.getHealingProperties())
                    && singleProduct.getComposition().equals(testedProduct.getComposition())
                    && singleProduct.getDosage().equals(testedProduct.getDosage())
                    && singleProduct.getWeight() == testedProduct.getWeight()
                    && singleProduct.getVolume() == testedProduct.getVolume()
                    && singleProduct.getHasSugar() == testedProduct.getHasSugar()
                    && singleProduct.getHasSalt() == testedProduct.getHasSalt()
                    && singleProduct.getIsVege() == testedProduct.getIsVege()
                    && singleProduct.getIsBio() == testedProduct.getIsBio()
                    && singleProduct.getTaste().equals(testedProduct.getTaste())
                    && singleProduct.getStorageLocation().equals(testedProduct.getStorageLocation()))
                productList.add(singleProduct);
        }
        return productList;
    }

    public String[] getOwnCategoriesArray(){
        return categoryDb.categoryDao().getAllCategoriesArray();
    }

    public List<Category> getOwnCategoriesList(){
        return categoryDb.categoryDao().getAllOwnCategories();
    }

    public void addCategory(@NonNull Category category) {
        categoryDb.categoryDao().addCategory(category);
    }

    public Category getCategory(int id) {
        return categoryDb.categoryDao().getCategory(id);
    }

    public void updateCategory(Category category) {
        categoryDb.categoryDao().updateCategory(category);
    }

    public void deleteCategory(int id) {
        Category category = categoryDb.categoryDao().getCategory(id);
        categoryDb.categoryDao().deleteCategory(category);
    }

    public void addStorageLocation(StorageLocation storageLocation) {
        storageLocationDb.storageLocationDao().addStorageLocation(storageLocation);
    }

    public StorageLocation getStorageLocation(int id) {
        return storageLocationDb.storageLocationDao().getStorageLocation(id);
    }

    public void updateStorageLocation(StorageLocation storageLocation) {
        storageLocationDb.storageLocationDao().updateStorageLocation(storageLocation);
    }

    public void deleteStorageLocation(int id) {
        StorageLocation storageLocation = storageLocationDb.storageLocationDao().getStorageLocation(id);
        storageLocationDb.storageLocationDao().deleteStorageLocation(storageLocation);
    }

    public List<StorageLocation> getStorageLocationList() {
        return storageLocationDb.storageLocationDao().getAllStorageLocations();
    }

    public String[] getStorageLocationsArray(){
        return storageLocationDb.storageLocationDao().getAllStorageLocationsArray();
    }
}