package com.example.android.whatsgood;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.PhoneNumber;
import com.google.android.gms.maps.GoogleMap;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.Locale;

/**
 * Created by jyoun on 1/12/2018.
 */

public class ProfileFragment extends Fragment
{
    /**
     * Boolean set when ProfileFragment is active or not active (see onPause() and onResume())
     */
    public static boolean isActive;

    TextView idTextView;
    TextView infoLabelTextView;
    TextView infoTextView;
    Button logoutButton;

    /**
     * Empty Constructor
     */
    public ProfileFragment()
    {
        // Requires empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        // Hide the FAB by first getting the context of the MainActivity
        Context mainContext = inflater.getContext();
        FloatingActionButton fab = ((Activity) mainContext).findViewById(R.id.fab);
        fab.hide();

        // Make the listView invisible
        ListView listView = ((Activity) mainContext).findViewById(R.id.list);
        listView.setVisibility(View.INVISIBLE);

        // Get the textView
        idTextView = rootView.findViewById(R.id.id_text_view);
        infoLabelTextView = rootView.findViewById(R.id.info_label_text_view);
        infoTextView = rootView.findViewById(R.id.info_text_view);
        logoutButton = rootView.findViewById(R.id.logout_button);

        AccountKit.getCurrentAccount(new AccountKitCallback<Account>()
        {
            @Override
            public void onSuccess(final Account account)
            {
                // Get Account Kit ID
                String accountKitId = account.getId();
                idTextView.setText(accountKitId);

                PhoneNumber phoneNumber = account.getPhoneNumber();
                if (account.getPhoneNumber() != null)
                {
                    // if the phone number is available, display it
                    String formattedPhoneNumber = formatPhoneNumber(phoneNumber.toString());
                    infoTextView.setText(formattedPhoneNumber);
                    infoLabelTextView.setText(R.string.phone_label);
                } else
                {
                    // if the email address is available, display it
                    String emailString = account.getEmail();
                    infoTextView.setText(emailString);
                    infoLabelTextView.setText(R.string.email_label);
                }

            }

            @Override
            public void onError(final AccountKitError error)
            {
                // display error
                String toastMessage = error.getErrorType().getMessage();
                Toast.makeText(getContext(), toastMessage, Toast.LENGTH_LONG).show();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onLogout(v);
            }
        });

        return rootView;
    }

    public void onLogout(View view)
    {
        // logout of Account Kit
        AccountKit.logOut();
        launchLoginActivity();
    }

    private void launchLoginActivity()
    {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private String formatPhoneNumber(String phoneNumber)
    {
        // helper method to format the phone number for display
        try
        {
            PhoneNumberUtil pnu = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber pn = pnu.parse(phoneNumber, Locale.getDefault().getCountry());
            phoneNumber = pnu.format(pn, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
        } catch (NumberParseException e)
        {
            e.printStackTrace();
        }
        return phoneNumber;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        isActive = true;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        isActive = false;
    }
}
