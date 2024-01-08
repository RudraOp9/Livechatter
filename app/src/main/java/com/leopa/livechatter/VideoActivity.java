package com.leopa.livechatter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.leopa.livechatter.model.DataModel;
import com.leopa.livechatter.model.DataModelType;
import com.leopa.livechatter.utils_constants.MainRepository;

import org.webrtc.SurfaceViewRenderer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class VideoActivity extends AppCompatActivity implements MainRepository.Listener{


    //webrtc here

    private MainRepository mainRepository;
    private Boolean isCameraMuted = false;
    private Boolean isMicrophoneMuted = false;
    private SurfaceViewRenderer localView;
    private SurfaceViewRenderer remoteView;


    private String otherUserId;
    boolean Boolean;
    final String one = "4ulZcx1npyc38W6zDlvBO6ZVkMh2";
    final String two = "WdIWB4mNyuSKWYBUuqhfGaEWERw1";
    Button micButton;



    //webrtc here



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        mainRepository = MainRepository.getInstance();
        String UID =" kuch nhi";

        UID = DataModel.DataModeluid;
        Toast.makeText(this, UID, Toast.LENGTH_SHORT).show();

        localView = findViewById(R.id.local_view);
        remoteView = findViewById(R.id.remote_view);
        micButton = findViewById(R.id.micButton);



        try {
            switch (UID) {
                case one:
                    otherUserId = two;
                    Boolean = true;

                    break;
                case two :
                    otherUserId = one;
                    Boolean= false;

                    break;
                default:
                    break;
            }
        }catch (Exception e){
            Log.d("error",e.toString());
        }

        //webrtc here

        //from login activity


        //if success then we want to move to call activity
        mainRepository.login(UID, otherUserId,this,this::init
            );


        //end
        //webrtc here
        //on create end
    }
    // custom methods here

    public void back (View view){finish();}



    //webrtc here
    // let one be the one who calls , two picks and , but we by one sends offer , two receive offer , then both sends ice.


    private void init(){
        mainRepository = MainRepository.getInstance();
            if (Boolean){
                mainRepository.startCall(otherUserId);
                mainRepository.subscribeForLatestEvent(data->{});
            }else {
                mainRepository.subscribeForLatestEvent(data->{});
            }
        mainRepository.initLocalView(localView);
        mainRepository.initRemoteView(remoteView);
        mainRepository.listener = VideoActivity.this;

        micButton.setOnClickListener(v->{
            if (isMicrophoneMuted){
                Toast.makeText(this, "mic is muted , unmuting now", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "mic is unmuted , muting now", Toast.LENGTH_SHORT).show();

            }
            mainRepository.toggleAudio(isMicrophoneMuted);
            isMicrophoneMuted=!isMicrophoneMuted;
        });

        micButton.setOnClickListener(v->{
            if (isCameraMuted){
                Toast.makeText(this, "vid is muted , unmuting now", Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(this, "vid is unmuted , muting now", Toast.LENGTH_SHORT).show();
            }
            mainRepository.toggleVideo(isCameraMuted);
            isCameraMuted=!isCameraMuted;
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainRepository.endCall();

    }

    @Override
    public void webrtcConnected() {
        runOnUiThread(()->{
            Toast.makeText(this, "Webrtc connected", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void webrtcClosed() {
        runOnUiThread(this::finish);
    }

    //webrtc here





    //end
}