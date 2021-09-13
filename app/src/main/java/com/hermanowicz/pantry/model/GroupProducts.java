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

import androidx.annotation.NonNull;

import com.hermanowicz.pantry.db.product.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>GroupProducts</h1>
 * Model class
 *
 * @author  Mateusz Hermanowicz
 */

public final class GroupProducts {

    private Product product;
    private int quantity;

    public GroupProducts(@NonNull Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setProduct(Product product){
        this.product = product;
    }

    void setQuantity(int quantity){
        this.quantity = quantity;
    }

    @NonNull
    public static List<GroupProducts> getGroupProducts(@NonNull List<Product> productList){
        List<GroupProducts> groupProductsList = new ArrayList<>();
        List<GroupProducts> toAddGroupProductsList = new ArrayList<>();
        List<GroupProducts> toRemoveGroupProductsList = new ArrayList<>();
        for (Product product: productList){
            boolean productOnList = false;
            if(product.getPhotoName() == null)
                product.setPhotoName("");
            if(product.getPhotoDescription() == null)
                product.setPhotoDescription("");
            GroupProducts testedGroupProducts = new GroupProducts(product, 1);
            for (GroupProducts groupProduct : groupProductsList) {
                if (groupProduct.getProduct().getName().toLowerCase().contains(product.getName().toLowerCase())
                        && groupProduct.getProduct().getExpirationDate().equals(product.getExpirationDate())
                        && groupProduct.getProduct().getProductFeatures().equals(product.getProductFeatures())
                        && groupProduct.getProduct().getComposition().equals(product.getComposition())
                        && groupProduct.getProduct().getHealingProperties().equals(product.getHealingProperties())
                        && groupProduct.getProduct().getDosage().equals(product.getDosage())
                        && groupProduct.getProduct().getVolume() == product.getVolume()
                        && groupProduct.getProduct().getWeight() == product.getWeight()
                        && groupProduct.getProduct().getTypeOfProduct().equals(product.getTypeOfProduct())
                        && groupProduct.getProduct().getProductFeatures().equals(product.getProductFeatures())
                        && groupProduct.getProduct().getHasSalt() == product.getHasSalt()
                        && groupProduct.getProduct().getHasSugar() == product.getHasSugar()
                        && groupProduct.getProduct().getIsBio() == product.getIsBio()
                        && groupProduct.getProduct().getIsVege() == product.getIsVege()
                        && groupProduct.getProduct().getBarcode().equals(product.getBarcode())
                        && groupProduct.getProduct().getPhotoName().equals(product.getPhotoName())
                        && groupProduct.getProduct().getPhotoDescription().equals(product.getPhotoDescription())
                        && groupProduct.getProduct().getTaste().equals(product.getTaste())) {
                    productOnList = true;
                    testedGroupProducts = groupProduct;
                    break;
                }
            }
            if(productOnList) {
                toRemoveGroupProductsList.add(testedGroupProducts);
                testedGroupProducts.setQuantity(testedGroupProducts.getQuantity() + 1);
                toAddGroupProductsList.add(testedGroupProducts);
            }
            else {
                GroupProducts newGroupProduct = new GroupProducts(product, 1);
                toAddGroupProductsList.add(newGroupProduct);
            }
            groupProductsList.removeAll(toRemoveGroupProductsList);
            groupProductsList.addAll(toAddGroupProductsList);
            toAddGroupProductsList.clear();
            toRemoveGroupProductsList.clear();
        }
        return groupProductsList;
    }
}