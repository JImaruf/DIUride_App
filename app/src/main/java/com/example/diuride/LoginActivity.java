package com.example.diuride;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.diuride.databinding.ActivityLoginBinding;
import com.example.diuride.databinding.ActivityRegBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    String userType = "passenger";
    String email,pass;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
       // getWindow().setNavigationBarColor(Color.parseColor("#52C498"));
        getWindow().setStatusBarColor(Color.parseColor("#52C498"));
        mAuth = FirebaseAuth.getInstance();

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
                                if(userType.equals("passenger"))
                                {
                                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                }
                                else if(userType.equals("rider"))
                                {
                                    startActivity(new Intent(LoginActivity.this,MainActivity2.class));

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
                            Toast.makeText(LoginActivity.this, "Log In failed.Try again!", Toast.LENGTH_SHORT).show();

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

        String checkuvdom = "@diu.edu.bd";
        int size = email.length();
        String currentDom = email.substring(email.indexOf("@"),size);

        if(!checkuvdom.equals(currentDom))
        {
            binding.emailet.setError("Use DIU Varsity mail");
            binding.emailet.requestFocus();
            return false;

        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            binding.emailet.setError("Enter a valid Email");
            binding.emailet.requestFocus();
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
                userType= "passenger";

            }


        } else if (id == R.id.riderRbtn) {
            if (checked)
            {
                Toast.makeText(this, "rider.", Toast.LENGTH_SHORT).show();
                userType= "rider";
            }
        }
    }
}