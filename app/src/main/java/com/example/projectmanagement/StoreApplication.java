package com.example.projectmanagement;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StoreApplication extends AppCompatActivity {

    private final AppCompatActivity activity = StoreApplication.this;
    EditText businessName, title, mAddress, city, outlet, name, mPhone, eAddress;
    Button proceed;
    CheckBox confirm;

    @Override
    public void onBackPressed() {
        Intent chooseSignup = new Intent(getApplicationContext(), ChooseSignup.class);
        startActivity(chooseSignup);
        finish();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_application);

        businessName = findViewById(R.id.etBusinessName);
        title = findViewById(R.id.etTitle);
        mAddress = findViewById(R.id.etMerchantAddress);
        city = findViewById(R.id.etCity);
        outlet = findViewById(R.id.etOutlet);
        name = findViewById(R.id.etName);
        mPhone = findViewById(R.id.etMobilePhone);
        eAddress = findViewById(R.id.etEmailAddress);
        proceed = findViewById(R.id.proceed);
        confirm = findViewById(R.id.checkBox);

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(1000);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setDuration(1000);

        mPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(confirm.isChecked()) {
                    proceed.startAnimation(fadeIn);
                    proceed.setVisibility(View.VISIBLE);
                }
                else {
                    proceed.startAnimation(fadeOut);
                    proceed.setVisibility(View.INVISIBLE);
                }

            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(businessName.getText().toString().equals(""))
                    businessName.setError("Enter Business Name");

                if(title.getText().toString().equals(""))
                    title.setError("Enter Title");

                if(mAddress.getText().toString().equals(""))
                    mAddress.setError("Enter Main Business Address");

                if(city.getText().toString().equals(""))
                    city.setError("Enter City");

                if(outlet.getText().toString().equals(""))
                    outlet.setError("Enter Outlet");

                if(name.getText().toString().equals(""))
                    name.setError("Enter Merchant");

                if(mPhone.getText().toString().equals(""))
                    mPhone.setError("Enter Mobile Phone");

                if(eAddress.getText().toString().equals("") || !isValidEmail(eAddress.getText().toString().trim()))
                    eAddress.setError("Invalid Email!");

                if(!businessName.getText().toString().equals("") && !title.getText().toString().equals("") && !mAddress.getText().toString().equals("")
                && !outlet.getText().toString().equals("") && !name.getText().toString().equals("") && !mPhone.getText().toString().equals("")
                && isValidEmail(eAddress.getText().toString().trim())) {

                    String mphone = mPhone.getText().toString().replaceAll("[^0-9]+", "");

                    Intent storeApplication2 = new Intent(getApplicationContext(), StoreApplication2.class);
                    storeApplication2.putExtra("businessName", businessName.getText().toString());
                    storeApplication2.putExtra("title", title.getText().toString());
                    storeApplication2.putExtra("mAddress", mAddress.getText().toString());
                    storeApplication2.putExtra("city", city.getText().toString());
                    storeApplication2.putExtra("outlet", outlet.getText().toString());
                    storeApplication2.putExtra("name", name.getText().toString());
                    storeApplication2.putExtra("mPhone", mphone);
                    storeApplication2.putExtra("eAddress", eAddress.getText().toString());
                    startActivity(storeApplication2);

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