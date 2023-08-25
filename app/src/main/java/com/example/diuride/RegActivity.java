package com.example.diuride;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.diuride.databinding.ActivityRegBinding;

public class RegActivity extends AppCompatActivity {
    ActivityRegBinding  binding;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

                String checkuvdom = "@diu.edu.bd";
                int size = email.length();
                String currentDom = email.substring(email.indexOf("@"),size);
                if(!checkuvdom.equals(currentDom))
                {
                    Toast.makeText(RegActivity.this, "Use DIU Varsity mail ", Toast.LENGTH_SHORT).show();

                }

            }
        });


    }
}