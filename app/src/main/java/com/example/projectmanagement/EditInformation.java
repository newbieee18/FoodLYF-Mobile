package com.example.projectmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class EditInformation extends AppCompatActivity {

    private final AppCompatActivity activity = EditInformation.this;
    ImageView back;
    String phoneNumber;
    TextView FullName, Email, Address, Id;
    Button save;
    ImageView ivShowImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_information);

        FullName = findViewById(R.id.fullName);
        Email = findViewById(R.id.email);
        Address = findViewById(R.id.address);
        Id = findViewById(R.id.id);
        ivShowImage = findViewById(R.id.ivShowImage);

        phoneNumber = getIntent().getExtras().getString("number");

        getData();

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent account = new Intent(getApplicationContext(), Account.class);
                account.putExtra("number", phoneNumber);
                startActivity(account);
                finish();

            }
        });

        save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String phone, fullname, email, address, id;

                fullname = String.valueOf(FullName.getText());
                email = String.valueOf(Email.getText());
                address = String.valueOf(Address.getText());
                id = String.valueOf(Id.getText());

                if(!id.equals("") && !fullname.equals("") && !email.equals("") && !address.equals("")) {
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[4];
                            field[0] = "id";
                            field[1] = "fullname";
                            field[2] = "email";
                            field[3] = "address";
                            //Creating array for data
                            String[] data = new String[4];
                            data[0] = id;
                            data[1] = fullname;
                            data[2] = email;
                            data[3] = address;
                            PutData putData = new PutData("http://192.168.254.109/fadSystem/update_user.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if(result.equals("Record updated successfully")){
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                        Intent editInformation = new Intent(getApplicationContext(), EditInformation.class);
                                        editInformation.putExtra("number", phoneNumber);
                                        startActivity(editInformation);
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            //End Write and Read data with URL
                        }

                    });
                }
                else{
                    Toast.makeText(getApplicationContext(), "All fields required", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(EditInformation.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void showJSONS(String response) {

        String username1 = "";
        String name = "";
        String email = "";
        String address = "";
        String id = "";
        String image = "";

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
            JSONObject collegeData = result.getJSONObject(0);
            name = collegeData.getString(Config.FULLNAME);
            email = collegeData.getString(Config.EMAIL);
            address = collegeData.getString(Config.ADDRESS);
            id = collegeData.getString(Config.ID);
            image = collegeData.getString(Config.IMG);
            username1 = collegeData.getString(Config.PHONE);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        FullName.setText("" + name);
        Email.setText("" + email);
        Address.setText("" + address);
        Id.setText(id);

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