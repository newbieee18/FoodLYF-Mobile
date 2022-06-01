package com.example.projectmanagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigation;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class RiderDeliver extends AppCompatActivity implements LocationListener {

    String phoneNumber, customerPhone;
    double latitude, longitude;
    TextView total, cName, cPhone, suggestion, branchDetails, cLink;
    TableLayout tableLayout;
    Button acceptOrder, btnContinue;
    Double rLongitude, rLatitude, bLatitude, bLongitude, cLatitude, cLongitude;

    //MAPBOX
//    Point origin, origin1;
//    Point destination, destination1;
//    MapboxNavigation navigation;
//    private MapView mapView;
//    private MapboxMap mapboxMap;
//    private PermissionsManager permissionsManager;
//    private static final long DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L;
//    private static final long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5;
//    private LocationEngine locationEngine;
//    private RiderDeliver.LocationChangeListeningActivityLocationCallback callback =
//            new RiderDeliver.LocationChangeListeningActivityLocationCallback(this);

    private LocationRequest locationRequest;
    private static final int REQUEST_CHECK_SETTINGS = 10001;


    protected LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_deliver);

        phoneNumber = getIntent().getExtras().getString("number");
        customerPhone = getIntent().getExtras().getString("customerPhone");
        tableLayout = findViewById(R.id.tableLayout);
        total = findViewById(R.id.total);
        acceptOrder = findViewById(R.id.acceptOrder);
        btnContinue = findViewById(R.id.btnContinue);
        cName = findViewById(R.id.customerName);
        cPhone = findViewById(R.id.customerPhone);
        suggestion = findViewById(R.id.suggestion);
        branchDetails = findViewById(R.id.branchDetails);
        //cLink = findViewById(R.id.cLink);

        getItems();

//        //MAPBOX
//        navigation = new MapboxNavigation(this, getString(R.string.mapbox_access_token));
//        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
//        mapView = (MapView) findViewById(R.id.mapView);
//        mapView.onCreate(savedInstanceState);
//        mapView.onStart();
//        mapView.getMapAsync(RiderDeliver.this::onMapReady);

        acceptOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //getRoute();
                getLocation();
                acceptOrder.setVisibility(View.INVISIBLE);
                btnContinue.setVisibility(View.VISIBLE);

            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //getRoute1();

                Intent scanner = new Intent(getApplicationContext(), Scanner.class);
                scanner.putExtra("customerPhone", customerPhone);
                scanner.putExtra("number", phoneNumber);
                startActivity(scanner);

            }
        });

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    //Toast.makeText(RiderDeliver.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException)e;
                                resolvableApiException.startResolutionForResult(RiderDeliver.this,REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });

    }

