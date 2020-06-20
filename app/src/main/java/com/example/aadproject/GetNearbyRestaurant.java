package com.example.aadproject;

/**
 * This class is function to get data from the Google Places APIs using JSON Parsing
 *
 * Created by Shania Frincella
 */

import android.Manifest;
import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class GetNearbyRestaurant extends AsyncTask<Object, String, String> {

    private GoogleMap mMap;
    private String url;
    private InputStream inputStream;
    private BufferedReader bufferedReader;
    private StringBuilder stringBuilder;
    private String data;
    private ArrayList<RestaurantDetail> restaurantArrayList;
    public Context context;


    interface taskInterface {
        void passResult(ArrayList restaurantArrayList);
    }
    taskInterface delegate = null;
    GetNearbyRestaurant(taskInterface delegate) {
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap) objects[0];
        url = (String) objects[1];
        context = (Context) objects[2];

        try {
            URL myurl = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) myurl.openConnection();
            httpURLConnection.connect();
            inputStream = httpURLConnection.getInputStream();
            //Convert input stream data into readable text
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line = "";
            stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            data = stringBuilder.toString();
            System.out.println(data);
            bufferedReader.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected void onPostExecute(String string) {
        super.onPostExecute(string);
        try {
            JSONObject parentObject = new JSONObject(data);
            JSONArray resultsArray = parentObject.getJSONArray("results");

            restaurantArrayList = new ArrayList<>();
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject jsonObject = resultsArray.getJSONObject(i);
                JSONObject locationObj = jsonObject.getJSONObject("geometry").getJSONObject("location");

                String latitude = locationObj.getString("lat");
                String longitude = locationObj.getString("lng");

                JSONObject nameObject = resultsArray.getJSONObject(i);
                String name_restaurant = nameObject.getString("name");

                String price_level = "Not available";
                try {
                    price_level = nameObject.getString("price_level");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String rating = "Not available";
                try {
                    rating = nameObject.getString("rating");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String vicinity = nameObject.getString("vicinity");

                String image_reference = "";
                try {
                    JSONArray imageArray = jsonObject.getJSONArray("photos");
                    JSONObject imageObject = imageArray.getJSONObject(0);
                    String reference = imageObject.getString("photo_reference");
                    StringBuilder url = new StringBuilder("https://maps.googleapis.com/maps/api/place/photo?");
                    url.append("key=" + context.getResources().getString(R.string.google_api_key));
                    url.append("&photoreference=" + reference);
                    url.append("&maxwidth=400");
                    image_reference = url.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title(name_restaurant);
                markerOptions.snippet(vicinity);
                markerOptions.position(latLng);

                mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                mMap.getUiSettings().setMyLocationButtonEnabled(true);

                restaurantArrayList.add(new RestaurantDetail(name_restaurant, vicinity, rating, price_level, image_reference));
            }
            delegate.passResult(restaurantArrayList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
