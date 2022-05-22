package com.example.projectmanagement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.text.Editable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.logging.Logger;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.CartListViewHolder> {

    private Context mCtx;
    private List<CartList> cartList;


    public CartListAdapter(Context mCtx, List<CartList> cartList) {
        this.mCtx = mCtx;
        this.cartList = cartList;

    }

    @Override
    public CartListAdapter.CartListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.cart, parent, false);
        return new CartListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartListAdapter.CartListViewHolder holder, int position) {
        CartList cart = cartList.get(position);

        final int[] count = {cart.getQuantity()};

        holder.cart_id.setText(String.valueOf(cart.getId()));

        final String[] imageUrl = {cart.getProductImage()};
        byte[] decodedString = Base64.decode(imageUrl[0], Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.productImage.setImageBitmap(decodedByte);
        holder.productImage.setScaleType(ImageView.ScaleType.FIT_XY);
        holder.productName.setText(cart.getProductName());
        holder.productQuantity.setText(String.valueOf(cart.getQuantity()));
        holder.productPrice.setText(String.format("₱%.2f", cart.getProductSubtotal()));

        holder.decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                count[0]--;

                int a = count[0];

                double b = a * cart.getProductPrice();
                String quantity, subtotal, cart_id;

                cart_id = String.valueOf(cart.getId());
                quantity = String.valueOf(count[0]);
                subtotal = String.valueOf(b);

                holder.productQuantity.setText(quantity);
                holder.productPrice.setText("₱" + subtotal);

                    if(count[0] <= 0){
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            String[] field = new String[1];
                            field[0] = "cart_id";

                            String[] data = new String[1];
                            data[0] = cart_id;

                            PutData putData = new PutData("http://192.168.254.109/fadSystem/delete_cart.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if(result.equals("Record deleted successfully")){
                                        Intent intent = ((Activity) mCtx).getIntent();
                                        String phoneNumber = intent.getExtras().getString("number");
                                        String storeName = intent.getExtras().getString("storeName");
                                        Intent cart = new Intent(mCtx, Cart.class);
                                        cart.putExtra("number", phoneNumber);
                                        cart.putExtra("storeName", storeName);
                                        mCtx.startActivity(cart);
                                        ((Activity) mCtx).overridePendingTransition(0, 0);
                                        ((Activity) mCtx).finish();
                                    }
                                    else{
                                        Toast.makeText(mCtx, result, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });
                    }

                    if(!cart_id.equals("") && !quantity.equals("") && !subtotal.equals("")){
                        Handler handler = new Handler();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                String[] field = new String[3];
                                field[0] = "cart_id";
                                field[1] = "quantity";
                                field[2] = "subtotal";

                                String[] data = new String[3];
                                data[0] = cart_id;
                                data[1] = quantity;
                                data[2] = subtotal;

                                PutData putData = new PutData("http://192.168.254.109/fadSystem/update_cart.php", "POST", field, data);
                                if (putData.startPut()) {
                                    if (putData.onComplete()) {
                                        String result = putData.getResult();
                                        if(result.equals("Record updated successfully")){
                                            holder.productQuantity.setText(quantity);
                                            holder.productPrice.setText("₱" + subtotal);
                                        }
                                        else{
                                            Toast.makeText(mCtx, result, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        });
                    }



            }
        });


        holder.increment.setOnClickListener(new View.OnClickListener() {
            //int count = cart.getQuantity();
            @Override
            public void onClick(View v) {

                count[0]++;

                int a = count[0];

                double b = a * cart.getProductPrice();
                String quantity, subtotal, cart_id;

                cart_id = String.valueOf(cart.getId());
                quantity = String.valueOf(count[0]);
                subtotal = String.valueOf(b);


                //Log.d("Var :",String.valueOf(holder.increment.getId()));
                Log.d("Count: ", String.valueOf(count));

                if(!cart_id.equals("") && !quantity.equals("") && !subtotal.equals("")){
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            String[] field = new String[3];
                            field[0] = "cart_id";
                            field[1] = "quantity";
                            field[2] = "subtotal";

                            String[] data = new String[3];
                            data[0] = cart_id;
                            data[1] = quantity;
                            data[2] = subtotal;


                            PutData putData = new PutData("http://192.168.254.109/fadSystem/update_cart.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if(result.equals("Record updated successfully")){
                                        holder.productQuantity.setText(quantity);
                                        holder.productPrice.setText("₱" + subtotal);
                                    }
                                    else{
                                        Toast.makeText(mCtx, result, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });
                }


                }
            });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cart_id = String.valueOf(cart.getId());

                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        String[] field = new String[1];
                        field[0] = "cart_id";

                        String[] data = new String[1];
                        data[0] = cart_id;

                        PutData putData = new PutData("http://192.168.254.109/fadSystem/delete_cart.php", "POST", field, data);
                        if (putData.startPut()) {
                            if (putData.onComplete()) {
                                String result = putData.getResult();
                                if(result.equals("Record deleted successfully")){
                                    Intent intent = ((Activity) mCtx).getIntent();
                                    String phoneNumber = intent.getExtras().getString("number");
                                    String storeName = intent.getExtras().getString("storeName");
                                    Intent cart = new Intent(mCtx, Cart.class);
                                    cart.putExtra("number", phoneNumber);
                                    cart.putExtra("storeName", storeName);
                                    mCtx.startActivity(cart);
                                    ((Activity) mCtx).overridePendingTransition(0, 0);
                                    ((Activity) mCtx).finish();
                                }
                                else{
                                    Toast.makeText(mCtx, result, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                });

            }
        });


    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    class CartListViewHolder extends RecyclerView.ViewHolder {

        TextView productName, productPrice, productQuantity;
        ImageView productImage, delete;
        CardView productCardView;
        Button decrement, increment;
        EditText cart_id;

        public CartListViewHolder(View itemView) {
            super(itemView);

            productCardView = itemView.findViewById(R.id.productCardView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            decrement = itemView.findViewById(R.id.decrement);
            increment = itemView.findViewById(R.id.increment);
            cart_id = itemView.findViewById(R.id.cart_id);
            delete = itemView.findViewById(R.id.delete);


        }
    }
}
