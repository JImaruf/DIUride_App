package com.example.diuride;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class SplashScreen extends AppCompatActivity {

    Animation rightani;
    ConstraintLayout textLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(SplashScreen.this,R.color.maincolor));
        setContentView(R.layout.activity_splash_screen);

        textLayout = findViewById(R.id.textfield);

        rightani = AnimationUtils.loadAnimation(this,R.anim.righttoleft);

        textLayout.setAnimation(rightani);





        Intent intent = new Intent(SplashScreen.this, WelcomeActivity.class);


        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
                finish();
            }
        }).start();

    }
}