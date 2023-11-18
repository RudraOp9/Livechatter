package com.leopa.livechatter;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class profile extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView img;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ImageButton btn;
    Bitmap bitmap;




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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //Toolbar
        Toolbar ab = (Toolbar) findViewById(R.id.abc);
        setSupportActionBar(ab);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //done button
        resouces resouces = new resouces();

        try {
            img.setImageBitmap(resouces.getImageSrc());
            bitmap=resouces.getImageSrc();
        } catch (Exception e) {
            e.printStackTrace();
        }




        editName.setText( resouces.getUserNameOfficial());
        Button doneButton = findViewById(R.id.buttonDone);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editName.getText().toString();

                Toast.makeText(profile.this, name, Toast.LENGTH_SHORT).show();
               resouces.setUserNameOfficial(name);

               resouces.setImageSrc(bitmap);
              finish();

            }
        });

        }

 //Custom Methods Here

    private void openGallery() {
        // Create an Intent to pick an image from the gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(galleryIntent);
    }

     // displaying new image from user input to image view

        //ToolBar Exit Method
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}