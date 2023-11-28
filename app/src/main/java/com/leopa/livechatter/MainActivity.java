package com.leopa.livechatter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {







    DrawerLayout drawerLayout;



    @Override
    protected void onResume() {
        // put your code here...
        super.onResume();
        TextView textView =  findViewById(R.id.name1);
        ImageView imageView = findViewById(R.id.profileImg);


        SharedPreferences sharedPreferences = getSharedPreferences("UserData",MODE_PRIVATE);
        String k = sharedPreferences.getString("userName"," ");
        String kimg = sharedPreferences.getString("userImg","");
        byte[] b = Base64.decode(kimg, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        imageView.setImageBitmap(bitmap);
        textView.setText(k);

        Toast.makeText(this, "you called on resume", Toast.LENGTH_SHORT).show();



    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "you called on pause", Toast.LENGTH_SHORT).show();
       // closeDrawer(drawerLayout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView menu;
        DrawerLayout drawerLayout;

        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);






        //munu drawer button

       /* menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });*/


        // sending user name and switching to edit profile activity
        Button btn = findViewById(R.id.buttonProfile);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView txt = findViewById(R.id.name1);
                String name = (String) txt.getText();
                Intent switchActivity = new Intent(MainActivity.this,profile.class);
                startActivity(switchActivity);
                //switchActivity.putExtra("userName",name);
            }
        });   //button ends here




    }
// CUSTOM METHODS

    public static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public static void closeDrawer(DrawerLayout drawerLayout){
      if (drawerLayout.isDrawerOpen(GravityCompat.START)){
          drawerLayout.closeDrawer(GravityCompat.START);
      }
    }

       /* public void faceRedirect(View view) {
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
        }*/
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

    public void pagal (View view){
        openDrawer(drawerLayout);
    }


}
