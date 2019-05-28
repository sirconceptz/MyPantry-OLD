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

package com.hermanowicz.pantry;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.hermanowicz.pantry.db.Product;
import com.hermanowicz.pantry.db.ProductDb;
import com.hermanowicz.pantry.db.ProductsDao;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductsViewModel extends AndroidViewModel {

    private ProductsDao productsDao;
    private ExecutorService executorService;

    public ProductsViewModel(@NonNull Application application) {
        super(application);
        productsDao = ProductDb.getInstance(application).productsDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    LiveData<List<Product>> getAllProducts() {
        return productsDao.getAllProducts();
    }

    List<Product> getAllProductsAsList(){
        return productsDao.getAllProductsAsList();
    }

    Product getProductById(int id){
        return productsDao.getProductById(id);
    }

    void insertProduct(List<Product> productEntities) {
        executorService.execute(() -> productsDao.insertProductToDB(productEntities));
    }

    void deleteProductById(int id) {
        executorService.execute(() -> productsDao.deleteProductById(id));
    }

    void deleteAllProducts() {
        executorService.execute(() -> productsDao.clearDb());
    }
}