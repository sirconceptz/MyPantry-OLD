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

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.widget.Button;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.activities.MainActivity;
import com.hermanowicz.pantry.activities.PrintQRCodesActivity;
import com.hermanowicz.pantry.db.Product;
import com.hermanowicz.pantry.utils.PrintQRData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import models.ProductTestModel;

import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class PrintQRCodesActivityTest {

    private PrintQRCodesActivity activity;

    private Button printQrCodes, sendPdfByEmail, skip;
    private UiDevice uiDevice;
    private Product product = ProductTestModel.getTestProduct1();

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
            productList.add(product);

            ArrayList<String> textToQrCode = PrintQRData.getTextToQRCodeList(productList, 0);
            ArrayList<String> namesOfProducts = PrintQRData.getNamesOfProductsList(productList);
            ArrayList<String> expirationDates = PrintQRData.getExpirationDatesList(productList);

            return new Intent(targetContext, PrintQRCodesActivity.class)
                    .putStringArrayListExtra("text_to_qr_code", textToQrCode)
                    .putStringArrayListExtra("names_of_products", namesOfProducts)
                    .putStringArrayListExtra("expiration_dates", expirationDates);
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
        File pdfFile = new File(Environment.getExternalStorageDirectory(), "qrcodes-mypantry.pdf");
        assertTrue(pdfFile.exists());
    }

    @Test
    public void onClickSendByEmailShouldCreateChooserIntent() throws InterruptedException {
        assertNotNull(sendPdfByEmail);
        activity.runOnUiThread(() -> sendPdfByEmail.performClick());
        Thread.sleep(2000);
        uiDevice.pressBack();
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
        intended(hasComponent(MainActivity.class.getName()));
    }
}