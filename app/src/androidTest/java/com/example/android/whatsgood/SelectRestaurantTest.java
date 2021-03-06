package com.example.android.whatsgood;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

/**
 * Created by jyoun on 1/25/2018.
 * AndroidJUnit4 is a class which will help us control launching the app as well as running UI Tests on it
 * We will be using annotations (i.e. @Override, @Rule, and @Test)
 * - annotations indicate to Android Studio that certain code will need special processing
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
public class SelectRestaurantTest
{
    // The Slate Pub and Detzi's Tavern are the 1st and 2nd restaurants in the list when testing from my house.
    // If my location were to change and The Slate and Detzi's is no longer closest to me,
    // the RESTAURANT_NAMEs will have to change accordingly in order for this test to pass.
    public static final String FIRST_RESTAURANT_NAME = "The Slate Pub";
    public static final String SECOND_RESTAURANT_NAME = "Detzi's Tavern";

    // Add the Activity test rule using the @Rule annotation
    // ActivityTestRule is a rule that provides functional testing for a specific single activity
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    // To write the actual test, start with the @Test annotation
    // Test name should clearly indicate the purpose of the test
    @Test
    public void clickRestaurantListItem_TakesUserToRestaurantActivity()
    {
        // Remember the 3 steps needed to write a UI Test
        // 1) Find the View
        // 2) Perform action on the View
        // click the first item in the list
        onData(anything()).inAdapterView(withId(R.id.list)).atPosition(0).perform(click());

        // 3) Check if the View does what you expected
        // Check that the RestaurantActivity opens with the correct restaurant name displayed
        onView(withId(R.id.restaurant_name)).check(matches(withText(FIRST_RESTAURANT_NAME)));

        // Go back to the MainActivity
        onView(isRoot()).perform(pressBack());

        // click the first item in the list
        onData(anything()).inAdapterView(withId(R.id.list)).atPosition(1).perform(click());

        // Check that the RestaurantActivity opens with the correct restaurant name displayed
        onView(withId(R.id.restaurant_name)).check(matches(withText(SECOND_RESTAURANT_NAME)));

        // Go back to the MainActivity
        onView(isRoot()).perform(pressBack());
    }
}
