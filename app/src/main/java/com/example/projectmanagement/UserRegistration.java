package com.example.projectmanagement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

public class UserRegistration extends AppCompatActivity {

    EditText user, pass, fName, Age, Address, Contact, Email;
    Spinner Gender, Register;
    ImageView ivCamera, ivGallery, ivShowImage;
    ArrayAdapter adapter, adapter1;
    Button signup;
    CameraPhoto cameraPhoto;
    GalleryPhoto galleryPhoto;
    String encodedImage;
    Intent camera;
    public static final int RequestPermissionCode = 1;
    final int GALLERY_REQUEST = 21313;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        String[] genderList = {"Gender", "Male", "Female"};
        String[] registerList = {"Register As", "Driver", "Customer"};

        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, genderList);
        adapter1 = new ArrayAdapter(this,android.R.layout.simple_list_item_1, registerList);
        user = findViewById(R.id.username);
        pass = findViewById(R.id.password);
        fName = findViewById(R.id.fname);
        Age = findViewById(R.id.age);
        Address = findViewById(R.id.address);
        Contact = findViewById(R.id.contact);
        Email = findViewById(R.id.email);
        Gender = findViewById(R.id.sex);
        Register = findViewById(R.id.register);
        signup = findViewById(R.id.btnSignup);
        ivCamera = findViewById(R.id.ivCamera);
        ivGallery = findViewById(R.id.ivGallery);
        ivShowImage = findViewById(R.id.ivShowImage);

        Gender.setAdapter(adapter);
        Register.setAdapter(adapter1);
        Gender.setSelection(0, false);
        Register.setSelection(0, false);

        cameraPhoto = new CameraPhoto(getApplicationContext());
        galleryPhoto = new GalleryPhoto(getApplicationContext());
        enableRuntimePermission();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String username, password, fullname, age, gender, contact, email, address, position, img;

                username = String.valueOf(user.getText());
                password = String.valueOf(pass.getText());
                fullname = String.valueOf(fName.getText());
                age = String.valueOf(Age.getText());
                gender = String.valueOf(Gender.getSelectedItem());
                contact = String.valueOf(Contact.getText());
                email = String.valueOf(Email.getText());
                address = String.valueOf(Address.getText());
                position = String.valueOf(Register.getSelectedItem());
                img = encodedImage;

                if(!username.equals("") && !password.equals("") && !fullname.equals("") && !age.equals("")
                        && !gender.equals("Gender") && !contact.equals("") && !email.equals("") && !address.equals("") && !position.equals("Register As") && !img.equals("")) {
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[10];
                            field[0] = "username";
                            field[1] = "password";
                            field[2] = "fullname";
                            field[3] = "age";
                            field[4] = "gender";
                            field[5] = "contact";
                            field[6] = "email";
                            field[7] = "address";
                            field[8] = "position";
                            field[9] = "img";
                            //Creating array for data
                            String[] data = new String[10];
                            data[0] = username;
                            data[1] = password;
                            data[2] = fullname;
                            data[3] = age;
                            data[4] = gender;
                            data[5] = contact;
                            data[6] = email;
                            data[7] = address;
                            data[8] = position;
                            data[9] = img;
                            PutData putData = new PutData("http://192.168.254.109/fadSystem/userRegistration.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if(result.equals("Sign Up Success")){
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                        Intent login = new Intent(getApplicationContext(), Login.class);
                                        startActivity(login);
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            //End Write and Read data with URL
                        }

                    });
                }
                else{
                    Toast.makeText(getApplicationContext(), "All fields required", Toast.LENGTH_SHORT).show();
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
        if(ActivityCompat.shouldShowRequestPermissionRationale(UserRegistration.this, Manifest.permission.CAMERA)){
            Toast.makeText(UserRegistration.this, "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();
        }else {
            ActivityCompat.requestPermissions(UserRegistration.this, new String[]{Manifest.permission.CAMERA}, RequestPermissionCode);
        }
    }


}