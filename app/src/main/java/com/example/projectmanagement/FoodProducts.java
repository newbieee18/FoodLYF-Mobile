package com.example.projectmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FoodProducts extends AppCompatActivity {

    String phoneNumber;
    ArrayList<Product> productList;
    RecyclerView product_list;
    FoodProductsAdapter foodProductsAdapter;
    Button addProduct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_products);

        phoneNumber = getIntent().getExtras().getString("number");

        addProduct = findViewById(R.id.btnAdd);
        product_list = findViewById(R.id.product_list);
        productList = new ArrayList<>();
        getProducts();

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent addFoodProduct = new Intent(getApplicationContext(), AddFoodProduct.class);
                addFoodProduct.putExtra("number", phoneNumber);
                startActivity(addFoodProduct);

            }
        });

    }

    private void setProducts(List<Product> productList){

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        product_list.setLayoutManager(layoutManager);
        foodProductsAdapter = new FoodProductsAdapter(this, productList);
        product_list.setAdapter(foodProductsAdapter);

    }

    private void getProducts() {

        String url = Config.PRODUCT_URL1 + phoneNumber;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                showJSONS(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FoodProducts.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void showJSONS(String response) {
        String product_name = "";
        String product_desc = "";
        String product_image = "";
        Integer product_id;
        Double ratings, product_price;

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY4);

            for(int i=0; i<result.length(); i++) {

                JSONObject products = result.getJSONObject(i);
                product_name = products.getString(Config.PRODUCT_NAME);
                product_desc = products.getString(Config.PRODUCT_DESC);
                product_image = products.getString(Config.PRODUCT_IMAGE);
                product_id = products.getInt(Config.PRODUCT_ID);
                product_price = products.getDouble(Config.PRODUCT_PRICE);
                ratings = products.getDouble(Config.RATINGS);

                productList.add(new Product(product_id, product_name, product_desc, ratings, product_price, product_image));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        setProducts(productList);

    }


    @Override
    public void onBackPressed() {
        Intent dashboardStore = new Intent(getApplicationContext(), DashboardStore.class);
        dashboardStore.putExtra("number", phoneNumber);
        startActivity(dashboardStore);
    }


}