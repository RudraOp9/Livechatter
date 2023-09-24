package com.leopa.livechatter;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

public class profile extends AppCompatActivity {

    private ActivityResultLauncher<String> pickImageLauncher;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ImageButton btn = findViewById(R.id.pickBtn);
        //Picking the image
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
                            // Handle the selected image URI here
                            displaySelectedImage(result);
                        }
                    }
                });

        //Selecting image
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the file picker
                pickImageLauncher.launch("image/*");
            }
        });


        //Toolbar
        Toolbar ab = (Toolbar) findViewById(R.id.abc);
        setSupportActionBar(ab);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


         // Getting Name And image from mainActivity

        }

 //Custom Methods Here

     // displaying new image from user input to image view
    private void displaySelectedImage(Uri imageUri) {
        try {
            ContentResolver contentResolver = getContentResolver();
            InputStream inputStream = contentResolver.openInputStream(imageUri);

            if (inputStream != null) {
                // Load the image into the ImageView
                img.setImageDrawable(Drawable.createFromStream(inputStream, imageUri.toString()));

                // Save the image to internal storage if needed
                // You can use a FileOutputStream to write the image to internal storage
                // Here, you'd save it to a location within your app's internal files directory.
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        //ToolBar Exit Method
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}