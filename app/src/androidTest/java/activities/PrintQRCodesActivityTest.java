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
import static org.junit.Assert.assertNotNull;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.widget.Button;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.activity.MainActivity;
import com.hermanowicz.pantry.activity.PrintQRCodesActivity;
import com.hermanowicz.pantry.db.product.Product;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import models.ProductTestModel;

@RunWith(AndroidJUnit4.class)
public class PrintQRCodesActivityTest {

    private PrintQRCodesActivity activity;

    private Button printQrCodes, sendPdfByEmail, skip;
    private UiDevice uiDevice;

    @Rule
    public GrantPermissionRule readPermissionRule =
            GrantPermissionRule.grant(Manifest.permission.READ_EXTERNAL_STORAGE);

    @Rule
    public GrantPermissionRule writePermissionRule =
            GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);

    @Rule
    public IntentsTestRule<PrintQRCodesActivity>
            activityRule = new IntentsTestRule<PrintQRCodesActivity>(PrintQRCodesActivity.class){

        @Override
        protected Intent getActivityIntent() {
            Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
            List<Product> productList = new ArrayList<>();
            Resources resources = activity.getResources();
            Product product1 = ProductTestModel.getTestProduct1(resources);
            Product product2 = ProductTestModel.getTestProduct2(resources);
            productList.add(product1);
            productList.add(product2);

            return new Intent(targetContext, PrintQRCodesActivity.class)
                    .putExtra("all_product_list", (Serializable) productList)
                    .putExtra("product_list", (Serializable) productList);
        }
    };

    @Before
    public void setUp() {
        activity = activityRule.getActivity();
        uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        printQrCodes = activity.findViewById(R.id.button_printQRCodes);
        sendPdfByEmail = activity.findViewById(R.id.button_sendPdfByEmail);
        skip = activity.findViewById(R.id.button_skip);
    }

    @Test
    public void onClickPrintQRCodesShouldCheckPermissionsAndCreateAndSaveFileAndOpenTheFile() throws InterruptedException {
        assertNotNull(printQrCodes);
        activity.runOnUiThread(() -> printQrCodes.performClick());
        Thread.sleep(3000);
        uiDevice.pressBack();
        uiDevice.pressBack();
        uiDevice.pressBack();
        intended(hasComponent(MainActivity.class.getName()));
    }

    @Test
    public void onClickSendByEmailShouldCreateChooserIntent() throws InterruptedException {
        assertNotNull(sendPdfByEmail);
        activity.runOnUiThread(() -> sendPdfByEmail.performClick());
        Thread.sleep(2000);
        uiDevice.pressBack();
        uiDevice.pressBack();
        uiDevice.pressBack();
        intended(hasComponent(MainActivity.class.getName()));
    }

    @Test
    public void onClickSkipButtonShouldNavigateToMainActivity() {
        assertNotNull(skip);
        activity.runOnUiThread(() -> skip.performClick());
        intended(hasComponent(MainActivity.class.getName()));
    }

    @Test
    public void onPressedBackNavigateToMainActivity() {
        uiDevice.pressBack();
        uiDevice.pressBack();
        intended(hasComponent(MainActivity.class.getName()));
    }
}