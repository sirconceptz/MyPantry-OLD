package activities;

import android.widget.TextView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.runner.AndroidJUnit4;

import com.google.android.material.navigation.NavigationView;
import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.activities.MainActivity;
import com.hermanowicz.pantry.activities.MyPantryActivity;
import com.hermanowicz.pantry.db.Product;
import com.hermanowicz.pantry.db.ProductDb;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import models.ProductModelTest;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class MyPantryActivityTest {

    private MyPantryActivity activity;

    private RecyclerView recyclerViewProducts;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView emptyPantryStatement;
    private ProductDb productDb;

    @Rule
    public IntentsTestRule<MyPantryActivity>
            activityRule = new IntentsTestRule<>(MyPantryActivity.class);

    @Before
    public void setUp() {
        activity = activityRule.getActivity();
        recyclerViewProducts = activity.findViewById(R.id.recyclerview_products);
        drawerLayout = activity.findViewById(R.id.my_pantry_drawer_layout);
        navigationView = activity.findViewById(R.id.nav_view);
        emptyPantryStatement = activity.findViewById(R.id.text_emptyPantryStatement);

        productDb = ProductDb.getInstance(activity);

        List<Product> productList = new ArrayList<>();
        for(int counter = 0; 3 > counter; counter++){
            productList.add(ProductModelTest.getTestProduct());
        }
        productDb.productsDao().insertProductsToDB(productList);
    }

    @Test
    public void onStartActivityShowsProductsInRecyclerView(){
    }

    @Test
    public void whenDatabaseHasProductsEmptyPantryStatementNotShows(){
        try {
            activityRule.runOnUiThread(() -> activity.updateSelectsRecyclerViewAdapter());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        assertEquals("", emptyPantryStatement.getText());
    }

    @Test
    public void whenDatabaseIsEmptyShowsEmptyPantryStatement(){
        productDb.productsDao().clearDb();
        try {
            activityRule.runOnUiThread(() -> activity.updateSelectsRecyclerViewAdapter());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        assertEquals(activity.getString(R.string.MyPantryActivity_empty_pantry), emptyPantryStatement.getText());
    }

    @Test
    public void onClickedMenuIconDrawerLayoutIsOpen(){
        Espresso.onView(ViewMatchers.withId(R.id.homeAsUp)).perform(click());
        assertTrue(drawerLayout.isShown());
    }

    @Test
    public void onPressedBackNavigateToMainActivity(){
        Espresso.pressBack();
        intended(hasComponent(MainActivity.class.getName()));
    }
}