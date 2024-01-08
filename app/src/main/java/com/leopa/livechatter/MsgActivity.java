package com.leopa.livechatter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.leopa.livechatter.utils_constants.Constants.CONNECT_ROOM_REF;
import static com.leopa.livechatter.utils_constants.Constants.MATCH_ROOM_REF;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.SetOptions;
import com.leopa.livechatter.adapter.MessageAdapter;
import com.leopa.livechatter.model.MessageReceive;
import com.leopa.livechatter.model.MessageSend;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MsgActivity extends AppCompatActivity {

    AlertDialog alertDialog;

    ProgressBar progressBar;
    TextView customBarText;
    ImageButton sendMsgButton, imageButton2;
    EditText editText;
    RecyclerView recyclerView;

    private MessageAdapter adapter;


    FirebaseFirestore db;
    CollectionReference matchRef;
    CollectionReference connectedRef;

    ArrayList<Object> combinedList;
    LinearLayoutManager linearLayoutManager;
    ListenerRegistration listenerRegistration;
    private String UID;
    private String user2Uid;


    private void connectRoom(String user1) {
        // This is for user2 who finds user1
        Map<String, Object> matchData = new HashMap<>();
        matchData.put("connectedTo", user1);
        connectedRef.document(UID).set(matchData,SetOptions.merge()).addOnSuccessListener(unused -> receiveMessages(user1)).addOnFailureListener(e -> {
            makeToast(e + " Trying again in 3 seconds");
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                connectRoom(user1); // Restart the method
            }, 3000); // Delay of 3 seconds
        });

    }

    private void createRoom() {

        DocumentReference createRoomRef = db.collection(MATCH_ROOM_REF).document(UID);
        Map<String, Object> userPutData = new HashMap<>();
        userPutData.put("user1", UID);
        userPutData.put("user2","");
        //will be deleted by user2
        createRoomRef.set(userPutData).addOnFailureListener(e -> {
            makeToast(e.toString());
            finish();
        });

        listenerRegistration = db.collection(CONNECT_ROOM_REF)
                .whereEqualTo("connectedTo", UID)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        // Handle error
                        Log.w("TAG", "Listen failed.", e);
                        return;
                    }
                    if (snapshots != null) {
                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            DocumentSnapshot document = dc.getDocument();
                            String documentId = document.getId();
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                Log.d("TAG", "Document added: " + documentId);
                                // Do something with the added document ID
                                listenerRegistration.remove();
                                makeToast("going to receive messages");
                                user2Uid = documentId;
                                receiveMessages(documentId);
                            }
                        }
                    }
                });

