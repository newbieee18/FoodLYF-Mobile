package com.example.projectmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

public class RiderApplication extends AppCompatActivity {

    private final AppCompatActivity activity = RiderApplication.this;
    TextInputLayout textInputLayoutOTP2, textInputLayoutPhoneNumber2;
    TextInputEditText mPhoneNumber2, mOTP2;
    TextView resendCode2;
    String verification_code2;
    Button action2;
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
        setContentView(R.layout.activity_rider_application);

        resendCode2 = findViewById(R.id.resendCodeRider);
        mPhoneNumber2 = findViewById(R.id.InputPhoneNumber2);
        textInputLayoutPhoneNumber2 = findViewById(R.id.textInputLayoutPhoneNumber2);
        mOTP2 = findViewById(R.id.InputOTP2);
        textInputLayoutOTP2 = findViewById(R.id.textInputLayoutOTP2);
        action2 = findViewById(R.id.action2);
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

        if(action2.getText().toString().equals("Confirm")) {
            if (mPhoneNumber2.getText().toString().isEmpty()) {
                textInputLayoutPhoneNumber2.setError("Enter Phone Number");
                textInputLayoutOTP2.setVisibility(View.INVISIBLE);
            }
            else {
                progressBar.setVisibility(View.VISIBLE);
                textInputLayoutPhoneNumber2.setError(null);
                PhoneAuthProvider.getInstance().verifyPhoneNumber("+63" + mPhoneNumber2.getText().toString(),
                        50, TimeUnit.SECONDS, RiderApplication.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {

                            }

                            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                                verification_code2 = s;
                                Toast.makeText(getApplicationContext(), "Code sent to the number", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.INVISIBLE);
                                action2.setText("Next");
                                textInputLayoutOTP2.setVisibility(View.VISIBLE);
                                resendCode2.setVisibility(View.VISIBLE);
                                textInputLayoutPhoneNumber2.setClickable(false);
                                mPhoneNumber2.setEnabled(false);
                                new Thread(new Runnable() {
                                    public void run() {
                                        while (counter > total) {
                                            try {
                                                Thread.sleep(1000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            resendCode2.post(new Runnable() {
                                                public void run() {
                                                    if(counter != 0) {
                                                        resendCode2.setText("Resend Code in " + counter);
                                                        resendCode2.setEnabled(false);
                                                        resendCode2.setTextColor(ColorStateList.valueOf(Color.parseColor("#808080")));
                                                    }
                                                    else{
                                                        resendCode2.setEnabled(true);
                                                        resendCode2.setText("Resend Code");
                                                        resendCode2.setTextColor(ColorStateList.valueOf(Color.parseColor("#FB8C00")));
                                                    }
                                                }
                                            });
                                            counter--;
                                        }
                                    }
                                }).start();

                            }
                        });

                resendCode2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressBar.setVisibility(View.VISIBLE);
                        if (action2.getText().toString().equals("Next")) {
                            PhoneAuthProvider.getInstance().verifyPhoneNumber("+63" + mPhoneNumber2.getText().toString(),
                                    50, TimeUnit.SECONDS, RiderApplication.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                        @Override
                                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                        }

                                        @Override
                                        public void onVerificationFailed(@NonNull FirebaseException e) {

                                        }

                                        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                                            verification_code2 = s;
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

            String input_code = mOTP2.getText().toString();
            if(!verification_code2.equals("") && !mOTP2.getText().toString().equals("")) {
                PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verification_code2, input_code);
                FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final String phoneNumber;
                            phoneNumber = mPhoneNumber2.getText().toString();

                            if (!phoneNumber.isEmpty() && !mOTP2.getText().toString().isEmpty()) {
                                textInputLayoutPhoneNumber2.setError(null);
                                textInputLayoutOTP2.setError(null);

                                FirebaseAuth.getInstance().signOut();
                                Intent riderApplication = new Intent(getApplicationContext(), RiderApplication1.class);
                                riderApplication.putExtra("phoneNumber", phoneNumber);
                                startActivity(riderApplication);

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
                textInputLayoutOTP2.setError("Enter OTP");
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