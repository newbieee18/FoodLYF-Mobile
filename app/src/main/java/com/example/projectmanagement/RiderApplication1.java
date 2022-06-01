package com.example.projectmanagement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RiderApplication1 extends AppCompatActivity {

    private final AppCompatActivity activity = RiderApplication1.this;
    Spinner riderGender;
    ArrayAdapter adapter;
    TextView dp, dl, Nbi, Tin;
    EditText riderName, riderBirthDate, riderEmail, contactName, contactRelationship, contactNumber;
    DatePickerDialog datePickerDialog;
    ImageView ivShowImage, ivShowImage1, ivShowImage2, ivShowImage3;
    ImageView ivCamera, ivCamera1, ivCamera2, ivCamera3;
    ImageView ivGallery, ivGallery1, ivGallery2, ivGallery3;
    Button btnSave;
    String phoneNumber;
    CameraPhoto cameraPhoto;
    GalleryPhoto galleryPhoto;
    Intent camera;
    String encodedImage, encodedImage1, encodedImage2, encodedImage3;
    public static final int RequestPermissionCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_application1);

        phoneNumber = getIntent().getExtras().getString("phoneNumber");
        dp = findViewById(R.id.textViewDP);
        dl = findViewById(R.id.textViewDL);
        Nbi = findViewById(R.id.textViewNBI);
        Tin = findViewById(R.id.textViewTIN);
        ivShowImage = findViewById(R.id.ivShowImage);
        ivShowImage1 = findViewById(R.id.ivShowImage1);
        ivShowImage2 = findViewById(R.id.ivShowImage2);
        ivShowImage3 = findViewById(R.id.ivShowImage3);
        ivCamera = findViewById(R.id.ivCamera);
        ivCamera1 = findViewById(R.id.ivCamera1);
        ivCamera2 = findViewById(R.id.ivCamera2);
        ivCamera3 = findViewById(R.id.ivCamera3);
        ivGallery = findViewById(R.id.ivGallery);
        ivGallery1 = findViewById(R.id.ivGallery1);
        ivGallery2 = findViewById(R.id.ivGallery2);
        ivGallery3 = findViewById(R.id.ivGallery3);
        contactName = findViewById(R.id.contactName);
        contactRelationship = findViewById(R.id.contactRelationship);
        contactNumber = findViewById(R.id.contactNumber);
        btnSave = findViewById(R.id.btnSave);
        riderName = findViewById(R.id.riderName);
        riderGender = findViewById(R.id.riderGender);
        riderBirthDate = findViewById(R.id.riderBirthDate);
        riderEmail = findViewById(R.id.riderEmail);
        riderGender = findViewById(R.id.riderGender);

        contactNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        String[] genderList = {"Male", "Female"};

        adapter = new ArrayAdapter(this, R.layout.spinner_item, genderList);
        riderGender.setAdapter(adapter);

        riderBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(RiderApplication1.this,
                        new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // set day of month , month and year value in the edit text
                        riderBirthDate.setText((monthOfYear+1) + "/" + dayOfMonth + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });

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

        ivGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(galleryPhoto.openGalleryIntent(), 5);
            }
        });

        ivGallery1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(galleryPhoto.openGalleryIntent(), 6);
            }
        });

        ivGallery2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(galleryPhoto.openGalleryIntent(), 7);
            }
        });

        ivGallery3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(galleryPhoto.openGalleryIntent(), 8);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String phone, fullname, gender, birth_date, email, display_picture, drivers_license, nbi, tin, contact_person, person_relationship, person_number;

                phone = phoneNumber;
                fullname = riderName.getText().toString();
                gender = riderGender.getSelectedItem().toString();
                birth_date = riderBirthDate.getText().toString();
                email = riderEmail.getText().toString();
                display_picture = encodedImage;
                drivers_license = encodedImage1;
                nbi = encodedImage2;
                tin = encodedImage3;
                contact_person = contactName.getText().toString();
                person_relationship = contactRelationship.getText().toString();
                person_number = contactNumber.getText().toString().replaceAll("[^0-9]+", "");;

                if(fullname.equals("")) riderName.setError("Please input your name");
                if(birth_date.equals("")) riderBirthDate.setError("Please input your birthdate");
                if(email.equals("") || !isValidEmail(email)) riderEmail.setError("Invalid Email");
                if(contactName.getText().toString().equals("")) contactName.setError("Please input contact person name");
                if(person_relationship.equals("")) contactRelationship.setError("Please input relationship");
                if(person_number.equals("")) contactNumber.setError("Please enter contact number");

                if(!phone.equals("") && !fullname.equals("") && !gender.equals("") && !birth_date.equals("") && !email.equals("") && isValidEmail(email)
                        && ivShowImage.getDrawable() != null && ivShowImage1.getDrawable() != null && ivShowImage2.getDrawable() != null
                        && ivShowImage3.getDrawable() != null && !contact_person.equals("") && !person_relationship.equals("") && !person_number.equals("")) {
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[12];
                            field[0] = "phone";
                            field[1] = "fullname";
                            field[2] = "gender";
                            field[3] = "birth_date";
                            field[4] = "display_picture";
                            field[5] = "drivers_license";
                            field[6] = "nbi";
                            field[7] = "tin";
                            field[8] = "contact_person";
                            field[9] = "person_relationship";
                            field[10] = "person_number";
                            field[11] = "email";
                            //Creating array for data
                            String[] data = new String[12];
                            data[0] = phone;
                            data[1] = fullname;
                            data[2] = gender;
                            data[3] = birth_date;
                            data[4] = display_picture;
                            data[5] = drivers_license;
                            data[6] = nbi;
                            data[7] = tin;
                            data[8] = contact_person;
                            data[9] = person_relationship;
                            data[10] = person_number;
                            data[11] = email;
                            PutData putData = new PutData("http://192.168.254.109/fadSystem/riderApplication.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if(result.equals("Application Success!")){
                                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                        builder.setCancelable(true);
                                        builder.setTitle("Rider Application");
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
                            //End Write and Read data with URL
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
            else if (requestCode == 6) {
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
            else if (requestCode == 7) {
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
            else if (requestCode == 8) {
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

        }
    }

    public  void enableRuntimePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(RiderApplication1.this, Manifest.permission.CAMERA)){
            Toast.makeText(RiderApplication1.this, "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();
        }else {
            ActivityCompat.requestPermissions(RiderApplication1.this, new String[]{Manifest.permission.CAMERA}, RequestPermissionCode);
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