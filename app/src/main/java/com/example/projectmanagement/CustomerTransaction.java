package com.example.projectmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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

public class CustomerTransaction extends AppCompatActivity {

    String phoneNumber;
    List<OrderList> deliveredList;
    DeliveredOrdersAdapter deliveredOrdersAdapter;
    RecyclerView deliveredOrders;
    TextView totalOrders, mTotal;

    @Override
    public void onBackPressed() {
        Intent account = new Intent(getApplicationContext(), Account.class);
        account.putExtra("number", phoneNumber);
        startActivity(account);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_transaction);

        phoneNumber = getIntent().getExtras().getString("number");

        deliveredOrders = findViewById(R.id.deliveredOrders);
        deliveredList = new ArrayList<>();

        totalOrders = findViewById(R.id.totalOrders);
        mTotal = findViewById(R.id.total);

        getOrders();


    }

    private void setOrders(List<OrderList> orderList){

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        deliveredOrders.setLayoutManager(layoutManager);
        deliveredOrdersAdapter = new DeliveredOrdersAdapter(this, orderList);
        deliveredOrders.setAdapter(deliveredOrdersAdapter);

    }

    private void getOrders() {

        String url = "http://192.168.254.109/fadSystem/deliveredOrders.php?phone=" + phoneNumber;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                showJSONS(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CustomerTransaction.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void showJSONS(String response) {
        Integer order_id = 0;
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
        int tOrders = 0;
        double fTotal = 0;

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");


            for(int i=0; i<result.length(); i++) {

                JSONObject orders = result.getJSONObject(i);
                product_name = orders.getString("product_name");
                subtotal = orders.getInt("subtotal");
                quantity = orders.getInt("quantity");
                total = orders.getDouble("total");

                deliveredList.add(new OrderList(order_id, customer_phone, customer_name, customer_address, product_name, quantity, subtotal, latitude, longitude, store_name, branch_name, branch_latitude, branch_longitude, total, distance));

                tOrders = result.length();
                fTotal += subtotal;
            }
            Log.i("tagconvertstr", "["+result+"]");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setOrders(deliveredList);



        if(String.valueOf(tOrders).isEmpty() && String.valueOf(total).isEmpty()){
            totalOrders.setText("Total Orders");
            mTotal.setText("Total");
        }
        else{
            totalOrders.setText("Total Orders\n" + String.valueOf(tOrders));
            mTotal.setText(String.format("Total\n₱%.2f", fTotal));
        }

    }
}