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

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.activities.AppSettingsActivity;
import com.hermanowicz.pantry.activities.MainActivity;
import com.hermanowicz.pantry.activities.MyPantryActivity;
import com.hermanowicz.pantry.activities.NewProductActivity;
import com.hermanowicz.pantry.activities.ScanProductActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static junit.framework.TestCase.assertNotNull;

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
        Button myPantry = activity.findViewById(R.id.button_myPantry);
        assertNotNull(myPantry);
        activity.runOnUiThread(() -> myPantry.performClick());
        intended(hasComponent(MyPantryActivity.class.getName()));
    }

    @Test
    public void shouldGoToScanProductActivity(){
        Button scanProduct = activity.findViewById(R.id.button_scanProduct);
        assertNotNull(scanProduct);
        activity.runOnUiThread(() -> scanProduct.performClick());
        intended(hasComponent(ScanProductActivity.class.getName()));
    }

    @Test
    public void shouldGoToNewProductActivity(){
        Button newProduct = activity.findViewById(R.id.button_newProduct);
        assertNotNull(newProduct);
        activity.runOnUiThread(() -> newProduct.performClick());
        intended(hasComponent(NewProductActivity.class.getName()));
    }

    @Test
    public void shouldGoToAppSettingsActivity(){
        Button appSettings = activity.findViewById(R.id.button_appSettings);
        assertNotNull(appSettings);
        activity.runOnUiThread(() -> appSettings.performClick());
        intended(hasComponent(AppSettingsActivity.class.getName()));
    }
}