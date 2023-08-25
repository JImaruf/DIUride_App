package com.example.diuride;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.diuride.databinding.ActivityMain2Binding;
import com.google.android.material.navigation.NavigationView;

import me.ibrahimsn.lib.OnItemSelectedListener;

public class MainActivity2 extends AppCompatActivity {
    ActivityMain2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setContentView(binding.getRoot());
        getWindow().setNavigationBarColor(Color.parseColor("#52C498"));
        getWindow().setStatusBarColor(Color.parseColor("#52C498"));


        binding.navmenubtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!binding.drawerid.isDrawerOpen(GravityCompat.START))
                {

                    binding.drawerid.openDrawer(GravityCompat.START);
                }
                else
                {
                    binding.drawerid.closeDrawer(GravityCompat.START);
                }

            }
        });

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();

        ft.add(R.id.fragmentcontainer,new offerRideFragment());
        ft.commit();









        binding.navbarid.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {


                if (i == 0) {

                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();

                    ft.replace(R.id.fragmentcontainer, new offerRideFragment());


                    ft.commit();

                    Toast.makeText(MainActivity2.this, "home", Toast.LENGTH_SHORT).show();
                } else if (i == 1) {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();

                    ft.replace(R.id.fragmentcontainer, new NotificationFragment());
                    ft.commit();

                    Toast.makeText(MainActivity2.this, "notification", Toast.LENGTH_SHORT).show();
                }

                else {
                    Toast.makeText(MainActivity2.this, "default", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        }) ;




        binding.drawernavbar.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                if(id == R.id.aboutUs)
                {
                    Toast.makeText(MainActivity2.this, "about us!", Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.logout)
                {
                    Toast.makeText(MainActivity2.this, "log out!", Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.rHistory)
                {
                    Toast.makeText(MainActivity2.this, "history!", Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.settings)
                {
                    Toast.makeText(MainActivity2.this, "Settings!", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });
    }

}
