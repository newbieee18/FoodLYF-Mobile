package com.example.projectmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mapbox.mapboxsdk.Mapbox;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RiderJob extends AppCompatActivity {

    String phoneNumber;
    List<OrderList> orderList;
    JobListAdapter jobListAdapter;
    RecyclerView jobList;
    double lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_job);

        phoneNumber = getIntent().getExtras().getString("number");

        jobList = findViewById(R.id.jobList);
        orderList = new ArrayList<>();

        getJob();


        if (ActivityCompat.checkSelfPermission(RiderJob.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            gpsLoc(this);
        } else {
            ActivityCompat.requestPermissions(RiderJob.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }


        final String phone = phoneNumber;

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.acceptJob);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.dashboardRider:
                        Intent dashboardRider = new Intent(getApplicationContext(), DashboardDriver.class);
                        dashboardRider.putExtra("number", phoneNumber);
                        startActivity(dashboardRider);
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_from_left);
                        return true;
                }
                switch (item.getItemId()){
                    case R.id.acceptJob:
                        Intent riderJob = new Intent(getApplicationContext(), RiderJob.class);
                        riderJob.putExtra("number", phoneNumber);
                        startActivity(riderJob);
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_from_left);
                        return true;
                }
                switch (item.getItemId()){
                    case R.id.Account:
                        Intent account = new Intent(getApplicationContext(), RiderAccount.class);
                        account.putExtra("number", phoneNumber);
                        startActivity(account);
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_from_left);
                        return true;
                }
                return false;
            }
        });

    }

    @SuppressWarnings("ResourceType")
    public void gpsLoc(Context context) {
        android.location.LocationManager manager = (android.location.LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        if (manager != null) {
            for (String provider : manager.getAllProviders()) {
                Location location = manager.getLastKnownLocation(provider);
                if (location != null) {
                    lat = location.getLatitude();
                    lon = location.getLongitude();
                }
            }
        }
    }

    private void setOrders(List<OrderList> orderList){

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        jobList.setLayoutManager(layoutManager);
        jobListAdapter = new JobListAdapter(this, orderList);
        jobList.setAdapter(jobListAdapter);

    }

    private void getJob() {

        if (ActivityCompat.checkSelfPermission(RiderJob.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            gpsLoc(this);
        } else {
            ActivityCompat.requestPermissions(RiderJob.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

        final String rLatitude = String.valueOf(lat);
        final String rLongitude = String.valueOf(lon);

        String url = Config.ORDER_URL2 + phoneNumber + "&latitude=" + rLatitude + "&longitude=" + rLongitude;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                showJSONS(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RiderJob.this, error.getMessage(), Toast.LENGTH_LONG).show();
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
            JSONArray result = jsonObject.getJSONArray(Config.JOB_LIST);

            for(int i=0; i<result.length(); i++) {

                JSONObject jobs = result.getJSONObject(i);
                product_name = jobs.optString(Config.ORDER_PRODUCT_NAME2);
                customer_phone = jobs.optString(Config.ORDER_CUSTOMER_PHONE2);
                customer_name = jobs.optString(Config.ORDER_CUSTOMER_NAME2);
                customer_address = jobs.getString(Config.ORDER_CUSTOMER_ADDRESS);
                order_id = jobs.optInt(Config.ORDER_ID2);
                subtotal = jobs.optInt(Config.ORDER_SUBTOTAL2);
                quantity = jobs.optInt(Config.ORDER_QUANTITY2);
                total = jobs.optDouble(Config.ORDER_TOTAL2);
                latitude = jobs.optString(Config.ORDER_CUSTOMER_LATITUDE2);
                longitude = jobs.optString(Config.ORDER_CUSTOMER_LONGITUDE2);
                store_name = jobs.optString(Config.ORDER_STORE_NAME2);
                branch_name = jobs.optString(Config.ORDER_BRANCH_NAME2);
                branch_latitude = jobs.optString(Config.ORDER_BRANCH_LATITUDE2);
                branch_longitude = jobs.optString(Config.ORDER_BRANCH_LONGITUDE2);
                distance = jobs.optDouble(Config.ORDER_DISTANCE2);

                orderList.add(new OrderList(order_id, customer_phone, customer_name, customer_address, product_name, quantity, subtotal, latitude, longitude, store_name, branch_name, branch_latitude, branch_longitude, total, distance));


            }
            //Log.e("tagconvertstr", "["+result+"]");
        } catch (JSONException e) {
            e.printStackTrace();

        }

        setOrders(orderList);


    }


}