//    public void comparisonClicked(View view){
//
//        if(view.getId() == R.id.cLink){
//            Intent astar =  getPackageManager().getLaunchIntentForPackage("com.DefaultCompany.AstarAlgorithm");
//            startActivity(astar);
//        }
//        if(view.getId() == R.id.cLink1){
//            Intent dijkstra =  getPackageManager().getLaunchIntentForPackage("com.DefaultCompany.DijkstraAlgorithm");
//            startActivity(dijkstra);
//        }
//
//    }

    private void getItems() {

        String url = "http://192.168.254.109/fadSystem/deliveryList.php?phone="+ customerPhone;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                showJSONS(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RiderDeliver.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void showJSONS(String response) {
        int order_id = 0;
        int quantity = 0;
        String customer_name = "";
        String customer_phone = "";
        String product_name = "";
        String latitude = "";
        String longitude = "";
        String sug = "";
        double subtotal = 0;
        String store_name = "";
        String branch_name = "";
        String branch_latitude = "";
        String branch_longitude = "";
        String dFee = "";
        double mTotal = 0;

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("deliveryList");

            TableRow.LayoutParams textViewParam = new TableRow.LayoutParams
                    (0,
                            TableRow.LayoutParams.MATCH_PARENT,3f);

            TableRow.LayoutParams textViewParam1 = new TableRow.LayoutParams
                    (0,
                            TableRow.LayoutParams.MATCH_PARENT,1f);

            Typeface typeface = ResourcesCompat.getFont(RiderDeliver.this,R.font.chakra_petch_medium);

            TableRow tbrow0 = new TableRow(this);
            tbrow0.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.FILL_PARENT,
                    TableLayout.LayoutParams.FILL_PARENT, 1.0f));
            TextView tv0 = new TextView(this);
            tv0.setText("List Of Orders");
            tv0.setGravity(Gravity.LEFT);
            tv0.setTextSize(18);
            tv0.setTypeface(typeface);
            tv0.setTextColor(Color.parseColor("#FF000000"));
            tv0.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            tv0.setLayoutParams(textViewParam);
            tbrow0.addView(tv0);
            TextView tv01 = new TextView(this);
            tv01.setText("Price");
            tv01.setGravity(Gravity.RIGHT);
            tv01.setTextSize(18);
            tv01.setTypeface(typeface);
            tv01.setTextColor(Color.parseColor("#FF000000"));
            tv01.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            tv01.setLayoutParams(textViewParam1);
            tbrow0.addView(tv01);
            tableLayout.addView(tbrow0);

            for (int i = 0; i < result.length(); i++) {

                JSONObject dList = result.getJSONObject(i);
                product_name = dList.getString("product_name");
                order_id = dList.getInt("order_id");
                customer_name = dList.getString("customer_name");
                customer_phone = dList.getString("customer_phone");
                latitude = dList.getString("latitude");
                longitude = dList.getString("longitude");
                quantity = dList.getInt("quantity");
                subtotal = dList.getDouble("subtotal");
                sug = dList.getString("suggestion");
                store_name = dList.getString("store_name");
                branch_name = dList.getString("branch_name");
                branch_latitude = dList.getString("branch_latitude");
                branch_longitude = dList.getString("branch_longitude");
                mTotal = dList.getDouble("total");
                dFee = dList.getString("dfee");

                TableRow tbRow = new TableRow(this);
                tbRow.setLayoutParams(new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.WRAP_CONTENT,
                        TableLayout.LayoutParams.WRAP_CONTENT, 1.0f));
                TextView tv1 = new TextView(this);
                tv1.setText("x" + quantity + " " + product_name);
                tv1.setTextSize(16);
                tv1.setTypeface(typeface);
                tv1.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                tv1.setLayoutParams(textViewParam);
                tbRow.addView(tv1);
                TextView tv2 = new TextView(this);
                String Subtotal = String.format("₱%.2f", subtotal);
                tv2.setText(Subtotal);
                tv2.setTextSize(16);
                tv2.setTypeface(typeface);
                tv2.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                tv2.setLayoutParams(textViewParam1);
                tbRow.addView(tv2);
                tableLayout.addView(tbRow);




            }
            Log.e("tagconvertstr", "["+result+"]");
