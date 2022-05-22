package com.example.projectmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kosalgeek.android.photoutil.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Payment extends AppCompatActivity implements LocationListener {


    TextView productName, total, subTotal, deliveryFee, txtAddress;
    String phoneNumber, storeName, finalTotal, sTotal, sName, fullName, email, address, id;
    double latitude, longitude;
    double fee = 50;
    List<CartList> cartList;
    TableLayout tableLayout;
    Button placeOrder, btnEdit, btnClose, btnSaveAddress;
    EditText suggestion, customerAddress;
    protected LocationManager mLocationManager;
    int LOCATION_REFRESH_TIME = 15000; // 15 seconds to update
    int LOCATION_REFRESH_DISTANCE = 500; // 500 meters to update
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    @Override
    public void onBackPressed() {
        Intent cart = new Intent(getApplicationContext(), Cart.class);
        cart.putExtra("number", phoneNumber);
        cart.putExtra("storeName", storeName);
        startActivity(cart);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        phoneNumber = getIntent().getExtras().getString("number");
        storeName = getIntent().getExtras().getString("storeName");

        if (storeName == null) sName = "none";
        else sName = getIntent().getExtras().getString("storeName");

        txtAddress = findViewById(R.id.txtAddress);
        productName = findViewById(R.id.productName);
        tableLayout = findViewById(R.id.tableLayout);
        subTotal = findViewById(R.id.subtotal);
        deliveryFee = findViewById(R.id.deliveryFee);
        total = findViewById(R.id.total);
        suggestion = findViewById(R.id.suggestion);

        cartList = new ArrayList<>();
        getItems();
        getCustomerDetails();
        if (ContextCompat.checkSelfPermission(Payment.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Payment.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }


        placeOrder = findViewById(R.id.placeOrder);
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getLocation();


            }
        });

        btnEdit = findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAddress();
            }
        });



