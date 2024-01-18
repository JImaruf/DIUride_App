package com.example.diuride;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.diuride.databinding.ActivityRegBinding;
import com.example.diuride.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;

public class RegActivity extends AppCompatActivity {
    ActivityRegBinding  binding;
    String email="",name="",diuID="",conpass,FCMtoken;
    String pass;
    FirebaseAuth mAuth;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setNavigationBarColor(Color.parseColor("#52C498"));
        getWindow().setStatusBarColor(Color.parseColor("#52C498"));
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();



        binding.backlinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(RegActivity.this,WelcomeActivity.class));
            }
        });

        binding.signBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = binding.emailet.getText().toString();
                pass = binding.passet.getText().toString();
                conpass = binding.conpasset.getText().toString();
                name = binding.userName.getText().toString();
                diuID = binding.stuid.getText().toString();

               if(verifyallRequirement())
               {

                    mAuth.createUserWithEmailAndPassword(email,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification();
                            Toast.makeText(RegActivity.this, "Verify your email.Check emails!then login", Toast.LENGTH_SHORT).show();
                            UserModel user = new UserModel();

                            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                                @Override
                                public void onComplete(@NonNull Task<String> task) {
                                    if(task.isSuccessful())
                                    {
                                      FCMtoken =  task.getResult();
                                        Toast.makeText(RegActivity.this, "token:"+FCMtoken, Toast.LENGTH_LONG).show();
                                        user.setEmail(email);
                                        user.setName(name);
                                        user.setDiuid(diuID);
                                        user.setUserType("none");
                                        user.setProimage("pending");
                                        user.setFCMtoken(FCMtoken);

                                        db.collection("Users").document(mAuth.getCurrentUser().getUid()).set(user);

                                        //  startActivity(new Intent(RegActivity.this,LoginActivity.class));
                                        // finish();

                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegActivity.this, "token reg failed", Toast.LENGTH_SHORT).show();
                                }
                            });




                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                          //  Toast.makeText(RegActivity.this, "Sorry,Can't Create your Account.Try again", Toast.LENGTH_SHORT).show();
                             if(e instanceof FirebaseAuthUserCollisionException)
                             {
                                 Toast.makeText(RegActivity.this, "Account Already Registered.Please Log In", Toast.LENGTH_SHORT).show();
                             }
                             if( e instanceof FirebaseAuthWeakPasswordException)
                            {
                                Toast.makeText(RegActivity.this, "Weak password!Enter more than 6 passcode", Toast.LENGTH_SHORT).show();
                            }
                             else
                             {
                                 Toast.makeText(RegActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                             }
                        }
                    });
               }
            }
        });

      binding.regTologin.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              startActivity(new Intent(RegActivity.this,LoginActivity.class));
              finish();
          }
      });
    }

    private boolean verifyallRequirement() {
        if (email.isEmpty())
        {
            binding.emailet.setError("Enter a email first!");
            binding.emailet.requestFocus();
            return false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            binding.emailet.setError("Enter a valid Email");
            binding.emailet.requestFocus();
            return false;
        }

//        String checkuvdom = "@diu.edu.bd";
//        int size = email.length();
//        String currentDom = email.substring(email.indexOf("@"),size);
//
//        if(!checkuvdom.equals(currentDom))
//        {
//            binding.emailet.setError("Use DIU Varsity mail");
//            binding.emailet.requestFocus();
//            return false;
//
//        }


        if(diuID.isEmpty())
        {
            binding.stuid.setError("Enter Student ID");
            binding.stuid.requestFocus();
            return false;
        }
        if(name.isEmpty())
        {
            binding.userName.setError("Enter your nick name");
            binding.userName.requestFocus();
            return false;
        }
        if(pass.isEmpty())
        {
            binding.passet.setError("Enter a Password");
            binding.passet.requestFocus();
            return false;
        }

        if (pass.length()<6)
        {
            binding.passet.setError("Minimum length of password should be 6");
            binding.passet.requestFocus();
            return false;
        }
        if(conpass.isEmpty())
        {
            binding.conpasset.setError("Enter Confirm Password");
            binding.conpasset.requestFocus();
            return false;
        }

        if (!pass.equals(conpass)) {
            Toast.makeText(RegActivity.this, "Both password must to be same!", Toast.LENGTH_SHORT).show();
            return false;

        }

        else
        {
            return true;
        }
    }


}