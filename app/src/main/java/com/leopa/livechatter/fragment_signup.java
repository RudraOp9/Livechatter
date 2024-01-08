package com.leopa.livechatter;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_signup#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_signup extends Fragment {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user;
    View parentHolder;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_signup() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_signup.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_signup newInstance(String param1, String param2) {
        fragment_signup fragment = new fragment_signup();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parentHolder = inflater.inflate(R.layout.fragment_signup, container, false);


        EditText emailText =(EditText) parentHolder.findViewById(R.id.emailTextCreate);
        EditText passwordText =(EditText) parentHolder.findViewById(R.id.passTextCreate);
        EditText userName =(EditText) parentHolder.findViewById(R.id.userName);
        Button signUp = (Button) parentHolder.findViewById(R.id.btnSignUp);



        signUp.setOnClickListener(v -> {
            if (
                    emailText.getText().toString().trim().isEmpty()
                    || passwordText.getText().toString().trim().isEmpty()
                    || userName.getText().toString().isEmpty()) {

                Toast.makeText(parentHolder.getContext(), "fields are empty !", Toast.LENGTH_SHORT).show();
            }else {
                String qwerty = userName.getText().toString().trim();
                DocumentReference docRef = db.collection("users").document(qwerty);
                docRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Toast.makeText(parentHolder.getContext(), "UserName already exists, choose another", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "DocumentSnapshot data: " );
                        } else {
                            createAccount(emailText.getText().toString() , passwordText.getText().toString(),userName.getText().toString());
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                });

            }
        });

        return parentHolder;
    }



    private void createAccount(String email , String password,String userName){

        mAuth.createUserWithEmailAndPassword(email, password )
                .addOnCompleteListener((Activity) parentHolder.getContext(), task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        user = mAuth.getCurrentUser();
                        Toast.makeText(parentHolder.getContext(), "Authentication success.",
                               Toast.LENGTH_LONG).show();
                        String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                        String userMail = mAuth.getCurrentUser().getEmail();

                        Map<String, Object> userData = new HashMap<>();
                        userData.put("mail",userMail);
                        userData.put("UId", userId);
                        db.collection("users")
                                .document(userName)
                                .set(userData);
                        if(user.isEmailVerified()){
                            startActivity(new Intent(parentHolder.getContext(),MainActivity.class));
                            requireActivity().finish();

                        }else {
                            user.sendEmailVerification();
                            AlertDialog.Builder builder = new AlertDialog.Builder(parentHolder.getContext());
                            builder.setTitle("Verify Your Email");
                            builder.setMessage("An mail has been sent to you email address to verify you identity");
                            builder.setCancelable(false);
                            builder.setPositiveButton("ok",(DialogInterface.OnClickListener )(dialog, which) -> {
                                dialog.cancel();

                            });

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();

                        }
                       // updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(parentHolder.getContext(), "Authentication failed. ", Toast.LENGTH_SHORT).show();

                        //updateUI(null);
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String error = e.getMessage();
                        Toast.makeText(parentHolder.getContext(), error, Toast.LENGTH_SHORT).show();
                    }
                });
    }


}