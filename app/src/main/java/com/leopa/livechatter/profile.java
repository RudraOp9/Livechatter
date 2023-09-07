package com.leopa.livechatter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar ab = (Toolbar) findViewById(R.id.abc);
        setSupportActionBar(ab);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

            ImageView img = findViewById(R.id.profileImg);
            TextView edt = findViewById(R.id.profileName);

            Intent i = getIntent();
            String a = i.getStringExtra("name");
            int img1 = i.getIntExtra("img", 0);
              if (img1 != 0) {
                img.setImageResource(img1);
              }
            edt.setText(a);

        }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}