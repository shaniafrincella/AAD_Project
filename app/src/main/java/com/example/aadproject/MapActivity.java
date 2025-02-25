package com.example.aadproject;

/**
 * This class will be the main screen and display Google Map Layout
 * Created by Shania Frincella
 */

import android.Manifest;
import android.app.Dialog;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MapActivity extends FragmentActivity implements OnMapReadyCallback, SensorEventListener {
    public BottomNavigationView bottomNavigationView;
    private static final String TAG = "MapActivity";
    // handle the error for invalid services version
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    public static final int PROXIMITY_RADIUS = 2000;
    private GoogleApi googleApi;

    //List of widgets
    private EditText editTextSearchText;
    private ImageView mGPS;
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private boolean lightSensorPresent;

    //List of variables
    private Boolean locationPermissionGranted = false;
    private GoogleMap mMap;
    private LatLng mLatLng;

    // Google API object
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (lightSensor == null) {
            lightSensorPresent = false;
        } else {
            lightSensorPresent = true;
        }

        editTextSearchText = findViewById(R.id.input_search);
        mGPS = findViewById(R.id.gps_icon);
        if (isGoogleServicesAvailable()) {
            getLocationPermission();
            init();
        }
        //Initialize Bottom Navigation
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.map_bottom_navigation);

        // Select Map Icon
        bottomNavigationView.setSelectedItemId(R.id.map);

        // Switch between activity
        bottomNavigationView.setOnNavigationItemSelectedListener(new OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.user_profile:
                        Intent intent1 = new Intent(MapActivity.this,
                                UserProfile.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent1);
                        return true;
                    case R.id.map:
                        return true;
                    case R.id.restaurant:
                        Intent intent2 = new Intent(MapActivity.this,
                                RestaurantRecyclerView.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent2);
                        return true;
                }
                return false;
            }
        });


    }

    public boolean isGoogleServicesAvailable() {
        Log.d(TAG, "isGoogleServicesAvailable: Checking the google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                MapActivity.this);

        if (available == ConnectionResult.SUCCESS) {
            // The user google services is valid and the user can make map requests
            Log.d(TAG, "isGoogleServicesAvailable: Google Play Services is available");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            // An error occured message
            Log.d(TAG, "isGoogleServicesAvailable: An error occured. We are trying to fix it.");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(
                    MapActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "You cannot make map requests",
                    Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    private void init() {
        Log.d(TAG, "init: initializing");
        editTextSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER) {
                    geoLocate();
                }
                return false;
            }
        });
        mGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick:clicked gps icon");
                getCurrentDeviceLocation();
            }
        });
    }

    private void geoLocate() {
        Log.d(TAG, "geolocate: geolocating");
        String searchString = editTextSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.d(TAG, "geolocate :IOExecptiion " + e.getMessage());
        }
        if (list.size() > 0) {
            Address address = list.get(0);
            Log.d(TAG, "geolocate : found a location" + address.toString());
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,
                    address.getAddressLine(0));
        }
    }

    private void getCurrentDeviceLocation() {
        Log.d(TAG, "getCurrentDeviceLocation: getting the devices current location");
        // getting the location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(
                new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location currentLocation = task.getResult();
                if (currentLocation != null) {
                    Log.d(TAG, "onComplete: Location found.");
                    mLatLng = new LatLng(currentLocation.getLatitude(),
                            currentLocation.getLongitude());
                    moveCamera(mLatLng, DEFAULT_ZOOM, "My Location");
                } else {
                    Log.d(TAG, "onComplete: current location is null");
                    Toast.makeText(MapActivity.this,
                            "Unable to locate current location", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.d(TAG, "moveCamera : moving camera to latitude:" + latLng.latitude + ", " +
                ", longitude:" + latLng.longitude);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if (!title.equals("My Location")) {
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mMap.addMarker(options);
        }
    }

    // Initialize the map
    private void initMap() {
        Log.d(TAG, "initMap: creating the map");
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        mMap = googleMap;

        // Showing the device's current location in the map
        if (locationPermissionGranted) {
            getCurrentDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            init();
        }
    }

    // Ask for permission request
    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting permission for location");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this.getApplicationContext(),
                COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
            initMap();
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionResult: calling for request");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationPermissionGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    // checking more than one permission
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            locationPermissionGranted = false;
                            Log.d(TAG, "getLocationPermission: request failed");
                            return;
                        }
                    }
                    // if permission is granted then initialize the map
                    Log.d(TAG, "getLocationPermission: request granted");
                    locationPermissionGranted = true;
                    initMap();
                }
            }
        }
    }

    public void findRestaurant(View view) {
        StringBuilder stringBuilder =
                new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        String locationStr = mLatLng.latitude + "," + mLatLng.longitude;
        stringBuilder.append("location=" + locationStr);
        stringBuilder.append("&radius=" + 1000);
        stringBuilder.append("&type=" + "restaurant");
        stringBuilder.append("&sensor=true");
        stringBuilder.append("&key=" + getResources().getString(R.string.google_api_key));

        String url = stringBuilder.toString();
        System.out.println(url);

        Object passData[] = new Object[3];
        passData[0] = mMap;
        passData[1] = url;
        passData[2] = getApplicationContext();

        GetNearbyRestaurant nearbyRestaurant = new GetNearbyRestaurant(
                new GetNearbyRestaurant.taskInterface() {
            ArrayList<RestaurantDetail> restaurantArrayListNew = new ArrayList<>();

            @Override
            public void passResult(ArrayList restaurantArrayList) {
                restaurantArrayListNew = restaurantArrayList;
                Intent intent = new Intent(MapActivity.this,
                        RestaurantRecyclerView.class);
                intent.putExtra("mylist", restaurantArrayListNew);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
        nearbyRestaurant.execute(passData);
        Log.d(TAG, "Locating Restaurant");
        Toast.makeText(this, "Showing Nearby Restaurants", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setSelectedItemId(R.id.map);
        if (lightSensorPresent) {
            sensorManager.registerListener(this, lightSensor,
                    sensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (lightSensorPresent) {
            sensorManager.unregisterListener(this);
        }
    }

    // Light Sensor Implementation
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (lightSensorPresent) {
            WindowManager.LayoutParams params = getWindow().getAttributes();
            float light = event.values[0];
            if (light > 700) {
                params.screenBrightness = 1.0f;
            } else if (light < 350) {
                params.screenBrightness = 0.4f;
            } else if (light < 150) {
                params.screenBrightness = 0.2f;
            }
            getWindow().setAttributes(params);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
