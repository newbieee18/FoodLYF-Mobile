package com.example.projectmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.Result;
import com.google.zxing.qrcode.encoder.QRCode;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Scanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView ScannerView;
    private static int cam = Camera.CameraInfo.CAMERA_FACING_BACK;
    String customerPhone, phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        phoneNumber = getIntent().getExtras().getString("number");
        customerPhone = getIntent().getExtras().getString("customerPhone");
        ScannerView = new ZXingScannerView(this);
        setContentView(ScannerView);

        int currentVersion = Build.VERSION.SDK_INT;
        if(currentVersion >= Build.VERSION_CODES.M){
            if(checkPermission()){
                Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            requestPermission();
        }

    }

    private boolean checkPermission(){
        return (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(Scanner.this, new String[] {Manifest.permission.CAMERA}, REQUEST_CAMERA);
    }

    public void onRequestPermissionResult(int requestCode, String permissions[], int[] grantResult){
        switch (requestCode){
            case REQUEST_CAMERA:
                if(grantResult.length > 0){
                    boolean cameraAccept = grantResult[0] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccept){
                        Toast.makeText(getApplicationContext(), "Permission Granted By User", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Permission not Granted By User", Toast.LENGTH_LONG).show();
                        if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                            showMessageOKCancel("You need to grant the permission",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                                requestPermissions(new String[] {Manifest.permission.CAMERA}, REQUEST_CAMERA);
                                            }
                                        }
                                    });
                            return;
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onResume(){
        super.onResume();

        int currentVersion = Build.VERSION.SDK_INT;
        if(currentVersion >= Build.VERSION_CODES.M){
            if(checkPermission()){
                if(ScannerView == null){
                    ScannerView = new ZXingScannerView(this);
                    setContentView(ScannerView);
                }
                ScannerView.setResultHandler(this);
                ScannerView.startCamera();
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ScannerView.stopCamera();
        ScannerView = null;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener oklistener){
        new AlertDialog.Builder(Scanner.this)
                .setMessage(message)
                .setPositiveButton("OK", oklistener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void handleResult(Result result) {
        final String rawresult = result.getText();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String url = "http://192.168.254.109/fadSystem/update_order2.php?customer_phone=" + customerPhone + "&status=Delivered";
                StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Intent job = new Intent(getApplicationContext(), RiderJob.class);
                        job.putExtra("number", phoneNumber);
                        startActivity(job);
                        Log.e("tagconvertstr", "["+response+"]");

                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Scanner.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                            }
                        });

                RequestQueue requestQueue = Volley.newRequestQueue(Scanner.this);
                requestQueue.add(stringRequest);


            }
        });

        builder.setNegativeButton("Restart", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onRestart();
            }
        });

        builder.setMessage("Successfully scanned..");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}