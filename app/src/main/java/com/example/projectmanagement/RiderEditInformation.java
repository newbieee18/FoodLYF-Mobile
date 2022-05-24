package com.example.projectmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RiderEditInformation extends AppCompatActivity {

    private TextView fullname, email, gender, birthdate;
    private final AppCompatActivity activity = RiderEditInformation.this;
    String phoneNumber;
    private Button btnSave;
    ImageView back, ivShowImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_edit_information);

        phoneNumber = getIntent().getExtras().getString("number");
        fullname = findViewById(R.id.fullName);
        email = findViewById(R.id.email);
        gender = findViewById(R.id.gender);
        birthdate = findViewById(R.id.birthdate);
        btnSave = findViewById(R.id.btnSave);
        back = findViewById(R.id.back);
        ivShowImage = findViewById(R.id.ivShowImage);

        getData();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent account = new Intent(getApplicationContext(), RiderAccount.class);
                account.putExtra("number", phoneNumber);
                startActivity(account);
                finish();

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String rider_name, rider_email, rider_gender, rider_birthdate, rider_phone;

                rider_phone = phoneNumber;
                rider_name = fullname.getText().toString();
                rider_email = email.getText().toString();
                rider_gender = gender.getText().toString();
                rider_birthdate = birthdate.getText().toString();

                if(!rider_name.equals("") && !rider_email.equals("") && !rider_gender.equals("") && !rider_birthdate.equals("")){
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] field = new String[5];
                            field[0] = "rider_phone";
                            field[1] = "rider_name";
                            field[2] = "rider_email";
                            field[3] = "rider_gender";
                            field[4] = "rider_birthdate";
                            //Creating array for data
                            String[] data = new String[5];
                            data[0] = rider_phone;
                            data[1] = rider_name;
                            data[2] = rider_email;
                            data[3] = rider_gender;
                            data[4] = rider_birthdate;
                            PutData putData = new PutData("http://192.168.254.109/fadSystem/update_rider.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if(result.equals("Record updated successfully")){
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                        Intent editInformation = new Intent(getApplicationContext(), RiderEditInformation.class);
                                        editInformation.putExtra("number", phoneNumber);
                                        startActivity(editInformation);
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
                        Toast.makeText(RiderEditInformation.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void showJSONS(String response) {

        String name = "";
        String rEmail = "";
        String rGender = "";
        String rBirthdate = "";
        String image = "";

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAYY);
            JSONObject collegeData = result.getJSONObject(0);
            name = collegeData.getString(Config.FULLNAME1);
            rEmail = collegeData.getString(Config.EMAIL1);
            rGender = collegeData.getString(Config.GENDER1);
            rBirthdate = collegeData.getString(Config.BIRTHDATE);
            image = collegeData.getString("display_picture");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        fullname.setText("" + name);
        email.setText("" + rEmail);
        gender.setText("" + rGender);
        birthdate.setText("" + rBirthdate);
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