package com.example.aadproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {
    public ArrayList<RestaurantDetail> restaurantArrayList = new ArrayList<RestaurantDetail>();

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView address;
        public TextView rating;
        public TextView price;
        public ImageView image;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.restaurant_name);
            address = itemView.findViewById(R.id.address);
            rating = itemView.findViewById(R.id.rating);
            price = itemView.findViewById(R.id.price);
            image = itemView.findViewById(R.id.image);
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

        holder.image.setImageResource(0);
        String imageUrl = currentRestaurant.getImagereference();
        new downloadImage(holder.image).execute(imageUrl);

        holder.name.setText(currentRestaurant.getName());
        holder.address.setText(currentRestaurant.getVicinity());
        holder.rating.setText(currentRestaurant.getRating());
        holder.price.setText(currentRestaurant.getPricelevel());
    }

    @Override
    public int getItemCount() {
        if (restaurantArrayList.size() != 0) {
            return restaurantArrayList.size();
        }
        return 0;
    }

    private class downloadImage extends AsyncTask<String, Void, Bitmap> {
        ImageView image;

        public downloadImage(ImageView image) {
            this.image = image;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String url = strings[0];
            Bitmap bitmap = null;
            try {
                URL urlObj = new URL(url);
                InputStream inputStream = urlObj.openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            image.setImageBitmap(bitmap);
        }
    }
}
