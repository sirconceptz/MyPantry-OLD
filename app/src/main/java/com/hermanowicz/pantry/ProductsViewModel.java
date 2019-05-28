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