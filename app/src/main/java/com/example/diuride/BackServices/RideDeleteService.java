package com.example.diuride.BackServices;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.widget.Toast;

import androidx.annotation.NonNull;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RideDeleteService extends BroadcastReceiver {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "alarm triggered", Toast.LENGTH_SHORT).show();
        if(mAuth.getCurrentUser()!=null) {


            db.collection("RideList").document(mAuth.getCurrentUser().getUid()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}