// To remove the listener later:
// listenerRegistration.remove();

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);

        displayAlertDialogue();


        Log.w("TAG", "Error getting documents.");
        //instantiation
        progressBar = findViewById(R.id.progressBar);
        editText = findViewById(R.id.editTextText);
        sendMsgButton = findViewById(R.id.sendMsgButton);
        imageButton2 = findViewById(R.id.sendMsgButton2);
        recyclerView = findViewById(R.id.recyclerViewMessages);
        customBarText = findViewById(R.id.customBarText);


        customBarText.setText("Message room");
        //db
        db = FirebaseFirestore.getInstance();
        matchRef = db.collection("matchRoom");
        connectedRef = db.collection("connectRoom");

        //getNewimestamp();
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        combinedList = new ArrayList<>();
        adapter = new MessageAdapter(combinedList);
        recyclerView.setAdapter(adapter);

        //temp
        imageButton2.setOnClickListener(v -> displayReceiveMsg(editText.getText().toString().trim(), "hii"));//
        // find send
        findUser();
        sendMsgButton.setOnClickListener(v -> sendMessage());


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        deleteData();

    }

    private void deleteData() {
        DocumentReference storeMsgRef = db.collection(CONNECT_ROOM_REF).document(UID);
        DocumentReference connectedToRef = db.collection(CONNECT_ROOM_REF).document(user2Uid);
        storeMsgRef.delete();
        connectedToRef.delete();
    }


    private void receiveMessages(String fromUserId) {
        DocumentReference getStr = connectedRef.document(fromUserId);
        alertDialog.dismiss();

        getStr.addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                Log.w(TAG, "Listen failed.", e);
                makeToast("connect room listener failed please exit");
                return;
            }

            if (snapshot != null && snapshot.exists()) {
                Map<String, Object> data = (Map<String, Object>) snapshot.getData();
                // Proceed with keySet() operations

                String highestKey = null;
                int highestValue = 1;
                // assert data != null;
                // Set<String> datakeys =  data.keySet();
                assert data != null;
                for (String key : data.keySet()) {
                    try {
                        makeToast("user is typing...");
                        int currentValue = Integer.parseInt(key);
                        if (currentValue > highestValue) {
                            highestValue = currentValue;
                            highestKey = key;
                        }
                    } catch (NumberFormatException err) {
                        // Ignore non-numeric keys
                    }
                }
                if (highestKey != null) {
                    String message = (String) data.get(highestKey);
                    // Use the highest string value here
                    Log.d("TAG", "Highest string value: " + message);
                    displayReceiveMsg(message, highestKey);
                }
            } else {
                // Handle the case where the snapshot is null or doesn't exist
                makeToast("snapshot is null");
            }

            /*if (snapshot != null && snapshot.exists()) {
                Log.d(TAG, "Current data: " + snapshot.getData());
            } else {
                Log.d(TAG, "Current data: null");
            }*/
        });


    }

    private void sendMessage() {

        //show send message
        String msg = editText.getText().toString().trim();
        editText.setText(null);
        combinedList.add(new MessageSend(msg));
        adapter.notifyItemInserted(combinedList.size());
        //messages will be deleted on exit.
        Objects.requireNonNull(recyclerView.getLayoutManager()).scrollToPosition(combinedList.size() - 1);


        //sending message to opposite user
        String timestamp;
        timestamp = getNewimestamp();


        DocumentReference storeMsgRef = db.collection(CONNECT_ROOM_REF).document(UID);
        Map<String, Object> sendMsgDb = new HashMap<>();
        sendMsgDb.put(timestamp, msg);
        storeMsgRef.set(sendMsgDb,SetOptions.merge());
        //TODO: ADDING ON SUCCESS ETC.. TO SHOW NEAR TEXTVIEW THAT IT IS SEND OR NOT.

    }

    private void displayAlertDialogue(){
        alertDialog = new MaterialAlertDialogBuilder(this).setView(R.layout.loading_dilogue_1).create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        alertDialog.setOnDismissListener(dialog -> finishActivityCustom());
        if (alertDialog.isShowing()) {
            Toast.makeText(this, "Showing", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayReceiveMsg(String message, String timeStamp) {

        combinedList.add(new MessageReceive(message));
        Objects.requireNonNull(recyclerView.getLayoutManager()).scrollToPosition(combinedList.size() - 1);
        adapter.notifyItemInserted(combinedList.size());


    }

    private String getNewimestamp() {

        Instant instant = Instant.now();
        long timestamp = instant.getEpochSecond();
        return String.valueOf(timestamp);
    }

    public void back(View view) {
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseAuth mAuth;

        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(firebaseAuth -> {

            if (firebaseAuth.getCurrentUser() != null) {
                UID = firebaseAuth.getUid();
                makeToast(UID);


            } else {
                startActivity(new Intent(MsgActivity.this, login_page.class));
            }
        });




    }

    private void finishActivityCustom() {
        alertDialog.dismiss();
        finish();
        Toast.makeText(this, "Finished.", Toast.LENGTH_SHORT).show();
    }

    private void findUser() {

        CollectionReference collectionReference = db.collection("matchRoom");
        collectionReference.whereEqualTo("user2", "").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
            if (queryDocumentSnapshots.isEmpty()) {
                // Value and key not found
                Toast.makeText(MsgActivity.this, "Value and key do not exist", Toast.LENGTH_SHORT).show();
                makeToast("going to create room");
                createRoom();
            } else {
                // Value and key found
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String documentName = documentSnapshot.getId();
                    Toast.makeText(MsgActivity.this, "Value and key found in document: " + documentName, Toast.LENGTH_SHORT).show();
                    // Do something with the ID
                    String userJoin = documentSnapshot.getString("user1");
                    makeToast("going to connect room");
                    connectRoom(userJoin);

                    //puting user2 value in database matching room
                    DocumentReference dcod = collectionReference.document(documentName);
                    Map<String, Object> userPutData = new HashMap<>();
                    userPutData.put("user2", UID);
                    dcod.set(userPutData , SetOptions.merge()).addOnFailureListener((OnFailureListener) e -> Log.w(TAG, "Error setting document", e));
                    dcod.delete().addOnFailureListener(e -> Log.w(TAG, "Error deleting document", e));

                }
            }
        }).addOnFailureListener(e -> makeToast(e.toString()));
    }
    private void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}