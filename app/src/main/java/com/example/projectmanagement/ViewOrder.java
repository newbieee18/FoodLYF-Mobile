package com.example.projectmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
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

public class ViewOrder extends AppCompatActivity {

    String phoneNumber, customerPhone, riderPhone;
    TextView total, cName, cPhone, suggestion, txtRiderName, txtGender, txtContactNumber;
    TableLayout tableLayout;
    Button processOrder;
    CardView cardView;

    @Override
    public void onBackPressed() {
        Intent orders = new Intent(getApplicationContext(), Orders.class);
        orders.putExtra("number", phoneNumber);
        startActivity(orders);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);

        phoneNumber = getIntent().getExtras().getString("number");
        customerPhone = getIntent().getExtras().getString("customerPhone");
        tableLayout = findViewById(R.id.tableLayout);
        total = findViewById(R.id.total);
        processOrder = findViewById(R.id.processOrder);
        cName = findViewById(R.id.customerName);
        cPhone = findViewById(R.id.customerPhone);
        suggestion = findViewById(R.id.suggestion);
        txtRiderName = findViewById(R.id.txtRiderName);
        txtGender = findViewById(R.id.txtGender);
        txtContactNumber =findViewById(R.id.txtContactNumber);
        getItems();

        if(processOrder.getText().equals("PROCESS ORDER")) {
            processOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String status = "Processing Order";
                    final String customer_phone = customerPhone;
                    final String branch_phone = phoneNumber;

                    if (!status.equals("")) {
                        Handler handler = new Handler();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                String[] field = new String[3];
                                field[0] = "status";
                                field[1] = "customer_phone";
                                field[2] = "branch_phone";

                                String[] data = new String[3];
                                data[0] = status;
                                data[1] = customer_phone;
                                data[2] = branch_phone;

                                PutData putData = new PutData("http://192.168.254.109/fadSystem/update_order.php", "POST", field, data);
                                if (putData.startPut()) {
                                    if (putData.onComplete()) {
                                        String result = putData.getResult();
                                        if (result.equals("Record updated successfully")) {
                                            processOrder.setText("PROCEED FOR PICKUP");
                                            if(processOrder.getText().equals("PROCEED FOR PICKUP")) {
                                                processOrder.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        final String status = "Order Pickup";
                                                        final String customer_phone = customerPhone;
                                                        final String branch_phone = phoneNumber;

                                                        if (!status.equals("")) {
                                                            Handler handler = new Handler();
                                                            handler.post(new Runnable() {
                                                                @Override
                                                                public void run() {

                                                                    String[] field = new String[3];
                                                                    field[0] = "status";
                                                                    field[1] = "customer_phone";
                                                                    field[2] = "branch_phone";

                                                                    String[] data = new String[3];
                                                                    data[0] = status;
                                                                    data[1] = customer_phone;
                                                                    data[2] = branch_phone;

                                                                    PutData putData = new PutData("http://192.168.254.109/fadSystem/update_order.php", "POST", field, data);
                                                                    if (putData.startPut()) {
                                                                        if (putData.onComplete()) {
                                                                            String result = putData.getResult();
                                                                            if (result.equals("Record updated successfully")) {
                                                                                processOrder.setText("NEXT ORDER");
                                                                                processOrder.setOnClickListener(new View.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(View v) {
                                                                                        Intent intent = new Intent(getApplicationContext(), Orders.class);
                                                                                        intent.putExtra("number", phoneNumber);
                                                                                        startActivity(intent);
                                                                                    }
                                                                                });

                                                                            } else {
                                                                                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            });
                                                        }

                                                    }
                                                });
                                            }
                                        } else {
                                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        });
                    }

                }
            });
        }

    }

    private void getItems() {

        String url = Config.ORDER_URL1 + customerPhone;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                showJSONS(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ViewOrder.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void showJSONS(String response) {
        int order_id = 0;
        int quantity = 0;
        String customer_name = "";
        String customer_phone = "";
        String product_name = "";
        String latitude = "";
        String longitude = "";
        String sug = "";
        String rider_phone = "";
        double subtotal = 0;
        double orderTotal = 0;

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY91);

            TableRow.LayoutParams textViewParam = new TableRow.LayoutParams
                    (0,
                            TableRow.LayoutParams.MATCH_PARENT,3f);

            TableRow.LayoutParams textViewParam1 = new TableRow.LayoutParams
                    (0,
                            TableRow.LayoutParams.MATCH_PARENT,1f);

            Typeface typeface = ResourcesCompat.getFont(ViewOrder.this,R.font.chakra_petch_medium);

            TableRow tbrow0 = new TableRow(this);
            tbrow0.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.FILL_PARENT,
                    TableLayout.LayoutParams.FILL_PARENT, 1.0f));
            TextView tv0 = new TextView(this);
            tv0.setText("List Of Orders");
            tv0.setGravity(Gravity.LEFT);
            tv0.setTextSize(18);
            tv0.setTypeface(typeface);
            tv0.setTextColor(Color.parseColor("#FF000000"));
            tv0.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            tv0.setLayoutParams(textViewParam);
            tbrow0.addView(tv0);
            TextView tv01 = new TextView(this);
            tv01.setText("Price");
            tv01.setGravity(Gravity.RIGHT);
            tv01.setTextSize(18);
            tv0.setTypeface(typeface);
            tv01.setTextColor(Color.parseColor("#FF000000"));
            tv01.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            tv01.setLayoutParams(textViewParam);
            tbrow0.addView(tv01);
            tableLayout.addView(tbrow0);

            for (int i = 0; i < result.length(); i++) {

                JSONObject cart = result.getJSONObject(i);
                product_name = cart.getString(Config.ORDER_PRODUCT_NAME1);
                order_id = cart.getInt(Config.ORDER_ID1);
                customer_name = cart.getString(Config.ORDER_CUSTOMER_NAME1);
                customer_phone = cart.getString(Config.ORDER_CUSTOMER_PHONE1);
                latitude = cart.getString(Config.ORDER_CUSTOMER_LATITUDE1);
                longitude = cart.getString(Config.ORDER_CUSTOMER_LONGITUDE1);
                quantity = cart.getInt(Config.ORDER_QUANTITY1);
                subtotal = cart.getDouble(Config.ORDER_SUBTOTAL1);
                sug = cart.getString(Config.ORDER_SUGGESTION);
                rider_phone = cart.getString(Config.ORDER_RIDER_PHONE);
                orderTotal += subtotal;


                TableRow tbRow = new TableRow(this);
                tbRow.setLayoutParams(new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.WRAP_CONTENT,
                        TableLayout.LayoutParams.WRAP_CONTENT, 1.0f));
                TextView tv1 = new TextView(this);
                tv1.setText("x" + quantity + " " + product_name);
                tv1.setTextSize(16);
                tv0.setTypeface(typeface);
                tv1.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                tv1.setLayoutParams(textViewParam);
                tbRow.addView(tv1);
                TextView tv2 = new TextView(this);
                String Subtotal = String.format("₱%.2f", subtotal);
                tv2.setText(Subtotal);
                tv2.setTextSize(16);
                tv0.setTypeface(typeface);
                tv2.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                tv2.setLayoutParams(textViewParam1);
                tbRow.addView(tv2);
                tableLayout.addView(tbRow);

                total.setText(String.format("Total: ₱%.2f", orderTotal));
                cName.setText("Customer Name: " + customer_name);
                cPhone.setText("Contact#: " + customer_phone);
                suggestion.setText("Suggestion: " + sug);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}