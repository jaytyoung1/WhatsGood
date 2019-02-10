package com.example.android.whatsgood;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.android.whatsgood.data.CommonDataAccess;
import com.example.android.whatsgood.data.GetRestaurantsAsyncTask;
import com.example.android.whatsgood.data.WhatsGoodContract.RestaurantEntry;
import com.example.android.whatsgood.data.WhatsGoodContract;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


/**
 * Created by jyoun on 1/12/2018.
 */

public class LoginActivity extends AppCompatActivity
{
    public static int APP_REQUEST_CODE = 1;

    public static boolean isLoggedIn;

    // For testing
    public static boolean isInsertingRestaurants = true;

    LoginButton fbLoginButton;

    /**
     * ArrayList of restaurants
     */
    ArrayList<Restaurant> restaurantsArrayList = new ArrayList<>();

    /**
     * Handles the result of the login attempt
     */
    CallbackManager callbackManager;

    // Event logger
    AppEventsLogger logger;

    /**
     * Common data access class
     */
    CommonDataAccess commonDataAccess = new CommonDataAccess(this);

    /**
     * Count variables
     */
    int dbRestaurantCountBefore;
    int dbRestaurantCountAfter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FontHelper.setCustomTypeface(findViewById(R.id.view_root));

//        // This will be the first time user enters the app
//        // Grab the restaurants from google sheet and insert into db if db is empty
//        // If not empty, maybe check to see size of db equals size of google sheet table
//
//        // First query the db to see if db is empty
//        //Cursor cursorBefore = commonDataAccess.examineDataBaseInfo();
//
//        // Get the total number of restaurants in the database. At first run, this should be 0
//        dbRestaurantCountBefore = commonDataAccess.getRestaurantDBCount();
//
//        // If the restaurants ArrayList is empty, grab them from the Google sheet
//        if (restaurantsArrayList.size() == 0)
//        {
//            try
//            {
//                restaurantsArrayList = new GetRestaurantsAsyncTask(this).execute().get();
//            } catch (java.lang.InterruptedException e)
//            {
//
//            } catch (java.util.concurrent.ExecutionException e)
//            {
//
//            }
//        }
//
//        // If the # of restaurants in the DB equals the # of restaurants in the Google sheet, we don't need to insert into DB
//        if (dbRestaurantCountBefore == restaurantsArrayList.size() && restaurantsArrayList.size() > 0)
//            isInsertingRestaurants = false;
//
//        // If we are inserting restaurants into the DB, loop through each restaurant and insert into db
//        if (isInsertingRestaurants)
//        {
//            for (Restaurant restaurant : restaurantsArrayList)
//            {
//                // Create a ContentValues object where column names are the keys,
//                // and the Restaurant attributes are the values
//                ContentValues values = new ContentValues();
//                values.put(RestaurantEntry.COLUMN_RESTAURANT_NAME, restaurant.getName());
//                values.put(RestaurantEntry.COLUMN_RESTAURANT_LINK, restaurant.getLink());
//                values.put(RestaurantEntry.COLUMN_RESTAURANT_ADDRESS, restaurant.getAddress());
//                values.put(RestaurantEntry.COLUMN_RESTAURANT_LATITUDE, restaurant.getLatitude());
//                values.put(RestaurantEntry.COLUMN_RESTAURANT_LONGITUDE, restaurant.getLongitude());
//                values.put(RestaurantEntry.COLUMN_RESTAURANT_IMAGE_RESOURCE_ID, restaurant.getImageResourceId());
//                values.put(RestaurantEntry.COLUMN_RESTAURANT_MONDAY_SPECIALS, restaurant.getSpecial("Monday"));
//                values.put(RestaurantEntry.COLUMN_RESTAURANT_TUESDAY_SPECIALS, restaurant.getSpecial("Tuesday"));
//                values.put(RestaurantEntry.COLUMN_RESTAURANT_WEDNESDAY_SPECIALS, restaurant.getSpecial("Wednesday"));
//                values.put(RestaurantEntry.COLUMN_RESTAURANT_THURSDAY_SPECIALS, restaurant.getSpecial("Thursday"));
//                values.put(RestaurantEntry.COLUMN_RESTAURANT_FRIDAY_SPECIALS, restaurant.getSpecial("Friday"));
//                values.put(RestaurantEntry.COLUMN_RESTAURANT_SATURDAY_SPECIALS, restaurant.getSpecial("Saturday"));
//                values.put(RestaurantEntry.COLUMN_RESTAURANT_SUNDAY_SPECIALS, restaurant.getSpecial("Sunday"));
//
//                // Insert a new row for the restaurant into the provider using the ContentResolver.
//                // Use the {@link RestaurantEntry#CONTENT_URI} to indicate that we want to insert
//                // into the restaurants database table.
//                // Receive the new content URI that will allow us to access the restaurant's data in the future.
//                Uri newUri = getContentResolver().insert(RestaurantEntry.CONTENT_URI, values);
//
//                // Show a toast message depending on whether or not the insertion was successful
//                if (newUri == null)
//                    Toast.makeText(this, "Error with saving restaurant", Toast.LENGTH_SHORT).show();
//                else
//                    Toast.makeText(this, "Restaurant saved", Toast.LENGTH_SHORT);
//            }
//        }
//
//        dbRestaurantCountAfter = commonDataAccess.getRestaurantDBCount();

