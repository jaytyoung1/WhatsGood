package com.example.android.whatsgood;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.login.LoginManager;
import com.google.android.gms.maps.GoogleMap;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.Locale;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * Created by jyoun on 1/12/2018.
 */

public class ProfileFragment extends Fragment
{
    /**
     * Boolean set when ProfileFragment is active or not active (see onPause() and onResume())
     */
    public static boolean isActive;

    ProfileTracker profileTracker;

    ImageView profilePic;

    TextView idLabelTextView;
    TextView idTextView;
    //TextView infoLabelTextView;
    //TextView locationTextView;
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

//        if (LoginActivity.isLoggedIn)
//            Toast.makeText(getContext(), "User is logged in", Toast.LENGTH_LONG).show();
//        else
//            Toast.makeText(getContext(), "User is not logged in", Toast.LENGTH_LONG).show();


        // Hide the FAB by first getting the context of the MainActivity
        Context mainContext = inflater.getContext();
        FloatingActionButton fab = ((Activity) mainContext).findViewById(R.id.fab);
        fab.hide();

        // Hide the toolbar
        Toolbar toolbar = ((Activity) mainContext).findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);

        // Make the listView invisible
        ListView listView = ((Activity) mainContext).findViewById(R.id.list);
        listView.setVisibility(View.INVISIBLE);

        profilePic = rootView.findViewById(R.id.profile_image);
        idLabelTextView = rootView.findViewById(R.id.id_label_text_view);
        idTextView = rootView.findViewById(R.id.id_text_view);
        //infoLabelTextView = rootView.findViewById(R.id.info_label_text_view);
        //locationTextView = rootView.findViewById(R.id.location_text_view);
        logoutButton = rootView.findViewById(R.id.logout_button);

        // register a receiver for the onCurrentProfileChanged event
        profileTracker = new ProfileTracker()
        {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile)
            {
                if (currentProfile != null)
                {
                    displayProfileInfo(currentProfile);
                }
            }
        };

        if (AccessToken.getCurrentAccessToken() != null)
        {
            // If there is an access token then Facebook Login Button was used
            // Check if the profile has already been fetched
            Profile currentProfile = Profile.getCurrentProfile();
            if (currentProfile != null)
            {
                displayProfileInfo(currentProfile);
            } else
            {
                // Fetch the profile, which will trigger the onCurrentProfileChanged receiver
                Profile.fetchProfileForCurrentAccessToken();
            }
        } else
        {
            // Otherwise, get Account Kit login information
            AccountKit.getCurrentAccount(new AccountKitCallback<Account>()
            {
                @Override
                public void onSuccess(final Account account)
                {
                    // Set the ID label to 'Account ID'
                    idLabelTextView.setText(R.string.account_id_label);

                    // Get Account Kit ID
                    String accountKitId = account.getId();
                    idTextView.setText(accountKitId);

                    PhoneNumber phoneNumber = account.getPhoneNumber();
                    if (account.getPhoneNumber() != null)
                    {
                        // if the phone number is available, display it
                        String formattedPhoneNumber = formatPhoneNumber(phoneNumber.toString());
                        //locationTextView.setText(formattedPhoneNumber);
                        //infoLabelTextView.setText(R.string.phone_label);
                    } else
                    {
                        // if the email address is available, display it
                        String emailString = account.getEmail();
                        //locationTextView.setText(emailString);
                        //infoLabelTextView.setText(R.string.email_label);
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
        }

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

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        // unregister the profile tracker receiver
        profileTracker.stopTracking();
    }

    public void onLogout(View view)
    {
        // Logout of Account Kit
        AccountKit.logOut();

        // Logout of Login Button
        LoginManager.getInstance().logOut();

        launchLoginActivity();
    }

    private void displayProfileInfo(Profile profile)
    {
        // Set the ID label to 'Name'
        idLabelTextView.setText(R.string.name_label);

        // display the Profile name
        String name = profile.getName();
        idTextView.setText(name);

        // display the profile picture
        Uri profilePicUri = profile.getProfilePictureUri(120, 120);
        displayProfilePic(profilePicUri);
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

    private void displayProfilePic(Uri uri)
    {
        // helper method to load the profile pic in a circular imageview
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(30)
                .oval(false)
                .build();
        Picasso.with(getContext())
                .load(uri)
                .transform(transformation)
                .into(profilePic);
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
        // unregister the profile tracker receiver
        profileTracker.stopTracking();
    }
}
