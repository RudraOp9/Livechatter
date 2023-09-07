package com.leopa.livechatter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView name_id = findViewById(R.id.name);
        Button btn = findViewById(R.id.button);
       /* ImageView pImage = findViewById(R.id.imageView);*/

        String name =  name_id.getText().toString();
       /* Drawable img = pImage.getDrawable();*/


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchProfile = new Intent(getApplicationContext() , profile.class);
                int img = R.drawable.images__3_ ;
                switchProfile.putExtra("name" , name );
                switchProfile.putExtra("img" , img);

                startActivity(switchProfile);
                }
             });


    }
        public void faceRedirect(View view) {
            Intent openBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/narendramodi/"));
            startActivity(openBrowser);
        }

        public void instaRedirect(View view) {
            Intent openBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/artist_kratika76/"));
            startActivity(openBrowser);
        }

        public void gitRedirect(View view) {
            Intent openBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/RudraOp9"));
            startActivity(openBrowser);
        }

        public void donateMe(View view) {
            Intent openBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/RudraOp9"));
            startActivity(openBrowser);
        }
         public void videoOpen (View view){
        Intent switchActivity = new Intent(this , VideoActivity.class);
        startActivity(switchActivity);
    }
         public void msgOpen (View view){
        Intent switchActivity = new Intent(this , MsgActivity.class);
        startActivity(switchActivity);
    }
    public void voiceOpen (View view){
        Intent switchActivity = new Intent(this , ActivtyVoice.class);
        startActivity(switchActivity);
    }

}