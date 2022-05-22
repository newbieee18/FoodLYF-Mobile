package com.example.projectmanagement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

public class StoreApplication2 extends AppCompatActivity {

    private final AppCompatActivity activity = StoreApplication2.this;
    String businessName, title, mAddress, city, outlet, name, mPhone, eAddress;
    Button btnSave;
    ImageView ivCamera, ivCamera1, ivCamera2, ivCamera3, ivCamera4, ivCamera5, ivCamera6;
    ImageView ivGallery, ivGallery1, ivGallery2, ivGallery3, ivGallery4, ivGallery5, ivGallery6;
    ImageView ivShowImage, ivShowImage1, ivShowImage2, ivShowImage3, ivShowImage4, ivShowImage5, ivShowImage6;
    CameraPhoto cameraPhoto;
    GalleryPhoto galleryPhoto;
    Intent camera;
    String encodedImage, encodedImage1, encodedImage2, encodedImage3, encodedImage4, encodedImage5, encodedImage6;
    public static final int RequestPermissionCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_application2);

        businessName = getIntent().getExtras().getString("businessName");
        title = getIntent().getExtras().getString("title");
        mAddress = getIntent().getExtras().getString("mAddress");
        city = getIntent().getExtras().getString("city");
        outlet = getIntent().getExtras().getString("outlet");
        name = getIntent().getExtras().getString("name");
        mPhone = getIntent().getExtras().getString("mPhone");
        eAddress = getIntent().getExtras().getString("eAddress");

        btnSave = findViewById(R.id.btnSave);
        ivShowImage = findViewById(R.id.ivShowImage);
        ivShowImage1 = findViewById(R.id.ivShowImage1);
        ivShowImage2 = findViewById(R.id.ivShowImage2);
        ivShowImage3 = findViewById(R.id.ivShowImage3);
        ivShowImage4 = findViewById(R.id.ivShowImage4);
        ivShowImage5 = findViewById(R.id.ivShowImage5);
        ivShowImage6 = findViewById(R.id.ivShowImage6);
        ivCamera = findViewById(R.id.ivCamera);
        ivCamera1 = findViewById(R.id.ivCamera1);
        ivCamera2 = findViewById(R.id.ivCamera2);
        ivCamera3 = findViewById(R.id.ivCamera3);
        ivCamera4 = findViewById(R.id.ivCamera4);
        ivCamera5 = findViewById(R.id.ivCamera5);
        ivCamera6 = findViewById(R.id.ivCamera6);
        ivGallery = findViewById(R.id.ivGallery);
        ivGallery1 = findViewById(R.id.ivGallery1);
        ivGallery2 = findViewById(R.id.ivGallery2);
        ivGallery3 = findViewById(R.id.ivGallery3);
        ivGallery4 = findViewById(R.id.ivGallery4);
        ivGallery5 = findViewById(R.id.ivGallery5);
        ivGallery6 = findViewById(R.id.ivGallery6);

        TextView dti = findViewById(R.id.textViewDTI);
        TextView bp = findViewById(R.id.textViewBP);
        TextView Form9 = findViewById(R.id.textViewForm9);
        TextView Form49 = findViewById(R.id.textViewForm49);
        TextView vid = findViewById(R.id.textViewVId);
        TextView hc = findViewById(R.id.textViewHC);


        cameraPhoto = new CameraPhoto(getApplicationContext());
        galleryPhoto = new GalleryPhoto(getApplicationContext());
        enableRuntimePermission();

        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera, 1);

            }
        });

        ivCamera1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera, 2);

            }
        });

        ivCamera2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera, 3);

            }
        });

        ivCamera3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera, 4);

            }
        });

        ivCamera4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera, 5);

            }
        });

        ivCamera5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera, 6);

            }
        });

        ivCamera6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera, 7);

            }
        });

        ivGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(galleryPhoto.openGalleryIntent(), 8);
            }
        });

        ivGallery1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(galleryPhoto.openGalleryIntent(), 9);
            }
        });

        ivGallery2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(galleryPhoto.openGalleryIntent(), 10);
            }
        });

        ivGallery3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(galleryPhoto.openGalleryIntent(), 11);
            }
        });

        ivGallery4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(galleryPhoto.openGalleryIntent(), 12);
            }
        });

        ivGallery5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(galleryPhoto.openGalleryIntent(), 13);
            }
        });

        ivGallery6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(galleryPhoto.openGalleryIntent(), 14);
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone, business_name, store_name, merchant_address, fcity, outlets, fname, email, dti_certification, business_permit, form9, form13 = null,
                        form49, valid_id, halal_certificate;

                phone = mPhone.substring(1);
                business_name = businessName;
                store_name = title;
                merchant_address = mAddress;
                fcity = city;
                outlets = outlet;
                fname = name;
                email = eAddress;
                dti_certification = encodedImage;
                business_permit = encodedImage1;
                form9 = encodedImage2;
                form49 = encodedImage4;
                valid_id = encodedImage5;
                halal_certificate = encodedImage6;

                if(ivShowImage.getDrawable() == null) dti.setError("This is Required!");
                if(ivShowImage1.getDrawable() == null) bp.setError("This is Required!");
                if(ivShowImage2.getDrawable() == null) Form9.setError("This is Required!");
                if(ivShowImage4.getDrawable() == null) Form49.setError("This is Required!");
                if(ivShowImage5.getDrawable() == null) vid.setError("This is Required!");
                if(ivShowImage6.getDrawable() == null) hc.setError("This is Required!");
                if(ivShowImage3.getDrawable() == null) form13 = "null";
                if(ivShowImage3.getDrawable() != null) form13 = encodedImage3;

                if(!phone.equals("") && !business_name.equals("") && !store_name.equals("") && !merchant_address.equals("") && !fcity.equals("")
                && !fname.equals("") && !email.equals("") && ivShowImage.getDrawable()!=null && ivShowImage1.getDrawable()!=null
                && ivShowImage2.getDrawable()!=null && ivShowImage4.getDrawable()!=null && ivShowImage5.getDrawable()!=null && ivShowImage6.getDrawable()!=null){
                    Handler handler = new Handler();
                    String finalForm1 = form13;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            String[] field = new String[15];
                            field[0] = "phone";
                            field[1] = "business_name";
                            field[2] = "store_name";
                            field[3] = "merchant_address";
                            field[4] = "city";
                            field[5] = "outlets";
                            field[6] = "name";
                            field[7] = "email";
                            field[8] = "dti_certification";
                            field[9] = "business_permit";
                            field[10] = "form9";
                            field[11] = "form13";
                            field[12] = "form49";
                            field[13] = "valid_id";
                            field[14] = "halal_certificate";

                            String[] data = new String[15];
                            data[0] = phone;
                            data[1] = business_name;
                            data[2] = store_name;
                            data[3] = merchant_address;
                            data[4] = fcity;
                            data[5] = outlets;
                            data[6] = fname;
                            data[7] = email;
                            data[8] = dti_certification;
                            data[9] = business_permit;
                            data[10] = form9;
                            data[11] = finalForm1;
                            data[12] = form49;
                            data[13] = valid_id;
                            data[14] = halal_certificate;

                            PutData putData = new PutData("http://192.168.254.109/fadSystem/storeApplication.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if(result.equals("Your application has been submitted!")){
                                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                        builder.setCancelable(true);
                                        builder.setTitle("Merchant Application");
                                        builder.setMessage("Your application has been sent! \n We will contact you if it is verified.");
                                        builder.setPositiveButton("Confirm",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        FirebaseAuth.getInstance().signOut();
                                                        Intent login = new Intent(getApplicationContext(), Login.class);
                                                        startActivity(login);
                                                        finish();
                                                    }
                                                });
                                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        });

                                        AlertDialog dialog = builder.create();
                                        dialog.show();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ivShowImage.setImageBitmap(bitmap);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] byteOfImages = byteArrayOutputStream.toByteArray();
                encodedImage = Base64.encodeToString(byteOfImages, Base64.DEFAULT);
            }
            else if (requestCode == 2) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ivShowImage1.setImageBitmap(bitmap);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] byteOfImages = byteArrayOutputStream.toByteArray();
                encodedImage1 = Base64.encodeToString(byteOfImages, Base64.DEFAULT);
            }
            else if (requestCode == 3) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ivShowImage2.setImageBitmap(bitmap);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] byteOfImages = byteArrayOutputStream.toByteArray();
                encodedImage2 = Base64.encodeToString(byteOfImages, Base64.DEFAULT);
            }
            else if (requestCode == 4) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ivShowImage3.setImageBitmap(bitmap);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] byteOfImages = byteArrayOutputStream.toByteArray();
                encodedImage3 = Base64.encodeToString(byteOfImages, Base64.DEFAULT);
            }
            else if (requestCode == 5) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ivShowImage4.setImageBitmap(bitmap);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] byteOfImages = byteArrayOutputStream.toByteArray();
                encodedImage4 = Base64.encodeToString(byteOfImages, Base64.DEFAULT);
            }
            else if (requestCode == 6) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ivShowImage5.setImageBitmap(bitmap);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] byteOfImages = byteArrayOutputStream.toByteArray();
                encodedImage5 = Base64.encodeToString(byteOfImages, Base64.DEFAULT);
            }
            else if (requestCode == 7) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ivShowImage6.setImageBitmap(bitmap);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] byteOfImages = byteArrayOutputStream.toByteArray();
                encodedImage6 = Base64.encodeToString(byteOfImages, Base64.DEFAULT);
            }
            else if (requestCode == 8) {
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

            else if (requestCode == 9) {
                Uri uri = data.getData();
                galleryPhoto.setPhotoUri(uri);
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                    ivShowImage1.setImageBitmap(bitmap);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] byteOfImages = byteArrayOutputStream.toByteArray();
                    encodedImage1 = Base64.encodeToString(byteOfImages, Base64.DEFAULT);
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Something Wrong while loading photos", Toast.LENGTH_SHORT).show();
                }
            }

            else if (requestCode == 10) {
                Uri uri = data.getData();
                galleryPhoto.setPhotoUri(uri);
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                    ivShowImage2.setImageBitmap(bitmap);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] byteOfImages = byteArrayOutputStream.toByteArray();
                    encodedImage2 = Base64.encodeToString(byteOfImages, Base64.DEFAULT);
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Something Wrong while loading photos", Toast.LENGTH_SHORT).show();
                }
            }

            else if (requestCode == 11) {
                Uri uri = data.getData();
                galleryPhoto.setPhotoUri(uri);
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                    ivShowImage3.setImageBitmap(bitmap);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] byteOfImages = byteArrayOutputStream.toByteArray();
                    encodedImage3 = Base64.encodeToString(byteOfImages, Base64.DEFAULT);
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Something Wrong while loading photos", Toast.LENGTH_SHORT).show();
                }
            }

            else if (requestCode == 12) {
                Uri uri = data.getData();
                galleryPhoto.setPhotoUri(uri);
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                    ivShowImage4.setImageBitmap(bitmap);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] byteOfImages = byteArrayOutputStream.toByteArray();
                    encodedImage4 = Base64.encodeToString(byteOfImages, Base64.DEFAULT);
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Something Wrong while loading photos", Toast.LENGTH_SHORT).show();
                }
            }

            else if (requestCode == 13) {
                Uri uri = data.getData();
                galleryPhoto.setPhotoUri(uri);
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                    ivShowImage5.setImageBitmap(bitmap);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] byteOfImages = byteArrayOutputStream.toByteArray();
                    encodedImage5 = Base64.encodeToString(byteOfImages, Base64.DEFAULT);
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Something Wrong while loading photos", Toast.LENGTH_SHORT).show();
                }
            }

            else if (requestCode == 14) {
                Uri uri = data.getData();
                galleryPhoto.setPhotoUri(uri);
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                    ivShowImage6.setImageBitmap(bitmap);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] byteOfImages = byteArrayOutputStream.toByteArray();
                    encodedImage6 = Base64.encodeToString(byteOfImages, Base64.DEFAULT);
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Something Wrong while loading photos", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    public  void enableRuntimePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(StoreApplication2.this, Manifest.permission.CAMERA)){
            Toast.makeText(StoreApplication2.this, "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();
        }else {
            ActivityCompat.requestPermissions(StoreApplication2.this, new String[]{Manifest.permission.CAMERA}, RequestPermissionCode);
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