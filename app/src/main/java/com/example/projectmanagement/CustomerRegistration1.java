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

import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomerRegistration1 extends AppCompatActivity {

    private final AppCompatActivity activity = CustomerRegistration1.this;
    String phoneNumber;
    EditText customerName, customerAddress, customerEmail;
    Button btnContinue;

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_registration1);

        phoneNumber = getIntent().getExtras().getString("phoneNumber");
        customerName = findViewById(R.id.etCustomerName);
        customerAddress = findViewById(R.id.etCustomerAddress);
        customerEmail = findViewById(R.id.etCustomerEmail);
        btnContinue = findViewById(R.id.btnContinue);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(customerName.getText().toString().equals("")){
                    customerName.setError("Enter Name");
                }

                if(customerAddress.getText().toString().equals("")){
                    customerAddress.setError("Enter Address");
                }

                if(!isValidEmail(customerEmail.getText().toString().trim())){
                    customerEmail.setError("Invalid Email");
                }

                if(!phoneNumber.equals("") && !customerName.getText().toString().equals("") && !customerAddress.getText().toString().equals("")
                        && !customerEmail.getText().toString().equals("") && isValidEmail(customerEmail.getText().toString().trim())){

                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            String[] field = new String[4];
                            field[0] = "phoneNumber";
                            field[1] = "customerName";
                            field[2] = "customerAddress";
                            field[3] = "customerEmail";

                            String[] data = new String[4];
                            data[0] = phoneNumber;
                            data[1] = customerName.getText().toString();
                            data[2] = customerAddress.getText().toString();
                            data[3] = customerEmail.getText().toString();

                            PutData putData = new PutData("http://192.168.254.109/fadSystem/customerRegistration.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if(result.equals("Sign Up Success")){
                                        Intent customerDashboard = new Intent(getApplicationContext(), DashboardCustomer.class);
                                        customerDashboard.putExtra("number", phoneNumber);
                                        startActivity(customerDashboard);
                                        finish();
                                    }
                                    else{
                                        FirebaseAuth.getInstance().signOut();
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                        Intent signUp = new Intent(getApplicationContext(), ChooseSignup.class);
                                        startActivity(signUp);
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

    public static boolean isValidEmail(String str) {
        boolean isValid = false;
        if (Build.VERSION.SDK_INT >= 8) {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(str).matches();
        }
        String expression = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        CharSequence inputStr = str;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
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