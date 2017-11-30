package com.example.android.whatsgood;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.whatsgood.data.GetRestaurantsAsyncTask;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private static final int WHATSGOOD_LOADER = 0;

    //WhatsGoodCursorAdapter mCursorAdapter;

    ArrayList<Restaurant> restaurantsArrayList = new ArrayList<>();


    /**
     * Spinner drop down arrow to enter day of the week
     */
    private Spinner mDaySpinner;

    /**
     * Day of the week
     */
    private int mDayOfTheWeek;

    public static String dayString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //WhatsGoodDbHelper mDbHelper = new WhatsGoodDbHelper(this);

        //SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Set the content of the activity to use the activity_main.xml layout file
        setContentView(R.layout.activity_main);

        // Set up the action bar, but get rid of the title
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");

//        // Find the view pager that will allow the user to swipe between fragments
//        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
//
//        // Create an adapter that knows which fragment should be shown on each page
//        TabAdapter adapter = new TabAdapter(this, getSupportFragmentManager());
//
//        // Set the adapter onto the view pager
//        viewPager.setAdapter(adapter);
//
//        // Find the tab layout that shows the tabs
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
//
//        // Connect the tab layout with the view pager. This will
//        //   1. Update the tab layout when the view pager is swiped
//        //   2. Update the view pager when a tab is selected
//        //   3. Set the tab layout's tab names with the view pager's adapter's titles
//        //      by calling onPageTitle()
//        tabLayout.setupWithViewPager(viewPager);
//
//        // Use icons for the tabs
//        tabLayout.getTabAt(0).setIcon(R.drawable.ic_list_white);
//        tabLayout.getTabAt(1).setIcon(R.drawable.ic_location_on_white);

        // Get the current day to know which day to display
        //SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        //Date date = new Date();
        //String dayOfTheWeek = sdf.format(date);

        mDaySpinner = (Spinner) findViewById(R.id.spinner_day_of_the_week);

        setupSpinner();

        TextView dayOfWeekTextView = (TextView) findViewById(R.id.day_of_week_text);
        dayOfWeekTextView.setText(dayString);

        ImageView mapButton = (ImageView) findViewById(R.id.action_button_map);

        // Add a click listener to go to the Map activity
        mapButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

//        // Add a page change listener on the viewPager to show/hide fab depending on which tab is selected
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
//        {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
//            {
//            }
//
//            @Override
//            public void onPageSelected(int position)
//            {
//                switch (position)
//                {
//                    case 0:
//                        fab.show();
//                        break;
//                    case 1:
//                        fab.hide();
//                        break;
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state)
//            {
//            }
//        });

        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                CharSequence options[] = new CharSequence[]{"Add new restaurant", "Edit existing restaurant"};

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Select an option.");
                builder.setItems(options, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        switch (which)
                        {
                            case 0:
                                Intent intent = new Intent(MainActivity.this, AddNewRestaurantActivity.class);
                                //intent.putExtra("currentRestaurant", currentRestaurant);
                                startActivity(intent);
                        }
                    }
                });
                builder.show();
            }
        });

        try
        {
            restaurantsArrayList = new GetRestaurantsAsyncTask(this).execute().get();
        } catch (java.lang.InterruptedException e)
        {

        } catch (java.util.concurrent.ExecutionException e)
        {

        }

        // Create an {@link RestaurantAdapter}, whose data source is a list of {@link Restaurant}s.
        // The adapter knows how to create list items for each item in the list.
        RestaurantAdapter adapter = new RestaurantAdapter(this, restaurantsArrayList, R.color.colorBackground);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // word_list.xml layout file.
        ListView listView = (ListView) findViewById(R.id.list);

        // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Word} in the list.
        listView.setAdapter(adapter);

