package com.leopa.livechatter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    redirect[] myListData;

    redirectAdapter adapter;


    @Override
    protected void onResume() {
        // put your code here...
        super.onResume();
        resouces resouces = new resouces();
        String ututu = resouces.getUserName();
        TextView userNameView = findViewById(R.id.name1);
        userNameView.setText(ututu);
        Toast.makeText(this, "you called on resume", Toast.LENGTH_SHORT).show();



    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "you called on pausae", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        myListData = new redirect[]{
                new redirect(R.drawable.donate , "Donate"),
                new redirect(R.drawable.facebook, "FaceBook"),
                new redirect(R.drawable.instagram, "InstaGram"),
                new redirect(R.drawable.git, "GitHub")
        };

        adapter = new redirectAdapter(myListData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


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
        });

        // getting new username from profile activity and seting to the textview

        resouces resouces = new resouces();
        String ututu = resouces.getUserName();
        Intent intent1 = getIntent();
        //String username = intent1.getStringExtra("name");
        TextView userNameView = findViewById(R.id.name1);

            userNameView.setText(ututu);




       // ImageView imgg = findViewById(R.id.imageView5);
        //imgg.setImageDrawable(Drawable.createFromPath(String.valueOf(R.drawable._314955)));




    }
// CUSTOM METHODS

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


}
