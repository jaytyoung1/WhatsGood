package com.example.android.whatsgood;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

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

        Intent intent = getIntent();
        Restaurant currentRestaurant = (Restaurant)intent.getSerializableExtra("currentRestaurant");

        ImageView restaurantImageView = (ImageView) findViewById(R.id.restaurant_photo);
        restaurantImageView.setImageResource(currentRestaurant.getImageResourceId());
    }
}
