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

import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.activities.MainActivity;
import com.hermanowicz.pantry.activities.NewProductActivity;
import com.hermanowicz.pantry.activities.PrintQRCodesActivity;
import com.hermanowicz.pantry.db.Product;
import com.hermanowicz.pantry.db.ProductDb;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import models.ProductTestModel;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

@RunWith(AndroidJUnit4.class)
public class NewProductActivityTest {

    private NewProductActivity activity;

    private EditText    name, expirationDate, productionDate, quantity, composition, healingProperties,
                        dosage, volume, weight;
    private Spinner     typeOfProduct;
    private RadioButton isSweet;
    private Button      addProduct;
    private Product     product;

    @Rule
    public IntentsTestRule<NewProductActivity>
            activityRule = new IntentsTestRule<>(NewProductActivity.class);

    @Before
    public void setUp() {
        activity = activityRule.getActivity();
        name = activity.findViewById(R.id.edittext_name);
        expirationDate = activity.findViewById(R.id.edittext_expirationDate);
        typeOfProduct = activity.findViewById(R.id.spinner_productType);
        productionDate = activity.findViewById(R.id.edittext_productionDate);
        quantity = activity.findViewById(R.id.edittext_quantity);
        composition = activity.findViewById(R.id.edittext_composition);
        healingProperties = activity.findViewById(R.id.edittext_healingProperties);
        dosage = activity.findViewById(R.id.edittext_dosage);
        volume = activity.findViewById(R.id.edittext_volume);
        weight = activity.findViewById(R.id.edittext_weight);
        isSweet = activity.findViewById(R.id.radiobtn_isSweet);
        addProduct = activity.findViewById(R.id.button_addProduct);
        product = ProductTestModel.getTestProduct1();
    }

    @Test
    public void notSpecifyingTheProductNameShouldDisplayAnError(){
        activity.runOnUiThread(() -> {
            quantity.requestFocus();
            quantity.setText("5");
            typeOfProduct.requestFocus();
            typeOfProduct.setSelection(4);
            expirationDate.requestFocus();
            expirationDate.setText(getDateInLocalFormat(product.getExpirationDate()));
            productionDate.requestFocus();
            productionDate.setText(getDateInLocalFormat(product.getProductionDate()));
            composition.requestFocus();
            composition.setText(product.getComposition());
            healingProperties.requestFocus();
            healingProperties.setText(product.getHealingProperties());
            dosage.requestFocus();
            dosage.setText(product.getDosage());
            volume.requestFocus();
            volume.setText(String.valueOf(product.getVolume()));
            weight.requestFocus();
            weight.setText(String.valueOf(product.getWeight()));
            isSweet.requestFocus();
            isSweet.setChecked(true);
            addProduct.performClick();
        });
        onView(withId(R.id.edittext_name)).check(matches(hasErrorText(activity.getString(R.string.Errors_product_name_is_required))));
    }

    @Test
    public void notSpecifyingTheTypeOfProductShouldDisplayAnError(){
        activity.runOnUiThread(() -> {
            name.requestFocus();
            name.setText(product.getName());
            quantity.requestFocus();
            quantity.setText("5");
            expirationDate.requestFocus();
            expirationDate.setText(getDateInLocalFormat(product.getExpirationDate()));
            productionDate.requestFocus();
            productionDate.setText(getDateInLocalFormat(product.getProductionDate()));
            composition.requestFocus();
            composition.setText(product.getComposition());
            healingProperties.requestFocus();
            healingProperties.setText(product.getHealingProperties());
            dosage.requestFocus();
            dosage.setText(product.getDosage());
            volume.requestFocus();
            volume.setText(String.valueOf(product.getVolume()));
            weight.requestFocus();
            weight.setText(String.valueOf(product.getWeight()));
            isSweet.requestFocus();
            isSweet.setChecked(true);
            addProduct.performClick();
        });
        onView(withText(R.string.Errors_category_not_selected)).inRoot(withDecorView(not(is(activity.getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void onPressedBackNavigateToMainActivity(){
        Espresso.pressBack();
        intended(hasComponent(MainActivity.class.getName()));
    }

    @Test
    public void addingTheProductShouldNavigateToPrintQRDetailsActivity(){
        activity.runOnUiThread(() -> {
            name.requestFocus();
            name.setText(product.getName());
            typeOfProduct.requestFocus();
            typeOfProduct.setSelection(4);
            quantity.requestFocus();
            quantity.setText("5");
            expirationDate.requestFocus();
            expirationDate.setText(getDateInLocalFormat(product.getExpirationDate()));
            productionDate.requestFocus();
            productionDate.setText(getDateInLocalFormat(product.getProductionDate()));
            composition.requestFocus();
            composition.setText(product.getComposition());
            healingProperties.requestFocus();
            healingProperties.setText(product.getHealingProperties());
            dosage.requestFocus();
            dosage.setText(product.getDosage());
            volume.requestFocus();
            volume.setText(String.valueOf(product.getVolume()));
            weight.requestFocus();
            weight.setText(String.valueOf(product.getWeight()));
            isSweet.requestFocus();
            isSweet.setChecked(true);
            addProduct.performClick();
        });
        intended(hasComponent(PrintQRCodesActivity.class.getName()));
    }

    private String getDateInLocalFormat(String dateToConvert){
        DateFormat localDateFormat = DateFormat.getDateInstance();
        Calendar calendar = Calendar.getInstance();
        String[] dateArray = dateToConvert.split("-");
        calendar.set(Integer.parseInt(dateArray[0]), Integer.parseInt(dateArray[1])-1, Integer.parseInt(dateArray[2]));
        Date date = calendar.getTime();
        return localDateFormat.format(date);
    }

    @After
    public void tearDown(){
        ProductDb.getInstance(activity.getApplicationContext()).productsDao().clearDb();
    }
}