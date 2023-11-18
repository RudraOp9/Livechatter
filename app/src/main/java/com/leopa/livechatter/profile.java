package com.leopa.livechatter;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.InputStream;

public class profile extends AppCompatActivity {

    private ActivityResultLauncher<String> pickImageLauncher;
    private ImageView img;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ImageButton btn = findViewById(R.id.pickBtn);
        EditText editName = findViewById(R.id.profileName);

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

        //done button
        resouces resouces = new resouces();

        editName.setText( resouces.getUserName());


        Button doneButton = findViewById(R.id.buttonDone);




        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editName.getText().toString();

                Toast.makeText(profile.this, name, Toast.LENGTH_SHORT).show();
                resouces.changeUserName(name);
                finish();

            }
        });

        // getting the name from mainactivity

      /*  Intent intent223 = getIntent();
        String userName= intent223.getStringExtra("userName");
       // editName.setText(userName);
        String j = String.valueOf(R.raw.username);
        */
        //displaying image



         // Getting Name And image from mainActivity

        }

 //Custom Methods Here

     // displaying new image from user input to image view
    private void displaySelectedImage(Uri imageUri) {
        

        try {
            ContentResolver contentResolver = getContentResolver();
            InputStream inputStream = contentResolver.openInputStream(imageUri);

            if (inputStream != null) {
                //ImageView img ;
               // img.findViewById(R.id.imgProfile);
                // Load the image into the ImageView
               // img.setImageDrawable(Drawable.createFromStream(inputStream, imageUri.toString()));
                File imgFile = new File((imageUri).getPath());
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    ImageView myImage = (ImageView) findViewById(R.id.imgProfile);
                    myImage.setImageBitmap(myBitmap);


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