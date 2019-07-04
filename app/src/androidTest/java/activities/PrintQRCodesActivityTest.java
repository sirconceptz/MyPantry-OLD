package activities;

import android.content.Intent;
import android.os.Environment;
import android.widget.Button;
import android.widget.ImageView;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.activities.MainActivity;
import com.hermanowicz.pantry.activities.NewProductActivity;
import com.hermanowicz.pantry.activities.PrintQRCodesActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.ArrayList;

import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class PrintQRCodesActivityTest {

    private PrintQRCodesActivity activity;

    private ImageView qrCodeImage;
    private Button printQrCodes, sendPdfByEmail, skip;
    private UiDevice uiDevice;

    @Rule
    public IntentsTestRule<PrintQRCodesActivity>
            activityRule = new IntentsTestRule<>(PrintQRCodesActivity.class,
            false, false);

    @Before
    public void setUp() {
        Intent intent = new Intent();
        ArrayList<String> textToQrCode = new ArrayList<>();
        ArrayList<String> namesOfProducts = new ArrayList<>();
        ArrayList<String> expirationDates = new ArrayList<>();

        textToQrCode.add("{\"product_id\":5,\"hash_code\":123456789}");
        namesOfProducts.add("Raspberry juice");
        expirationDates.add("2020-05-14");

        intent.putStringArrayListExtra("text_to_qr_code", textToQrCode);
        intent.putStringArrayListExtra("names_of_products", namesOfProducts);
        intent.putStringArrayListExtra("expiration_dates", expirationDates);
        activityRule.launchActivity(intent);
        activity = activityRule.getActivity();
        uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        qrCodeImage = activity.findViewById(R.id.image_qrCode);
        printQrCodes = activity.findViewById(R.id.button_printQRCodes);
        sendPdfByEmail = activity.findViewById(R.id.button_sendPdfByEmail);
        skip = activity.findViewById(R.id.button_skip);
    }

    @Test
    public void onClickPrintQRCodesShouldCheckPermissionsAndCreateAndSaveFileAndOpenTheFile(){
        assertNotNull(printQrCodes);
        activity.runOnUiThread(() -> printQrCodes.performClick());
        uiDevice.pressBack();
        File pdfFile = new File(Environment.getExternalStorageDirectory(), "qrcodes-mypantry.pdf");
        assertTrue(pdfFile.exists());
    }

    @Test
    public void onClickSendByEmailShouldCreateChooserIntent(){
        assertNotNull(sendPdfByEmail);
        activity.runOnUiThread(() -> sendPdfByEmail.performClick());
        uiDevice.pressBack();
    }

    @Test
    public void onClickSkipButtonShouldNavigateToNewProductActivity(){
        assertNotNull(skip);
        activity.runOnUiThread(() -> skip.performClick());
        intended(hasComponent(NewProductActivity.class.getName()));
    }

    @Test
    public void onPressedBackNavigateToMainActivity(){
        Espresso.pressBack();
        intended(hasComponent(MainActivity.class.getName()));
    }
}