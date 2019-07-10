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

package activities;

import android.content.Intent;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.rule.IntentsTestRule;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.activities.MyPantryActivity;
import com.hermanowicz.pantry.activities.ProductDetailsActivity;
import com.hermanowicz.pantry.db.Product;
import com.hermanowicz.pantry.db.ProductDb;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import models.ProductModelTest;

import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static org.junit.Assert.assertEquals;

public class ProductDetailsActivityTest {

    private ProductDetailsActivity activity;

    private Product product = ProductModelTest.getTestProduct();
    private Toolbar toolbar;
    private TextView productType, productFeatures, productionDate, expirationDate, volume, weight,
                     taste, hasSugar, hasSalt, composition, healingProperties, dosage;

    @Rule
    public IntentsTestRule<ProductDetailsActivity> activityRule = new IntentsTestRule<>
            (ProductDetailsActivity.class);

    @Before
    public void setUp() {
        activity = activityRule.getActivity();
        List<Product> productList = new ArrayList<>();
        productList.add(product);

        Intent intent = new Intent()
                .putExtra("product_id", 1)
                .putExtra("hash_code", product.getHashCode());

        ProductDb productDb = Room.inMemoryDatabaseBuilder(activity,
                ProductDb.class).allowMainThreadQueries().build();

        productDb.productsDao().clearDb();
        productDb.productsDao().insertProductsToDB(productList);

        activityRule.launchActivity(intent);

        toolbar = activity.findViewById(R.id.toolbar);
        productType = activity.findViewById(R.id.text_productTypeValue);
        productFeatures = activity.findViewById(R.id.text_productFeaturesValue);
        productionDate = activity.findViewById(R.id.text_productProductionDateValue);
        expirationDate = activity.findViewById(R.id.text_productExpirationDateValue);
        volume = activity.findViewById(R.id.text_productVolumeValue);
        weight = activity.findViewById(R.id.text_productWeightValue);
        taste = activity.findViewById(R.id.text_productTasteValue);
        hasSugar = activity.findViewById(R.id.text_productHasSugarValue);
        hasSalt = activity.findViewById(R.id.text_productHasSaltValue);
        composition = activity.findViewById(R.id.text_productCompositionValue);
        healingProperties = activity.findViewById(R.id.text_productHealingPropertiesValue);
        dosage = activity.findViewById(R.id.text_productDosageValue);
    }

    @Test
    public void shouldShowTheProductDetails(){
        assertEquals(product.getName(), toolbar.getTitle().toString());
        assertEquals(product.getTypeOfProduct(), productType.getText().toString());
        assertEquals(product.getProductFeatures(), productFeatures.getText().toString());
        assertEquals(product.getProductionDate(), productionDate.getText().toString());
        assertEquals(product.getExpirationDate(), expirationDate.getText().toString());
        assertEquals(String.valueOf(product.getVolume()), volume.getText().toString());
        assertEquals(String.valueOf(product.getWeight()), weight.getText().toString());
        assertEquals(product.getTaste(), taste.getText().toString());
        assertEquals(activity.getString(R.string.ProductDetailsActivity_yes), hasSugar.getText().toString());
        assertEquals(activity.getString(R.string.ProductDetailsActivity_no), hasSalt.getText().toString());
        assertEquals(product.getComposition(), composition.getText().toString());
        assertEquals(product.getHealingProperties(), healingProperties.getText().toString());
        assertEquals(product.getDosage(), dosage.getText().toString());
    }

    @Test
    public void onPressedBackNavigateToMyPantryActivity(){
        Espresso.pressBack();
        intended(hasComponent(MyPantryActivity.class.getName()));
    }
}