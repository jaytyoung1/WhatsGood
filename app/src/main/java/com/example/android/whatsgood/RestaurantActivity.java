package com.example.android.whatsgood;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by jyoun on 11/10/2017.
 */

public class RestaurantActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        // Get the currentRestaurant object
        Intent intent = getIntent();
        Restaurant currentRestaurant = (Restaurant) intent.getSerializableExtra("currentRestaurant");

        // Set the restaurant name text view
        TextView restaurantTextView = (TextView) findViewById(R.id.restaurant_name);
        restaurantTextView.setText(currentRestaurant.getName());

        // Set the restaurant image
        ImageView restaurantImageView = (ImageView) findViewById(R.id.restaurant_photo);
        restaurantImageView.setImageResource(currentRestaurant.getImageResourceId());

        // Set the specials text views for each day of the week
        TextView mondaySpecialsTextView = (TextView) findViewById(R.id.monday_specials_text);
        mondaySpecialsTextView.setText(currentRestaurant.getSpecial("Monday"));
        TextView tuesdaySpecialsTextView = (TextView) findViewById(R.id.tuesday_specials_text);
        tuesdaySpecialsTextView.setText(currentRestaurant.getSpecial("Tuesday"));
        TextView wednesdaySpecialsTextView = (TextView) findViewById(R.id.wednesday_specials_text);
        wednesdaySpecialsTextView.setText(currentRestaurant.getSpecial("Wednesday"));
        TextView thursdaySpecialsTextView = (TextView) findViewById(R.id.thursday_specials_text);
        thursdaySpecialsTextView.setText(currentRestaurant.getSpecial("Thursday"));
        TextView fridaySpecialsTextView = (TextView) findViewById(R.id.friday_specials_text);
        fridaySpecialsTextView.setText(currentRestaurant.getSpecial("Friday"));
        TextView saturdaySpecialsTextView = (TextView) findViewById(R.id.saturday_specials_text);
        saturdaySpecialsTextView.setText(currentRestaurant.getSpecial("Saturday"));
        TextView sundaySpecialsTextView = (TextView) findViewById(R.id.sunday_specials_text);
        sundaySpecialsTextView.setText(currentRestaurant.getSpecial("Sunday"));
    }
}
