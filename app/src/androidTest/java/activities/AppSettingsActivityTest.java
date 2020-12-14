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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.room.Room;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.hermanowicz.pantry.BuildConfig;
import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.activities.AppSettingsActivity;
import com.hermanowicz.pantry.activities.MainActivity;
import com.hermanowicz.pantry.db.ProductDb;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class AppSettingsActivityTest {

    private AppSettingsActivity activity;

    private EditText daysToNotification, emailAddress;
    private Spinner appThemeSelector, cameraSelector;
    private NumberPicker hourOfNotification;
    private CheckBox emailNotifications, pushNotifications;
    private Button saveSettings, clearProductDatabase;
    private TextView appVersion;

    @Rule
    public IntentsTestRule<AppSettingsActivity>
            activityRule = new IntentsTestRule<>(AppSettingsActivity.class);

    @Before
    public void setUp() {
        activity = activityRule.getActivity();
        appThemeSelector = activity.findViewById(R.id.spinner_appThemeSelector);
        cameraSelector = activity.findViewById(R.id.spinner_cameraSelector);
        daysToNotification = activity.findViewById(R.id.edittext_daysToNotification);
        hourOfNotification = activity.findViewById(R.id.numberpicker_hourOfNotification);
        emailNotifications = activity.findViewById(R.id.checkbox_emailNotifications);
        pushNotifications = activity.findViewById(R.id.checkbox_pushNotifications);
        emailAddress = activity.findViewById(R.id.edittext_emailAddress);
        saveSettings = activity.findViewById(R.id.button_saveSettings);
        clearProductDatabase = activity.findViewById(R.id.button_clear_product_database);
        appVersion = activity.findViewById(R.id.appVersion);
    }

    @Test
    public void onClickSaveNewSettingsShouldBeSaved(){
        String[] cameraAvailableModes, appThemeAvailableModes;
        cameraAvailableModes = activity.getResources().getStringArray(R.id.spinner_cameraSelector);
        appThemeAvailableModes = activity.getResources().getStringArray(R.id.spinner_appThemeSelector);

        activity.runOnUiThread(() -> {
            appThemeSelector.setId(1);
            cameraSelector.setId(1);
            daysToNotification.setText("7");
            hourOfNotification.setValue(15);
            emailAddress.setText("email@address.com");
            pushNotifications.setChecked(false);
            emailNotifications.setChecked(true);

            saveSettings.performClick();

        });
        onView(withText(R.string.AppSettingsActivity_settings_saved_successful)).inRoot(withDecorView(not(is(activity.getWindow().getDecorView())))).check(matches(isDisplayed()));

        activity.finish();
        activity = activityRule.getActivity();

        assertEquals(appThemeAvailableModes[1], appThemeSelector.getSelectedItem().toString());
        assertEquals(cameraAvailableModes[1], cameraSelector.getSelectedItem().toString());
        assertEquals("7", daysToNotification.getText().toString());
        assertEquals(15, hourOfNotification.getValue());
        assertTrue(emailNotifications.isChecked());
        assertFalse(pushNotifications.isChecked());
        assertTrue(emailNotifications.isEnabled());
        assertEquals("email@address.com", emailAddress.getText().toString());
    }

    @Test
    public void onClickClearDatabaseShouldBeCleared(){
        ProductDb productDb = Room.inMemoryDatabaseBuilder(activity.getApplicationContext(),
                ProductDb.class).allowMainThreadQueries().build();

        activity.runOnUiThread(() -> activity.onDatabaseClear());
        assertEquals(0, productDb.productsDao().getAllProductsList().size());
    }

    @Test
    public void onStartActivityShouldShowAppVersion(){
        assertEquals(String.format("%s: %s", activity.getString(R.string.AppSettingsActivity_version),
                BuildConfig.VERSION_NAME), appVersion.getText().toString());
    }

    @Test
    public void onPressedBackNavigateToMainActivity(){
        Espresso.pressBack();
        intended(hasComponent(MainActivity.class.getName()));
    }
}