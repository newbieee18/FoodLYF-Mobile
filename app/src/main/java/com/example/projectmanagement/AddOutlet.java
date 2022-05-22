package com.example.projectmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddOutlet extends AppCompatActivity {

    private final AppCompatActivity activity = AddOutlet.this;
    Button btnAddOutlet;
    EditText outletPhone, outletBranchName, outletLongitude, outletLatitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_outlet);

        String phoneNumber = getIntent().getExtras().getString("number");
        btnAddOutlet = findViewById(R.id.btnAddOutlet);
        outletPhone = findViewById(R.id.outletPhone);
        outletBranchName = findViewById(R.id.outletBranchName);
        outletLatitude = findViewById(R.id.outletLatitude);
        outletLongitude = findViewById(R.id.outletLongitude);

        btnAddOutlet.setOnClickListener(new View.OnClickListener() {
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
                            field[4] = "phone_number";

                            String[] data = new String[5];
                            data[0] = phone;
                            data[1] = branch_name;
                            data[2] = latitude;
                            data[3] = longitude;
                            data[4] = phoneNumber;

                            PutData putData = new PutData("http://192.168.254.109/fadSystem/addOutlet.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if(result.equals("Successfully Added!")){
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                        Intent storeOutlet = new Intent(getApplicationContext(), StoreOutlets.class);
                                        storeOutlet.putExtra("number", phoneNumber);
                                        startActivity(storeOutlet);
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
                else{
                    Toast.makeText(getApplicationContext(), "All fields Required!", Toast.LENGTH_SHORT).show();
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