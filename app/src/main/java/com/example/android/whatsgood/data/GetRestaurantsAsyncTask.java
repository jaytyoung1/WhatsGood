package com.example.android.whatsgood.data;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.whatsgood.Restaurant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetRestaurantsAsyncTask extends AsyncTask<Void, Void, ArrayList<Restaurant>>
{
    Context mContext;
    private InputStream inputStream = null;
    public String result = "";
    ArrayList<Restaurant> arrList = new ArrayList<>();

    private String googleSheetURL = "https://spreadsheets.google.com/feeds/list/1QDaoKuaGocEzWXKM_IhRFua3mIaAglknARyooptIz40/od6/public/values?alt=json";

    public GetRestaurantsAsyncTask(Context context)
    {
        super();
        mContext = context;
    }

    @Override
    protected ArrayList<Restaurant> doInBackground(Void... params)
    {
        HttpURLConnection urlConnection = null;
        try
        {
            URL url = new URL(googleSheetURL);

            urlConnection = (HttpURLConnection) url.openConnection();
            inputStream = new BufferedInputStream(urlConnection.getInputStream());

            // Convert response to string using String Builder
            try
            {
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream), 8);
                StringBuilder sBuilder = new StringBuilder();

                String line = bReader.readLine();
                sBuilder.append(line);

                inputStream.close();
                result = sBuilder.toString();

            } catch (Exception e)
            {
                Log.e("StringBuilding & BufferedReader", "Error converting result " + e.toString());
            }

        } catch (java.net.MalformedURLException e)
        {
            // TODO handle problems...
        } catch (java.io.IOException e)
        {
            // TODO handle problems...
        } finally
        {
            urlConnection.disconnect();
        }

        // Parse the result
        try
        {
            JSONObject feed = new JSONObject(result).getJSONObject("feed");
            JSONArray arr = feed.getJSONArray("entry");

            // For each Restaurant entry
            for (int i = 0; i < arr.length(); i++)
            {
                JSONObject o = new JSONObject(arr.getString(i));

                String name = o.getJSONObject("gsx$name").getString("$t");
                String link = o.getJSONObject("gsx$link").getString("$t");
                String address = o.getJSONObject("gsx$address").getString("$t");
                double latitude = Double.parseDouble(o.getJSONObject("gsx$latitude").getString("$t"));
                double longitude = Double.parseDouble(o.getJSONObject("gsx$longitude").getString("$t"));
                String imageID = o.getJSONObject("gsx$imageid").getString("$t");
                int resID = mContext.getResources().getIdentifier(imageID, "drawable", mContext.getPackageName());

                String mondaySpecials = o.getJSONObject("gsx$mondayspecials").getString("$t");
                String tuesdaySpecials = o.getJSONObject("gsx$tuesdayspecials").getString("$t");
                String wednesdaySpecials = o.getJSONObject("gsx$wednesdayspecials").getString("$t");
                String thursdaySpecials = o.getJSONObject("gsx$thursdayspecials").getString("$t");
                String fridaySpecials = o.getJSONObject("gsx$fridayspecials").getString("$t");
                String saturdaySpecials = o.getJSONObject("gsx$saturdayspecials").getString("$t");
                String sundaySpecials = o.getJSONObject("gsx$sundayspecials").getString("$t");

                Restaurant rest = new Restaurant(
                        name,
                        link,
                        address,
                        latitude,
                        longitude,
                        resID);

                rest.mSpecialsHashMap.put("Monday", mondaySpecials);
                rest.mSpecialsHashMap.put("Tuesday", tuesdaySpecials);
                rest.mSpecialsHashMap.put("Wednesday", wednesdaySpecials);
                rest.mSpecialsHashMap.put("Thursday", thursdaySpecials);
                rest.mSpecialsHashMap.put("Friday", fridaySpecials);
                rest.mSpecialsHashMap.put("Saturday", saturdaySpecials);
                rest.mSpecialsHashMap.put("Sunday", sundaySpecials);
                arrList.add(rest);
            }
        } catch (JSONException e)
        {
            Log.e("JSONException", "Error: " + e.toString());
        }

        // Return the ArrayList of Restaurants
        return arrList;
    }

    @Override
    protected void onPostExecute(ArrayList<Restaurant> result)
    {
        super.onPostExecute(result);
    }
}