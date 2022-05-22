package com.example.projectmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OutletAccount extends AppCompatActivity {

    private final AppCompatActivity activity = OutletAccount.this;
    String phoneNumber;
    TextView StoreName, BranchName, Logout;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;

    float x1, x2, y1, y2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outlet_account);

        phoneNumber = getIntent().getExtras().getString("number");

        StoreName = findViewById(R.id.storeName);
        BranchName = findViewById(R.id.branchName);
        Logout = findViewById(R.id.logout);

        getData();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.Account);

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

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();
                Intent login = new Intent(getApplicationContext(), SplashScreen.class);
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(login);

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

    public void buttonClicked(View view) {

        if (view.getId() == R.id.editInfo) {
            Intent editInfo = new Intent(getApplicationContext(), EditInformation.class);
            editInfo.putExtra("number", phoneNumber);
            startActivity(editInfo);
        }

    }

    private void getData() {

        String phone = phoneNumber;
        if (phone.equals("")) {
            Toast.makeText(this, "Check Detail!", Toast.LENGTH_LONG).show();
            return;
        }

        String url = Config.OUTLETS_URL1 + phone;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                showJSONS(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(OutletAccount.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void showJSONS(String response) {
        String sname = "";
        String bname= "";

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY10);
            JSONObject collegeData = result.getJSONObject(0);
            sname = collegeData.getString(Config.OUTLET_SNAME1);
            bname = collegeData.getString(Config.OUTLET_BNAME1);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        StoreName.setText("" + sname);
        BranchName.setText("" + bname);

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
                    Intent dashboardOutlet = new Intent(getApplicationContext(), DashboardOutlets.class);
                    dashboardOutlet.putExtra("number", phoneNumber);
                    startActivity(dashboardOutlet);
                }else if(x1 > x2){
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