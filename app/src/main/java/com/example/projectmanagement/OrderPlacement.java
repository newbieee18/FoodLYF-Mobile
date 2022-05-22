package com.example.projectmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OrderPlacement extends AppCompatActivity {

    private Button txtContinue;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placement);

        phoneNumber = getIntent().getExtras().getString("number");
        txtContinue = findViewById(R.id.txtContinue);

        txtContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent trackOrder = new Intent(getApplicationContext(), TrackOrder.class);
                trackOrder.putExtra("number", phoneNumber);
                startActivity(trackOrder);

            }
        });


    }
}