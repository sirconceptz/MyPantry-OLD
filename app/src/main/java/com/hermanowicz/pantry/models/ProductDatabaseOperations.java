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

package com.hermanowicz.pantry.models;

import androidx.lifecycle.LiveData;

import com.hermanowicz.pantry.db.Product;
import com.hermanowicz.pantry.db.ProductDb;

import java.util.ArrayList;
import java.util.List;

public class ProductDatabaseOperations {

    private ProductDb db;

    public ProductDatabaseOperations (ProductDb db){
        this.db = db;
    }

    public Product getProductFromDb(int productId){
        return db.productsDao().getProductById(productId);
    }

    public LiveData<List<Product>> getProductLiveData(){
        return db.productsDao().getAllProductsAsLivedata();
    }

    public List<Product> getAllProducts(){
        return db.productsDao().getAllProductsAsList();
    }

    public void deleteSimilarProducts(int productId){
        Product productToDelete = db.productsDao().getProductById(productId);
        List<Product> productsListToDelete = getSimilarProductsList(productToDelete);
        db.productsDao().deleteProductsFromList(productsListToDelete);
    }

    public void deleteProductsFromList(List<Product> productList){
        db.productsDao().deleteProductsFromList(productList);
    }

    public void addProducts(List<Product> productList){
        db.productsDao().insertProductsToDB(productList);
    }

    public void updateProducts(List<Product> productList){
        for(Product product : productList)
            db.productsDao().updateProduct(product);
    }

    public List<Product> getSimilarProductsList(Product testedProduct){
        List<Product> allProducts = db.productsDao().getAllProductsAsList();
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
                    && singleProduct.getTaste().equals(testedProduct.getTaste()))
                productList.add(singleProduct);
        }
        return productList;
    }
}