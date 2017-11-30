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
import java.io.IOException;
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

    private String googleSheetQuery = "https://spreadsheets.google.com/tq?tqx=out:JSON&tq=select%20%2A&key=1QDaoKuaGocEzWXKM_IhRFua3mIaAglknARyooptIz40";

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
            URL url = new URL(googleSheetQuery);

            urlConnection = (HttpURLConnection) url.openConnection();
            inputStream = new BufferedInputStream(urlConnection.getInputStream());

            // Convert response to string using String Builder
            try
            {
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream), 8);
                StringBuilder sBuilder = new StringBuilder();

                String line;
                try
                {
                    while ((line = bReader.readLine()) != null)
                        sBuilder.append(line + "\n");
                } catch (IOException e)
                {
                    e.printStackTrace();
                } finally
                {
                    try
                    {
                        inputStream.close();
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }

//                String line = bReader.readLine();
//                sBuilder.append(line);
//
//                inputStream.close();
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

        // Remove the unnecessary parts from the response
        int start = result.indexOf("{", result.indexOf("{") + 1);
        int end = result.lastIndexOf("}");
        String jsonResponse = result.substring(start, end);

        // Parse the result
        JSONObject table;
        JSONArray rows;
        try
        {
            table = new JSONObject(jsonResponse);
            rows = table.getJSONArray("rows");

            for (int i = 0; i < rows.length(); i++)
            {
                JSONObject row = rows.getJSONObject(i);
                JSONArray columns = row.getJSONArray("c");

                String name = columns.getJSONObject(0).getString("v");
                String link = columns.getJSONObject(6).getString("v");
                String address = columns.getJSONObject(3).getString("v");
                double latitude = columns.getJSONObject(4).getDouble("v");
                double longitude = columns.getJSONObject(5).getDouble("v");
                String imageID = columns.getJSONObject(8).getString("v");
                int resID = mContext.getResources().getIdentifier(imageID, "drawable", mContext.getPackageName());

                String mondaySpecials;
                if (!columns.get(9).equals(null))
                    mondaySpecials = columns.getJSONObject(9).getString("v");
                else
                    mondaySpecials = "";

                String tuesdaySpecials;
                if (!columns.get(10).equals(null))
                    tuesdaySpecials = columns.getJSONObject(10).getString("v");
                else
                    tuesdaySpecials = "";

                String wednesdaySpecials;
                if (!columns.get(11).equals(null))
                    wednesdaySpecials = columns.getJSONObject(11).getString("v");
                else
                    wednesdaySpecials = "";

                String thursdaySpecials;
                if (!columns.get(12).equals(null))
                    thursdaySpecials = columns.getJSONObject(12).getString("v");
                else
                    thursdaySpecials = "";

                String fridaySpecials;
                if (!columns.get(13).equals(null))
                    fridaySpecials = columns.getJSONObject(13).getString("v");
                else
                    fridaySpecials = "";

                String saturdaySpecials;
                if (!columns.get(14).equals(null))
                    saturdaySpecials = columns.getJSONObject(14).getString("v");
                else
                    saturdaySpecials = "";

                String sundaySpecials;
                if (!columns.get(15).equals(null))
                    sundaySpecials = columns.getJSONObject(15).getString("v");
                else
                    sundaySpecials = "";

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
        } catch (org.json.JSONException e)
        {
            e.printStackTrace();
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