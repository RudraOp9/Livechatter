package com.leopa.livechatter;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;

public class login_page_profile extends AppCompatActivity {

    Button doneButtonLogin;
    ImageButton pickImgLogin;
    EditText profileNameLogin;
    ImageView profileImgLogin;
    private ActivityResultLauncher<Intent> galleryLauncher;
    Bitmap bitmap;
    String encodedImage;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page_profile);

        doneButtonLogin = findViewById(R.id.buttonDoneLogin);
        pickImgLogin = findViewById(R.id.pickBtnLogin);
        profileImgLogin = findViewById(R.id.imgProfileLogin);
        profileNameLogin = findViewById(R.id.profileNameLogin);


        final int[] valueKey = {2};
        SharedPreferences sp1 = getSharedPreferences("UserData",MODE_PRIVATE);
        int value = sp1.getInt("isLoggedIn",9);
        if(value==0){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

            // 0 is logged in 1 is not loggedddd
        }else{

        }


        pickImgLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });


        // Initialize ActivityResultLauncher
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                // Get the selected image URI
                Uri selectedImageUri = result.getData().getData();

                try {
                    // Convert the URI to a Bitmap and set it to the ImageView
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                    profileImgLogin.setImageBitmap(bitmap);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] b = baos.toByteArray();
                    encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        doneButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 String name;
                 name = profileNameLogin.getText().toString().trim();

                 uploadUserDataToSP(name,encodedImage);  //uploading the user data to sharedprefrances using this function.

                if (name.isEmpty()){
                    Toast.makeText(login_page_profile.this, "fields are empty", Toast.LENGTH_SHORT).show();
                }else {

                    Toast.makeText(login_page_profile.this, name, Toast.LENGTH_SHORT).show();

                    // setting so that user do not come back here onards
                    valueKey[0] = 0;
                    SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                    SharedPreferences.Editor edit1 = sharedPreferences.edit();
                    edit1.putInt("isLoggedIn", valueKey[0]);
                    edit1.apply();

                    //come here soon and give some intent
                    Intent intent = new Intent(login_page_profile.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }


            }
        });



    }
    //Methods here

    private void openGallery() { // this method opens gallery
        // Create an Intent to pick an image from the gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(galleryIntent);
    }


    private void uploadUserDataToSP(String nameValue, String imgValue) { // this method uploads data to sp

        SharedPreferences sharedPreferences = getSharedPreferences("UserData",MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();

        edit.putString("userName", nameValue);
        edit.putString("userImg", imgValue);
        edit.commit();
    }
}