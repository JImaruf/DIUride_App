package com.example.diuride;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diuride.adapters.rlistAdapter;
import com.example.diuride.models.RlistModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RideListFragment extends Fragment {

    rlistAdapter adapter;
    RecyclerView rlistRecyler;
    FirebaseAuth mAuth;
    FirebaseFirestore mDB;
    TextView noRideTv;
    RlistModel rlistModel = new RlistModel();
    ArrayList<RlistModel>  rideList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ride_list,container,false);
        mAuth = FirebaseAuth.getInstance();
        mDB = FirebaseFirestore.getInstance();
        rlistRecyler = v.findViewById(R.id.RlistRecyler);
        noRideTv = v.findViewById(R.id.noRideTv);
        rideList = new ArrayList<>();
        rlistRecyler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new rlistAdapter(getContext(),rideList,rlistRecyler,noRideTv);
        rlistRecyler.setAdapter(adapter);

            mDB.collection("RideList").get()
                      .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                          @Override
                          public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                              List<DocumentSnapshot> list =queryDocumentSnapshots.getDocuments();
                              for(DocumentSnapshot d:list)
                              {

                                  rlistModel = d.toObject(RlistModel.class);
                                  rideList.add(rlistModel);



                              }
                              adapter.notifyDataSetChanged();


                          }
                      });

        return v;
    }


}