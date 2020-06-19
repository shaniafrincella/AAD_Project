package com.example.aadproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ListNearbyRestaurant extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerViewAdapter;
    RecyclerView.LayoutManager recyclerLayoutManager;
    ArrayList<RestaurantDetail> restaurantArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_nearby_restaurant);

        //Bundle objectUser = getIntent().getExtras();
        /*if (objectUser != null) {
            restaurantArrayList = (ArrayList) getIntent().getSerializableExtra("TAKETHIS");
            showRecycler(restaurantArrayList);
        }*/
        ArrayList<RestaurantDetail> restaurantArrayList = (ArrayList<RestaurantDetail>) getIntent().getSerializableExtra("mylist");
        showRecycler(restaurantArrayList);
    }

    public void showRecycler(ArrayList restaurantList) {
        /*for (int i = 0; i < restaurantList.size(); i++) {
                    RestaurantDetail restaurant = (RestaurantDetail) restaurantList.get(0);
                    String name = (String) restaurant.getName();
                    String address = (String) restaurant.getVicinity();
                    String rating = (String) restaurant.getRating();
                    String price = (String) restaurant.getPricelevel();
        }*/
        System.out.println(restaurantList);
        recyclerView = findViewById(R.id.places_list);
        recyclerView.setHasFixedSize(true);
        recyclerLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewAdapter = new RecyclerViewAdapter(restaurantList);
        recyclerView.setLayoutManager(recyclerLayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
    }
}
