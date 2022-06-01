package com.example.projectmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.concurrent.TimeUnit;


public class Login extends AppCompatActivity {

    private final AppCompatActivity activity = Login.this;
    private TextInputLayout textInputLayoutOTP, textInputLayoutPhoneNumber;
    private TextInputEditText mPhoneNumber, mOTP;
    private Button action, signup;
    private TextView resendCode;
    private LoginValidation loginValidation;
    private ConstraintLayout constraintLayout;
    private Animation bottomAnim;
    private FirebaseAuth mAuth;
    String verification_code;
    ProgressBar progressBar;
    private int counter = 60;
    private int total = 0;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onBackPressed() {

        FirebaseAuth.getInstance().signOut();
        Intent login = new Intent(getApplicationContext(), Login.class);
        startActivity(login);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mAuth.getCurrentUser();

        if(mFirebaseUser != null){
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {

                    final String phoneNumber = mFirebaseUser.getPhoneNumber().substring(3);

                    String[] field = new String[1];
                    field[0] = "phoneNumber";
                    //Creating array for data
                    String[] data = new String[1];
                    data[0] = phoneNumber;
                    PutData putData = new PutData("http://192.168.254.109/fadSystem/login.php", "POST", field, data);
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            String result = putData.getResult();;
                            if (result.equals("Successfully Login!")) {
                                Intent driverDashboard = new Intent(getApplicationContext(), DashboardDriver.class);
                                driverDashboard.putExtra("number", phoneNumber);
                                startActivity(driverDashboard);
                                finish();
                            }
                            if (result.equals("Login Successfully!")) {
                                Intent customerDashboard = new Intent(getApplicationContext(), DashboardCustomer.class);
                                customerDashboard.putExtra("number", phoneNumber);
                                startActivity(customerDashboard);
                                finish();
                            }
                            if (result.equals("Login Successfully..")) {
                                Intent outletDashboard = new Intent(getApplicationContext(), DashboardOutlets.class);
                                outletDashboard.putExtra("number", phoneNumber);
                                startActivity(outletDashboard);
                                finish();
                            }
                            if (result.equals("Successfully Login..")) {
                                Intent storeDashboard = new Intent(getApplicationContext(), DashboardStore.class);
                                storeDashboard.putExtra("number", phoneNumber);
                                startActivity(storeDashboard);
                                finish();
                            }
                            if (result.equals("Please wait for the confirmation..")) {
                                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                }
            });

        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        progressBar = findViewById(R.id.loginProgressBar);
        resendCode = findViewById(R.id.resend);
        mPhoneNumber = findViewById(R.id.InputPhoneNumber);
        textInputLayoutPhoneNumber = findViewById(R.id.textInputLayoutPhoneNumber);
        mOTP = findViewById(R.id.InputOTP);
        textInputLayoutOTP = findViewById(R.id.textInputLayoutOTP);
        constraintLayout = findViewById(R.id.constraintLayout);
        action = findViewById(R.id.action);
        signup = findViewById(R.id.btnSignup);
        loginValidation = new LoginValidation(activity);

        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        mPhoneNumber.setAnimation(bottomAnim);
        textInputLayoutPhoneNumber.setAnimation(bottomAnim);
        action.setAnimation(bottomAnim);
        signup.setAnimation(bottomAnim);

        mPhoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signup = new Intent(getApplicationContext(), ChooseSignup.class);
                startActivity(signup);
            }
        });

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = cm
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        android.net.NetworkInfo datac = cm
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null & datac != null)
                && (wifi.isConnected() | datac.isConnected())) {


        }else{
            setContentView(R.layout.no_internet);
            swipeRefreshLayout = findViewById(R.id.swipelayout);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

                    swipeRefreshLayout.setRefreshing(false);
                    Intent login = new Intent(Login.this, Login.class);
                    startActivity(login);
                    finish();

                }
            });
        }

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

    public void onClicked(View v){

        if (!loginValidation.isInputEditTextFilled(mPhoneNumber, textInputLayoutPhoneNumber, getString(R.string.error_message_phone))) {
            return;
        }
        if(action.getText().toString().equals("Send OTP")) {
            progressBar.setVisibility(View.VISIBLE);
            PhoneAuthProvider.getInstance().verifyPhoneNumber("+63" + mPhoneNumber.getText().toString(),
                    50, TimeUnit.SECONDS, Login.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                        }

                        @Override
                        public void onVerificationFailed(@NonNull FirebaseException e) {

                        }

                        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken){

                            verification_code = s;
                            Toast.makeText(getApplicationContext(), "Code sent to the number", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            action.setText("Login");
                            textInputLayoutPhoneNumber.setClickable(false);
                            mPhoneNumber.setEnabled(false);
                            textInputLayoutOTP.setVisibility(View.VISIBLE);
                            resendCode.setVisibility(View.VISIBLE);
                            mOTP.setAnimation(bottomAnim);
                            textInputLayoutOTP.setAnimation(bottomAnim);
                            resendCode.setAnimation(bottomAnim);
                            new Thread(new Runnable() {
                                public void run() {
                                    while (counter > total) {
                                        try {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        resendCode.post(new Runnable() {
                                            public void run() {
                                                if(counter != 0) {
                                                    resendCode.setText("Resend Code in " + counter);
                                                    resendCode.setEnabled(false);
                                                    resendCode.setTextColor(ColorStateList.valueOf(Color.parseColor("#808080")));
                                                }
                                                else{
                                                    resendCode.setEnabled(true);
                                                    resendCode.setText("Resend Code");
                                                    resendCode.setTextColor(ColorStateList.valueOf(Color.parseColor("#FB8C00")));
                                                }
                                            }
                                        });
                                        counter--;
                                    }
                                }
                            }).start();

                        }
                    });

            resendCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressBar.setVisibility(View.VISIBLE);
                    if(action.getText().toString().equals("Login")) {
                        PhoneAuthProvider.getInstance().verifyPhoneNumber("+63" + mPhoneNumber.getText().toString(),
                                50, TimeUnit.SECONDS, Login.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    @Override
                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                    }

                                    @Override
                                    public void onVerificationFailed(@NonNull FirebaseException e) {

                                    }

                                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                                        verification_code = s;
                                        Toast.makeText(getApplicationContext(), "Code sent to the number", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.INVISIBLE);

                                    }
                                });
                    }
                }
            });
        }
        else{

            if (!loginValidation.isInputEditTextFilled(mPhoneNumber, textInputLayoutPhoneNumber, getString(R.string.error_message_phone))) {
                return;
            }
            if (!loginValidation.isInputEditTextFilled(mOTP, textInputLayoutOTP, getString(R.string.error_message_otp))) {
                return;
            }

            String input_code = mOTP.getText().toString();
            if(!verification_code.equals("")) {
                PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verification_code, input_code);
                FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            final String phoneNumber;
                            phoneNumber = mPhoneNumber.getText().toString().replaceAll("[^0-9]+", "");

                            if (!mPhoneNumber.equals("") && !mOTP.equals("")) {
                                Handler handler = new Handler();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Starting Write and Read data with URL
                                        //Creating array for parameters
                                        String[] field = new String[1];
                                        field[0] = "phoneNumber";
                                        //Creating array for data
                                        String[] data = new String[1];
                                        data[0] = phoneNumber;
                                        PutData putData = new PutData("http://192.168.254.109/fadSystem/login.php", "POST", field, data);
                                        if (putData.startPut()) {
                                            if (putData.onComplete()) {
                                                String result = putData.getResult();
                                                if (result.equals("Successfully Login!")) {
                                                    Intent driverDashboard = new Intent(getApplicationContext(), DashboardDriver.class);
                                                    driverDashboard.putExtra("number", phoneNumber);
                                                    startActivity(driverDashboard);
                                                    finish();
                                                }
                                                if (result.equals("Login Successfully!")) {
                                                    Intent customerDashboard = new Intent(getApplicationContext(), DashboardCustomer.class);
                                                    customerDashboard.putExtra("number", phoneNumber);
                                                    startActivity(customerDashboard);
                                                    finish();
                                                }
                                                if (result.equals("Login Successfully..")) {
                                                    Intent outletDashboard = new Intent(getApplicationContext(), DashboardOutlets.class);
                                                    outletDashboard.putExtra("number", phoneNumber);
                                                    startActivity(outletDashboard);
                                                    finish();
                                                }
                                                if (result.equals("Successfully Login..")) {
                                                    Intent storeDashboard = new Intent(getApplicationContext(), DashboardStore.class);
                                                    storeDashboard.putExtra("number", phoneNumber);
                                                    startActivity(storeDashboard);
                                                    finish();
                                                }
                                                if (result.equals("Please wait for the confirmation..")) {
                                                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                        //End Write and Read data with URL
                                    }

                                });
                            } else {
                                Toast.makeText(getApplicationContext(), "All fields required", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Please input valid code", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

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