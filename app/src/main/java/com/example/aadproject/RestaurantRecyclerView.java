package com.example.aadproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class RestaurantRecyclerView extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerLayoutManager;
    private ArrayList<RestaurantDetail> restaurantArrayList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_recycler_view);

        //Initialize Bottom Navigation
        bottomNavigationView = findViewById(R.id.nearby_bottom_navigation);

        // Select the restaurant icon
        bottomNavigationView.setSelectedItemId(R.id.restaurant);

        // Switch between activity
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.user_profile:
                        Intent intent1 = new Intent(RestaurantRecyclerView.this, UserProfile.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        overridePendingTransition(0, 0);
                        startActivity(intent1);
                        return true;
                    case R.id.map:
                        Intent intent2 = new Intent(RestaurantRecyclerView.this, MapActivity.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        overridePendingTransition(0, 0);
                        startActivity(intent2);
                        return true;
                    case R.id.restaurant:
                        return true;
                }
                return false;
            }
        });

        try {
            restaurantArrayList = (ArrayList<RestaurantDetail>) getIntent().getSerializableExtra("mylist");

            if (restaurantArrayList != null) {
                showRecycler(restaurantArrayList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
        }
    }

    // Show the restaurant in recycler view
    public void showRecycler(ArrayList restaurantList) {
        recyclerView = findViewById(R.id.places_list);
        recyclerView.setHasFixedSize(true);
        recyclerLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewAdapter = new RecyclerViewAdapter(restaurantList);
        recyclerView.setLayoutManager(recyclerLayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setSelectedItemId(R.id.restaurant);
    }

    public void returnMap(View view) {
        Intent intent = new Intent(RestaurantRecyclerView.this, MapActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
}
