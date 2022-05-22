package com.example.projectmanagement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

public class EditOutlet extends AppCompatActivity {

    private final AppCompatActivity activity = EditOutlet.this;
    Button btnUpdateOutlet;
    TextView id;
    EditText outletPhone, outletBranchName, outletLatitude, outletLongitude;
    String phoneNumber, outletID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_outlet);

        btnUpdateOutlet = findViewById(R.id.btnUpdateOutlet);
        id = findViewById(R.id.outletID);
        outletPhone = findViewById(R.id.outletPhone);
        outletBranchName = findViewById(R.id.outletBranchName);
        outletLatitude = findViewById(R.id.outletLatitude);
        outletLongitude = findViewById(R.id.outletLongitude);

        phoneNumber = getIntent().getExtras().getString("number");
        outletID = getIntent().getExtras().getString("outletID");

        id.setText(outletID);

        getOutlet();

        btnUpdateOutlet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String phone, branch_name, latitude, longitude;

                phone = outletPhone.getText().toString();
                branch_name = outletBranchName.getText().toString();
                latitude = outletLatitude.getText().toString();
                longitude = outletLongitude.getText().toString();

                if(phone.equals("")) outletPhone.setError("This is Required!");
                if(branch_name.equals("")) outletBranchName.setError("This is Required!");
                if(latitude.equals("")) outletLatitude.setError("This is Required!");
                if(longitude.equals("")) outletLongitude.setError("This is Required!");

                if(!phone.equals("") && !branch_name.equals("") && !latitude.equals("") && !longitude.equals("")){
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            String[] field = new String[5];
                            field[0] = "phone";
                            field[1] = "branch_name";
                            field[2] = "latitude";
                            field[3] = "longitude";
                            field[4] = "id";

                            String[] data = new String[5];
                            data[0] = phone;
                            data[1] = branch_name;
                            data[2] = latitude;
                            data[3] = longitude;
                            data[4] = outletID;

                            PutData putData = new PutData("http://192.168.254.109/fadSystem/update_outlet.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if(result.equals("Record updated successfully")){
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                        Intent storeOutlets = new Intent(getApplicationContext(), StoreOutlets.class);
                                        storeOutlets.putExtra("number", phoneNumber);
                                        startActivity(storeOutlets);
                                        finish();
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

    private void getOutlet() {

        String url = Config.EDIT_OUTLET_URL + id.getText().toString();
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                showJSONS(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditOutlet.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void showJSONS(String response) {
        String phone = "";
        String branch_name = "";
        String latitude = "";
        String longitude = "";

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY7);
            JSONObject collegeData = result.getJSONObject(0);
            phone = collegeData.getString(Config.EDIT_OUTLET_PHONE);
            branch_name = collegeData.getString(Config.EDIT_OUTLET_BNAME);
            latitude = collegeData.getString(Config.EDIT_OUTLET_LATITUDE);
            longitude = collegeData.getString(Config.EDIT_OUTLET_LONGITUDE);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        outletPhone.setText(phone);
        outletBranchName.setText(branch_name);
        outletLatitude.setText(latitude);
        outletLongitude.setText(longitude);

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