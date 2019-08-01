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

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.runner.AndroidJUnit4;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.activities.MainActivity;
import com.hermanowicz.pantry.activities.MyPantryActivity;
import com.hermanowicz.pantry.activities.ProductDetailsActivity;
import com.hermanowicz.pantry.db.Product;
import com.hermanowicz.pantry.db.ProductDb;

import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import models.ProductTestModel;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

//Tests MyPantryActivity, ProductDetailActivity

@RunWith(AndroidJUnit4.class)
public class MyPantryActivityTest {

    private MyPantryActivity activity;

    private DrawerLayout drawerLayout;
    private TextView emptyPantryStatement;
    private int count = 0;

    @Rule
    public IntentsTestRule<MyPantryActivity>
            activityRule = new IntentsTestRule<>(MyPantryActivity.class);

    @Before
    public void setUp() {
        activity = activityRule.getActivity();
        drawerLayout = activity.findViewById(R.id.my_pantry_drawer_layout);
        emptyPantryStatement = activity.findViewById(R.id.text_emptyPantryStatement);
    }

    @Test
    public void whenProductsInDbShowsProductsInRecyclerView() {
        insertTestProductsToDbAndRestartActivity();
        int recyclerViewItems = getCountFromRecyclerView(R.id.recyclerview_products);
        assertEquals(9, recyclerViewItems);
        ProductDb.getInstance(activity).productsDao().clearDb();
    }

    @Test
    public void onClickOnItemInRecyclerViewNavigateToProductDetailsActivity() {
        insertTestProductsToDbAndRestartActivity();
        onView(ViewMatchers.withId(R.id.recyclerview_products))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        intended(hasComponent(ProductDetailsActivity.class.getName()));
        ProductDb.getInstance(activity).productsDao().clearDb();
    }

    @Test
    public void whenDatabaseHasProductsEmptyPantryStatementNotShows() {
        insertTestProductsToDbAndRestartActivity();
        assertEquals("", emptyPantryStatement.getText());
        ProductDb.getInstance(activity).productsDao().clearDb();
    }

    @Test
    public void onSetNameFilterShouldShowProductsWithThisName() throws InterruptedException {
        insertTestProductsToDbAndRestartActivity();
        onView(withId(R.id.my_pantry_drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.filter_name));
        onView(withId(R.id.name)).perform(typeText(ProductTestModel.getTestProduct1().getName()));
        onView(withText(R.string.MyPantryActivity_set)).perform(click());
        Thread.sleep(300);
        int recyclerViewItems = getCountFromRecyclerView(R.id.recyclerview_products);
        assertEquals(3, recyclerViewItems);
        ProductDb.getInstance(activity).productsDao().clearDb();
    }

    @Test
    public void onSetWeightFilterShouldShowProductsWithThisName() throws InterruptedException {
        insertTestProductsToDbAndRestartActivity();
        onView(withId(R.id.my_pantry_drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.filter_weight));
        onView(withId(R.id.edittext_weightSince)).perform(typeText("1200"));
        onView(withText(R.string.MyPantryActivity_set)).perform(click());
        Thread.sleep(300);
        int recyclerViewItems = getCountFromRecyclerView(R.id.recyclerview_products);
        assertEquals(6, recyclerViewItems);
        ProductDb.getInstance(activity).productsDao().clearDb();
    }

    @Test
    public void onSetVolumeFilterShouldShowProductsWithThisVolume() throws InterruptedException {
        insertTestProductsToDbAndRestartActivity();
        onView(withId(R.id.my_pantry_drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.filter_volume));
        onView(withId(R.id.edittext_volumeSince)).perform(typeText("100"));
        onView(withId(R.id.edittext_volumeFor)).perform(typeText("700"));
        onView(withText(R.string.MyPantryActivity_set)).perform(click());
        Thread.sleep(300);
        int recyclerViewItems = getCountFromRecyclerView(R.id.recyclerview_products);
        assertEquals(6, recyclerViewItems);
        ProductDb.getInstance(activity).productsDao().clearDb();
    }

    @Test
    public void onSetHasSugarFilterShouldShowProductsWithThisName() throws InterruptedException {
        insertTestProductsToDbAndRestartActivity();
        onView(withId(R.id.my_pantry_drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.filter_product_features));
        onView(withId(R.id.checkbox_hasSugar)).perform(click());
        onView(withText(R.string.MyPantryActivity_set)).perform(click());
        Thread.sleep(300);
        int recyclerViewItems = getCountFromRecyclerView(R.id.recyclerview_products);
        assertEquals(3, recyclerViewItems);
        ProductDb.getInstance(activity).productsDao().clearDb();
    }

    @Test
    public void whenDatabaseIsEmptyShowsEmptyPantryStatement() {
        assertEquals(activity.getString(R.string.MyPantryActivity_empty_pantry), emptyPantryStatement.getText());
    }

    @Test
    public void onClickedMenuIconDrawerLayoutIsOpen() {
        onView(withId(R.id.my_pantry_drawer_layout)).perform(DrawerActions.open());
        assertTrue(drawerLayout.isShown());
    }

    @Test
    public void onPressedBackNavigateToMainActivity() {
        Espresso.pressBack();
        intended(hasComponent(MainActivity.class.getName()));
    }

    private void insertTestProductsToDbAndRestartActivity() {
        List<Product> productList = new ArrayList<>();
        for (int counter = 0; 3 > counter; counter++) {
            productList.add(ProductTestModel.getTestProduct1());
            productList.add(ProductTestModel.getTestProduct2());
            productList.add(ProductTestModel.getTestProduct3());
        }
        ProductDb.getInstance(activity).productsDao().insertProductsToDB(productList);
        activityRule.finishActivity();
        activityRule.launchActivity(new Intent());
    }

    public int getCountFromRecyclerView(@IdRes int RecyclerViewId) {
        Matcher matcher = new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(org.hamcrest.Description description) {
            }

            @Override
            protected boolean matchesSafely(View item) {
                count = ((RecyclerView) item).getAdapter().getItemCount();
                return true;
            }
        };
        onView(allOf(withId(RecyclerViewId), isDisplayed())).check(matches(matcher));
        int result = count;
        count = 0;
        return result;
    }
}