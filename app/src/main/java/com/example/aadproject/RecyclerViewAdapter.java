package com.example.aadproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>{
    public ArrayList<RestaurantDetail> restaurantArrayList = new ArrayList<RestaurantDetail>();

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView address;
        public TextView rating;
        public TextView price;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.restaurant_name);
            address = itemView.findViewById(R.id.address);
            rating = itemView.findViewById(R.id.rating);
            price = itemView.findViewById(R.id.price);
        }
    }

    public RecyclerViewAdapter(ArrayList<RestaurantDetail> restaurantList) {
        restaurantArrayList = restaurantList;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        RestaurantDetail currentRestaurant = restaurantArrayList.get(position);

        holder.name.setText(currentRestaurant.getName());
        holder.address.setText(currentRestaurant.getVicinity());
        holder.rating.setText(currentRestaurant.getRating());
        holder.price.setText(currentRestaurant.getPricelevel());
    }

    @Override
    public int getItemCount() {
        return restaurantArrayList.size();
    }
}
