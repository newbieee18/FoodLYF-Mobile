package com.example.projectmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
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

    TextView txtStoreName, deliveredOrders, nOutlets, nFoodProducts;
    String phoneNumber;
    Animation fadeIn;
    ImageView ivShowImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_store);

        phoneNumber = getIntent().getExtras().getString("number");

        deliveredOrders = findViewById(R.id.deliveredOrders);
        nFoodProducts = findViewById(R.id.nFoodProducts);
        nOutlets = findViewById(R.id.nOutlets);
        txtStoreName = findViewById(R.id.txtStoreName);
        ivShowImage = findViewById(R.id.ivShowImage);

        getTransaction();
        getFoodProducts();
        getStore();
        getOutlets();


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
        String count = "";

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("foodProducts");

            for (int i = 0; i < result.length(); i++) {

                JSONObject products = result.getJSONObject(i);
                count = String.valueOf(result.length());

            }
            Log.i("tagconvertstr", "[" + response + "]");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        nFoodProducts.setText(count);

    }

    private void getStore() {

        String url = "http://192.168.254.109/fadSystem/storeInfo1.php?phone=" + phoneNumber;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                showJSONS2(response);
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

    private void showJSONS2(String response) {
        String store_name = "";
        String imageUrl = "";

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("store");

            for (int i = 0; i < result.length(); i++) {

                JSONObject store = result.getJSONObject(i);
                store_name = store.getString("store_name");
                imageUrl = store.getString("store_image");


            }
            Log.i("tagconvertstr", "[" + response + "]");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        txtStoreName.setText(store_name);
        byte[] decodedString = Base64.decode(imageUrl, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        ivShowImage.setImageBitmap(decodedByte);

    }

    private void getOutlets() {

        String url = "http://192.168.254.109/fadSystem/outlets.php?phone=" + phoneNumber;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                showJSONS3(response);
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

    private void showJSONS3(String response) {
        String count = "";

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("outlets");

            for (int i = 0; i < result.length(); i++) {

                JSONObject outlets = result.getJSONObject(i);
                count = String.valueOf(result.length());


            }
            Log.i("tagconvertstr", "[" + response + "]");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        nOutlets.setText(count);

    }

}