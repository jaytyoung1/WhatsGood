package com.example.android.whatsgood;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;

/**
 * Created by jyoun on 1/12/2018.
 */

public class LoginActivity extends AppCompatActivity
{
    public static int APP_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //FontHelper.setCustomTypeface(findViewById(R.id.view_root));
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
        onLogin(LoginType.PHONE);
    }

    public void onEmailLogin(View view)
    {
        onLogin(LoginType.EMAIL);
    }

    private void launchAccountActivity()
    {
//        Intent intent = new Intent(this, AccountActivity.class);
//        startActivity(intent);
//        finish();
    }
}
