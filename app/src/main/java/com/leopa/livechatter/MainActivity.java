package com.leopa.livechatter;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
// CUSTOM METHODS

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
