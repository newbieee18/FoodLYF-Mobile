package com.example.projectmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.List;


public class DashboardDriver extends AppCompatActivity{

    TextView totalDelivered, deliveredToday, availableOrders;
    ImageView getDirection;
    float x1, x2, y1, y2;
    String phoneNumber;

    private MapView mapView;
    private MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private static final long DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L;
    private static final long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5;
    private LocationEngine locationEngine;
//    private LocationChangeListeningActivityLocationCallback callback =
//            new LocationChangeListeningActivityLocationCallback(this);
    LinearLayout linearLayout1;
    TextView txtWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Mapbox
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

        setContentView(R.layout.activity_dashboard_driver);

        phoneNumber = getIntent().getExtras().getString("number");

        totalDelivered = findViewById(R.id.totalDelivered);
        deliveredToday = findViewById(R.id.deliveredOrders);
        availableOrders = findViewById(R.id.availableOrders);
        linearLayout1 = findViewById(R.id.mLinearLayout);
        txtWelcome = findViewById(R.id.txtWelcome);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.dashboard);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.dashboardRider:
                        Intent dashboardRider = new Intent(getApplicationContext(), DashboardDriver.class);
                        dashboardRider.putExtra("number", phoneNumber);
                        startActivity(dashboardRider);
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_from_left);
                        return true;
                }
                switch (item.getItemId()){
                    case R.id.acceptJob:
                        Intent riderJob = new Intent(getApplicationContext(), RiderJob.class);
                        riderJob.putExtra("number", phoneNumber);
                        startActivity(riderJob);
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_from_left);
                        return true;
                }
                switch (item.getItemId()){
                    case R.id.Account:
                        Intent account = new Intent(getApplicationContext(), RiderAccount.class);
                        account.putExtra("number", phoneNumber);
                        startActivity(account);
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_from_left);
                        return true;
                }
                return false;
            }
        });




    }

    private void getDeliveredOrders() {

        String url = "http://192.168.254.109/fadSystem/transaction_rider.php?phone=" + phoneNumber;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                showJSONS3(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DashboardDriver.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void showJSONS3(String response) {
        String count = "";

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");

            for (int i = 0; i < result.length(); i++) {

                JSONObject rider = result.getJSONObject(i);
                count = String.valueOf(result.length());


            }
            Log.i("tagconvertstr", "[" + response + "]");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        totalDelivered.setText(count);

    }


//    //MAPBOX
//    @SuppressWarnings( {"MissingPermission"})
//    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
//        // Check if permissions are enabled and if not request
//        if (PermissionsManager.areLocationPermissionsGranted(this)) {
//
//            // Get an instance of the component
//            LocationComponent locationComponent = mapboxMap.getLocationComponent();
//
//            // Set the LocationComponent activation options
//            LocationComponentActivationOptions locationComponentActivationOptions =
//                    LocationComponentActivationOptions.builder(this, loadedMapStyle)
//                            .useDefaultLocationEngine(false)
//                            .build();
//
//            // Activate with the LocationComponentActivationOptions object
//            locationComponent.activateLocationComponent(locationComponentActivationOptions);
//
//            // Enable to make component visible
//            locationComponent.setLocationComponentEnabled(true);
//
//            // Set the component's camera mode
//            locationComponent.setCameraMode(CameraMode.TRACKING);
//
//            // Set the component's render mode
//            locationComponent.setRenderMode(RenderMode.COMPASS);
//
//            initLocationEngine();
//        } else {
//            permissionsManager = new PermissionsManager(this);
//            permissionsManager.requestLocationPermissions(this);
//        }
//    }
//
//    @SuppressLint("MissingPermission")
//    private void initLocationEngine() {
//        locationEngine = LocationEngineProvider.getBestLocationEngine(this);
//
//        LocationEngineRequest request = new LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
//                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
//                .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build();
//
//        locationEngine.requestLocationUpdates(request, callback, getMainLooper());
//        locationEngine.getLastLocation(callback);
//    }
//
//    @SuppressLint("MissingSuperCall")
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }
//
//    @Override
//    public void onExplanationNeeded(List<String> permissionsToExplain) {
//        Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void onPermissionResult(boolean granted) {
//        if (granted) {
//            mapboxMap.getStyle(new Style.OnStyleLoaded() {
//                @Override
//                public void onStyleLoaded(@NonNull Style style) {
//                    enableLocationComponent(style);
//                }
//            });
//        } else {
//            Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
//            finish();
//        }
//    }
//
//    @Override
//    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
//        this.mapboxMap = mapboxMap;
//
//        mapboxMap.setStyle(Style.TRAFFIC_DAY,
//                new Style.OnStyleLoaded() {
//                    @Override public void onStyleLoaded(@NonNull Style style) {
//                        enableLocationComponent(style);
//                    }
//                });
//    }
//
//    private static class LocationChangeListeningActivityLocationCallback
//            implements LocationEngineCallback<LocationEngineResult> {
//
//        private final WeakReference<DashboardDriver> activityWeakReference;
//
//        LocationChangeListeningActivityLocationCallback(DashboardDriver activity) {
//            this.activityWeakReference = new WeakReference<>(activity);
//        }
//
//        /**
//         * The LocationEngineCallback interface's method which fires when the device's location has changed.
//         *
//         * @param result the LocationEngineResult object which has the last known location within it.
//         */
//        @Override
//        public void onSuccess(LocationEngineResult result) {
//            DashboardDriver activity = activityWeakReference.get();
//
//            if (activity != null) {
//                Location location = result.getLastLocation();
//
//                if (location == null) {
//                    return;
//                }
//
//                // Pass the new location to the Maps SDK's LocationComponent
//                if (activity.mapboxMap != null && result.getLastLocation() != null) {
//                    activity.mapboxMap.getLocationComponent().forceLocationUpdate(result.getLastLocation());
//                }
//            }
//        }
//
//        /**
//         * The LocationEngineCallback interface's method which fires when the device's location can't be captured
//         *
//         * @param exception the exception message
//         */
//        @Override
//        public void onFailure(@NonNull Exception exception) {
//            DashboardDriver activity = activityWeakReference.get();
//            if (activity != null) {
//                Toast.makeText(activity, exception.getLocalizedMessage(),
//                        Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        mapView.onStart();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mapView.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mapView.onPause();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        mapView.onStop();
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        mapView.onSaveInstanceState(outState);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        // Prevent leaks
//        if (locationEngine != null) {
//            locationEngine.removeLocationUpdates(callback);
//        }
//        mapView.onDestroy();
//    }
//
//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//        mapView.onLowMemory();
//    }
//
//    //END OF MAPBOX
//
//    @Override
//    public void onTaskDone(Object... values) {
//
//    }
//
//    public boolean onTouchEvent(MotionEvent touchEvent){
//        switch(touchEvent.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                x1 = touchEvent.getX();
//                y1 = touchEvent.getY();
//                break;
//            case MotionEvent.ACTION_UP:
//                x2 = touchEvent.getX();
//                y2 = touchEvent.getY();
//                if(x1 < x2){
//                }else if(x1 > x2){
//                    Intent account = new Intent(getApplicationContext(), RiderAccount.class);
//                    account.putExtra("number", phoneNumber);
//                    startActivity(account);
//                }
//                break;
//        }
//        return false;
//    }

}