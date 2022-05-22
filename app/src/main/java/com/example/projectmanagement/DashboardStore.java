package com.example.projectmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardStore extends AppCompatActivity {

    TextView Logout;
    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_store);

        phoneNumber = getIntent().getExtras().getString("number");

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        Logout = findViewById(R.id.logoutStore);
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

    }

    public void productClicked(View view){

        if(view.getId() == R.id.txt_foodProduct || view.getId() == R.id.foodProduct){
            Intent stores =  new Intent(getApplicationContext(), FoodProducts.class);
            stores.putExtra("number", phoneNumber);
            startActivity(stores);
        }

    }

    public void outletsClicked(View view){

        if(view.getId() == R.id.tvOutlets || view.getId() == R.id.ivOutlets){
            Intent outlets = new Intent(getApplicationContext(), StoreOutlets.class);
            outlets.putExtra("number", phoneNumber);
            startActivity(outlets);
        }

    }

}