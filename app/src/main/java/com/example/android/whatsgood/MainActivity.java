package com.example.android.whatsgood;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
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

        // Set the content of the activity to use the activity_main.xml layout file
        setContentView(R.layout.activity_main);

        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        TabAdapter adapter = new TabAdapter(this, getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Find the tab layout that shows the tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        // Connect the tab layout with the view pager. This will
        //   1. Update the tab layout when the view pager is swiped
        //   2. Update the view pager when a tab is selected
        //   3. Set the tab layout's tab names with the view pager's adapter's titles
        //      by calling onPageTitle()
        tabLayout.setupWithViewPager(viewPager);

        // Use icons for the tabs
        tabLayout.getTabAt(0).setIcon(R.drawable.icon_list);
        tabLayout.getTabAt(1).setIcon(R.drawable.icon_maps);

        // Set up the action bar, but get rid of the title
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");

        // Get the current day to know which day to display
        //SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        //Date date = new Date();
        //String dayOfTheWeek = sdf.format(date);

        mDaySpinner = (Spinner) findViewById(R.id.spinner_day_of_the_week);

        setupSpinner();

        TextView dayOfWeekTextView = (TextView) findViewById(R.id.day_of_week_text);
        dayOfWeekTextView.setText(dayString);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                CharSequence options[] = new CharSequence[] {"Add new restaurant", "Edit existing restaurant"};

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Select an option.");
                builder.setItems(options, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });
                builder.show();
            }
        });
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
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
