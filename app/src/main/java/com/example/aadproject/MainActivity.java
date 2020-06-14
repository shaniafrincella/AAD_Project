package com.example.aadproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity {

    private static final String TAG ="MainActivity";
    // handle the error for invalid services version
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private Button buttonMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonMap = findViewById(R.id.button_map);

        if(isGoogleServicesAvailable()){
            init();
        }

    }

    private void init(){
        buttonMap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(getApplicationContext(), MapActivity.class);
               startActivity(intent);
               finish();
            }
        });
    }

    // To check the google service version
    public boolean isGoogleServicesAvailable(){
        Log.d(TAG, "isGoogleServicesAvailable: Checking the google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if(available == ConnectionResult.SUCCESS){
            // The user google services is valid and the user can make map requests
            Log.d(TAG,"isGoogleServicesAvailable: Google Play Services is available");
            return true;
        }else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            // An error occured message
            Log.d(TAG,"isGoogleServicesAvailable: An error occured. We are trying to fix it.");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this,"You cannot make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
