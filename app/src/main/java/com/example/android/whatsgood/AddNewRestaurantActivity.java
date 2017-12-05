package com.example.android.whatsgood;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

/**
 * Created by jyoun on 11/12/2017.
 */

public class AddNewRestaurantActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_restaurant);
    }

    /**
     * This method is called when the Submit button is pressed
     */
    public void submitNewRestaurant(View view)
    {
        // Get all the Edit Text fields
        EditText nameField = (EditText) findViewById(R.id.add_new_restaurant_name);
        EditText addressField = (EditText) findViewById(R.id.add_new_restaurant_address);
        EditText websiteLinkField = (EditText) findViewById(R.id.add_new_restaurant_website_link);
        EditText imageLinkField = (EditText) findViewById(R.id.add_new_restaurant_image_link);
        EditText mondayField = (EditText) findViewById(R.id.add_new_restaurant_monday);
        EditText tuesdayField = (EditText) findViewById(R.id.add_new_restaurant_tuesday);
        EditText wednesdayField = (EditText) findViewById(R.id.add_new_restaurant_wednesday);
        EditText thursdayField = (EditText) findViewById(R.id.add_new_restaurant_thursday);
        EditText fridayField = (EditText) findViewById(R.id.add_new_restaurant_friday);
        EditText saturdayField = (EditText) findViewById(R.id.add_new_restaurant_saturday);
        EditText sundayField = (EditText) findViewById(R.id.add_new_restaurant_sunday);

        // Get the values
        String name = nameField.getText().toString();
        String address = addressField.getText().toString();
        String websiteLink = websiteLinkField.getText().toString();
        String imageLink = imageLinkField.getText().toString();
        String monday = mondayField.getText().toString();
        String tuesday = tuesdayField.getText().toString();
        String wednesday = wednesdayField.getText().toString();
        String thursday = thursdayField.getText().toString();
        String friday = fridayField.getText().toString();
        String saturday = saturdayField.getText().toString();
        String sunday = sundayField.getText().toString();

        // Get the message to be sent in the email
        String message = createMessage(
                name,
                address,
                websiteLink,
                imageLink,
                monday,
                tuesday,
                wednesday,
                thursday,
                friday,
                saturday,
                sunday);

        // Send the email by using an Intent
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:jaytyoung1@gmail.com"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "New Restaurant");
        intent.putExtra(Intent.EXTRA_TEXT, message);
        if (intent.resolveActivity(getPackageManager()) != null)
            startActivity(intent);
    }

    /**
     * Create the message with all the new restaurant info
     *
     * @param name          of the restaurant
     * @param address       of the restaurant
     * @param websiteLink   link to restaurant's homepage
     * @param imageLink     link to restaurant's logo
     * @param monday        monday specials
     * @param tuesday       tuesday specials
     * @param wednesday     wednesday specials
     * @param thursday      thursday specials
     * @param friday        friday specials
     * @param saturday      saturday specials
     * @param sunday        sunday specials
     * @return msg          message to be attached to the email
     */
    private String createMessage(
            String name,
            String address,
            String websiteLink,
            String imageLink,
            String monday,
            String tuesday,
            String wednesday,
            String thursday,
            String friday,
            String saturday,
            String sunday)
    {
        String msg = "Name: " + name;
        msg += "\n" + "Address: " + address;
        msg += "\n" + "Website Link: " + websiteLink;
        msg += "\n" + "Image Link: " + imageLink;
        msg += "\n" + "Monday Specials: " + monday;
        msg += "\n" + "Tuesday Specials: " + tuesday;
        msg += "\n" + "Wednesday Specials: " + wednesday;
        msg += "\n" + "Thursday Specials: " + thursday;
        msg += "\n" + "Friday Specials: " + friday;
        msg += "\n" + "Saturday Specials: " + saturday;
        msg += "\n" + "Sunday Specials: " + sunday;

        return msg;
    }

}
