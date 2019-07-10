package activities;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.room.Room;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.runner.AndroidJUnit4;

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
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class AppSettingsActivityTest {

    private AppSettingsActivity activity;

    private EditText daysToNotification, emailAddress;
    private NumberPicker hourOfNotification;
    private CheckBox emailNotifications, pushNotifications;
    private Button saveSettings, clearDatabase;
    private TextView appVersion;

    @Rule
    public IntentsTestRule<AppSettingsActivity>
            activityRule = new IntentsTestRule<>(AppSettingsActivity.class);

    @Before
    public void setUp() {
        activity = activityRule.getActivity();
        daysToNotification = activity.findViewById(R.id.edittext_daysToNotification);
        hourOfNotification = activity.findViewById(R.id.numberpicker_hourOfNotification);
        emailNotifications = activity.findViewById(R.id.checkbox_emailNotifications);
        pushNotifications = activity.findViewById(R.id.checkbox_pushNotifications);
        emailAddress = activity.findViewById(R.id.edittext_emailAddress);
        saveSettings = activity.findViewById(R.id.button_saveSettings);
        clearDatabase = activity.findViewById(R.id.button_clearDatabase);
        appVersion = activity.findViewById(R.id.textView_version);
    }

    @Test
    public void onClickSaveNewSettingsShouldBeSaved(){
        activity.runOnUiThread(() -> {
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

        assertEquals("7", daysToNotification.getText().toString());
        assertEquals(15, hourOfNotification.getValue());
        assertTrue(emailNotifications.isChecked());
        assertTrue(!pushNotifications.isChecked());
        assertTrue(emailNotifications.isEnabled());
        assertEquals("email@address.com", emailAddress.getText().toString());
    }

    @Test
    public void onClickClearDatabaseShouldBeCleared(){
        ProductDb productDb = Room.inMemoryDatabaseBuilder(activity.getApplicationContext(),
                ProductDb.class).allowMainThreadQueries().build();

        activity.runOnUiThread(() -> activity.onDatabaseClear());
        assertEquals(0, productDb.productsDao().getAllProductsAsList().size());
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