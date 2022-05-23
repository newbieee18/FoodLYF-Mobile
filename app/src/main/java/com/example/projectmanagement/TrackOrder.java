package com.example.projectmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class TrackOrder extends AppCompatActivity {

    String mStatus, phoneNumber;
    View vLine1, vLine2, vLine3, vLine4, vLine5, vOrderConfirmed, vProcessingOrder, vOrderPickup, vForDelivery, vOrderPlaced;
    ImageView ivOrderConfirmed, ivProcessingOrder, ivOrderPickup, ivForDelivery, ivQR, ivNoOrder, ivOrderPlaced;
    TextView txtOrderConfirmed, txtOrderConfirmedDesc, txtProcessingOrder, txtProcessingOrderDesc, txtOrderPickup,
            txtOrderPickupDesc, txtForDelivery, txtForDeliveryDesc, txtOrderNumber, txtStatus, txtOrderPlaced, txtOrderPlacedDesc;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);

        phoneNumber = getIntent().getExtras().getString("number");
        vLine1 = findViewById(R.id.vLine1);
        vLine2 = findViewById(R.id.vLine2);
        vLine3 = findViewById(R.id.vLine3);
        vLine4 = findViewById(R.id.vLine4);
        ivOrderConfirmed = findViewById(R.id.ivOrderConfirmed);
        ivProcessingOrder = findViewById(R.id.ivProcessingOrder);
        ivOrderPickup = findViewById(R.id.ivOrderPickup);
        ivForDelivery = findViewById(R.id.ivForDelivery);
        txtOrderConfirmed = findViewById(R.id.txtOrderConfirmed);
        txtOrderConfirmedDesc = findViewById(R.id.txtOrderConfirmedDesc);
        txtProcessingOrder = findViewById(R.id.txtProcessingOrder);
        txtProcessingOrderDesc = findViewById(R.id.txtProcessingOrderDesc);
        txtOrderPickup = findViewById(R.id.txtOrderPickup);
        txtOrderPickupDesc = findViewById(R.id.txtOrderPickupDesc);
        txtForDelivery = findViewById(R.id.txtForDelivery);
        txtForDeliveryDesc = findViewById(R.id.txtForDeliveryDesc);
        txtStatus = findViewById(R.id.txtStatus);
        txtOrderNumber = findViewById(R.id.txtOrderNumber);
        vOrderConfirmed = findViewById(R.id.vOrderConfirmed);
        vProcessingOrder = findViewById(R.id.vProcessingOrder);
        vOrderPickup = findViewById(R.id.vOrderPickup);
        vForDelivery = findViewById(R.id.vForDelivery);
        ivQR = findViewById(R.id.ivQR);
        ivNoOrder = findViewById(R.id.ivNoOrder);
        ivOrderPlaced = findViewById(R.id.ivOrderPlaced);
        txtOrderPlaced = findViewById(R.id.txtOrderPlaced);
        txtOrderPlacedDesc = findViewById(R.id.txtOrderPlacedDesc);
        vOrderPlaced = findViewById(R.id.vOrderPlaced);

        getItems();

        swipeRefreshLayout = findViewById(R.id.swipelayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                swipeRefreshLayout.setRefreshing(false);
                finish();
                overridePendingTransition( 0, 0);
                getIntent().addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(getIntent());
                overridePendingTransition( 0, 0);

            }
        });

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                overridePendingTransition( 0, 0);
//                getIntent().addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                startActivity(getIntent());
//                finish();
//                overridePendingTransition( 0, 0);
//            }
//        },60000);

    }

    public void getItems() {

        String url = Config.ORDER_URL1 + phoneNumber;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                showJSONS(response);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(TrackOrder.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void showJSONS(String response) {
        String status = "";
        String id = "";

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY91);

            for (int i = 0; i < result.length(); i++) {

                JSONObject orders = result.getJSONObject(i);
                status = orders.getString(Config.ORDER_STATUS);
                id = orders.getString(Config.ORDER_ID);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        mStatus = status;
        txtOrderNumber.setText("ORDER ID\n#" + id);
        txtStatus.setText("ORDER STATUS\n" + mStatus);

        if(mStatus.equals("Accepted")){
            vLine1.setAlpha(1);
            vOrderConfirmed.setAlpha(1);
            ivOrderConfirmed.setAlpha(1f);
            txtOrderConfirmed.setAlpha(1);
            txtOrderConfirmedDesc.setAlpha(1);
        }
        if(mStatus.equals("Processing Order")){
            vLine1.setAlpha(1);
            vOrderConfirmed.setAlpha(1);
            ivOrderConfirmed.setAlpha(1f);
            txtOrderConfirmed.setAlpha(1);
            txtOrderConfirmedDesc.setAlpha(1);
            vLine2.setAlpha(1);
            vProcessingOrder.setAlpha(1);
            ivProcessingOrder.setAlpha(1f);
            txtProcessingOrder.setAlpha(1);
            txtProcessingOrderDesc.setAlpha(1);
        }
        if(mStatus.equals("Order Pickup")){
            vLine1.setAlpha(1);
            vOrderConfirmed.setAlpha(1);
            ivOrderConfirmed.setAlpha(1f);
            txtOrderConfirmed.setAlpha(1);
            txtOrderConfirmedDesc.setAlpha(1);
            vLine2.setAlpha(1);
            vProcessingOrder.setAlpha(1);
            ivProcessingOrder.setAlpha(1f);
            txtProcessingOrder.setAlpha(1);
            txtProcessingOrderDesc.setAlpha(1);
            vLine3.setAlpha(1);
            vOrderPickup.setAlpha(1);
            ivOrderPickup.setAlpha(1f);
            txtOrderPickup.setAlpha(1);
            txtOrderPickupDesc.setAlpha(1);
        }
        if(mStatus.equals("For Delivery")){
            vLine1.setAlpha(1);
            vOrderConfirmed.setAlpha(1);
            ivOrderConfirmed.setAlpha(1f);
            txtOrderConfirmed.setAlpha(1);
            txtOrderConfirmedDesc.setAlpha(1);
            vLine2.setAlpha(1);
            vProcessingOrder.setAlpha(1);
            ivProcessingOrder.setAlpha(1f);
            txtProcessingOrder.setAlpha(1);
            txtProcessingOrderDesc.setAlpha(1);
            vLine3.setAlpha(1);
            vOrderPickup.setAlpha(1);
            ivOrderPickup.setAlpha(1f);
            txtOrderPickup.setAlpha(1);
            txtOrderPickupDesc.setAlpha(1);
            vLine4.setAlpha(1);
            vForDelivery.setAlpha(1);
            ivForDelivery.setAlpha(1f);
            txtForDelivery.setAlpha(1);
            txtForDeliveryDesc.setAlpha(1);

        }
        if(mStatus.isEmpty()){
            vLine1.getAlpha();
            vOrderConfirmed.getAlpha();
            ivOrderConfirmed.getAlpha();
            txtOrderConfirmed.getAlpha();
            txtOrderConfirmedDesc.getAlpha();
            vLine2.getAlpha();
            vProcessingOrder.getAlpha();
            ivProcessingOrder.getAlpha();
            txtProcessingOrder.getAlpha();
            txtProcessingOrderDesc.getAlpha();
            vLine3.getAlpha();
            vOrderPickup.getAlpha();
            ivOrderPickup.getAlpha();
            txtOrderPickup.getAlpha();
            txtOrderPickupDesc.getAlpha();
            vLine4.getAlpha();
            vForDelivery.getAlpha();
            ivForDelivery.getAlpha();
            txtForDelivery.getAlpha();
            txtForDeliveryDesc.getAlpha();
        }

        MultiFormatWriter writer = new MultiFormatWriter();
        if(!id.isEmpty()) {
            try {
                BitMatrix matrix = writer.encode(id, BarcodeFormat.QR_CODE, 350, 350);
                BarcodeEncoder encoder = new BarcodeEncoder();
                Bitmap bitmap = encoder.createBitmap(matrix);
                ivQR.setImageBitmap(bitmap);
                ivNoOrder.setVisibility(View.INVISIBLE);

            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
        if(id.isEmpty()){
            ivQR.setVisibility(View.INVISIBLE);
            ivNoOrder.setVisibility(View.VISIBLE);
            vLine1.setVisibility(View.INVISIBLE);
            vOrderConfirmed.setVisibility(View.INVISIBLE);
            ivOrderConfirmed.setVisibility(View.INVISIBLE);
            txtOrderConfirmed.setVisibility(View.INVISIBLE);
            txtOrderConfirmedDesc.setVisibility(View.INVISIBLE);
            vLine2.setVisibility(View.INVISIBLE);
            vProcessingOrder.setVisibility(View.INVISIBLE);
            ivProcessingOrder.setVisibility(View.INVISIBLE);
            txtProcessingOrder.setVisibility(View.INVISIBLE);
            txtProcessingOrderDesc.setVisibility(View.INVISIBLE);
            vLine3.setVisibility(View.INVISIBLE);
            vOrderPickup.setVisibility(View.INVISIBLE);
            ivOrderPickup.setVisibility(View.INVISIBLE);
            txtOrderPickup.setVisibility(View.INVISIBLE);
            txtOrderPickupDesc.setVisibility(View.INVISIBLE);
            vLine4.setVisibility(View.INVISIBLE);
            vForDelivery.setVisibility(View.INVISIBLE);
            ivForDelivery.setVisibility(View.INVISIBLE);
            txtForDelivery.setVisibility(View.INVISIBLE);
            txtForDeliveryDesc.setVisibility(View.INVISIBLE);
            ivOrderPlaced.setVisibility(View.INVISIBLE);
            vOrderPlaced.setVisibility(View.INVISIBLE);
            txtOrderPlacedDesc.setVisibility(View.INVISIBLE);
            txtOrderPlaced.setVisibility(View.INVISIBLE);

        }

    }

}