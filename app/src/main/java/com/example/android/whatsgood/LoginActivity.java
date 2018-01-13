package com.example.android.whatsgood;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.appevents.AppEventsLogger;

/**
 * Created by jyoun on 1/12/2018.
 */

public class LoginActivity extends AppCompatActivity
{
    public static int APP_REQUEST_CODE = 1;

    // Event logger
    AppEventsLogger logger;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FontHelper.setCustomTypeface(findViewById(R.id.view_root));

        // Create a new logger
        logger = AppEventsLogger.newLogger(this);

        // Check for an existing access token
        AccessToken accessToken = AccountKit.getCurrentAccessToken();

        if (accessToken != null)
            // if previously logged in, proceed to the MainActivity
            launchMainActivity();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // Confirm that this response matches your request
        if (requestCode == APP_REQUEST_CODE)
        {
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);

            if (loginResult.getError() != null)
            {
                // Display login error
                String toastMessage = loginResult.getError().getErrorType().getMessage();
                Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
            } else if (loginResult.getAccessToken() != null)
                // On successful login, proceed to the MainActivity
                launchMainActivity();
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

    private void launchMainActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
