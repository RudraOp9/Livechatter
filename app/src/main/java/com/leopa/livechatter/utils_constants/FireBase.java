package com.leopa.livechatter.utils_constants;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.common.net.InternetDomainName;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.leopa.livechatter.ActivtyVoice;
import com.leopa.livechatter.webrtc.AppRtcClient;

import org.webrtc.IceCandidate;
import org.webrtc.SessionDescription;

public class FireBase {
     String otherUserId;
     String UId;
     boolean sender;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    AppRtcClient appRtcClient;
    private final Gson gson = new Gson();

    public FireBase(String UID, String otherUserId,boolean sender){
        this.UId = UID ;
        this.otherUserId = otherUserId;
        this.sender = sender;
        appRtcClient = new AppRtcClient();

        if (sender){
            appRtcClient.createOffer();
        }
        receiveIceCandidate();
        receiveSessionDescription();
    }
    public FireBase(){

    }

    public void receiveSessionDescription() {
        dbRef.child("sessionDescriptions").child(otherUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getValue() != null) {
                    if (sender) {
                        String data = snapshot.getValue().toString();
                        appRtcClient.sessionReceived(new SessionDescription(SessionDescription.Type.ANSWER, data));
                        Log.d("TAG : position", "retrived session desc data from db : type answer" + data);
                    } else {
                        String data = snapshot.getValue(String.class);
                        appRtcClient.sessionReceived(new SessionDescription(SessionDescription.Type.OFFER, data));
                        appRtcClient.createAnswer();
                        Log.d("TAG : position", "retried session description data from db : type offer" + data);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public void receiveIceCandidate() {
        dbRef.child("IceCandidate").child(otherUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getValue() != null) {
                    IceCandidate candidate = gson.fromJson(snapshot.getValue().toString(), IceCandidate.class);
                    appRtcClient.addIceCandidata(candidate);
                    Log.d("TAG : position : ", "retrieved ice data from db" + snapshot.getValue().toString());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });
    }

    public void sendIce(IceCandidate iceCandidate) {
        dbRef.child("IceCandidate").child(UId).setValue((gson.toJson(iceCandidate)));
    }

    public void sendSSD(String sessionDescription) {
        dbRef.child("sessionDescriptions").child(UId).setValue(sessionDescription);
    }
}
