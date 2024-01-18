package com.example.diuride;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.diuride.models.UserModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class SplashScreen extends AppCompatActivity {

    private static final long SPLASH_DISPLAY_LENGTH = 4000;
    Animation rightani;
    ConstraintLayout textLayout;
    FirebaseAuth mAuth;
    FirebaseFirestore mdb;
    String userType="user";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setNavigationBarColor(Color.parseColor("#52C498"));
        getWindow().setStatusBarColor(Color.parseColor("#52C498"));
        mAuth = FirebaseAuth.getInstance();
        mdb = FirebaseFirestore.getInstance();

        textLayout = findViewById(R.id.textfield);

        rightani = AnimationUtils.loadAnimation(this,R.anim.righttoleft);

        textLayout.setAnimation(rightani);

        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(4000);
                    if(mAuth.getCurrentUser()!=null && mAuth.getCurrentUser().isEmailVerified()) {
                        mdb.collection("Users").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                UserModel mUser = new UserModel();

                                mUser = documentSnapshot.toObject(UserModel.class);
                                if (mUser != null) {

                                    userType = mUser.getUserType();
                                    if(userType.equals("rider"))
                                    {
                                        startActivity(new Intent(SplashScreen.this,MainActivity2.class));
                                        finish();


                                    } else if (userType.equals("passenger")) {
                                        startActivity(new Intent(SplashScreen.this,MainActivity.class));
                                        finish();
                                    }
                                    else {

                                        startActivity(new Intent(SplashScreen.this,WelcomeActivity.class));
                                        finish();
                                    }
                                }

                            }
                        });
                    }
                    else
                    {
                        startActivity(new Intent(SplashScreen.this,WelcomeActivity.class));
                        finish();
                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                    if(mAuth.getCurrentUser()!=null && mAuth.getCurrentUser().isEmailVerified()) {
                        mdb.collection("Users").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                UserModel mUser = new UserModel();

                                mUser = documentSnapshot.toObject(UserModel.class);
                                if (mUser != null) {

                                    userType = mUser.getUserType();
                                    if(userType.equals("rider"))
                                    {
                                        startActivity(new Intent(SplashScreen.this,MainActivity2.class));
                                        finish();


                                    } else if (userType.equals("passenger")) {
                                        startActivity(new Intent(SplashScreen.this,MainActivity.class));
                                        finish();
                                    }
                                    else {

                                        startActivity(new Intent(SplashScreen.this,WelcomeActivity.class));
                                        finish();
                                    }
                                }

                            }
                        });
                    }
                    else
                    {
                        startActivity(new Intent(SplashScreen.this,WelcomeActivity.class));
                        finish();
                    }

                }
            }
        }).start();


//        // Retrieving user data in another activity
//        SharedPreferences prefs = getSharedPreferences("userInfo", MODE_PRIVATE);
//        String userType = prefs.getString("usertype", "");
//
//        // Retrieve other user information as needed












//        new Thread(new Runnable() {
//            public void run() {
//                try {
//                    Thread.sleep(4000);
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//
//
//            }
//        }).start();

//        new Handler().postDelayed(new Runnable(){
//            @Override
//            public void run() {
//                if(mAuth.getCurrentUser()!=null)
//                {
//                    if(userType.equals("rider"))
//                    {
//                        startActivity(new Intent(SplashScreen.this,MainActivity2.class));
//                        finish();
//
//
//                    } else if (userType.equals("passenger")) {
//                        startActivity(new Intent(SplashScreen.this,MainActivity.class));
//                        finish();
//
//
//                    }
//
//                }
//                else {
//                    startActivity(new Intent(SplashScreen.this,WelcomeActivity.class));
//                    finish();
//
//                }
//                /* Create an Intent that will start the Menu-Activity. */
////                Intent mainIntent = new Intent(Splash.this,Menu.class);
////                Splash.this.startActivity(mainIntent);
////                Splash.this.finish();
////                startActivity(new Intent(SplashScreen.this,WelcomeActivity.class));
////                finish();
//            }
//        }, SPLASH_DISPLAY_LENGTH);
//
//
//


    }
}