package com.example.aadproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.LinearLayout;

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

public class NearbyRestaurant extends AsyncTask<Object, String, String> {

    GoogleMap mMap;
    String url;
    InputStream inputStream;
    BufferedReader bufferedReader;
    StringBuilder stringBuilder;
    String data;
    RecyclerView recyclerView;

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

            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject jsonObject = resultsArray.getJSONObject(i);
                JSONObject locationObj = jsonObject.getJSONObject("geometry").getJSONObject("location");

                String latitude = locationObj.getString("lat");
                String longitude = locationObj.getString("lng");

                JSONObject nameObject = resultsArray.getJSONObject(i);
                String name_restaurant = nameObject.getString("name");
                String vicinity = nameObject.getString("vicinity");

                System.out.println(name_restaurant + " ~ " + vicinity + " ~ " + latitude + "/" + longitude);

                LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title(vicinity);
                markerOptions.position(latLng);

                mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                mMap.getUiSettings().setMyLocationButtonEnabled(true);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
