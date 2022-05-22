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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class DeliveredOrdersAdapter extends RecyclerView.Adapter<DeliveredOrdersAdapter.DeliveredOrdersAdapterViewHolder> {

    private Context mCtx;
    private List<OrderList> deliveredList;
    String product_name, imageUrl;

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

        product_name = deliveredOrders.getProductName();

        product_name = product_name.replaceAll("\\(.*\\)", "");

        String url = "http://192.168.254.109/fadSystem/getProductImage.php?product_name=" + product_name;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                String product_image = "";

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray("result");

                    for(int i=0; i<result.length(); i++) {

                        JSONObject products = result.getJSONObject(i);
                        product_image = products.getString("product_image");

                    }
                    Log.i("tagconvertstr", "["+result+"]");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                imageUrl = product_image;
                byte[] decodedString = Base64.decode(imageUrl, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.productImage.setImageBitmap(decodedByte);
                holder.quantity.setText("Quantity: " + String.valueOf(deliveredOrders.getProductQuantity()));
                holder.productName.setText(product_name);
                holder.productPrice.setText(String.format("₱%.2f", deliveredOrders.getSubtotal()));

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mCtx, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(mCtx);
        requestQueue.add(stringRequest);



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
