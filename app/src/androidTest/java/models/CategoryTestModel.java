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

import com.hermanowicz.pantry.db.category.Category;

public class CategoryTestModel {

    public static Category getTestCategory1(){
        Category category = new Category();
        category.setName("Garage");
        category.setDescription("My little kingdom ;-)");
        return category;
    }

    public static Category getTestCategory2(){
        Category category = new Category();
        category.setName("Basement");
        category.setDescription("Basement under the stairs");
        return category;
    }

    public static Category getTestCategory3(){
        Category category = new Category();
        category.setName("Cabinet in the kitchen");
        category.setDescription("Cabinet in the kitchen above the refrigerator");
        return category;
    }

}