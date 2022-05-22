package com.example.projectmanagement;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class DeliveredOrdersAdapter extends RecyclerView.Adapter<DeliveredOrdersAdapter.DeliveredOrdersAdapterViewHolder> {

    private Context mCtx;
    private List<OrderList> deliveredList;

    public DeliveredOrdersAdapter(Context mCtx, List<OrderList> deliveredList) {
        this.mCtx = mCtx;
        this.deliveredList = deliveredList;
    }

    @Override
    public DeliveredOrdersAdapter.DeliveredOrdersAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.delivered_orders, parent, false);
        return new DeliveredOrdersAdapter.DeliveredOrdersAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DeliveredOrdersAdapter.DeliveredOrdersAdapterViewHolder holder, int position) {
        OrderList deliveredOrders = deliveredList.get(position);

//        String imageUrl = deliveredOrders.getProductImage();
//        byte[] decodedString = Base64.decode(imageUrl, Base64.DEFAULT);
//        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//        holder.productImage.setImageBitmap(decodedByte);
        holder.productName.setText(deliveredOrders.getProductName());
        holder.productPrice.setText("₱%.2f" + String.valueOf(deliveredOrders.getSubtotal()));




    }

    @Override
    public int getItemCount() {
        return deliveredList.size();
    }

    class DeliveredOrdersAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView productName, quantity, productPrice;
        ImageView productImage;

        public DeliveredOrdersAdapterViewHolder(View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.productName);
            quantity = itemView.findViewById(R.id.quantity);
            productPrice = itemView.findViewById(R.id.productPrice);
            productImage = itemView.findViewById(R.id.productImage);


        }
    }
}
