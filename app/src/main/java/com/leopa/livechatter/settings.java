package com.leopa.livechatter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

public class settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        AppCompatSpinner vidQuaSpin = findViewById(R.id.videoQuality);
        String[] quality = {"Auto","High","Medium","Low"};
        ArrayAdapter<String> qualityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_gallery_item,quality);
        vidQuaSpin.setAdapter(qualityAdapter);






    }

    public void back (View view){finish();}
}