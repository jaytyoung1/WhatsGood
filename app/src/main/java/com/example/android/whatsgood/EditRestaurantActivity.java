package com.example.android.whatsgood;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_restaurant);

        // Get the currentRestaurant object
        Intent intent = getIntent();
        currentRestaurant = (Restaurant) intent.getSerializableExtra("currentRestaurant");

        // Get all the Edit Text fields
        EditText nameField = (EditText) findViewById(R.id.edit_restaurant_name);
        EditText mondayField = (EditText) findViewById(R.id.edit_restaurant_monday);
        EditText tuesdayField = (EditText) findViewById(R.id.edit_restaurant_tuesday);
        EditText wednesdayField = (EditText) findViewById(R.id.edit_restaurant_wednesday);
        EditText thursdayField = (EditText) findViewById(R.id.edit_restaurant_thursday);
        EditText fridayField = (EditText) findViewById(R.id.edit_restaurant_friday);
        EditText saturdayField = (EditText) findViewById(R.id.edit_restaurant_saturday);
        EditText sundayField = (EditText) findViewById(R.id.edit_restaurant_sunday);

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

    }
}
