package com.example.projectmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
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

public class StoreOutlets extends AppCompatActivity {

    private final AppCompatActivity activity = StoreOutlets.this;
    RecyclerView rvOutlets;
    OutletsAdapter outletsAdapter;
    List<Outlets> outletList;
    String phoneNumber, store;
    Button btnAddOutlet;

    @Override
    public void onBackPressed() {
        Intent dashboardStore = new Intent(getApplicationContext(), DashboardStore.class);
        dashboardStore.putExtra("number", phoneNumber);
        startActivity(dashboardStore);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_outlets);


        phoneNumber = getIntent().getExtras().getString("number");
        rvOutlets = findViewById(R.id.rvOutlets);
        outletList = new ArrayList<>();

        getOutlets();

        btnAddOutlet = findViewById(R.id.btnAddOutlet);
        btnAddOutlet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent addOutlet = new Intent(getApplicationContext(), AddOutlet.class);
                addOutlet.putExtra("number", phoneNumber);
                addOutlet.putExtra("store_name", store);
                startActivity(addOutlet);

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


    private void setOutlets(List<Outlets> outletList){

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvOutlets.setLayoutManager(layoutManager);
        outletsAdapter = new OutletsAdapter(this, outletList);
        rvOutlets.setAdapter(outletsAdapter);

    }

    private void getOutlets() {

        String url = Config.OUTLETS_URL + phoneNumber;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                showJSONS(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(StoreOutlets.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void showJSONS(String response) {
        int id = 0;
        String phone = "";
        String store_name = "";
        String branch_name = "";
        String latitude = "";
        String longitude = "";

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY6);

            for(int i=0; i<result.length(); i++) {

                JSONObject products = result.getJSONObject(i);
                phone = products.getString(Config.OUTLET_PHONE);
                store_name = products.getString(Config.OUTLET_SNAME);
                branch_name = products.getString(Config.OUTLET_BNAME);
                latitude = products.getString(Config.OUTLET_LATITUDE);
                longitude = products.getString(Config.OUTLET_LONGITUDE);
                id = products.getInt(Config.OUTLET_ID);

                outletList.add(new Outlets(id, phone, store_name, branch_name, latitude, longitude));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        setOutlets(outletList);
        store = store_name;

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