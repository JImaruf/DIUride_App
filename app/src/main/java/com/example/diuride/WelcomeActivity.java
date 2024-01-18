package com.example.diuride;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.diuride.databinding.ActivityWelcomeBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class WelcomeActivity extends AppCompatActivity {

    ActivityWelcomeBinding binding;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(WelcomeActivity.this,R.color.maincolor));

        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        getWindow().setNavigationBarColor(Color.parseColor("#52C498"));
        getWindow().setStatusBarColor(Color.parseColor("#52C498"));
        binding.regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WelcomeActivity.this,RegActivity.class));
                finish();
            }
        });

        binding.loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
                finish();
            }
        });

    }
}