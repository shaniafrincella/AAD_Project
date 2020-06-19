package com.example.aadproject;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class NearbyRestaurant extends AsyncTask<Object, String, String> {

    GoogleMap mMap;
    String url;
    InputStream inputStream;
    BufferedReader bufferedReader;
    StringBuilder stringBuilder;
    String data;
    ArrayList<RestaurantDetail> restaurantArrayList;


    interface taskInterface {
        void passResult(ArrayList restaurantArrayList);
    }

    taskInterface delegate = null;

    NearbyRestaurant(taskInterface delegate) {
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap) objects[0];
        url = (String) objects[1];

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

                String price_level = "";
                try {
                    price_level = nameObject.getString("price_level");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String rating = "";
                try {
                    rating = nameObject.getString("rating");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String vicinity = nameObject.getString("vicinity");

                System.out.println(name_restaurant + " ~ " + vicinity + " ~ " + rating + " ~ " + price_level);

                LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title(vicinity);
                markerOptions.position(latLng);

                mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                mMap.getUiSettings().setMyLocationButtonEnabled(true);

                restaurantArrayList.add(new RestaurantDetail(name_restaurant, vicinity, rating, price_level));
            }
            delegate.passResult(restaurantArrayList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
