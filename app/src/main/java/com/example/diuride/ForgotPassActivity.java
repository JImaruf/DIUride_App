package com.example.diuride;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.diuride.databinding.ActivityForgotPassBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassActivity extends AppCompatActivity {

    ActivityForgotPassBinding binding;
    String email;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();

        binding.forgotBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = binding.emailForResetPass.getText().toString();
                if(verifyRequirements())
                {
                    mAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            binding.msg.setVisibility(View.VISIBLE);
                            binding.loginBTNf.setVisibility(View.VISIBLE);
                            binding.emailForResetPass.setText("");

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            binding.msg.setText("Sorry!Request Failed.Please try again later");
                            binding.msg.setVisibility(View.VISIBLE);

                        }
                    });


                }
            }
        });

        binding.loginBTNf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgotPassActivity.this,LoginActivity.class));
            }
        });
    }

    private boolean verifyRequirements() {

        if (email.isEmpty())
        {
            binding.emailForResetPass.setError("Enter a email first!");
            binding.emailForResetPass.requestFocus();
            return false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            binding.emailForResetPass.setError("Enter a valid Email");
            binding.emailForResetPass.requestFocus();
            return false;
        }

        String checkuvdom = "@diu.edu.bd";
        int size = email.length();
        String currentDom = email.substring(email.indexOf("@"),size);

        if(!checkuvdom.equals(currentDom))
        {
            binding.emailForResetPass.setError("Use DIU Varsity mail");
            binding.emailForResetPass.requestFocus();
            return false;

        }

        else
        {
            return true;
        }
    }
}