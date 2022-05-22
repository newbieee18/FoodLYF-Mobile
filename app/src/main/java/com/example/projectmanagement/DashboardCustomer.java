package com.example.projectmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class DashboardCustomer extends AppCompatActivity {

    private final AppCompatActivity activity = DashboardCustomer.this;
    String phoneNumber;
    RecyclerView popularRestaurant;
    PopularRestaurantAdapter popularRestaurantAdapter;
    ArrayList<PopularRestaurant> popularRestaurantList;
    TextView header;

    float x1, x2, y1, y2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_customer);

        phoneNumber = getIntent().getExtras().getString("number");

        Intent productsAdapter = new Intent (getApplicationContext(), ProductsAdapter.class);
        productsAdapter.putExtra("number", phoneNumber);

        popularRestaurant = findViewById(R.id.popularRestaurant);
        popularRestaurantList = new ArrayList<>();
        getStore();

        header = findViewById(R.id.header);
        getData();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.dashboard);

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

    public void btnChicken(View view){

        if(view.getId() == R.id.imgChicken || view.getId() == R.id.txtChicken){
            Intent category =  new Intent(getApplicationContext(), Category.class);
            category.putExtra("number", phoneNumber);
            category.putExtra("category", "Chicken");
            startActivity(category);
        }

    }

    public void btnBurger(View view){

        if(view.getId() == R.id.txtBurger || view.getId() == R.id.imgBurger){
            Intent category =  new Intent(getApplicationContext(), Category.class);
            category.putExtra("number", phoneNumber);
            category.putExtra("category", "Burger");
            startActivity(category);
        }

    }

    public void btnPasta(View view){

        if(view.getId() == R.id.imgPasta || view.getId() == R.id.txtPasta){
            Intent category =  new Intent(getApplicationContext(), Category.class);
            category.putExtra("number", phoneNumber);
            category.putExtra("category", "Pasta");
            startActivity(category);
        }

    }

    public void btnDrinks(View view){

        if(view.getId() == R.id.txtDrinks || view.getId() == R.id.imgDrinks){
            Intent category =  new Intent(getApplicationContext(), Category.class);
            category.putExtra("number", phoneNumber);
            category.putExtra("category", "Drinks");
            startActivity(category);
        }

    }

    public void btnPizza(View view){

        if(view.getId() == R.id.imgPizza || view.getId() == R.id.txtPizza){
            Intent category =  new Intent(getApplicationContext(), Category.class);
            category.putExtra("number", phoneNumber);
            category.putExtra("category", "Pizza");
            startActivity(category);
        }

    }

    public void btnFries(View view){

        if(view.getId() == R.id.txtFries || view.getId() == R.id.imgFries){
            Intent category =  new Intent(getApplicationContext(), Category.class);
            category.putExtra("number", phoneNumber);
            category.putExtra("category", "Fries");
            startActivity(category);
        }

    }




    private void getData() {

        String phone = phoneNumber;
        if (phone.equals("")) {
            Toast.makeText(this, "Check Detail!", Toast.LENGTH_LONG).show();
            return;
        }

        String url = Config.DATA_URL + phone;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                showJSONS1(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DashboardCustomer.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void showJSONS1(String response) {
        String name = "";
        String email= "";

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
            JSONObject collegeData = result.getJSONObject(0);
            name = collegeData.getString(Config.FULLNAME);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        header.setText("Hello, " + name);

    }

    private void setPopularRestaurant(List<PopularRestaurant> popularRestaurantList){

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        popularRestaurant.setLayoutManager(layoutManager);
        popularRestaurantAdapter = new PopularRestaurantAdapter(this, popularRestaurantList);
        popularRestaurant.setAdapter(popularRestaurantAdapter);

    }

    private void getStore() {

        String url = Config.STORE_URL;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                showJSONS(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DashboardCustomer.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void showJSONS(String response) {
        String store_name = "";
        int store_id = 0;
        String store_image;

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY1);

            for(int i=0; i<result.length(); i++) {

                JSONObject stores = result.getJSONObject(i);
                store_name = stores.getString(Config.STORE_NAME);
                store_id = stores.getInt(Config.STORE_ID);
                store_image = stores.getString(Config.STORE_IMAGE);

                popularRestaurantList.add(new PopularRestaurant(store_image, store_name));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        setPopularRestaurant(popularRestaurantList);

    }


    public boolean onTouchEvent(MotionEvent touchEvent){
        switch(touchEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();
                if(x1 < x2){
                }else if(x1 > x2){
                Intent account = new Intent(getApplicationContext(), Account.class);
                account.putExtra("number", phoneNumber);
                startActivity(account);
                }
            break;
        }
        return false;
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