//            bLatitude = Double.parseDouble(branch_latitude);
//            bLongitude = Double.parseDouble(branch_longitude);
//            cLatitude = Double.parseDouble(latitude);
//            cLongitude = Double.parseDouble(longitude);
//            destination = Point.fromLngLat(bLongitude, bLatitude);
//            destination1 = Point.fromLngLat(cLongitude, cLatitude);
//            origin1 = Point.fromLngLat(bLongitude, bLatitude);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        total.setText(String.format("Total: ₱%.2f", mTotal));
        cName.setText("Customer Name: " + customer_name);
        cPhone.setText("Contact#: " + customer_phone);
        suggestion.setText("Suggestion: " + sug);
        branchDetails.setText("Branch Name: " + branch_name);

    }

    public void getLocation(){
        try {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, RiderDeliver.this);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        final String rLatitude, rLongitude, status, customer_phone, rider_phone;

        status = "For Delivery";
        rLatitude = String.valueOf(latitude);
        rLongitude = String.valueOf(longitude);
        customer_phone = getIntent().getExtras().getString("customerPhone");;
        rider_phone = getIntent().getExtras().getString("number");

        if (!rLatitude.equals("") && !rLongitude.equals("") && !status.equals("") && !rider_phone.equals("")) {
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {

                    String[] field = new String[5];
                    field[0] = "rider_latitude";
                    field[1] = "rider_longitude";
                    field[2] = "status";
                    field[3] = "customer_phone";
                    field[4] = "rider_phone";

                    String[] data = new String[5];
                    data[0] = rLatitude;
                    data[1] = rLongitude;
                    data[2] = status;
                    data[3] = customer_phone;
                    data[4] = rider_phone;

                    PutData putData = new PutData("http://192.168.254.109/fadSystem/update_order1.php", "POST", field, data);
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            String result = putData.getResult();
                            if (result.equals("")) {
                                final Loading loading = new Loading(RiderDeliver.this);
                                loading.startLoading();

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        loading.DismissLoading();
                                    }
                                }, 5000);
                                Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(), "You have error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

//    //MAPBOX
//    private void getRoute(){
//
//        NavigationRoute.builder(getApplicationContext())
//                .accessToken(getString(R.string.mapbox_access_token))
//                .origin(origin)
//                .destination(destination)
//                .build()
//                .getRoute(new Callback<DirectionsResponse>() {
//                    @Override
//                    public void onResponse(Call<DirectionsResponse> call, retrofit2.Response<DirectionsResponse> response) {
//                        if(response.body() == null){
//                            Toast.makeText(getApplicationContext(), "No routes found!", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                        else if(response.body().routes().size() < 1){
//                            Toast.makeText(getApplicationContext(), "No routes found.!", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                        DirectionsRoute route = response.body().routes().get(0);
//                        NavigationLauncherOptions options = NavigationLauncherOptions.builder()
//                                .directionsRoute(route)
//                                .shouldSimulateRoute(true)
//                                .build();
//                        NavigationLauncher.startNavigation(RiderDeliver.this, options);
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
//                        Toast.makeText(getApplicationContext(), "error" + t.toString(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//    }
//
//    private void getRoute1(){
//
//        NavigationRoute.builder(getApplicationContext())
//                .accessToken(getString(R.string.mapbox_access_token))
//                .origin(origin1)
//                .destination(destination1)
//                .build()
//                .getRoute(new Callback<DirectionsResponse>() {
//                    @Override
//                    public void onResponse(Call<DirectionsResponse> call, retrofit2.Response<DirectionsResponse> response) {
//                        if(response.body() == null){
//                            Toast.makeText(getApplicationContext(), "No routes found!", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                        else if(response.body().routes().size() < 1){
//                            Toast.makeText(getApplicationContext(), "No routes found.!", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                        DirectionsRoute route = response.body().routes().get(0);
//                        NavigationLauncherOptions options = NavigationLauncherOptions.builder()
//                                .directionsRoute(route)
//                                .shouldSimulateRoute(true)
//                                .build();
//                        NavigationLauncher.startNavigation(RiderDeliver.this, options);
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
//                        Toast.makeText(getApplicationContext(), "error" + t.toString(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//    }
//
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
//
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
//    private class LocationChangeListeningActivityLocationCallback
//            implements LocationEngineCallback<LocationEngineResult> {
//
//        private final WeakReference<RiderDeliver> activityWeakReference;
//
//        LocationChangeListeningActivityLocationCallback(RiderDeliver activity) {
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
//            RiderDeliver activity = activityWeakReference.get();
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
//                    rLatitude = location.getLatitude();
//                    rLongitude = location.getLongitude();
//                    origin = Point.fromLngLat(rLongitude, rLatitude);
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
//            RiderDeliver activity = activityWeakReference.get();
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
//        navigation.onDestroy();
//    }
//
//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//        mapView.onLowMemory();
//    }
//
//    //END OF MAPBOX

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == REQUEST_CHECK_SETTINGS) {
//
//            switch (resultCode) {
//                case Activity.RESULT_OK:
//                    Toast.makeText(this, "GPS is tured on", Toast.LENGTH_SHORT).show();
//
//                case Activity.RESULT_CANCELED:
//                    Toast.makeText(this, "GPS required to be tured on", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
}