//
//        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
//                LOCATION_REFRESH_DISTANCE, mLocationListener);

    }

    private void getLocation() {

        try {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, Payment.this);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

//    private final LocationListener mLocationListener = new LocationListener() {
//        @Override
//        public void onLocationChanged(final Location location) {
//            latitude = location.getLatitude();
//            longitude = location.getLongitude();
//        }
//    };

    private void getItems() {

        String url = Config.CART_URL + phoneNumber;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                showJSONS(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Payment.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void showJSONS(String response) {
        int cart_id = 0;
        int quantity = 0;
        double product_price = 0;
        String product_name = "";
        String product_image = "";
        double cartTotal = 0;
        double subtotal = 0;

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY8);

            TableRow.LayoutParams textViewParam = new TableRow.LayoutParams
                    (0,
                            TableRow.LayoutParams.MATCH_PARENT,3f);

            TableRow.LayoutParams textViewParam1 = new TableRow.LayoutParams
                    (0,
                            TableRow.LayoutParams.MATCH_PARENT,1f);

            Typeface typeface = ResourcesCompat.getFont(Payment.this,R.font.chakra_petch_medium);

            TableRow tbrow0 = new TableRow(this);
            TextView tv0 = new TextView(this);
            tv0.setText("Items");
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
            tv01.setTypeface(typeface);
            tv01.setTextColor(Color.parseColor("#FF000000"));
            tv01.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            tv01.setLayoutParams(textViewParam1);
            tbrow0.addView(tv01);
            tableLayout.addView(tbrow0);

            for (int i = 0; i < result.length(); i++) {

                JSONObject cart = result.getJSONObject(i);
                product_name = cart.getString(Config.CART_PRODUCT_NAME);
                cart_id = cart.getInt(Config.CART_PRODUCT_ID);
                product_price = cart.getDouble(Config.CART_PRODUCT_PRICE);
                product_image = cart.getString(Config.CART_PRODUCT_IMAGE);
                quantity = cart.getInt(Config.CART_PRODUCT_QUANTITY);
                subtotal = cart.getDouble(Config.CART_PRODUCT_SUBTOTAL);
                cartTotal += subtotal;


                TableRow tbRow = new TableRow(this);
                tbRow.setBaselineAligned(false);
                TextView tv1 = new TextView(this);
                tv1.setText("x" + quantity + " " + product_name);
                tv1.setTextSize(16);
                tv1.setTypeface(typeface);
                tv1.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                tv1.setIncludeFontPadding(false);
                tv1.setLayoutParams(textViewParam);
                tbRow.addView(tv1);
                TextView tv2 = new TextView(this);
                String Subtotal = String.format("₱%.2f", subtotal);
                tv2.setText(Subtotal);
                tv2.setTextSize(16);
                tv2.setTypeface(typeface);
                tv2.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                tv2.setIncludeFontPadding(false);
                tv2.setLayoutParams(textViewParam1);
                tbRow.addView(tv2);
                tableLayout.addView(tbRow);



            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        double Total = cartTotal + fee;

        subTotal.setText(String.format("₱%.2f", cartTotal));
        finalTotal = String.format("%.2f", Total);
        deliveryFee.setText(String.format("₱%.2f", fee));
        total.setText(String.format("₱%.2f", Total));

    }

    private void getCustomerDetails() {

        String url = Config.DATA_URL + phoneNumber;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                showJSONS1(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Payment.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void showJSONS1(String response) {
        String customer_address = "";
        String customer_name = "";
        String customer_email = "";
        String customer_id = "";

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);

            for(int i=0; i<result.length(); i++) {

                JSONObject customer = result.getJSONObject(i);
                customer_address = customer.getString(Config.ADDRESS);
                customer_name = customer.getString(Config.FULLNAME);
                customer_email = customer.getString(Config.EMAIL);
                customer_address = customer.getString(Config.ADDRESS);
                customer_id = customer.getString(Config.ID);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        txtAddress.setText(customer_address);
        fullName = customer_name;
        email = customer_email;
        address = customer_address;
        id = customer_id;

    }

    public void editAddress(){

        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_from_left);

        dialogBuilder = new AlertDialog.Builder(this);
        final View popupWindow = getLayoutInflater().inflate(R.layout.fadepopup, null);
        customerAddress = (EditText) popupWindow.findViewById(R.id.customerAddress);
        btnClose = (Button) popupWindow.findViewById(R.id.btnClose);
        btnSaveAddress = (Button) popupWindow.findViewById(R.id.btnSaveAddress);

        customerAddress.setText(address);

        dialogBuilder.setView(popupWindow);
        dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSaveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String cAddress, cName, cEmail, cId;
                cAddress = customerAddress.getText().toString();
                cName = fullName;
                cEmail = email;
                cId = id;

                if(!cAddress.equals("") && !cName.equals("") && !cEmail.equals("") && !cId.equals("")){
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            String[] field = new String[4];
                            field[0] = "fullname";
                            field[1] = "email";
                            field[2] = "address";
                            field[3] = "id";

                            String[] data = new String[4];
                            data[0] = cName;
                            data[1] = cEmail;
                            data[2] = cAddress;
                            data[3] = cId;

                            PutData putData = new PutData("http://192.168.254.109/fadSystem/update_user.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if(result.equals("Record updated successfully")){
                                        View layout = getLayoutInflater().inflate(R.layout.custom_toast, null);
                                        TextView text = (TextView) layout.findViewById(R.id.text);
                                        text.setText("Address has been Updated");

                                        Toast toast = new Toast(getApplicationContext());
                                        toast.setGravity(Gravity.BOTTOM, 0, 300);
                                        toast.setDuration(Toast.LENGTH_LONG);
                                        toast.setView(layout);
                                        toast.show();

                                        Intent payment = getIntent();
                                        payment.putExtra("number", phoneNumber);
                                        payment.putExtra("storeName", storeName);
                                        dialog.dismiss();
                                        finish();
                                        startActivity(payment);

                                    }
                                    else{
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

    @Override
    public void onLocationChanged(@NonNull Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        final String phone, store_name, cLatitude, cLongitude, sug, cAddress, dFee, finaltotal;
        finaltotal = finalTotal;
        dFee = String.valueOf(fee);
        phone = phoneNumber;
        store_name = sName;
        cAddress = txtAddress.getText().toString();
        sug = suggestion.getText().toString();

        cLatitude = String.valueOf(latitude);
        cLongitude = String.valueOf(longitude);

        if (!phone.equals("") && !cAddress.equals("") && !store_name.equals("") && !cLatitude.equals("") && !cLongitude.equals("") && (!sug.equals("") || sug.equals("")) && !dFee.equals("") && !finaltotal.equals("")) {
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {

                    String[] field = new String[8];
                    field[0] = "phone";
                    field[1] = "customer_address";
                    field[2] = "store_name";
                    field[3] = "latitude";
                    field[4] = "longitude";
                    field[5] = "suggestion";
                    field[6] = "delivery_fee";
                    field[7] = "total";

                    String[] data = new String[8];
                    data[0] = phone;
                    data[1] = cAddress;
                    data[2] = store_name;
                    data[3] = cLatitude;
                    data[4] = cLongitude;
                    data[5] = sug;
                    data[6] = dFee;
                    data[7] = finaltotal;

                    PutData putData = new PutData("http://192.168.254.109/fadSystem/insert_orders.php", "POST", field, data);
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            String result = putData.getResult();
                            if (result.equals("")) {
                                final Loading loading = new Loading(Payment.this);
                                loading.startLoading();

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        loading.DismissLoading();
                                    }
                                }, 5000);
                                Intent orderPlacement = new Intent(getApplicationContext(), OrderPlacement.class);
                                orderPlacement.putExtra("number", phoneNumber);
                                startActivity(orderPlacement);
                            } else {
                                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}