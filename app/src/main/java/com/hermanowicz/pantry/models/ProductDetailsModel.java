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

package com.hermanowicz.pantry.models;

import com.hermanowicz.pantry.db.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailsModel {

    private Product product;
    private String hashCode;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public boolean compareHashCode() {
        return hashCode.equals(product.getHashCode());
    }

    public List<Product> getProductList(){
        List<Product> productList = new ArrayList<>();
        productList.add(product);
        return productList;
    }

    public boolean productIsNull(){
        return product == null;
    }
}