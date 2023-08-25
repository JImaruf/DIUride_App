package com.example.diuride;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.diuride.databinding.ActivityLoginBinding;
import com.example.diuride.databinding.ActivityRegBinding;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    String userType = "passenger";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
       // getWindow().setNavigationBarColor(Color.parseColor("#52C498"));
        getWindow().setStatusBarColor(Color.parseColor("#52C498"));

        binding.loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userType.equals("passenger"))
                {
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                }
                else if(userType.equals("rider"))
                {
                    startActivity(new Intent(LoginActivity.this,MainActivity2.class));

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