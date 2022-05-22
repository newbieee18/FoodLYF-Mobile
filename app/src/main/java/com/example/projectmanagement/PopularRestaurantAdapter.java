package com.example.projectmanagement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PopularRestaurantAdapter extends RecyclerView.Adapter<PopularRestaurantAdapter.PopularRestaurantViewHolder> {

    Context context;
    List<PopularRestaurant> popularRestaurantList;

    public PopularRestaurantAdapter(Context context, List<PopularRestaurant> popularRestaurantList) {
        this.context = context;
        this.popularRestaurantList = popularRestaurantList;
    }

    @NonNull
    @Override
    public PopularRestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.popular_restaurant_row_item, parent, false);
        return new PopularRestaurantViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull PopularRestaurantViewHolder holder, int position) {

        PopularRestaurant currentItem = popularRestaurantList.get(position);

        Intent intent = ((Activity) context).getIntent();
        String phoneNumber = intent.getExtras().getString("number");

        String imageUrl = currentItem.getImageUrl();
        String storeName = currentItem.getName();

        String imageUri = imageUrl;
        byte[] decodedString = Base64.decode(imageUri, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.restaurantImage.setImageBitmap(decodedByte);
        holder.restaurantImage.getLayoutParams().height = 400;
        holder.restaurantImage.getLayoutParams().width = 400;
        holder.restaurantImage.setScaleType(ImageView.ScaleType.FIT_XY);
        holder.restaurantName.setText(storeName);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent storeTab = new Intent(context, StoreTab.class);
                storeTab.putExtra("storeName", storeName);
                storeTab.putExtra("number", phoneNumber);
                context.startActivity(storeTab);

            }
        });

    }

    @Override
    public int getItemCount() {
        return popularRestaurantList.size();
    }

    public class PopularRestaurantViewHolder extends RecyclerView.ViewHolder{

        ImageView restaurantImage;
        TextView restaurantName;

        public PopularRestaurantViewHolder(@NonNull View itemView){
            super(itemView);

            restaurantImage = itemView.findViewById(R.id.restaurant_image);
            restaurantName = itemView.findViewById(R.id.restaurant_name);


        }

    }

}
