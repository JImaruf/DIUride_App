package com.example.diuride;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.diuride.adapters.NotificationAdapter_Rider;
import com.example.diuride.adapters.rlistAdapter;
import com.example.diuride.models.RiderNotificationModel;
import com.example.diuride.models.RlistModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class NotificationFragRider extends Fragment {

    NotificationAdapter_Rider adapter;
    RecyclerView RiderNotiRecycler;
    FirebaseAuth mAuth;
    FirebaseFirestore mDB;
    TextView noNotiyetTv;
    ImageView notiIcon;
    RiderNotificationModel riderNotificationModel = new RiderNotificationModel();
    ArrayList<RiderNotificationModel> riderNotiList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View v = inflater.inflate(R.layout.fragment_notification_frag_rider, container, false);
        mAuth = FirebaseAuth.getInstance();
        mDB = FirebaseFirestore.getInstance();
        RiderNotiRecycler = v.findViewById(R.id.RiderNotiRecycler);
        noNotiyetTv = v.findViewById(R.id.noNotiYettv);
        notiIcon= v.findViewById(R.id.notiBigICon);
        riderNotiList = new ArrayList<>();
        RiderNotiRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NotificationAdapter_Rider(getContext(),riderNotiList,noNotiyetTv,notiIcon);
        RiderNotiRecycler.setAdapter(adapter);

      mDB.collection("RiderNotiList").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
          @Override
          public void onSuccess(DocumentSnapshot documentSnapshot) {

              riderNotificationModel  = documentSnapshot.toObject(RiderNotificationModel.class);
              if(riderNotificationModel!=null)
              {
                  riderNotiList.add(riderNotificationModel);
                  adapter.notifyDataSetChanged();

              }

          }
      }).addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {


          }
      });
//          @Override
//          public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//              for(DocumentSnapshot d:queryDocumentSnapshots)
//              {
//
//                  riderNotificationModel  = d.toObject(RiderNotificationModel.class);
//                  riderNotiList.add(riderNotificationModel);
//              }
//              adapter.notifyDataSetChanged();
//
//          }
//      });

        return  v;
    }
}