package com.example.projectmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class CustomerRegistration extends AppCompatActivity {

    private final AppCompatActivity activity = CustomerRegistration.this;
    TextInputLayout textInputLayoutOTP1, textInputLayoutPhoneNumber1;
    TextInputEditText mPhoneNumber1, mOTP1;
    TextView resendCode1;
    String verification_code1;
    Button action1;
    ProgressBar progressBar;
    private int counter = 60;
    private int total = 0;

    @Override
    public void onBackPressed() {
        Intent chooseSignup = new Intent(getApplicationContext(), ChooseSignup.class);
        startActivity(chooseSignup);
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_registration);

        resendCode1 = findViewById(R.id.resendCodeCustomer);
        mPhoneNumber1 = findViewById(R.id.InputPhoneNumber1);
        textInputLayoutPhoneNumber1 = findViewById(R.id.textInputLayoutPhoneNumber1);
        mOTP1 = findViewById(R.id.InputOTP1);
        textInputLayoutOTP1 = findViewById(R.id.textInputLayoutOTP1);
        action1 = findViewById(R.id.action1);
        progressBar = findViewById(R.id.progressBar);


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

    public void onClicked(View view) {

        if(action1.getText().toString().equals("Confirm")) {
            if (mPhoneNumber1.getText().toString().isEmpty()) {
                textInputLayoutPhoneNumber1.setError("Enter Phone Number");
                textInputLayoutOTP1.setVisibility(View.INVISIBLE);
            }
            else {
                progressBar.setVisibility(View.VISIBLE);
                textInputLayoutPhoneNumber1.setError(null);
                PhoneAuthProvider.getInstance().verifyPhoneNumber("+63" + mPhoneNumber1.getText().toString(),
                        50, TimeUnit.SECONDS, CustomerRegistration.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {

                            }

                            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                                verification_code1 = s;
                                Toast.makeText(getApplicationContext(), "Code sent to the number", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.INVISIBLE);
                                action1.setText("Next");
                                textInputLayoutOTP1.setVisibility(View.VISIBLE);
                                resendCode1.setVisibility(View.VISIBLE);
                                textInputLayoutPhoneNumber1.setClickable(false);
                                mPhoneNumber1.setEnabled(false);
                                new Thread(new Runnable() {
                                    public void run() {
                                        while (counter > total) {
                                            try {
                                                Thread.sleep(1000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            resendCode1.post(new Runnable() {
                                                public void run() {
                                                    if(counter != 0) {
                                                        resendCode1.setText("Resend Code in " + counter);
                                                        resendCode1.setEnabled(false);
                                                        resendCode1.setTextColor(ColorStateList.valueOf(Color.parseColor("#808080")));
                                                    }
                                                    else{
                                                        resendCode1.setEnabled(true);
                                                        resendCode1.setText("Resend Code");
                                                        resendCode1.setTextColor(ColorStateList.valueOf(Color.parseColor("#FB8C00")));
                                                    }
                                                }
                                            });
                                            counter--;
                                        }
                                    }
                                }).start();

                            }
                        });

                resendCode1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressBar.setVisibility(View.VISIBLE);
                        if (action1.getText().toString().equals("Next")) {
                            PhoneAuthProvider.getInstance().verifyPhoneNumber("+63" + mPhoneNumber1.getText().toString(),
                                    50, TimeUnit.SECONDS, CustomerRegistration.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                        @Override
                                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                        }

                                        @Override
                                        public void onVerificationFailed(@NonNull FirebaseException e) {

                                        }

                                        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                                            verification_code1 = s;
                                            Toast.makeText(getApplicationContext(), "Code sent to the number", Toast.LENGTH_LONG).show();
                                            progressBar.setVisibility(View.INVISIBLE);

                                        }
                                    });
                        }
                    }
                });
            }
        }
        else{

            String input_code = mOTP1.getText().toString();
            if(!verification_code1.equals("") && !mOTP1.getText().toString().equals("")) {
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verification_code1, input_code);
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                final String phoneNumber;
                                phoneNumber = mPhoneNumber1.getText().toString();

                                if (!phoneNumber.isEmpty() && !mOTP1.getText().toString().isEmpty()) {
                                    textInputLayoutPhoneNumber1.setError(null);
                                    textInputLayoutOTP1.setError(null);

                                    Intent customerRegistration = new Intent(getApplicationContext(), CustomerRegistration1.class);
                                    customerRegistration.putExtra("phoneNumber", phoneNumber);
                                    startActivity(customerRegistration);

                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "All fields required", Toast.LENGTH_SHORT).show();
                                }


                            } else {
                                Toast.makeText(getApplicationContext(), "Please input valid code", Toast.LENGTH_SHORT).show();
                            }
                        }

                    });

            }
            else{
                textInputLayoutOTP1.setError("Enter OTP");
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