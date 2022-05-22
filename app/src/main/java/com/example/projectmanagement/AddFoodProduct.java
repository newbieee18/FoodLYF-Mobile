package com.example.projectmanagement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
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

import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

public class AddFoodProduct extends AppCompatActivity {

    EditText productName, productDescription, productPrice;
    ImageView ivShowImage, ivCamera, ivGallery;
    Button btnSaveProduct;
    Switch availability;
    String encodedImage;
    CameraPhoto cameraPhoto;
    GalleryPhoto galleryPhoto;
    Intent camera;
    Spinner category;
    public static final int RequestPermissionCode = 1;
    final int GALLERY_REQUEST = 21313;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_product);

        String phoneNumber = getIntent().getExtras().getString("number");

        productName = findViewById(R.id.productName);
        productDescription = findViewById(R.id.productDescription);
        productPrice = findViewById(R.id.productPrice);
        availability = findViewById(R.id.availability);
        category = findViewById(R.id.category);
        ivShowImage = findViewById(R.id.ivShowImage);
        ivCamera = findViewById(R.id.ivCamera);
        ivGallery = findViewById(R.id.ivGallery);
        btnSaveProduct = findViewById(R.id.btnAddProduct);
        cameraPhoto = new CameraPhoto(getApplicationContext());
        galleryPhoto = new GalleryPhoto(getApplicationContext());
        enableRuntimePermission();

        String[] productCategory = {"Select Category", "Burger", "Pasta", "Chicken", "Drinks", "Fries", "Pizza", "Rice Meals", "Desserts and Floats"};
        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(this,
                R.layout.spinner_item, productCategory);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adp1);

        btnSaveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String phone, product_name, product_description, product_price, product_image, available, pCategory;
                phone = phoneNumber;
                product_name = productName.getText().toString();
                product_description = productDescription.getText().toString();
                product_price = productPrice.getText().toString();
                product_image = encodedImage;
                pCategory = category.getSelectedItem().toString();

                if(availability.isChecked()) available = "Yes";
                else available = "No";

                if(!phone.equals("") && !product_name.equals("") && !product_description.equals("") && !product_price.equals("") && ivShowImage.getDrawable()!=null
                && !available.equals("") && !pCategory.equals("")){
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[7];
                            field[0] = "phone";
                            field[1] = "product_name";
                            field[2] = "product_description";
                            field[3] = "product_price";
                            field[4] = "product_image";
                            field[5] = "available";
                            field[6] = "category";
                            //Creating array for data
                            String[] data = new String[7];
                            data[0] = phone;
                            data[1] = product_name;
                            data[2] = product_description;
                            data[3] = product_price;
                            data[4] = product_image;
                            data[5] = available;
                            data[6] = pCategory;
                            PutData putData = new PutData("http://192.168.254.109/fadSystem/add_product.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if(result.equals("Successfully Added!")){
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
                else{
                    Toast.makeText(getApplicationContext(), "All fields required!", Toast.LENGTH_SHORT).show();
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
        if(ActivityCompat.shouldShowRequestPermissionRationale(AddFoodProduct.this, Manifest.permission.CAMERA)){
            Toast.makeText(AddFoodProduct.this, "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();
        }else {
            ActivityCompat.requestPermissions(AddFoodProduct.this, new String[]{Manifest.permission.CAMERA}, RequestPermissionCode);
        }
    }

}