package com.example.projectmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Cart extends AppCompatActivity {

    private final AppCompatActivity activity = Cart.this;
    RecyclerView cart;
    CartListAdapter cartListAdapter;
    List<CartList> cartList;
    String phoneNumber, storeName;
    CardView proceedPayment;
    Button btnProceedPayment;
    ImageView ivEmptyCart;

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        final Loading loading = new Loading(Cart.this);
        loading.startLoading();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loading.DismissLoading();
            }
        }, 3000);

        phoneNumber = getIntent().getExtras().getString("number");
        storeName = getIntent().getExtras().getString("storeName");

        proceedPayment = findViewById(R.id.cvPayment);
        btnProceedPayment = findViewById(R.id.proceedPayment);
        cart = findViewById(R.id.cart);
        ivEmptyCart = findViewById(R.id.ivEmptyCart);
        cartList = new ArrayList<>();
        getCart();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_cart);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.dashboard:
                        Intent dashboard = new Intent(getApplicationContext(), DashboardCustomer.class);
                        dashboard.putExtra("number", phoneNumber);
                        startActivity(dashboard);
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_from_left);
                        return true;
                }
                switch (item.getItemId()){
                    case R.id.nav_cart:
                        Intent cart = new Intent(getApplicationContext(), Cart.class);
                        cart.putExtra("number", phoneNumber);
                        startActivity(cart);
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_from_left);
                        return true;
                }
                switch (item.getItemId()){
                    case R.id.Account:
                        Intent account = new Intent(getApplicationContext(), Account.class);
                        account.putExtra("number", phoneNumber);
                        startActivity(account);
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_from_left);
                        return true;
                }
                return false;
            }
        });


    }

    private void setCart(List<CartList> cartList){

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        cart.setLayoutManager(layoutManager);
        cartListAdapter = new CartListAdapter(this, cartList);
        cart.setAdapter(cartListAdapter);

    }

    private void getCart() {

        String url = Config.CART_URL + phoneNumber;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                showJSONS(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Cart.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void showJSONS(String response) {
        int cart_id = 0;
        int quantity = 0;
        double product_price = 0;
        String product_name = "";
        String product_image = "";
        double cartTotal = 0;
        double subtotal = 0;

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY8);

            for(int i=0; i<result.length(); i++) {

                JSONObject cart = result.getJSONObject(i);
                product_name = cart.getString(Config.CART_PRODUCT_NAME);
                cart_id = cart.getInt(Config.CART_PRODUCT_ID);
                product_price = cart.getDouble(Config.CART_PRODUCT_PRICE);
                product_image = cart.getString(Config.CART_PRODUCT_IMAGE);
                quantity = cart.getInt(Config.CART_PRODUCT_QUANTITY);
                subtotal = cart.getDouble(Config.CART_PRODUCT_SUBTOTAL);
                cartTotal += subtotal;

                cartList.add(new CartList(cart_id, product_name, product_price, subtotal, product_image, quantity));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        setCart(cartList);
        if(cartList==null || cartList.size()==0){
            proceedPayment.setVisibility(View.INVISIBLE);
            ivEmptyCart.setVisibility(View.VISIBLE);
        }
        else {
            proceedPayment.setVisibility(View.VISIBLE);
            ivEmptyCart.setVisibility(View.INVISIBLE);
            btnProceedPayment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent payment = new Intent(getApplicationContext(), Payment.class);
                    payment.putExtra("number", phoneNumber);
                    payment.putExtra("storeName", storeName);
                    startActivity(payment);
                    finish();

                }
            });
        }


    }




}