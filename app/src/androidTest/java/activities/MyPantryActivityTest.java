package activities;

import android.widget.TextView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.google.android.material.navigation.NavigationView;
import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.activities.MainActivity;
import com.hermanowicz.pantry.activities.MyPantryActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

@RunWith(AndroidJUnit4.class)
public class MyPantryActivityTest {

    private MyPantryActivity activity;

    private RecyclerView recyclerViewProducts;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView emptyPantryStatement;

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
    }

    @Test
    public void onStartActivityShowsProductsInRecyclerView(){

    }

    @Test
    public void whenDatabaseHasProductsEmptyPantryStatementNotShows(){

    }

    @Test
    public void whenDatabaseIsEmptyShowsEmptyPantryStatement(){

    }

    @Test
    public void onClickedMenuIconDrawerLayoutIsOpen(){


    }

    @Test
    public void onPressedBackNavigateToMainActivity(){
        Espresso.pressBack();
        intended(hasComponent(MainActivity.class.getName()));
    }
}