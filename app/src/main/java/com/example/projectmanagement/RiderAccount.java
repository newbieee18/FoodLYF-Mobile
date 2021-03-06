package com.example.projectmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
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

public class RiderAccount extends AppCompatActivity {

    private final AppCompatActivity activity = RiderAccount.this;
    String phoneNumber, online;
    TextView FullName, Email, Logout;
    ImageView ivShowImage;

    float x1, x2, y1, y2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_account);

        phoneNumber = getIntent().getExtras().getString("number");
        online = getIntent().getExtras().getString("online");

        FullName = findViewById(R.id.fullname);
        Email = findViewById(R.id.email);
        Logout = findViewById(R.id.logout);
        ivShowImage = findViewById(R.id.ivShowImage);

        getData();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.Account);

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

        if (view.getId() == R.id.btnEditInfo) {
            Intent editInfo = new Intent(getApplicationContext(), RiderEditInformation.class);
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

        String url = Config.DATA_URL1 + phone;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                showJSONS(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RiderAccount.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void showJSONS(String response) {
        String name = "";
        String email= "";
        String image = "";

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAYY);
            JSONObject collegeData = result.getJSONObject(0);
            name = collegeData.getString(Config.FULLNAME1);
            email = collegeData.getString(Config.EMAIL1);
            image = collegeData.getString("display_picture");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        FullName.setText("" + name);
        Email.setText("" + email);
        if(image.equals("")){
            ivShowImage.setImageResource(R.drawable.rider_image);
        }
        else{
            String imageUri = image;
            byte[] decodedString = Base64.decode(imageUri, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ivShowImage.setImageBitmap(decodedByte);
        }

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
                    Intent dashboardDriver = new Intent(getApplicationContext(), DashboardDriver.class);
                    dashboardDriver.putExtra("number", phoneNumber);
                    startActivity(dashboardDriver);
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