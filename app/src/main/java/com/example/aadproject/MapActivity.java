package com.example.aadproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;


public class MapActivity extends FragmentActivity {

    private static final String TAG= "MapActivity";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    //variable
    private boolean aBooleanLocationPermissionGranted = false;
    private GoogleMap mMap;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        //getLocationPermission();
    }
    // Initialize the map
    private void initMap(){
        Log.d(TAG,"initMap: creating the map");
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Toast.makeText(MapActivity.this, "Map is Ready",
                        Toast.LENGTH_SHORT).show();
                Log.d(TAG,"onMapReady: map is ready");
                mMap = googleMap;
            }
        });
    }

    // Ask for permission request
    private void getLocationPermission(){
        Log.d(TAG,"getLocationPermission: getting permission for location");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION};

        // check for permission to get fine location
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            // check for permission to get coarse location
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED){
                // boolean
                aBooleanLocationPermissionGranted = true;
            }else{
                ActivityCompat.requestPermissions(this, permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.d(TAG,"onRequestPermissionResult: calling for request");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        aBooleanLocationPermissionGranted =false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length>0){
                    // checking more than one permission
                    for(int i = 0; i<grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            aBooleanLocationPermissionGranted=false;
                            Log.d(TAG,"getLocationPermission: request failed");
                            return;
                        }
                    }
                    // if permission is granted then initialize the map
                    Log.d(TAG,"getLocationPermission: request granted");
                    aBooleanLocationPermissionGranted = true;
                    initMap();
                }
            }
        }
    }
}
