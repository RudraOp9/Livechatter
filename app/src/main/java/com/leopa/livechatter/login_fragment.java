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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link login_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class login_fragment extends Fragment {
    View parentHolder;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public login_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment login_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static login_fragment newInstance(String param1, String param2) {
        login_fragment fragment = new login_fragment();
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
        parentHolder = inflater.inflate(R.layout.fragment_login, container, false);

        mAuth.addAuthStateListener(firebaseAuth -> {
            if (firebaseAuth.getCurrentUser() != null){
                startActivity(new Intent(parentHolder.getContext(),MainActivity.class));
            }
        });


        EditText emailText =(EditText) parentHolder.findViewById(R.id.emailText);
        EditText passwordText =(EditText) parentHolder.findViewById(R.id.passText);
        Button login = (Button) parentHolder.findViewById(R.id.logIn);


        login.setOnClickListener(v -> {
            if (emailText.getText().toString().trim().isEmpty() || passwordText.getText().toString().trim().isEmpty()){
                Toast.makeText(parentHolder.getContext(), "fields are empty !", Toast.LENGTH_SHORT).show();
            }else {
                loginAccount(emailText.getText().toString().trim() , passwordText.getText().toString().trim());
            }
        });
        return parentHolder;
    }

    private void loginAccount(String email , String password){

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) parentHolder.getContext(), task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        assert user != null;
                        if(user.isEmailVerified()){
                            Toast.makeText(parentHolder.getContext(), "Authentication success.",
                                    Toast.LENGTH_LONG).show();
                            startActivity(new Intent(parentHolder.getContext(),MainActivity.class));
                            requireActivity().finish();

                        }else {
                            user.sendEmailVerification();
                            AlertDialog.Builder builder = new AlertDialog.Builder(parentHolder.getContext());
                            builder.setTitle("Email not Verified");
                            builder.setMessage("An mail has been sent to you email address to verify you identity");
                            builder.setCancelable(false);
                            builder.setPositiveButton("ok",(DialogInterface.OnClickListener )(dialog, which) -> dialog.cancel());

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }

                        //updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(parentHolder.getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        //updateUI(null);
                    }
                }).addOnFailureListener(e -> {
                    String error = e.getMessage();
                    Toast.makeText(parentHolder.getContext(), error, Toast.LENGTH_SHORT).show();
                });


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

    }


}
