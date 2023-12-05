package com.leopa.livechatter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class aboutUs extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        TextView textView;

        textView = findViewById(R.id.customBarText);
        textView.setText("About Us");


    }
    public void back (View view){finish();}
}