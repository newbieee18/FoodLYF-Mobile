package com.example.projectmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class StoreTab extends AppCompatActivity {

    private final AppCompatActivity activity = StoreTab.this;
    ArrayList<Product> productList;
    RecyclerView product_list;
    ProductsAdapter productsAdapter;
    String storeName, category;
    ImageView ivStore;
    CardView cvBurger, cvPasta, cvChicken, cvRm, cvFries, cvDaF, cvDrinks;
    Button btnCart, btnBurger, btnPasta, btnChicken, btnRiceMeals, btnDaF, btnFries, btnDrinks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_tab);

        String phoneNumber = getIntent().getExtras().getString("number");

        final Loading loading = new Loading(StoreTab.this);
        loading.startLoading();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loading.DismissLoading();
            }
        }, 3000);


        storeName = getIntent().getStringExtra("storeName");
        btnBurger = findViewById(R.id.btnBurger);
        btnPasta = findViewById(R.id.btnPasta);
        btnChicken = findViewById(R.id.btnChicken);
        btnRiceMeals = findViewById(R.id.btnRiceMeals);
        btnDaF = findViewById(R.id.btnDaF);
        btnFries = findViewById(R.id.btnFries);
        btnCart = findViewById(R.id.btnCart);
        product_list = findViewById(R.id.product_list);
        productList = new ArrayList<>();
        ivStore = findViewById(R.id.ivStore);
        cvBurger = findViewById(R.id.cvBurger);
        cvPasta = findViewById(R.id.cvPasta);
        cvChicken = findViewById(R.id.cvChicken);
        cvFries = findViewById(R.id.cvFries);
        cvRm = findViewById(R.id.cvRm);
        cvDaF = findViewById(R.id.cvDaF);
        cvDrinks = findViewById(R.id.cvDrinks);
        btnDrinks = findViewById(R.id.btnDrinks);

        getStore();

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent cart = new Intent(getApplication(), Cart.class);
                cart.putExtra("number", phoneNumber);
                cart.putExtra("storeName", storeName);
                startActivity(cart);

            }
        });

        category = "Burger";
        getProducts();

        btnBurger.setOnClickListener(new View.OnClickListener() {
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
                category = "Burger";
                cvBurger.setCardBackgroundColor(Color.parseColor("#00ACC1"));
                cvPasta.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                cvChicken.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                cvFries.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                cvRm.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                cvDaF.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                cvDrinks.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                productList.clear();
                getProducts();
            }
        });

        btnPasta.setOnClickListener(new View.OnClickListener() {
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
                category = "Pasta";
                cvBurger.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                cvPasta.setCardBackgroundColor(Color.parseColor("#00ACC1"));
                cvChicken.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                cvFries.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                cvRm.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                cvDaF.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                cvDrinks.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                productList.clear();
                getProducts();
            }
        });

        btnChicken.setOnClickListener(new View.OnClickListener() {
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
                category = "Chicken";
                cvBurger.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                cvPasta.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                cvChicken.setCardBackgroundColor(Color.parseColor("#00ACC1"));
                cvFries.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                cvRm.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                cvDaF.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                cvDrinks.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                productList.clear();
                getProducts();
            }
        });

        btnFries.setOnClickListener(new View.OnClickListener() {
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
                category = "Fries";
                cvBurger.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                cvPasta.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                cvChicken.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                cvFries.setCardBackgroundColor(Color.parseColor("#00ACC1"));
                cvRm.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                cvDaF.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                cvDrinks.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                productList.clear();
                getProducts();
            }
        });

        btnRiceMeals.setOnClickListener(new View.OnClickListener() {
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
                category = "Rice Meals";
                cvBurger.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                cvPasta.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                cvChicken.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                cvFries.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                cvRm.setCardBackgroundColor(Color.parseColor("#00ACC1"));
                cvDaF.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                cvDrinks.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                productList.clear();
                getProducts();
            }
        });

        btnDaF.setOnClickListener(new View.OnClickListener() {
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
                category = "Desserts and Floats";
                cvBurger.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                cvPasta.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                cvChicken.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                cvFries.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                cvRm.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                cvDaF.setCardBackgroundColor(Color.parseColor("#00ACC1"));
                cvDrinks.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                productList.clear();
                getProducts();
            }
        });

        btnDrinks.setOnClickListener(new View.OnClickListener() {
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
                category = "Drinks";
                cvBurger.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                cvPasta.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                cvChicken.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                cvFries.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                cvRm.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                cvDaF.setCardBackgroundColor(Color.parseColor("#FB8C00"));
                cvDrinks.setCardBackgroundColor(Color.parseColor("#00ACC1"));
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
                        Toast.makeText(StoreTab.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
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

    private void getStore() {

        String url = "http://192.168.254.109/fadSystem/storeInfo.php?storeName=" + storeName;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                showJSONS1(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(StoreTab.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void showJSONS1(String response) {
        String store_name = "";
        int store_id = 0;
        String store_image = "";

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("store");
            JSONObject stores = result.getJSONObject(0);
            store_name = stores.getString("store_name");
            store_id = stores.getInt("id");
            store_image = stores.getString("store_image");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String imageUri = store_image;
        byte[] decodedString = Base64.decode(imageUri, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        ivStore.setImageBitmap(decodedByte);
        Log.i("tagconvertstr", "["+response+"]");

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

}