//        // Find the ListView which will be populated with the restaurants data
//        ListView restaurantsListView = (ListView) findViewById(R.id.list);
//
//        // Find and set the empty view on the ListView, so that it only shows when the list has 0 items
//        View emptyView = findViewById(R.id.empty_view);
//        restaurantsListView.setEmptyView(emptyView);
//
//        // Setup an Adapter to create a list item for each row of restaurant data in the Cursor.
//        // There is no restaurant data yet (until the loader finishes) so pass in null for the Cursor.
//        mCursorAdapter = new WhatsGoodCursorAdapter(this, null);
//        restaurantsListView.setAdapter(mCursorAdapter);
//
//        // Kick off the loader
//        getLoaderManager().initLoader(WHATSGOOD_LOADER, null, this);
    }

    /**
     * Setup the dropdown spinner that allows the user to select the day of the week
     */
    private void setupSpinner()
    {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter daySpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_days_of_the_week, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        daySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mDaySpinner.setAdapter(daySpinnerAdapter);

        // Set the integer mSelected to the constant values
        mDaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection))
                {
                    if (selection.equals(getString(R.string.day_monday)))
                    {
                        mDayOfTheWeek = 1;
                        dayString = getString(R.string.day_monday);
                    } else if (selection.equals(getString(R.string.day_tuesday)))
                    {
                        mDayOfTheWeek = 2;
                        dayString = getString(R.string.day_tuesday);

                    } else if (selection.equals(getString(R.string.day_wednesday)))
                    {
                        mDayOfTheWeek = 3;
                        dayString = getString(R.string.day_wednesday);
                    } else if (selection.equals(getString(R.string.day_thursday)))
                    {
                        mDayOfTheWeek = 4;
                        dayString = getString(R.string.day_thursday);
                    } else if (selection.equals(getString(R.string.day_friday)))
                    {
                        mDayOfTheWeek = 5;
                        dayString = getString(R.string.day_friday);
                    } else if (selection.equals(getString(R.string.day_saturday)))
                    {
                        mDayOfTheWeek = 6;
                        dayString = getString(R.string.day_saturday);
                    } else if (selection.equals(getString(R.string.day_sunday)))
                    {
                        mDayOfTheWeek = 7;
                        dayString = getString(R.string.day_sunday);
                    }
                    finish();
                    startActivity(getIntent());
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                mDayOfTheWeek = 0;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_maps)
        {
            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public Loader<Cursor> onCreateLoader(int id, Bundle args)
//    {
//        // Define a projection that specifies the columns from the table we care about
//        String[] projection = {
//                RestaurantEntry._ID,
//                RestaurantEntry.COLUMN_RESTAURANT_NAME,
//                RestaurantEntry.COLUMN_RESTAURANT_LINK,
//                RestaurantEntry.COLUMN_RESTAURANT_ADDRESS,
//                RestaurantEntry.COLUMN_RESTAURANT_LATITUDE,
//                RestaurantEntry.COLUMN_RESTAURANT_LONGITUDE,
//                RestaurantEntry.COLUMN_RESTAURANT_IMAGE_RESOURCE_ID,
//                RestaurantEntry.COLUMN_RESTAURANT_MONDAY_SPECIALS,
//                RestaurantEntry.COLUMN_RESTAURANT_TUESDAY_SPECIALS,
//                RestaurantEntry.COLUMN_RESTAURANT_WEDNESDAY_SPECIALS,
//                RestaurantEntry.COLUMN_RESTAURANT_THURSDAY_SPECIALS,
//                RestaurantEntry.COLUMN_RESTAURANT_FRIDAY_SPECIALS,
//                RestaurantEntry.COLUMN_RESTAURANT_SATURDAY_SPECIALS,
//                RestaurantEntry.COLUMN_RESTAURANT_SUNDAY_SPECIALS};
//
//        // This loader will execute the ContentProvider's query method on a background thread
//        return new CursorLoader(this,     // Parent activity context
//                RestaurantEntry.CONTENT_URI,    // Provider content URI to query
//                projection,
//                null,
//                null,
//                null);
//    }
//
//    @Override
//    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
//    {
//        // Update {@link WhatsGoodCursorAdapter} with this new cursor containing updated pet data
//        mCursorAdapter.swapCursor(data);
//    }
//
//    @Override
//    public void onLoaderReset(Loader<Cursor> loader)
//    {
//        // Callback called when the data needs to be deleted
//        mCursorAdapter.swapCursor(null);
//    }
}
