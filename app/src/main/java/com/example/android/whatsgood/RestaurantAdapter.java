package com.example.android.whatsgood;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.whatsgood.R;
import com.example.android.whatsgood.Restaurant;

import java.util.ArrayList;

/*
* {@link RestaurantAdapter} is an {@link ArrayAdapter} that can provide the layout for each list
* based on a data source, which is a list of {@link Restaurant} objects.
* */
public class RestaurantAdapter extends ArrayAdapter<Restaurant>
{
    // Resource ID for the background color for this list of restaurants
    private int mColorResourceId;

    /**
     * Create a new {@link RestaurantAdapter} object
     *
     * @param context The current context. Used to inflate the layout file.
     * @param words   A List of AndroidFlavor objects to display in a list
     */
    public RestaurantAdapter(Activity context, ArrayList<Restaurant> words, int colorResourceId)
    {
        super(context, 0, words);
        mColorResourceId = colorResourceId;
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The position in the list of data that should be displayed in the list item view.
     * @param convertView The recycled view to populate.
     * @param parent      The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // Check if an existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null)
        {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        // Get the {@link Word} object located at this position in the list
        final Restaurant currentRestaurant = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID restaurant_name_text_view
        TextView restaurantNameTextView = (TextView) listItemView.findViewById(R.id.restaurant_name_text_view);
        // Get the Restaurant name from the currentRestaurant object and set this text on the text view
        restaurantNameTextView.setText(currentRestaurant.getName());

        // Find the TextView in the list_item.xml layout with the specials
        TextView specialsTextView = (TextView) listItemView.findViewById(R.id.specials_text_view);
        // Get the English translation from the currentWord object and set this text on the englishTextView
        //specialsTextView.setText(currentRestaurant.toString());
        specialsTextView.setText("Specials");

        // Find the ImageView in the list_item.xml layout with the ID image
        ImageView imageView = (ImageView) listItemView.findViewById(R.id.image);

        // Add a click listener to the image to open the site's url
        imageView.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(currentRestaurant.getLink()));
                getContext().startActivity(intent);
            }
        });

        if (currentRestaurant.hasImage())
        {
            // Set the ImageView to the image resource specified in the current Word
            imageView.setImageResource(currentRestaurant.getImageResourceId());

            // Make sure the view is visible
            imageView.setVisibility(View.VISIBLE);
        } else
        {
            // Otherwise hide the ImageView
            imageView.setVisibility(View.GONE);
        }

        // Set the theme color for the list item
        View textContainer = listItemView.findViewById(R.id.text_container);
        // Find the color that the resource ID maps to
        int color = ContextCompat.getColor(getContext(), mColorResourceId);
        // Set the background color of the text container View
        textContainer.setBackgroundColor(color);

        // Return the whole list item layout (containing 2 TextViews) so that it can be shown in the ListView
        return listItemView;
    }
}