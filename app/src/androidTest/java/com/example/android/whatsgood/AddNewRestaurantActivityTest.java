package com.example.android.whatsgood;

import static android.app.Instrumentation.ActivityResult;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtras;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by jyoun on 1/25/2018.
 * <p>
 * This test class tests intents using intent stubs and intent validation
 */
@RunWith(AndroidJUnit4.class)
public class AddNewRestaurantActivityTest
{
    //private static final String emailMessage = "Testing the AddNewRestaurantActivity. This is a stub.";

    private static final String emailMessage = "You did not enter any new restaurant info.";
    /**
     * This test demonstrates Espresso Intents using the IntentsTestRule, a class that extends
     * ActivityTestRule. IntentsTestRule initializes Espresso-Intents before each test that is annotated
     * with @Test and releases it once the test is complete. The designated Activity
     * is also terminated after each test.
     */
    @Rule
    public IntentsTestRule<AddNewRestaurantActivity> mIntentsTestRule
            = new IntentsTestRule<>(AddNewRestaurantActivity.class);

    @Before
    public void stubAllExternalIntents()
    {
        // By default Espresso Intents does not stub any Intents. Stubbing needs to be setup before
        // every test run. In this case all external Intents will be blocked.
        intending(not(isInternal())).respondWith(new ActivityResult(Activity.RESULT_OK, null));
    }

    // This tests intent stubbing
    @Test
    public void clickSubmitButton_SendsEmail()
    {
        onView(withId(R.id.submit_new_restaurant)).perform(click());
        // intended(Matcher<Intent> matcher) asserts the given matcher matches one and only one
        // intent sent by the application.

        intended(allOf(
                hasAction(Intent.ACTION_SENDTO),
                hasData("mailto:jaytyoung1@gmail.com"),
                hasExtra(Intent.EXTRA_SUBJECT, "New Restaurant"),
                hasExtra(Intent.EXTRA_TEXT, emailMessage)));
    }

    // This tests input validation
    @Test
    public void submitNewRestaurant_ValidInput()
    {
        // Type some info into the EditText fields
        onView(withId(R.id.add_new_restaurant_name))
                .perform(typeText("New Restaurant Name"), closeSoftKeyboard());

        onView(withId(R.id.add_new_restaurant_address))
                .perform(typeText("123 Center Street"), closeSoftKeyboard());

        // Click the Submit button to send the new Restaurant by email
        onView(withId(R.id.submit_new_restaurant)).perform(click());

        // Verify that an intent to me was sent with the correct action and package
        intended(allOf(
                hasAction(Intent.ACTION_SENDTO),
                hasData(Uri.parse("mailto:jaytyoung1@gmail.com"))));
    }
}
