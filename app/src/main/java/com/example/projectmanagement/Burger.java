package com.example.projectmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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

public class Burger extends AppCompatActivity {

    private final AppCompatActivity activity = Burger.this;
    ArrayList<Product> productList;
    RecyclerView product_list;
    ProductsAdapter productsAdapter;
    String storeName, category;
    Button btnJollibee, btnKFC, btnChowking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burger);

        btnJollibee = findViewById(R.id.btnJollibee);
        btnKFC = findViewById(R.id.btnKFC);
        btnChowking = findViewById(R.id.btnChowking);
        product_list = findViewById(R.id.product_list);
        productList = new ArrayList<>();
        category = getIntent().getExtras().getString("category");

        storeName = "Jollibee";
        getProducts();

        btnJollibee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeName = "Jollibee";
                productList.clear();
                getProducts();
            }
        });

        btnChowking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeName = "Chowking";
                productList.clear();
                getProducts();
            }
        });

        btnKFC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeName = "KFC";
                productList.clear();
                getProducts();
            }
        });

        //make full transparent statusBar
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(activity, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(activity, false);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

    }

    private static void setWindowFlag(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        } else {
            winParams.flags &= ~WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        }
        win.setAttributes(winParams);
    }

    private void setProducts(List<Product> productList){

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        product_list.setLayoutManager(layoutManager);
        productsAdapter = new ProductsAdapter(this, productList);
        product_list.setAdapter(productsAdapter);

    }

    private void getProducts() {

        String url = Config.PRODUCT_URL + storeName + "&category=" + category;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                showJSONS(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Burger.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
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
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY2);

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

}