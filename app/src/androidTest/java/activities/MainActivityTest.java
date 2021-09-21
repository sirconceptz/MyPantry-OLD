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

package activities;

import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static junit.framework.TestCase.assertNotNull;

import androidx.cardview.widget.CardView;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.activity.AppSettingsActivity;
import com.hermanowicz.pantry.activity.CategoriesActivity;
import com.hermanowicz.pantry.activity.MainActivity;
import com.hermanowicz.pantry.activity.MyPantryActivity;
import com.hermanowicz.pantry.activity.NewProductActivity;
import com.hermanowicz.pantry.activity.ScanProductActivity;
import com.hermanowicz.pantry.activity.StorageLocationsActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private MainActivity activity;

    @Rule
    public IntentsTestRule<MainActivity> activityRule = new IntentsTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        activity = activityRule.getActivity();
    }

    @Test
    public void shouldGoToMyPantryActivity(){
        CardView myPantry = activity.findViewById(R.id.myPantryCV);
        assertNotNull(myPantry);
        activity.runOnUiThread(() -> myPantry.performClick());
        intended(hasComponent(MyPantryActivity.class.getName()));
    }

    @Test
    public void shouldGoToScanProductActivity(){
        CardView scanProduct = activity.findViewById(R.id.scanProductCV);
        assertNotNull(scanProduct);
        activity.runOnUiThread(() -> scanProduct.performClick());
        intended(hasComponent(ScanProductActivity.class.getName()));
    }

    @Test
    public void shouldGoToNewProductActivity() {
        CardView newProduct = activity.findViewById(R.id.newProductCV);
        assertNotNull(newProduct);
        activity.runOnUiThread(() -> newProduct.performClick());
        intended(hasComponent(NewProductActivity.class.getName()));
    }

    @Test
    public void shouldGoToOwnCategoriesActivity() {
        CardView ownCategories = activity.findViewById(R.id.ownCategoriesCV);
        assertNotNull(ownCategories);
        activity.runOnUiThread(() -> ownCategories.performClick());
        intended(hasComponent(CategoriesActivity.class.getName()));
    }

    @Test
    public void shouldGoToStorageLocationsActivity() {
        CardView storageLocations = activity.findViewById(R.id.storageLocationsCV);
        assertNotNull(storageLocations);
        activity.runOnUiThread(() -> storageLocations.performClick());
        intended(hasComponent(StorageLocationsActivity.class.getName()));
    }

    @Test
    public void shouldGoToAppSettingsActivity() {
        CardView appSettings = activity.findViewById(R.id.appSettingsCV);
        assertNotNull(appSettings);
        activity.runOnUiThread(() -> appSettings.performClick());
        intended(hasComponent(AppSettingsActivity.class.getName()));
    }
}