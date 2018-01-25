package com.example.android.whatsgood;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;

/**
 * Created by jyoun on 1/25/2018.
 */

@RunWith(AndroidJUnit4.class)
public class BottomNavigationViewTest
{
    // Add the Activity test rule using the @Rule annotation
    // ActivityTestRule is a rule that provides functional testing for a specific single activity
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    // To write the actual test, start with the @Test annotation
    // Test name should clearly indicate the purpose of the test
    @Test
    public void clickBottomNavigationViewItems_TakeUserToFragments()
    {
        /**
         * Navigate to the MapFragment
         */
        onView(withId(R.id.action_map)).perform(click());

        // Check that the map fragment is displayed
        try
        {
            onView(withId(R.id.map)).check(matches(isDisplayed()));
            // View is in the hierarchy
        } catch (NoMatchingViewException e)
        {
            // View is not in the hierarchy
            Log.e("Error navigating to MapFragment", "The map fragment is not in the hierarchy");
        }

        /**
         * Navigate to the ProfileFragment
         */
        onView(withId(R.id.action_profile)).perform(click());

        // Check that the profile image is displayed
        try
        {
            onView(withId(R.id.profile_image)).check(matches(isDisplayed()));
            // View is in the hierarchy
        } catch (NoMatchingViewException e)
        {
            // View is not in the hierarchy
            Log.e("Error navigating to ProfileFragment", "The profile image is not in the hierarchy");
        }

        /**
         * Navigate back to the ListFragment
         */
        onView(withId(R.id.action_list)).perform(click());

        // Check that the restaurant list is displayed
        try
        {
            onView(withId(R.id.list)).check(matches(isDisplayed()));
            // View is in the hierarchy
        } catch (NoMatchingViewException e)
        {
            // View is not in the hierarchy
            Log.e("Error navigating to ListFragment", "The restaurant list is not in the hierarchy");
        }

        /**
         * Navigate to the MapFragment
         */
        onView(withId(R.id.action_map)).perform(click());

        // Check that the map fragment is displayed
        try
        {
            onView(withId(R.id.map)).check(matches(isDisplayed()));
            // View is in the hierarchy
        } catch (NoMatchingViewException e)
        {
            // View is not in the hierarchy
            Log.e("Error navigating to MapFragment", "The map fragment is not in the hierarchy");
        }

        /**
         * Navigate back to the ListFragment
         */
        onView(withId(R.id.action_list)).perform(click());

        // Check that the restaurant list is displayed
        try
        {
            onView(withId(R.id.list)).check(matches(isDisplayed()));
            // View is in the hierarchy
        } catch (NoMatchingViewException e)
        {
            // View is not in the hierarchy
            Log.e("Error navigating to ListFragment", "The restaurant list is not in the hierarchy");
        }

        /**
         * Navigate to the ProfileFragment
         */
        onView(withId(R.id.action_profile)).perform(click());

        // Check that the profile image is displayed
        try
        {
            onView(withId(R.id.profile_image)).check(matches(isDisplayed()));
            // View is in the hierarchy
        } catch (NoMatchingViewException e)
        {
            // View is not in the hierarchy
            Log.e("Error navigating to ProfileFragment", "The profile image is not in the hierarchy");
        }

        /**
         * Navigate back to the ListFragment
         */
        onView(withId(R.id.action_list)).perform(click());

        // Check that the restaurant list is displayed
        try
        {
            onView(withId(R.id.list)).check(matches(isDisplayed()));
            // View is in the hierarchy
        } catch (NoMatchingViewException e)
        {
            // View is not in the hierarchy
            Log.e("Error navigating to ListFragment", "The restaurant list is not in the hierarchy");
        }
    }
}
