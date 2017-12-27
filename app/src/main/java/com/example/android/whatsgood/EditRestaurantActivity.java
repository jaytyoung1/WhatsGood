package com.example.android.whatsgood;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

/**
 * Created by jyoun on 12/5/2017.
 */

public class EditRestaurantActivity extends AppCompatActivity
{
    // Restaurant object to hold to current restaurant
    Restaurant currentRestaurant = null;

    // EditText variables
    EditText nameField;
    EditText mondayField;
    EditText tuesdayField;
    EditText wednesdayField;
    EditText thursdayField;
    EditText fridayField;
    EditText saturdayField;
    EditText sundayField;
    EditText commentsField;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_restaurant);

        // Get the currentRestaurant object
        Intent intent = getIntent();
        currentRestaurant = (Restaurant) intent.getSerializableExtra("currentRestaurant");

        // Get all the Edit Text fields
        nameField = (EditText) findViewById(R.id.edit_restaurant_name);
        mondayField = (EditText) findViewById(R.id.edit_restaurant_monday);
        tuesdayField = (EditText) findViewById(R.id.edit_restaurant_tuesday);
        wednesdayField = (EditText) findViewById(R.id.edit_restaurant_wednesday);
        thursdayField = (EditText) findViewById(R.id.edit_restaurant_thursday);
        fridayField = (EditText) findViewById(R.id.edit_restaurant_friday);
        saturdayField = (EditText) findViewById(R.id.edit_restaurant_saturday);
        sundayField = (EditText) findViewById(R.id.edit_restaurant_sunday);
        commentsField = (EditText) findViewById(R.id.edit_restaurant_comments);

        // Set the hints
        nameField.setHint(currentRestaurant.getName());
        mondayField.setHint(currentRestaurant.getSpecial("Monday"));
        tuesdayField.setHint(currentRestaurant.getSpecial("Tuesday"));
        wednesdayField.setHint(currentRestaurant.getSpecial("Wednesday"));
        thursdayField.setHint(currentRestaurant.getSpecial("Thursday"));
        fridayField.setHint(currentRestaurant.getSpecial("Friday"));
        saturdayField.setHint(currentRestaurant.getSpecial("Saturday"));
        sundayField.setHint(currentRestaurant.getSpecial("Sunday"));
    }

    /**
     * This method is called when the Submit button is pressed
     */
    public void submitEditedRestaurant(View view)
    {
        // Create the message to be sent in the email
        String message = createMessage();

        // Send the email by using an Intent
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:jaytyoung1@gmail.com"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Edited Restaurant: " + currentRestaurant.getName() + ", " + currentRestaurant.getAddress());
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);
        if (emailIntent.resolveActivity(getPackageManager()) != null)
            startActivity(emailIntent);
    }

    /**
     * Create the message with all the edited restaurant info
     */
    private String createMessage()
    {
        //String msg = currentRestaurant.getName();
        //msg += "\nAddress: " + currentRestaurant.getAddress() + "\n\n";

        String msg = "";

        // Append the edited restaurant info to the message
        if (!nameField.getText().toString().equalsIgnoreCase(nameField.getHint().toString()) && !nameField.getText().toString().isEmpty())
        {
            msg += "\nName: " + nameField.getText().toString();
            msg += "\nOriginal: " + nameField.getHint().toString() + "\n";
        }
        if (!mondayField.getText().toString().equalsIgnoreCase(mondayField.getHint().toString()) && !mondayField.getText().toString().isEmpty())
        {
            msg += "\nMonday specials: " + mondayField.getText().toString();
            msg += "\nOriginal: " + mondayField.getHint().toString() + "\n";
        }
        if (!tuesdayField.getText().toString().equalsIgnoreCase(tuesdayField.getHint().toString()) && !tuesdayField.getText().toString().isEmpty())
        {
            msg += "\nTuesday specials: " + tuesdayField.getText().toString();
            msg += "\nOriginal: " + tuesdayField.getHint().toString() + "\n";
        }
        if (!wednesdayField.getText().toString().equalsIgnoreCase(wednesdayField.getHint().toString()) && !wednesdayField.getText().toString().isEmpty())
        {
            msg += "\nWednesday specials: " + wednesdayField.getText().toString();
            msg += "\nOriginal: " + wednesdayField.getHint().toString() + "\n";
        }
        if (!thursdayField.getText().toString().equalsIgnoreCase(thursdayField.getHint().toString()) && !thursdayField.getText().toString().isEmpty())
        {
            msg += "\nThursday specials: " + thursdayField.getText().toString();
            msg += "\nOriginal: " + thursdayField.getHint().toString() + "\n";
        }
        if (!fridayField.getText().toString().equalsIgnoreCase(fridayField.getHint().toString()) && !fridayField.getText().toString().isEmpty())
        {
            msg += "\nFriday specials: " + fridayField.getText().toString();
            msg += "\nOriginal: " + fridayField.getHint().toString() + "\n";
        }
        if (!saturdayField.getText().toString().equalsIgnoreCase(saturdayField.getHint().toString()) && !saturdayField.getText().toString().isEmpty())
        {
            msg += "\nSaturday specials: " + saturdayField.getText().toString();
            msg += "\nOriginal: " + saturdayField.getHint().toString() + "\n";
        }
        if (!sundayField.getText().toString().equalsIgnoreCase(sundayField.getHint().toString()) && !sundayField.getText().toString().isEmpty())
        {
            msg += "\nSunday specials: " + sundayField.getText().toString();
            msg += "\nOriginal: " + sundayField.getHint().toString() + "\n";
        }
        if (!commentsField.getText().toString().equalsIgnoreCase(commentsField.getHint().toString()) && !commentsField.getText().toString().isEmpty())
        {
            msg += "\nComments: " + commentsField.getText().toString();
            msg += "\nOriginal: " + commentsField.getHint().toString() + "\n";
        }

        // If no restaurant info was added in the EditText fields
        if (msg.isEmpty())
            msg = "You did not edit any of the restaurant info.";

        return msg;
    }
}
