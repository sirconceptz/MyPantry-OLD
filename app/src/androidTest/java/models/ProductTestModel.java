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

package models;

import com.hermanowicz.pantry.db.product.Product;

public class ProductTestModel {

    public static Product getTestProduct1(){
        Product product = new Product();
        product.setName("Raspberry juice");
        product.setTypeOfProduct("Fruits");
        product.setProductFeatures("Juice");
        product.setExpirationDate("2020-01-01");
        product.setProductionDate("2019-12-31");
        product.setComposition("Raspberries");
        product.setHealingProperties("");
        product.setDosage("Much");
        product.setVolume(500);
        product.setWeight(1000);
        product.setHasSugar(true);
        product.setHasSalt(false);
        product.setBarcode("");
        product.setTaste("Sweet");
        product.setStorageLocation("");
        product.setHashCode(String.valueOf(product.hashCode()));
        return product;
    }

    public static Product getTestProduct2(){
        Product product = new Product();
        product.setName("Pizza");
        product.setTypeOfProduct("Ready meals");
        product.setProductFeatures("Ready meals");
        product.setExpirationDate("2022-02-17");
        product.setProductionDate("2018-11-28");
        product.setComposition("");
        product.setHealingProperties("");
        product.setDosage("");
        product.setVolume(800);
        product.setWeight(9000);
        product.setHasSugar(false);
        product.setHasSalt(true);
        product.setBarcode("");
        product.setTaste("Salty");
        product.setStorageLocation("");
        product.setHashCode(String.valueOf(product.hashCode()));
        return product;
    }

    public static Product getTestProduct3(){
        Product product = new Product();
        product.setName("Tomato");
        product.setTypeOfProduct("Vegetables");
        product.setProductFeatures("Fresh");
        product.setExpirationDate("2021-07-12");
        product.setProductionDate("2018-12-10");
        product.setComposition("");
        product.setHealingProperties("");
        product.setDosage("");
        product.setVolume(400);
        product.setWeight(5000);
        product.setHasSugar(false);
        product.setHasSalt(false);
        product.setBarcode("");
        product.setTaste("");
        product.setStorageLocation("");
        product.setHashCode(String.valueOf(product.hashCode()));
        return product;
    }

}