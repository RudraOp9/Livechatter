package com.leopa.livechatter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        TextView AboutUs,logOut;


        if (checkLoggedIn()){

        }else {
            startActivity( new Intent(this, login_page_profile.class));
            finish();
        }

        TextView editProfile = findViewById(R.id.buttonProfile);


        drawerLayout = findViewById(R.id.drawerLayout);



        // ONLY ON CLICK LISTNERS HERE


        //switching to edit profile activity
      // Button btn = findViewById(R.id.buttonProfile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDrawer(drawerLayout);
                Intent switchActivity = new Intent(MainActivity.this,profile.class);
                startActivity(switchActivity);
            }
        });   //button ends here

        AboutUs = findViewById(R.id.AboutUs);
        logOut = findViewById(R.id.logOut);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,login_page.class));
                finish();
            }
        });
        AboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, aboutUs.class));
            }
        });




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
    public void settings (View view){
        Intent switchActivity = new Intent(this , settings.class);
        closeDrawer(drawerLayout);
        startActivity(switchActivity);
    }

    public void pagal (View view){
        openDrawer(drawerLayout);
    }

    private boolean checkLoggedIn() {

        SharedPreferences sp1 = getSharedPreferences("UserData",MODE_PRIVATE);
        int value = sp1.getInt("isLoggedIn",9);
        if(value==0){
            return true;// 0 is logged in 1 is not loggedddd
        }
        return false;
    }




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
        textView.setText("Welcome " +k+ " !");

        Toast.makeText(this, "you called on resume", Toast.LENGTH_SHORT).show();



    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "you called on pause", Toast.LENGTH_SHORT).show();
        closeDrawer(drawerLayout);
    }

/*    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }else {
            startActivity(new Intent(this,login_page.class));
        }
    }*/


}
