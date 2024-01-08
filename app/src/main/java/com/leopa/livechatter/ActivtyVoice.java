package com.leopa.livechatter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.SetOptions;

import com.leopa.livechatter.utils_constants.FireBase;
import com.leopa.livechatter.webrtc.AppRtcClient;
import com.leopa.livechatter.webrtc.CustomPeerObserver;
import org.webrtc.IceCandidate;

import org.webrtc.MediaStream;

import org.webrtc.PeerConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class ActivtyVoice extends AppCompatActivity {
    AppRtcClient appRtcClient;
    FirebaseAuth mAuth;
    String UID = FirebaseAuth.getInstance().getUid();
    Boolean sender;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference = db.collection("matchRoomVoice");
    ListenerRegistration listenerRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_voice);
        mAuth = FirebaseAuth.getInstance();
        authListner();



        appRtcClient = new AppRtcClient(getApplicationContext(),new CustomPeerObserver(){
            @Override
            public void onAddStream(MediaStream mediaStream) {
                super.onAddStream(mediaStream);
                try{
                    //adding media Stream for volume
                    mediaStream.audioTracks.get(0).setEnabled(true);
                    Log.d("TAG : Audio Status", mediaStream.audioTracks.get(0).toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onIceCandidate(IceCandidate iceCandidate) {
                super.onIceCandidate(iceCandidate);
                appRtcClient.sendIceCandidate(iceCandidate);
                Log.d("TAG : action", "onIceCandidate: ice candidate must be send" + (iceCandidate).toString());
            }

            @Override
            public void onConnectionChange(PeerConnection.PeerConnectionState newState) {
                super.onConnectionChange(newState);
                Log.d("TAG", "onConnectionChange: " + newState);
                if (newState == PeerConnection.PeerConnectionState.CONNECTED) {
                    webrtcConnected();
                    Log.d("TAG : position", "onConnectionChange: webrtc connected must be called");
                }
                if (newState == PeerConnection.PeerConnectionState.CLOSED ||
                        newState == PeerConnection.PeerConnectionState.DISCONNECTED) {
                    webrtcClosed();
                    Log.d("TAG : position", "onConnectionChange: webrtc closed must be called");
                }
            }
        });

        findUser();
    }

    private void findUser() {
        collectionReference.whereEqualTo("user2", "").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        createRoom();
                    } else {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            String documentName = documentSnapshot.getId();
                            String userJoin = documentSnapshot.getString("user1");
                            assert userJoin != null;
                            Log.d("TAG : other user id",userJoin);
                            DocumentReference dcod = collectionReference.document(documentName);
                            Map<String, Object> userPutData = new HashMap<>();
                            userPutData.put("user2", UID);
                            userPutData.put("connectedTo",userJoin);
                            dcod.set(userPutData, SetOptions.merge());
                            connectRoom(userJoin);

                        }
                    }
                }).addOnFailureListener(e -> {
                });
    }

    private void connectRoom(String otherUserId) {
        sender = false;
        appRtcClient.initAudio();
        new FireBase(UID,otherUserId,false);
    }

    private void createRoom() {
        DocumentReference createRoomRef = collectionReference.document(UID);
        Map<String, Object> userPutData = new HashMap<>();
        userPutData.put("user1", UID);
        userPutData.put("user2", "");
        createRoomRef.set(userPutData).addOnFailureListener(e -> {
            finish();
        });

        listenerRegistration = db.collection("matchRoomVoice").whereEqualTo("connectedTo", UID)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        // Handle error
                        Log.w("TAG", "Listen failed.", e);
                        return;
                    }
                    if (snapshots != null) {
                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            DocumentSnapshot document = dc.getDocument();
                            listenerRegistration.remove();
                            String otherUserId = document.get("user2",String.class);
                            assert otherUserId != null;
                            Log.d("TAG : other user id",otherUserId);
                            appRtcClient.initAudio();
                            sender = true;
                            new FireBase(UID,otherUserId,true);

                        }
                    }
                });
    }
    public void back(View view) {
        finish();

    }

    private void authListner() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(firebaseAuth -> {
            if (firebaseAuth.getCurrentUser() != null ) {
                UID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                Toast.makeText(this, "uid is : " + UID, Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(ActivtyVoice.this, login_page.class));
            }
        });
    }
    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, "onStop called", Toast.LENGTH_SHORT).show();
    }

    private void webrtcConnected() {
        runOnUiThread(() -> {
            Log.d("TAG : position", "in webrtc connected");

        });
    }

    public void webrtcClosed() {
        runOnUiThread(() -> {
            Log.d("TAG : position", "in webrtc closed");

        });
    }

}
