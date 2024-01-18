package com.example.diuride;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.diuride.databinding.ActivityLoginBinding;
import com.example.diuride.databinding.ActivityRegBinding;
import com.example.diuride.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    String radiobtnuserType ="";
    String email,pass;
    FirebaseAuth mAuth;
    FirebaseFirestore db;


    String uName,diuid,uEmail,prolink,userType,FCMToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setNavigationBarColor(Color.parseColor("#52C498"));
        getWindow().setStatusBarColor(Color.parseColor("#52C498"));
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        binding.loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = binding.emailet.getText().toString();
                pass = binding.passet.getText().toString();



                if(verifyRequirements())
                {
                    mAuth.signInWithEmailAndPassword(email,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {



                            if(Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified())
                            {
                                FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                                    @Override
                                    public void onComplete(@NonNull Task<String> task) {
                                        if(task.isSuccessful())
                                        {
                                            FCMToken = task.getResult();
                                            //first update userType
                                            db.collection("Users").document(mAuth.getCurrentUser().getUid()).update("userType",radiobtnuserType,"fcmtoken",FCMToken).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                }
                                            });

                                        }

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(LoginActivity.this, "token failed", Toast.LENGTH_SHORT).show();
                                    }
                                });

//                                //
//                                //fetch user info when log in and save to shared pref
//                              db.collection("Users").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                                    @Override
//                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                        UserModel mUser = new UserModel();
//
//                                       mUser = documentSnapshot.toObject(UserModel.class);
//                                        if (mUser != null) {
//                                            uName = mUser.getName();
//                                            diuid = mUser.getDiuid();
//                                            uEmail = mUser.getEmail();
//                                            userType = mUser.getUserType();
//                                            prolink = mUser.getProimage();
//
//
//                                            // Saving user data in SharedPreferences
//                                            SharedPreferences.Editor editor = getSharedPreferences("userInfo", MODE_PRIVATE).edit();
//                                            editor.putString("uname", uName);
//                                            editor.putString("udiuid", diuid);
//                                            editor.putString("uemail", uEmail);
//                                            editor.putString("usertype", userType);
//                                            editor.putString("userpropic", prolink);
//                                            // Add other user information as needed
//                                            editor.apply();
//
//                                        }
//
//                                    }
//                                });

                                if(radiobtnuserType.equals("passenger"))
                                {

                                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                    finish();

                                }
                                else if(radiobtnuserType.equals("rider"))
                                {

                                    startActivity(new Intent(LoginActivity.this,MainActivity2.class));
                                    finish();


                                }

                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this, "First verify Your email.Check mails", Toast.LENGTH_SHORT).show();
                            }


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                           if(e instanceof FirebaseAuthInvalidUserException)
                           {
                               Toast.makeText(LoginActivity.this, "Account Not Found", Toast.LENGTH_SHORT).show();
                           }
                           if(e instanceof FirebaseAuthInvalidCredentialsException)
                           {
                               binding.passet.setError("Invalid Password");
                               binding.passet.setText("");

                           }
                           else
                           {
                               Toast.makeText(LoginActivity.this, "Log In failed. "+e.getMessage(), Toast.LENGTH_SHORT).show();
                           }

                        }
                    });


                }

                else
                {
                    Toast.makeText(LoginActivity.this, "Requirements need to fill", Toast.LENGTH_SHORT).show();
                }



            }
        });


        binding.backklinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,WelcomeActivity.class));
                finish();
            }
        });
        binding.loginToregbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegActivity.class));
                finish();
            }
        });


        binding.forgotbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(LoginActivity.this,ForgotPassActivity.class));

            }
        });
    }

    private boolean verifyRequirements() {

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
        if(binding.radiogroup.getCheckedRadioButtonId()==-1)
        {
            Toast.makeText(LoginActivity.this, "Select User Type", Toast.LENGTH_SHORT).show();
            return false;
        }

        else
        {
            return true;
        }
    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        int id = view.getId();
        if (id == R.id.passengerRbtn) {
            if (checked)
            {
                Toast.makeText(this, "passenger", Toast.LENGTH_SHORT).show();
                radiobtnuserType= "passenger";

            }


        } else if (id == R.id.riderRbtn) {
            if (checked)
            {
                Toast.makeText(this, "rider.", Toast.LENGTH_SHORT).show();
                radiobtnuserType= "rider";
            }
        }
    }
}