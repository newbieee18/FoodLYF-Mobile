package com.example.projectmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DashboardStore extends AppCompatActivity {

    TextView Logout, deliveredOrders, nOutlets, nFoodProducts;
    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_store);

        phoneNumber = getIntent().getExtras().getString("number");

        deliveredOrders = findViewById(R.id.deliveredOrders);
        nFoodProducts = findViewById(R.id.nFoodProducts);
        nOutlets = findViewById(R.id.nOutlets);

        getTransaction();



    }

    public void logoutClicked(View view){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        FirebaseAuth.getInstance().signOut();
        Intent login = new Intent(getApplicationContext(), SplashScreen.class);
        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(login);
    }

    public void productClicked(View view){

        if(view.getId() == R.id.ibFoodProducts){
            Intent stores =  new Intent(getApplicationContext(), FoodProducts.class);
            stores.putExtra("number", phoneNumber);
            startActivity(stores);
        }

    }

    public void outletsClicked(View view){

        if(view.getId() == R.id.ibOutlets){
            Intent outlets = new Intent(getApplicationContext(), StoreOutlets.class);
            outlets.putExtra("number", phoneNumber);
            startActivity(outlets);
        }

    }

    private void getTransaction() {

        String url = "http://192.168.254.109/fadSystem/transaction_store.php?phone=" + phoneNumber;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                showJSONS(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DashboardStore.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void showJSONS(String response) {
        String delivered = "";

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("transaction");

            for(int i=0; i<result.length(); i++) {

                JSONObject transaction = result.getJSONObject(i);
                delivered = transaction.getString("total_orders");

            }
            Log.i("tagconvertstr", "["+response+"]");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        deliveredOrders.setText(delivered);
        if(delivered == null || delivered.equals("")){
            deliveredOrders.setText("5");
        }

    }

    private void getFoodProducts() {

        String url = "http://192.168.254.109/fadSystem/foodProducts.php?phone=" + phoneNumber;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                showJSONS1(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DashboardStore.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void showJSONS1(String response) {
        int count = 0;

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("foodProducts");

            for (int i = 0; i < result.length(); i++) {

                JSONObject transaction = result.getJSONObject(i);
                count = transaction.length();

            }
            Log.i("tagconvertstr", "[" + response + "]");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        nFoodProducts.setText(count);

    }


}