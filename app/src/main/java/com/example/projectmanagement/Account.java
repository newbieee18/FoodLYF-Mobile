package com.example.projectmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Button;
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

public class Account extends AppCompatActivity {

    private final AppCompatActivity activity = Account.this;
    String phoneNumber;
    TextView FullName, Email, Logout;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    ImageView ivShowImage;
    Button btnEditInfo;

    float x1, x2, y1, y2;

    @Override
    public void onBackPressed() {
        Intent account = new Intent(getApplicationContext(), Account.class);
        account.putExtra("number", phoneNumber);
        startActivity(account);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        phoneNumber = getIntent().getExtras().getString("number");

        FullName = findViewById(R.id.fullname);
        Email = findViewById(R.id.email);
        ivShowImage = findViewById(R.id.ivShowImage);
        Logout = findViewById(R.id.logout);
        btnEditInfo = findViewById(R.id.btnEditInfo);

        getData();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.Account);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.dashboard:
                        Intent dashboard = new Intent(getApplicationContext(), DashboardCustomer.class);
                        dashboard.putExtra("number", phoneNumber);
                        startActivity(dashboard);
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_from_left);
                        return true;
                }
                switch (item.getItemId()) {
                    case R.id.nav_cart:
                        Intent cart = new Intent(getApplicationContext(), Cart.class);
                        cart.putExtra("number", phoneNumber);
                        startActivity(cart);
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_from_left);
                        return true;
                }
                switch (item.getItemId()) {
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

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        btnEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editInfo = new Intent(getApplicationContext(), EditInformation.class);
                editInfo.putExtra("number", phoneNumber);
                startActivity(editInfo);
                finish();
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

    public void TransactionClicked(View view) {
        Intent trackOrder = new Intent(getApplicationContext(), CustomerTransaction.class);
        trackOrder.putExtra("number",phoneNumber);
        startActivity(trackOrder);
        finish();
    }

    public void TrackClicked(View view) {
        Intent trackOrder = new Intent(getApplicationContext(), TrackOrder.class);
        trackOrder.putExtra("number",phoneNumber);
        startActivity(trackOrder);
        finish();
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

                showJSONS(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Account.this, error.getMessage(), Toast.LENGTH_LONG).show();
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
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
            JSONObject collegeData = result.getJSONObject(0);
            name = collegeData.getString(Config.FULLNAME);
            email = collegeData.getString(Config.EMAIL);
            image = collegeData.getString(Config.IMG);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        FullName.setText("" + name);
        Email.setText("" + email);

        if(image.equals("")){
            ivShowImage.setImageResource(R.drawable.ic_username);
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
                    Intent dashboardCustomer = new Intent(getApplicationContext(), DashboardCustomer.class);
                    dashboardCustomer.putExtra("number", phoneNumber);
                    startActivity(dashboardCustomer);
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