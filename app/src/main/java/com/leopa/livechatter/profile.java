package com.leopa.livechatter;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class profile extends AppCompatActivity {

    private ImageView img;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ImageButton btn;
    Bitmap bitmap;
    String encodedImage;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        EditText editName = findViewById(R.id.profileName);



        //Picking the image from gallery
        btn = findViewById(R.id.pickBtn);
        img = findViewById(R.id.imgProfile);
        btn.setOnClickListener(new View.OnClickListener() {
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
                    img.setImageBitmap(bitmap);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] b = baos.toByteArray();
                    encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //Toolbar

        //getting image and text from sp and setting it to their views.
        SharedPreferences sharedPreferences = getSharedPreferences("UserData",MODE_PRIVATE);

        String kimg = sharedPreferences.getString("userImg","");
        byte[] b = Base64.decode(kimg, Base64.DEFAULT);
        bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        img.setImageBitmap(bitmap);    //image
        String k = sharedPreferences.getString("userName"," ");
        editName.setText(k);     //text



        // done button
        Button doneButton = findViewById(R.id.buttonDone);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editName.getText().toString();

                uploadUserDataToSP(name,encodedImage);  //uploading the user data to sharedprefrances using this function.

                Toast.makeText(profile.this, name, Toast.LENGTH_SHORT).show();
              // resouces.setUserNameOfficial(name);

              // resouces.setImageSrc(bitmap);
              finish();

            }
        });

        }



    //Custom Methods Here

    private void openGallery() { // this method opens gallery
        // Create an Intent to pick an image from the gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(galleryIntent);
    }

     // displaying new image from user input to image view

        //ToolBar Exit Method



    private void uploadUserDataToSP(String nameValue, String imgValue) { // this method uploads data to sp

        SharedPreferences sharedPreferences = getSharedPreferences("UserData",MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();

        edit.putBoolean("logIn",true);
        edit.putString("userName", nameValue);
        edit.putString("userImg", imgValue);
        edit.apply();
    }

    public void back (View view){finish();}

}