package com.example.projectmanagement;

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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
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

public class Category extends AppCompatActivity {

    private final AppCompatActivity activity = Category.this;
    ArrayList<Product> productList;
    RecyclerView product_list;
    ProductsAdapter productsAdapter;
    String storeName, category;
    Button btnJollibee, btnKFC, btnMcdo;
    CardView cvJollibee, cvKFC, cvMcdo;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        String phoneNumber = getIntent().getExtras().getString("number");

        final Loading loading = new Loading(Category.this);
        loading.startLoading();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loading.DismissLoading();
            }
        }, 3000);

        btnJollibee = findViewById(R.id.btnJollibee);
        btnKFC = findViewById(R.id.btnKFC);
        btnMcdo = findViewById(R.id.btnMcdo);
        cvJollibee = findViewById(R.id.cvJollibee);
        cvKFC = findViewById(R.id.cvKFC);
        cvMcdo = findViewById(R.id.cvMcdo);
        imageView = findViewById(R.id.imageView);
        product_list = findViewById(R.id.product_list);
        productList = new ArrayList<>();
        category = getIntent().getExtras().getString("category");

        storeName = "Jollibee";
        getProducts();

        btnJollibee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loading.startLoading();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loading.DismissLoading();
                    }
                }, 3000);
                storeName = "Jollibee";
                cvJollibee.setCardBackgroundColor(Color.parseColor("#00ACC1"));
                cvKFC.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                cvMcdo.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                productList.clear();
                getProducts();

                String URL = "http://192.168.254.109/fadSystem/delete_to_choose.php?phone=" + phoneNumber;
                StringRequest stringRequest1 = new StringRequest(URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Category.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                            }
                        });

                RequestQueue requestQueue1 = Volley.newRequestQueue(Category.this);
                requestQueue1.add(stringRequest1);

            }
        });

        btnMcdo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loading.startLoading();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loading.DismissLoading();
                    }
                }, 3000);
                storeName = "Mcdo";
                cvJollibee.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                cvKFC.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                cvMcdo.setCardBackgroundColor(Color.parseColor("#00ACC1"));
                productList.clear();
                getProducts();

                String URL = "http://192.168.254.109/fadSystem/delete_to_choose.php?phone=" + phoneNumber;
                StringRequest stringRequest1 = new StringRequest(URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Category.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                            }
                        });

                RequestQueue requestQueue1 = Volley.newRequestQueue(Category.this);
                requestQueue1.add(stringRequest1);

            }
        });

        btnKFC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loading.startLoading();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loading.DismissLoading();
                    }
                }, 3000);
                storeName = "KFC";
                cvJollibee.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                cvKFC.setCardBackgroundColor(Color.parseColor("#00ACC1"));
                cvMcdo.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                productList.clear();
                getProducts();

                String URL = "http://192.168.254.109/fadSystem/delete_to_choose.php?phone=" + phoneNumber;
                StringRequest stringRequest1 = new StringRequest(URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Category.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                            }
                        });

                RequestQueue requestQueue1 = Volley.newRequestQueue(Category.this);
                requestQueue1.add(stringRequest1);

            }
        });

        if(category.equals("Burger")) imageView.setImageResource(R.drawable.burger1);
        else if(category.equals("Chicken")) imageView.setImageResource(R.drawable.fried_chicken);
        else if(category.equals("Drinks")) imageView.setImageResource(R.drawable.drinks1);
        else if(category.equals("Fries")) imageView.setImageResource(R.drawable.fries1);
        else if(category.equals("Pizza")) imageView.setImageResource(R.drawable.pizza1);
        else if(category.equals("Pasta")) imageView.setImageResource(R.drawable.pasta1);


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
                        Toast.makeText(Category.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
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