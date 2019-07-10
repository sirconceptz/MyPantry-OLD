package presenters;

import androidx.room.Room;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.hermanowicz.pantry.activities.MainActivity;
import com.hermanowicz.pantry.activities.ProductDetailsActivity;
import com.hermanowicz.pantry.activities.ScanProductActivity;
import com.hermanowicz.pantry.db.Product;
import com.hermanowicz.pantry.db.ProductDb;
import com.hermanowicz.pantry.presenters.ScanProductPresenter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import models.ProductModelTest;

import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;


@RunWith(AndroidJUnit4.class)
public class ScanProductPresenterTest {
    private ProductDb productDb;

    private ScanProductActivity activity;
    private ScanProductPresenter presenter;

    @Rule
    public IntentsTestRule<ScanProductActivity> activityTestRule = new IntentsTestRule<>(ScanProductActivity.class);

    @Before
    public void setUp(){
        activity = activityTestRule.getActivity();
        presenter = new ScanProductPresenter(activity);
    }

    @Test
    public void onCorrectScanResultShouldNavigateToProductDetailsActivity() {
        productDb = Room.inMemoryDatabaseBuilder(activity.getApplicationContext(),
                ProductDb.class).allowMainThreadQueries().build();
        List<Product> productList = new ArrayList<>();

        productList.add(ProductModelTest.getTestProduct());
        productDb.productsDao().insertProductsToDB(productList);
        productList = productDb.productsDao().getAllProductsAsList();

        String happyScenario = "{\"product_id\":" + productList.get(0).getId() + ",\"hash_code\":" + productList.get(0).getHashCode() + "}";
        activity.runOnUiThread(() -> presenter.onScanResult(happyScenario));
        intended(hasComponent(ProductDetailsActivity.class.getName()));
    }

    @Test
    public void navigateToMainActivityIfProductNotFound() {
        String badScenario = "sgfsdfsdfsdf";

        activity.runOnUiThread(() -> presenter.onScanResult(badScenario));
        intended(hasComponent(MainActivity.class.getName()));
    }

    @Test
    public void navigateToMainActivity() {
        presenter.navigateToMainActivity();
        intended(hasComponent(MainActivity.class.getName()));
    }
}