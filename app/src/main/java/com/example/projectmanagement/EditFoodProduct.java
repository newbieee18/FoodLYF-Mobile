package com.example.projectmanagement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

public class EditFoodProduct extends AppCompatActivity {

    TextView productID;
    EditText productName, productDescription, productPrice;
    ImageView ivShowImage, ivCamera, ivGallery;
    Button btnUpdate;
    String encodedImage, prodID;
    CameraPhoto cameraPhoto;
    GalleryPhoto galleryPhoto;
    Intent camera;
    Switch availability;
    Spinner category;
    public static final int RequestPermissionCode = 1;
    final int GALLERY_REQUEST = 21313;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_food_products);

        String phoneNumber = getIntent().getExtras().getString("number");

        SharedPreferences sp=getSharedPreferences("credentials",MODE_PRIVATE);
        if(sp.contains("prodID"))
            prodID = sp.getString("prodID", "");

        productID = findViewById(R.id.productID);
        productID.setText(prodID);
        productName = findViewById(R.id.productName);
        productDescription = findViewById(R.id.productDescription);
        productPrice = findViewById(R.id.productPrice);
        availability = findViewById(R.id.availability);
        category = findViewById(R.id.category);
        ivShowImage = findViewById(R.id.ivShowImage);
        ivCamera = findViewById(R.id.ivCamera);
        ivGallery = findViewById(R.id.ivGallery);
        btnUpdate = findViewById(R.id.btnUpdateProduct);

        String[] productCategory = {"Select Category", "Burger", "Pasta", "Chicken", "Drinks", "Fries", "Pizza", "Rice Meals", "Desserts and Floats"};
        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(this,
                R.layout.spinner_item, productCategory);
        adp1.setDropDownViewResource(R.layout.spinner_item);
        category.setAdapter(adp1);
        getProducts();

        cameraPhoto = new CameraPhoto(getApplicationContext());
        galleryPhoto = new GalleryPhoto(getApplicationContext());
        enableRuntimePermission();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String product_id, product_name, product_description, product_price, product_image, available, pCategory;
                product_id = productID.getText().toString();
                product_name = productName.getText().toString();
                product_description = productDescription.getText().toString();
                product_price = productPrice.getText().toString();
                product_image = encodedImage;
                pCategory = category.getSelectedItem().toString();

                if(availability.isChecked()) available = "Yes";
                else available = "No";


                if(!product_id.equals("") && !product_name.equals("") && !product_description.equals("") && !product_price.equals("") && !product_image.equals("")
                && !available.equals("")){
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[7];
                            field[0] = "product_id";
                            field[1] = "product_name";
                            field[2] = "product_description";
                            field[3] = "product_price";
                            field[4] = "product_image";
                            field[5] = "available";
                            field[6] = "category";
                            //Creating array for data
                            String[] data = new String[7];
                            data[0] = product_id;
                            data[1] = product_name;
                            data[2] = product_description;
                            data[3] = product_price;
                            data[4] = product_image;
                            data[5] = available;
                            data[6] = pCategory;
                            PutData putData = new PutData("http://192.168.254.109/fadSystem/update_food.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if(result.equals("Record updated successfully")){
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                        Intent FoodProducts = new Intent(getApplicationContext(), FoodProducts.class);
                                        FoodProducts.putExtra("number", phoneNumber);
                                        startActivity(FoodProducts);
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });

        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera, 7);

            }
        });

        ivGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_REQUEST);
            }
        });

    }

    private void getProducts() {

        String url = Config.EDIT_PRODUCT_URL + productID.getText().toString();
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                showJSONS(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditFoodProduct.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void showJSONS(String response) {
        String product_name = "";
        String product_desc = "";
        String product_image = "";
        String product_price = "";
        String available = "";
        String pcategory = "";

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY5);
            JSONObject collegeData = result.getJSONObject(0);
            product_name = collegeData.getString(Config.EDIT_PRODUCT_NAME);
            product_desc = collegeData.getString(Config.EDIT_PRODUCT_DESC);
            product_image = collegeData.getString(Config.EDIT_PRODUCT_IMAGE);
            product_price = collegeData.getString(Config.EDIT_PRODUCT_PRICE);
            available = collegeData.getString(Config.AVAILABLE);
            pcategory = collegeData.getString(Config.EDIT_PRODUCT_CATEGORY);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        productName.setText(product_name);
        productDescription.setText(product_desc);
        productPrice.setText(product_price);
        if(available.equals("Yes"))
            availability.setChecked(true);
        else availability.setChecked(false);
        int selection = 0;
        if(pcategory.equals("Select Category")) selection = 0;
        else if(pcategory.equals("Burger")) selection = 1;
        else if(pcategory.equals("Pasta")) selection = 2;
        else if(pcategory.equals("Chicken")) selection = 3;
        else if(pcategory.equals("Drinks")) selection = 4;
        else if(pcategory.equals("Fries")) selection = 5;
        else if(pcategory.equals("Pizza")) selection = 6;
        else if(pcategory.equals("Rice Meals")) selection = 7;
        else if(pcategory.equals("Desserts and Floats")) selection = 8;
        category.setSelection(selection);
        String imageUri = product_image;
        byte[] decodedString = Base64.decode(imageUri, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        ivShowImage.setImageBitmap(decodedByte);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        decodedByte.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteOfImages = byteArrayOutputStream.toByteArray();
        encodedImage = Base64.encodeToString(byteOfImages, Base64.DEFAULT);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 7) {

                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ivShowImage.setImageBitmap(bitmap);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] byteOfImages = byteArrayOutputStream.toByteArray();
                encodedImage = Base64.encodeToString(byteOfImages, Base64.DEFAULT);
            }
            else if (requestCode == GALLERY_REQUEST) {
                Uri uri = data.getData();
                galleryPhoto.setPhotoUri(uri);
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                    ivShowImage.setImageBitmap(bitmap);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] byteOfImages = byteArrayOutputStream.toByteArray();
                    encodedImage = Base64.encodeToString(byteOfImages, Base64.DEFAULT);
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Something Wrong while loading photos", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }


    public  void enableRuntimePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(EditFoodProduct.this, Manifest.permission.CAMERA)){
            Toast.makeText(EditFoodProduct.this, "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();
        }else {
            ActivityCompat.requestPermissions(EditFoodProduct.this, new String[]{Manifest.permission.CAMERA}, RequestPermissionCode);
        }
    }

}