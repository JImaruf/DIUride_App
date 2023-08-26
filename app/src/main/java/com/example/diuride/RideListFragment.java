package com.example.diuride;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.diuride.adapters.rlistAdapter;

import java.util.ArrayList;


public class RideListFragment extends Fragment {
    ArrayList<RlistModel> rList;
    rlistAdapter adapter;
    RecyclerView rlistRecyler;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ride_list,container,false);
        rlistRecyler = v.findViewById(R.id.RlistRecyler);
        rList = new ArrayList<>();

        RlistModel testModel1 = new RlistModel("Maruf","mirpur to DSC",R.drawable.avatarrr,"40tk","3:34pm");
        RlistModel testModel2 = new RlistModel("Anik","savar to DSC",R.drawable.maruf,"90tk","3:34pm");
        RlistModel testModel3 = new RlistModel("sojib","mirpur to shamoli",R.drawable.profileicon,"67tk","6:40pm");
        rList.add(testModel1);
        rList.add(testModel2);
        rList.add(testModel3);

        rlistRecyler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new rlistAdapter(getContext(),rList);
        rlistRecyler.setAdapter(adapter);

        return v;
    }
}