        // Create a new logger
        logger = AppEventsLogger.newLogger(this);

        // Get the Facebook login button
        fbLoginButton = findViewById(R.id.facebook_login_button);
        // Set the read permissions to email
        fbLoginButton.setReadPermissions("email");

        // Login button callback registration
        callbackManager = CallbackManager.Factory.create();
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                isLoggedIn = true;
                launchMainActivity();
            }

            @Override
            public void onCancel()
            {

            }

            @Override
            public void onError(FacebookException exception)
            {
                // Display error
                String toastMessage = exception.getMessage();
                Toast.makeText(LoginActivity.this, toastMessage, Toast.LENGTH_LONG).show();
            }
        });

        // Check for an existing access token
        AccessToken accessToken = AccountKit.getCurrentAccessToken();
        com.facebook.AccessToken loginToken = com.facebook.AccessToken.getCurrentAccessToken();
        if (accessToken != null || loginToken != null)
        {
            // if previously logged in, proceed to the MainActivity
            isLoggedIn = true;
            launchMainActivity();
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // Forward the result to the callback manager for Login Button
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // For Account Kit, confirm that this response matches your request
        if (requestCode == APP_REQUEST_CODE)
        {
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);

            if (loginResult.getError() != null)
            {
                // Display login error
                String toastMessage = loginResult.getError().getErrorType().getMessage();
                Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
            } else if (loginResult.getAccessToken() != null)
            {
                // On successful login, proceed to the MainActivity
                isLoggedIn = true;
                launchMainActivity();
            }
        }
    }

    private void onLogin(final LoginType loginType)
    {
        // Create intent for the Account Kit activity
        final Intent intent = new Intent(this, AccountKitActivity.class);

        // Configure login type and response type
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        loginType,
                        AccountKitActivity.ResponseType.TOKEN
                );
        final AccountKitConfiguration configuration = configurationBuilder.build();

        // Launch the Account Kit activity
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configuration);
        startActivityForResult(intent, APP_REQUEST_CODE);
    }

    public void onPhoneLogin(View view)
    {
        logger.logEvent("onSMSLogin");
        onLogin(LoginType.PHONE);
    }

    public void onEmailLogin(View view)
    {
        logger.logEvent("onEmailLogin");
        onLogin(LoginType.EMAIL);
    }

    public void onContinueWithoutLoggingIn(View view)
    {
        logger.logEvent("onContinueWithoutLoggingIn");
        isLoggedIn = false;
        launchMainActivity();
    }

    private void launchMainActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
