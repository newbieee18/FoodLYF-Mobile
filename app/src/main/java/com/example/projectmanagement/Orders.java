package com.example.projectmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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

public class Orders extends AppCompatActivity {

    String phoneNumber;
    List<OrderList> orderList;
    OrderListAdapter orderListAdapter;
    RecyclerView orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        phoneNumber = getIntent().getExtras().getString("number");

        orders = findViewById(R.id.orderList);
        orderList = new ArrayList<>();

        getOrders();


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.acceptOrder);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.dashboardOutlet:
                        Intent dashboardOutlet = new Intent(getApplicationContext(), DashboardOutlets.class);
                        dashboardOutlet.putExtra("number", phoneNumber);
                        startActivity(dashboardOutlet);
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_from_left);
                        return true;
                }
                switch (item.getItemId()){
                    case R.id.acceptOrder:
                        Intent orders = new Intent(getApplicationContext(), Orders.class);
                        orders.putExtra("number", phoneNumber);
                        startActivity(orders);
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_from_left);
                        return true;
                }
                switch (item.getItemId()){
                    case R.id.Account:
                        Intent account = new Intent(getApplicationContext(), OutletAccount.class);
                        account.putExtra("number", phoneNumber);
                        startActivity(account);
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_from_left);
                        return true;
                }
                return false;
            }
        });

    }

    private void setOrders(List<OrderList> orderList){

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        orders.setLayoutManager(layoutManager);
        orderListAdapter = new OrderListAdapter(this, orderList);
        orders.setAdapter(orderListAdapter);

    }

    private void getOrders() {

        String url = Config.ORDER_URL + phoneNumber;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                showJSONS(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Orders.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void showJSONS(String response) {
        Integer order_id;
        String product_name = "";
        String customer_phone = "";
        String customer_name = "";
        String customer_address = "";
        Integer quantity = 0;
        Integer subtotal = 0;
        String latitude = "";
        String longitude = "";
        String store_name = "";
        String branch_name = "";
        String branch_latitude = "";
        String branch_longitude = "";
        double total = 0;
        double distance = 0;

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY9);

            for(int i=0; i<result.length(); i++) {

                JSONObject orders = result.getJSONObject(i);
                product_name = orders.getString(Config.ORDER_PRODUCT_NAME);
                customer_phone = orders.getString(Config.ORDER_CUSTOMER_PHONE);
                customer_name = orders.getString(Config.ORDER_CUSTOMER_NAME);
                customer_address = orders.getString(Config.ORDER_CUSTOMER_ADDRESS);
                order_id = orders.getInt(Config.ORDER_ID);
                subtotal = orders.getInt(Config.ORDER_SUBTOTAL);
                quantity = orders.getInt(Config.ORDER_QUANTITY);
                total = orders.getDouble(Config.ORDER_TOTAL);
                latitude = orders.getString(Config.ORDER_CUSTOMER_LATITUDE);
                longitude = orders.getString(Config.ORDER_CUSTOMER_LONGITUDE);
                store_name = orders.getString(Config.ORDER_STORE_NAME);
                distance = orders.getDouble(Config.ORDER_DISTANCE);

                orderList.add(new OrderList(order_id, customer_phone, customer_name, customer_address, product_name, quantity, subtotal, latitude, longitude, store_name, branch_name, branch_latitude, branch_longitude, total, distance));


            }
            Log.i("tagconvertstr", "["+result+"]");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setOrders(orderList);


    }

}