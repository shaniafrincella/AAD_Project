package com.example.aadproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //Initialize Bottom Navigation
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.user_bottom_navigation);

        // Select Map
        bottomNavigationView.setSelectedItemId(R.id.user_profile);

        // Switch between activity
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.user_profile:
                        return true;
                    case R.id.map:
                        Intent intent2 = new Intent(getApplicationContext(), MapActivity.class);
                        startActivity(intent2);
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }
}
